package com.hust.nb.Controller;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Dao.BigDeviceDao;
import com.hust.nb.Entity.BigDevice;
import com.hust.nb.util.BigDevicePropUtil;
import com.hust.nb.util.GetDate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import sun.net.www.http.HttpClient;

import javax.swing.text.EditorKit;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/bigDevice")
public class BigDeviceController {


    @Autowired
    private BigDevicePropUtil bigDevicePropUtil;

    @Autowired
    private BigDeviceDao bigDeviceDao;

    /**
     * 授权  获取第三方token
     */
    @ResponseBody
    @PostMapping("/bigDevice")
    public Object getBigDeviceToken() {
        //把第三方账号密码封装到map
        Map<String,Object> paramsMap = new HashMap<>();
        Map<String,Object> jsonMap = new HashMap<>();
        paramsMap.put("UserName", "whby");
        paramsMap.put("Password", "93bf1b1ee9ecef20b4306dde9253a8ca");
        String tokenStr ="";
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/Login/Login";
        //获取第三方token
        try {
            tokenStr = HttpRequest.post(url).header(Header.ACCEPT,"text/json").form(paramsMap).execute().body();

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("登录接口异常");
        }
        //fastjson 解析，返回
        try {
            JSONObject jsonObject = JSON.parseObject(tokenStr);
            String data = jsonObject.getString("Data");
            JSONObject token1 = JSON.parseObject(data);
            String token = token1.getString("Token");
            System.out.println(token);
            jsonMap.put("code","200");
            jsonMap.put("info",token);
        }catch (Exception e){
            jsonMap.put("code","-1");
            jsonMap.put("info","读取失败");

        }
        Object o =JSONObject.toJSON(jsonMap);
        return o;
    }

    /**
     * 获取设备列表
     */
    @ResponseBody
    @PostMapping("/getBigDevice")
    public Object getBigDevice(@RequestBody String msg){
        Map<String, Object> paramMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String token = jsonObject.getString("token");
        Integer page = jsonObject.getInteger("page");
        paramMap.put("Page", page);
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/Meter/GetMeterList";
        String resStr = "";
        try {
            resStr = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token).form(paramMap).execute().body();
        }catch (Exception e){
            e.printStackTrace();

        }
        JSONObject object = JSONObject.parseObject(resStr);

