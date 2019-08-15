package com.hust.nb.Controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Dao.DaycountDao;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.*;
import com.hust.nb.util.Adapter;
import com.hust.nb.util.EntityFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Controller
@CrossOrigin(origins = "*")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @Autowired
    HistorydataService historydataService;

    @Autowired
    DaycountDao daycountDao;

    @Autowired
    DeviceChangeService deviceChangeService;

    @Autowired
    DaycountService daycountService;

    @Autowired
    MonthcountService monthcountService;

    /**
     * 方法功能描述:获取水表详细信息
     */
    @ResponseBody
    @PostMapping("/GetDeviceInfo")
    public Object getDeviceByKey(@RequestBody String msg){
        Map<String,Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        try{
            Device device = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo);
            jsonMap.put("code","200");
            jsonMap.put("info","查询成功");
            jsonMap.put("data",device);
        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code","-1");
            jsonMap.put("info","查询失败");
            jsonMap.put("data","");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:修改水表信息
     */
    @ResponseBody
    @PostMapping("/UpdateDeviceDetailInfo")
    @CrossOrigin
    public Object updateDevice(@RequestBody String device){
        Device DeviceEntity = JSON.parseObject(device, Device.class);
        Map<String, Object> jsonMap = new HashMap<>();
        try{
            deviceService.updateDevice(DeviceEntity);
            jsonMap.put("code","200");
            jsonMap.put("info","更新成功");
        }catch (Exception e){
            jsonMap.put("code","-1");
            jsonMap.put("info","更新失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 更换水表
     */
    @ResponseBody
    @PostMapping("/ChangeDevice")
    public Object changeDevice(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        int userId = jsonObject.getInteger("userId");
        int operatorId = jsonObject.getInteger("operatorId");
        int valve = jsonObject.getInteger("valve");
        int deviceType = jsonObject.getInteger("deviceType");
        String oldNo = jsonObject.getString("oldNo");
        String newNo = jsonObject.getString("newNo");
        String enprNo = jsonObject.getString("enprNo");
        BigDecimal oldVal = jsonObject.getBigDecimal("oldVal");//旧表截止读数
        BigDecimal newVal = jsonObject.getBigDecimal("newVal");//新表起始读数
        String deviceVender = jsonObject.getString("deviceVender");
        DeviceChange deviceChange = EntityFactory.deviceChangeFactory(userId, timestamp, operatorId, oldNo, oldVal, newNo,
        newVal, 0, enprNo);
        Device oldDevice = deviceService.getByDeviceNoAndEnprNo(oldNo, enprNo);
        Device newDevice = deviceService.getByDeviceNoAndEnprNo(newNo, enprNo);
        Daycount latestDaycount = daycountDao.findLatestDaycountRecord(oldNo, enprNo);
        Daycount oldClear =new Daycount();
        /**
         * 旧表的daycount是最新旧表读数减去最近daycount的endValue
         */
        oldClear.setDeviceNo(oldNo);
        oldClear.setStartTime(latestDaycount.getEndTime());
        oldClear.setStartValue(latestDaycount.getEndValue());
        oldClear.setEndTime(timestamp);
        oldClear.setEndValue(oldVal);
        oldClear.setDayAmount(oldVal.subtract(latestDaycount.getEndValue()));
        oldClear.setDate(Calendar.getInstance().get(Calendar.DATE));
        oldClear.setState(0);
        /**
         * 新表的daycount是0
         */
        Daycount newClear =new Daycount();
        newClear.setDeviceNo(newNo);
        newClear.setStartTime(timestamp);
        newClear.setStartValue(newVal);
        newClear.setEndTime(timestamp);
        newClear.setEndValue(newVal);
        newClear.setDayAmount(new BigDecimal("0"));
        newClear.setDate(Calendar.getInstance().get(Calendar.DATE));
        newClear.setState(0);
        if(oldDevice != null && newDevice == null){
            try{
                Device device = (Device) oldDevice.clone();
                device.setDeviceNo(newNo);
                device.setValve(valve);
                device.setDeviceVender(deviceVender);
                device.setDeviceType(deviceType);
                device.setInstallDate((new java.sql.Date(new java.util.Date().getTime())));
                //事务，如果出现失败，添加删除操作都将回滚
                deviceChangeService.changeDevice(device, deviceChange, oldNo, enprNo, oldClear, newClear);
                jsonMap.put("code","200");
                jsonMap.put("info","更换水表成功");
            }catch (Exception e){
                e.printStackTrace();
                jsonMap.put("code","-1");
                jsonMap.put("info","更换水表失败");
            }
        }else {
            jsonMap.put("code","-1");
            jsonMap.put("info","不存在该旧表或新表已存在");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 查询表某一时间段得数据
     */
    @ResponseBody
    @PostMapping("/GetDeviceInData")
    public Object GetDeviceInData(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String start = startTime + " 00:00:00";
        String end = startTime + " 23:59:59";
        int rows = jsonObject.getInteger("rows");
        int page = jsonObject.getInteger("page");
        Object object = null;
        Device device = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo);
        if (device != null && device.getImei() != null){
            //NB水表
            try {
                Timestamp start1 = Timestamp.valueOf(start);
                Timestamp end1 = Timestamp.valueOf(end);
                Page<Daycount> res = daycountService.findPartDaycountPage(deviceNo,enprNo,start1,end1,rows,page);
//                List<Historydata> res = historydataService.getDataBetweenTime(device.getImei(),start,end);
//                List<NBHistoryData> ret = new ArrayList<>();
//                for(Historydata historydata : res){
//                    ret.add(Adapter.rawHisToNBHis(historydata));
//                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "查询成功");
                jsonMap.put("data", res);
            }catch (Exception e) {
                e.printStackTrace();
                jsonMap.put("code", "-1");
                jsonMap.put("info", "查询失败");
            }

        }
        object = JSONObject.toJSON(jsonMap);
        return object;

    }

    /**
     * 方法功能描述:查询任一水表的当月或者上月历史纪录。
     */
    @ResponseBody
    @PostMapping("/GetHistoryData")
    public Object getHistorydataByDeviceNo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        int isCurMon = jsonObject.getInteger("isCurMon");
        Object object = null;
        Device device = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo);
        if (device != null && device.getImei() != null) {
            String imei = device.getImei();
            //该表为NB水表，有imei号
            try {
                List<Historydata> res;
                if (isCurMon == 0) {
                    res = historydataService.getCurMonthData(imei);
                } else {
                    res = historydataService.getPreMonthData(imei);
                }
                List<NBHistoryData> ret = new ArrayList<>();
                for(Historydata historydata : res){
                    ret.add(Adapter.rawHisToNBHis(historydata));
                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "查询成功");
                jsonMap.put("data", ret);
            } catch (Exception e) {
                jsonMap.put("code", "-1");
                jsonMap.put("info", "查询失败");
            }
            object = JSONObject.toJSON(jsonMap);
        } else {
            //该表为集中器水表
            String qbttResult = HttpRequest.post("http://localhost:8089/QBTT/Device/QueryHistoryData")
                    .body(jsonObject.toString())
                    .execute()
                    .body();
            System.out.println(qbttResult);
            object = qbttResult;
        }
        return object;
    }

    /**
     * 查看水表的日用水量
     */
    @ResponseBody
    @PostMapping("/getDaycountPage")
    public Object getDaycountPage(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        int rows = jsonObject.getInteger("rows");
        int page = jsonObject.getInteger("page");
        try{
            Page<Daycount> pageList = daycountService.findDaycountPage(deviceNo, enprNo, rows, page);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", pageList);
        } catch (Exception e){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 查看水表的月用水量
     */
    @ResponseBody
    @PostMapping("/getMonthcountPage")
    public Object getMonthcountPage(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        int rows = jsonObject.getInteger("rows");
        int page = jsonObject.getInteger("page");
        try{
            Page<Monthcount> pageList = monthcountService.findMonthcountPage(deviceNo, enprNo, rows, page);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", pageList);
        } catch (Exception e){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }
}