        try {
            //得到数据，插入数据库
            ArrayList<BigDevice> list = new ArrayList<>();
            JSONArray data = object.getJSONArray("List");
            if (data.size() != 0){
                for (int i =0; i < data.size(); i++){
                    BigDevice verify = bigDeviceDao.findByMPipeDn(data.getJSONObject(i).get("M_PipeDn").toString());
                    if (verify == null ){
                        BigDevice bigDevice = new BigDevice();
                        bigDevice.setMPipeDn(data.getJSONObject(i).get("M_PipeDn").toString());
                        if (data.getJSONObject(i).get("M_Tag")!= null){
                            bigDevice.setMTag(data.getJSONObject(i).get("M_Tag").toString());
                        }
                        bigDevice.setParentListId(data.getJSONObject(i).get("ParentListId").toString());
                        bigDevice.setMUserType(data.getJSONObject(i).get("M_UserType").toString());
                        if (data.getJSONObject(i).get("M_DoorNo") != null){
                            bigDevice.setMDoorNo(data.getJSONObject(i).get("M_DoorNo").toString());
                        }
                        if (data.getJSONObject(i).get("OrgName")!=null){
                            bigDevice.setOrgName(data.getJSONObject(i).get("OrgName").toString());
                        }

                        bigDevice.setAddressCode(data.getJSONObject(i).get("AddressCode").toString());
                        if (data.getJSONObject(i).get("M_Type")!=null){
                            bigDevice.setMType(data.getJSONObject(i).get("M_Type").toString());
                        }
                        if (data.getJSONObject(i).get("M_InstallAddress") != null){
                            bigDevice.setMInstallAddress(data.getJSONObject(i).get("M_InstallAddress").toString());
                        }
                        if (data.getJSONObject(i).get("MeterName")!= null){
                            bigDevice.setMeterName(data.getJSONObject(i).get("MeterName").toString());
                        }
                        if (data.getJSONObject(i).get("WarnStatus")!= null){
                            bigDevice.setWarnStatus(data.getJSONObject(i).get("WarnStatus").toString());
                        }
                        bigDevice.setMMaterial(data.getJSONObject(i).get("M_Material").toString());
                        bigDevice.setPhoneNo(data.getJSONObject(i).get("PhoneNo").toString());
                        if (data.getJSONObject(i).get("CurStatus")!=null){
                            bigDevice.setCurStatus(data.getJSONObject(i).get("CurStatus").toString());
                        }
                        bigDevice.setMeterId(data.getJSONObject(i).get("MeterId").toString());
                        bigDevice.setOrganizeId(data.getJSONObject(i).get("OrganizeId").toString());
                        bigDevice.setOrganizeId(data.getJSONObject(i).get("OrganizeId").toString());
                        String date1 = data.getJSONObject(i).get("CreateTime").toString();
                        String[] date2 = date1.split("T");
                        String date = date2[0]+" "+date2[1];
                        bigDevice.setCreateTime(Timestamp.valueOf(date));
                        bigDevice.setRealValue(new BigDecimal(data.getJSONObject(i).get("RealValue").toString()));
                        if (data.getJSONObject(i).get("ToValue")!=null){
                            bigDevice.setToValue(new BigDecimal(data.getJSONObject(i).get("ToValue").toString()));
                        }
                        bigDevice.setRevValue(new BigDecimal(data.getJSONObject(i).get("RevValue").toString()));
                        bigDevice.setPressValue(new BigDecimal(data.getJSONObject(i).get("PressValue").toString()));
                        bigDevice.setForValue(new BigDecimal(data.getJSONObject(i).get("ForValue").toString()));
                        bigDevice.setMSortCode(Integer.parseInt(data.getJSONObject(i).get("M_SortCode").toString()));
                        bigDevice.setCelVal(new BigDecimal(data.getJSONObject(i).get("CelVal").toString()));
                        list.add(bigDevice);
                    }
                    if (verify!= null &&!data.getJSONObject(i).get("RealValue").toString().equals(verify.getRealValue())) {
                        verify.setRealValue(new BigDecimal(data.getJSONObject(i).get("RealValue").toString()));
                        if (data.getJSONObject(i).get("ToValue")!=null){
                            verify.setToValue(new BigDecimal(data.getJSONObject(i).get("ToValue").toString()));
                        }
                        verify.setRevValue(new BigDecimal(data.getJSONObject(i).get("RevValue").toString()));
                        verify.setPressValue(new BigDecimal(data.getJSONObject(i).get("PressValue").toString()));
                        verify.setForValue(new BigDecimal(data.getJSONObject(i).get("ForValue").toString()));
                        verify.setCelVal(new BigDecimal(data.getJSONObject(i).get("CelVal").toString()));

                        list.add(verify);



                    }

                }
            }

            try {
                bigDeviceDao.saveAll(list);
                object.put("code","200");
                object.put("info","数据库保存成功");
            }catch (Exception e){
                e.printStackTrace();
                object.put("code","-2");
                object.put("info","保存至本地数据库失败");
            }
            System.out.println(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }

    /**
     * 获取单位列表
     */
    @ResponseBody
    @PostMapping("/getOrganizeList")
    public Object getOrganizeList(@RequestBody String token){
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/Organize/GetOrganizeList";
        JSONObject jsonObject = JSONObject.parseObject(token);
        String token1 = jsonObject.getString("token");
        String res = "";
        JSONObject object = new JSONObject();
        try {
            res = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token1).execute().body();
            object = JSONObject.parseObject(res);
            object.put("code","200");
            object.put("info","查询成功");
        }catch (Exception e){
            e.printStackTrace();
            object.put("code","-1");
            object.put("info","查询失败");
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }

    /**
     * 获取设备实时数据
     */
    @ResponseBody
    @PostMapping("/getRealData")
    public Object getRealData(@RequestBody String msg){
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/RealData/GetRealData";
        JSONObject jsonObject = JSONObject.parseObject(msg);
        HashMap<String, Object> paramMap = new HashMap<>();
        String phoneNumber = jsonObject.getString("phoneNumber");
        String addressCode = jsonObject.getString("addressCode");
        paramMap.put("PhoneNumber",phoneNumber);
        paramMap.put("AddressCode",addressCode);
        String token = jsonObject.getString("token");
        String res = "";
        JSONObject object = new JSONObject();
        try {
            res = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token).form(paramMap).execute().body();
            object = JSONObject.parseObject(res);
            object.put("code","200");
            object.put("info","查询成功");
        }catch (Exception e){
            e.printStackTrace();
            object.put("code","-1");
            object.put("info","查询失败");
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }

    /**
     * 日用量统计
     */
    @ResponseBody
    @PostMapping("/dayAnalysis")
    public Object dayAnalysis(@RequestBody String msg) throws Exception{
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/Analyze/DayAnalysis";
        JSONObject jsonObject = JSONObject.parseObject(msg);
        HashMap<String, Object> paramMap = new HashMap<>();
        String phoneNumber = jsonObject.getString("phoneNumber");
        String addressCode = jsonObject.getString("addressCode");
        String day = jsonObject.getString("day");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(day);
        System.out.println(date);

        paramMap.put("PhoneNumber",phoneNumber);
        paramMap.put("AddressCode",addressCode);
        paramMap.put("AnalysisDate",format.format(date));
        String token = jsonObject.getString("token");
        String res = "";
        JSONObject object = new JSONObject();
        try {
            res = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token).form(paramMap).execute().body();
            object = JSONObject.parseObject(res);
            object.put("code","200");
            object.put("info","查询成功");
        }catch (Exception e){
            e.printStackTrace();
            object.put("code","-1");
            object.put("info","查询失败");
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }

    /**
     * 月用量统计
     */
    @ResponseBody
    @PostMapping("/monthAnalysis")
    public Object monthAnalysis(@RequestBody String msg) throws Exception{
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/Analyze/MonthAnalysis";
        JSONObject jsonObject = JSONObject.parseObject(msg);
        HashMap<String, Object> paramMap = new HashMap<>();
        String phoneNumber = jsonObject.getString("phoneNumber");
        String addressCode = jsonObject.getString("addressCode");
        String month = jsonObject.getString("month");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = format.parse(month);
        System.out.println(date);
        paramMap.put("PhoneNumber",phoneNumber);
        paramMap.put("AddressCode",addressCode);
        paramMap.put("AnalysisDate",date);
        String token = jsonObject.getString("token");
        String res = "";
        JSONObject object = new JSONObject();
        try {
            res = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token).form(paramMap).execute().body();
            object = JSONObject.parseObject(res);
            object.put("code","200");
            object.put("info","查询成功");
        }catch (Exception e){
            e.printStackTrace();
            object.put("code","-1");
            object.put("info","查询失败");
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }

    /**
     * 获取通信日志
     */
    @ResponseBody
    @PostMapping("/getCommLogList")
    public Object getCommLogList (@RequestBody String msg) throws Exception{
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/CommLog/GetCommLogList";
        JSONObject jsonObject = JSONObject.parseObject(msg);
        HashMap<String, Object> paramMap = new HashMap<>();
        Integer page = jsonObject.getInteger("page");
        String addressCode = jsonObject.getString("addressCode");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startTime);
        Date end = sdf.parse(endTime);
        System.out.println(start);
        System.out.println(end);
        paramMap.put("Page",page);
        paramMap.put("AddressCode",addressCode);
        paramMap.put("AnalysisDate",start);
        paramMap.put("AnalysisDate",end);
        String token = jsonObject.getString("token");
        String res = "";
        JSONObject object = new JSONObject();
        try {
            res = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token).form(paramMap).execute().body();
            object = JSONObject.parseObject(res);
            object.put("code","200");
            object.put("info","查询成功");
        }catch (Exception e){
            e.printStackTrace();
            object.put("code","-1");
            object.put("info","查询失败");
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }

    /**
     * 根据设备地址码获取设备历史数据列表
     */
    @ResponseBody
    @PostMapping("/getHistoryDataList")
    public Object getHistoryDataList (@RequestBody String msg) throws Exception{
        String url = bigDevicePropUtil.getBigDeviceUrl()+"api/HistoryData/GetHistoryDataList";
        JSONObject jsonObject = JSONObject.parseObject(msg);
        HashMap<String, Object> paramMap = new HashMap<>();
        Integer page = jsonObject.getInteger("page");
        String addressCode = jsonObject.getString("addressCode");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = sdf.parse(startTime);
        Date end = sdf.parse(endTime);
        System.out.println(start);
        System.out.println(end);
        paramMap.put("Page",page);
        paramMap.put("AddressCode",addressCode);
        paramMap.put("AnalysisDate",start);
        paramMap.put("AnalysisDate",end);
        String token = jsonObject.getString("token");
        String res = "";
        JSONObject object = new JSONObject();
        try {
            res = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token).form(paramMap).execute().body();
            object = JSONObject.parseObject(res);
            object.put("code","200");
            object.put("info","查询成功");
        }catch (Exception e){
            e.printStackTrace();
            object.put("code","-1");
            object.put("info","查询失败");
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }

    /**
     * 发送命令
     */
    @ResponseBody
    @PostMapping("/commandQueue")
    public Object readNowData (@RequestBody String msg){
        JSONObject jsonObject = JSONObject.parseObject(msg);
        HashMap<String, Object> paramMap = new HashMap<>();
        String phoneNumber = jsonObject.getString("phoneNumber");
        String addressCode = jsonObject.getString("addressCode");
        Integer flag = jsonObject.getInteger("flag");
        String url = "";
        if (flag == 1){
            //发送读取实时数据命令
            url = bigDevicePropUtil.getBigDeviceUrl()+"api/Remote/ReadNowData";
            System.out.println(url);
        }else if (flag == 2){
            //发送校正时间命令
            url = bigDevicePropUtil.getBigDeviceUrl()+"api/Remote/CorrectionTime";
            System.out.println(url);
        }else if (flag == 3){
            //设置采集和上报周期
            url = bigDevicePropUtil.getBigDeviceUrl()+"api/Remote/SetSTPITV";
            System.out.println(url);
            Integer TPITV = jsonObject.getInteger("TPITV");//采集周期 s
            Integer SPITV = jsonObject.getInteger("SPITV");//上报周期 m
            paramMap.put("TPITV",TPITV);
            paramMap.put("SPITV",SPITV);
        }
        paramMap.put("AddressCode",addressCode);
        paramMap.put("PhoneNumber",phoneNumber);
        String token = jsonObject.getString("token");
        String res = "";
        JSONObject object = new JSONObject();
        try {
            res = HttpRequest.post(url).header(Header.ACCEPT,"text/json").header("Authorization",token).form(paramMap).execute().body();
            object = JSONObject.parseObject(res);
            object.put("code","200");
            object.put("info","发送成功，具体查看flag字段，1成功，2失败");
        }catch (Exception e){
            e.printStackTrace();
        }
        Object o = JSONObject.toJSON(object);
        return o;
    }
}
