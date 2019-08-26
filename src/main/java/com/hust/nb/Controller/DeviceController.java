package com.hust.nb.Controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Dao.DaycountDao;
import com.hust.nb.Dao.DeviceCheckDao;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.*;
import com.hust.nb.util.Adapter;
import com.hust.nb.util.BigDevicePropUtil;
import com.hust.nb.util.EntityFactory;
import com.hust.nb.util.GetDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    BigDevicePropUtil bigDevicePropUtil;


    @Autowired
    DeviceCheckDao deviceCheckDao;

    /**
     * 方法功能描述:获取水表详细信息
     */
    @ResponseBody
    @PostMapping("/GetDeviceInfo")
    public Object getDeviceByKey(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        int flag = jsonObject.getInteger("flag");//0查询运营中的表数据，1查询初装表数据
        try {
            if (flag == 0){
                Device device = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo);
                jsonMap.put("data", device);
            }else if (flag == 1){
                DeviceCheck d = deviceCheckDao.findByEnprNoAndDeviceNo(enprNo,deviceNo);
                jsonMap.put("data", d);
            }

            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");

        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
            jsonMap.put("data", "");
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
    public Object updateDevice(@RequestBody String device) {
        Device DeviceEntity = JSON.parseObject(device, Device.class);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            deviceService.updateDevice(DeviceEntity);
            jsonMap.put("code", "200");
            jsonMap.put("info", "更新成功");
        } catch (Exception e) {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "更新失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

//    /**
//     * 更换水表
//     */
//    @ResponseBody
//    @PostMapping("/ChangeDevice")
//    public Object changeDevice(@RequestBody String msg) {
//        Map<String, Object> jsonMap = new HashMap<>();
//        JSONObject jsonObject = JSONObject.parseObject(msg);
//        Timestamp timestamp = new Timestamp(new Date().getTime());
//        int userId = jsonObject.getInteger("userId");
//        int operatorId = jsonObject.getInteger("operatorId");
//        int valve = jsonObject.getInteger("valve");
//        int deviceType = jsonObject.getInteger("deviceType");
//        String oldNo = jsonObject.getString("oldNo");
//        String newNo = jsonObject.getString("newNo");
//        String enprNo = jsonObject.getString("enprNo");
//        BigDecimal oldVal = jsonObject.getBigDecimal("oldVal");//旧表截止读数
//        BigDecimal newVal = jsonObject.getBigDecimal("newVal");//新表起始读数
//        String deviceVender = jsonObject.getString("deviceVender");
//        DeviceChange deviceChange = EntityFactory.deviceChangeFactory(userId, timestamp, operatorId, oldNo, oldVal, newNo,
//                newVal, 0, enprNo);
//        Device oldDevice = deviceService.getByDeviceNoAndEnprNo(oldNo, enprNo);
//        Device newDevice = deviceService.getByDeviceNoAndEnprNo(newNo, enprNo);
//        Daycount latestDaycount = daycountDao.findLatestDaycountRecord(oldNo, enprNo);
//        Daycount oldClear = new Daycount();
//        /**
//         * 旧表的daycount是最新旧表读数减去最近daycount的endValue
//         */
//        oldClear.setDeviceNo(oldNo);
//        oldClear.setStartTime(latestDaycount.getEndTime());
//        oldClear.setStartValue(latestDaycount.getEndValue());
//        oldClear.setEndTime(timestamp);
//        oldClear.setEndValue(oldVal);
//        oldClear.setDayAmount(oldVal.subtract(latestDaycount.getEndValue()));
//        oldClear.setDate(Calendar.getInstance().get(Calendar.DATE));
//        oldClear.setState(0);
//        /**
//         * 新表的daycount是0
//         */
//        Daycount newClear = new Daycount();
//        newClear.setDeviceNo(newNo);
//        newClear.setStartTime(timestamp);
//        newClear.setStartValue(newVal);
//        newClear.setEndTime(timestamp);
//        newClear.setEndValue(newVal);
//        newClear.setDayAmount(new BigDecimal("0"));
//        newClear.setDate(Calendar.getInstance().get(Calendar.DATE));
//        newClear.setState(0);
//        if (oldDevice != null && newDevice == null) {
//            try {
//                Device device = (Device) oldDevice.clone();
//                device.setDeviceNo(newNo);
//                device.setValve(valve);
//                device.setDeviceVender(deviceVender);
//                device.setDeviceType(deviceType);
//                device.setInstallDate((new java.sql.Date(new java.util.Date().getTime())));
//                //事务，如果出现失败，添加删除操作都将回滚
//                deviceChangeService.changeDevice(device, deviceChange, oldNo, enprNo, oldClear, newClear);
//                jsonMap.put("code", "200");
//                jsonMap.put("info", "更换水表成功");
//            } catch (Exception e) {
//                e.printStackTrace();
//                jsonMap.put("code", "-1");
//                jsonMap.put("info", "更换水表失败");
//            }
//        } else {
//            jsonMap.put("code", "-1");
//            jsonMap.put("info", "不存在该旧表或新表已存在");
//        }
//        Object object = JSONObject.toJSON(jsonMap);
//        return object;
//    }

    /**
     * 更换水表
     */
    @ResponseBody
    @PostMapping("/ChangeDevice")
    public Object changeDevice(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        int userId = jsonObject.getInteger("userId");
        int operatorId = jsonObject.getInteger("operatorId");
        String oldNo = jsonObject.getString("oldNo");
        String newNo = jsonObject.getString("newNo");
        BigDecimal oldVal = jsonObject.getBigDecimal("oldVal");//旧表截止读数
        BigDecimal newVal = jsonObject.getBigDecimal("newVal");//新表起始读数
        String enprNo = jsonObject.getString("enprNo");
        DeviceChange deviceChange = EntityFactory.deviceChangeFactory(userId, timestamp, operatorId, oldNo, oldVal, newNo,
                newVal, 0, enprNo);
        Device oldDevice = deviceService.getByDeviceNoAndEnprNo(oldNo, enprNo);
        Device newDevice = deviceService.getByDeviceNoAndEnprNo(newNo, enprNo);
        if (oldDevice != null && newDevice != null) {
            try {
                oldDevice.setUserId(null);
                newDevice.setUserId(userId);
                deviceChangeService.changeDevice(oldDevice, newDevice, deviceChange);
                jsonMap.put("code", "200");
                jsonMap.put("info", "更换水表成功");
            } catch (Exception e) {
                e.printStackTrace();
                jsonMap.put("code", "-1");
                jsonMap.put("info", "更换水表失败");
            }
        } else {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "旧表或新表不存在");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 查询表某一时间段得数据
     */
    @ResponseBody
    @PostMapping("/GetDeviceInData")
    public Object GetDeviceInData(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        String start = startTime + " 00:00:00";
        String end = endTime + " 23:59:59";
        int rows = jsonObject.getInteger("rows");
        int page = jsonObject.getInteger("page");
        Object object = null;
        Device device = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo);
        if (device != null && device.getImei() != null) {
            //NB水表
            try {
                Timestamp start1 = Timestamp.valueOf(start);
                Timestamp end1 = Timestamp.valueOf(end);
                Page<Daycount> res = daycountService.findPartDaycountPage(deviceNo, enprNo, start1, end1, rows, page);
                jsonMap.put("code", "200");
                jsonMap.put("info", "查询成功");
                jsonMap.put("data", res);
            } catch (Exception e) {
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
                for (Historydata historydata : res) {
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
        try {
            Page<Daycount> pageList = daycountService.findDaycountPage(deviceNo, enprNo, rows, page);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", pageList);
        } catch (Exception e) {
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
        try {
            Page<Monthcount> pageList = monthcountService.findMonthcountPage(deviceNo, enprNo, rows, page);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", pageList);
        } catch (Exception e) {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     * 定时功能，更新
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void updateNBDevice(){
        Map<String, Object> map = new HashMap<>();
        map.put("flag",0);
        map.put("position",0);
        map.put("count",500);
        map.put("check",1);
        try {
            getSZNBdevice(map);
            map.put("check",0);
            getSZNBdevice(map);
            System.out.println("更新成功");
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    /**
     * 读取数据接口(SZQH
     * 提供更新功能
     */
    public void getSZNBdevice(Map<String, Object> args) {
//        JSONObject jsonObject = JSONObject.parseObject(msg);
        int flag = Integer.parseInt(args.get("flag").toString());//获取flag，0为获得全部设备信息，1为根据地址获得表信息，2为根据开始日期获得表信息（起始日期,默认昨日
        String position = args.get("position").toString();//获取数据得开始号码，默认0
        String counts = args.get("count").toString();//获取数据得记录数默认50，最大不超过500
        int check = Integer.parseInt(args.get("check").toString());//如果只是对表进行数据读取出厂检测，为1，正式使用为0
        JSONObject paramMap = new JSONObject();
        String companyalias = "hbhxzn03";
        String loginname = "YWRtaW5oYmh4em4wMw==";
        String password = "YWRtaW5oYmh4em4wMw==";
        paramMap.put("companyalias", companyalias);
        paramMap.put("loginname", loginname);
        paramMap.put("password", password);
        paramMap.put("position", position);
        paramMap.put("counts", counts);
        RestTemplate restTemplate = new RestTemplate();
        String res = "";
        try {
            String url = "http://118.25.217.87/emac_android_connect/get_hbhxznas_all_data.php";


        JSONObject object = new JSONObject();
        if (flag == 0) {//获得所哟有信息,
            paramMap.put("operatecmd", "getdata");
            res = restTemplate.postForEntity(url, paramMap, String.class).getBody();
            object = JSONObject.parseObject(res);

        }else if (flag == 1){//根据地址获得表信息
            String macAddr = args.get("macAddr").toString();
            paramMap.put("mac_addr",macAddr);
            paramMap.put("operatecmd","getdatabyaddr");
            res = restTemplate.postForEntity(url, paramMap, String.class).getBody();
            object = JSONObject.parseObject(res);
        }else if (flag == 2){
            String date = args.get("startTime").toString();
            paramMap.put("startdatetime",date);
            paramMap.put("operatecmd","getdatabytime");
            res = restTemplate.postForEntity(url, paramMap, String.class).getBody();
            object = JSONObject.parseObject(res);
        }

            if (check == 0){
                //得到数据，插入数据库,
                JSONArray data = object.getJSONArray("message");
                for (int i = 0; i < data.size(); i++){
                    Device device = deviceService.findByImei(data.getJSONObject(i).get("imei").toString());
                    if (device != null){
                        if (device.getBatteryVoltage() != null){
                            if (!device.getReadTime().equals(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()))
                                    ||device.getValve() != (Integer.valueOf(data.getJSONObject(i).get("switch_status").toString()))){
                                //插入historyData表
                                Historydata h = new Historydata();
                                h.setDeviceNo(device.getDeviceNo());
                                h.setDeviceValue(new BigDecimal(data.getJSONObject(i).get("ton").toString()));
                                h.setImei(device.getImei());
                                h.setReadTime(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()));
                                if (data.getJSONObject(i).get("rssi")!= null){
                                    h.setSignalQuality(Integer.parseInt(data.getJSONObject(i).get("rssi").toString()));
                                }
                                try {
                                    historydataService.save(h);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            if ((!device.getReadTime().equals(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()))
                                    ||device.getValve() != (Integer.valueOf(data.getJSONObject(i).get("switch_status").toString()))
                                    ||!device.getBatteryVoltage().equals(data.getJSONObject(i).get("batteryval").toString()))
                                    &&data.getJSONObject(i).get("batteryval").toString() != null){
                                device.setBatteryVoltage(data.getJSONObject(i).get("batteryval").toString());
                                device.setReadValue(new BigDecimal(data.getJSONObject(i).get("ton").toString()));
                                if (data.getJSONObject(i).get("rssi")!= null){
                                    device.setRssi(data.getJSONObject(i).get("rssi").toString());
                                }
                                device.setMacAddr(data.getJSONObject(i).get("mac_addr").toString());
                                device.setNbDeviceType(Integer.parseInt(data.getJSONObject(i).get("devicetype").toString()));
                                device.setPinStatus(Integer.parseInt(data.getJSONObject(i).get("pin_status").toString()));
                                device.setReadTime(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()));
                                device.setValve(Integer.valueOf(data.getJSONObject(i).get("switch_status").toString()));
                                deviceService.addDevice(device);
                            }
                        }else if (device.getBatteryVoltage() == null && data.getJSONObject(i).get("batteryval").toString() != null){
                            device.setBatteryVoltage(data.getJSONObject(i).get("batteryval").toString());
                            device.setReadValue(new BigDecimal(data.getJSONObject(i).get("ton").toString()));
                            if (data.getJSONObject(i).get("rssi")!= null){
                                device.setRssi(data.getJSONObject(i).get("rssi").toString());
                            }
                            device.setMacAddr(data.getJSONObject(i).get("mac_addr").toString());
                            device.setNbDeviceType(Integer.parseInt(data.getJSONObject(i).get("devicetype").toString()));
                            device.setPinStatus(Integer.parseInt(data.getJSONObject(i).get("pin_status").toString()));
                            device.setReadTime(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()));
                            device.setValve(Integer.valueOf(data.getJSONObject(i).get("switch_status").toString()));
                            deviceService.addDevice(device);
                            //插入historyData表
                            Historydata h = new Historydata();
                            h.setDeviceNo(device.getDeviceNo());
                            h.setDeviceValue(new BigDecimal(data.getJSONObject(i).get("ton").toString()));
                            h.setImei(device.getImei());
                            h.setReadTime(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()));
                            if (data.getJSONObject(i).get("rssi")!= null){
                                h.setSignalQuality(Integer.parseInt(data.getJSONObject(i).get("rssi").toString()));
                            }
                            try {
                                historydataService.save(h);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }


            }

            }else if (check == 1){
                JSONArray data = object.getJSONArray("message");
                for (int i = 0; i < data.size(); i++){
                    String imei = data.getJSONObject(i).get("imei").toString();
                    DeviceCheck device = deviceCheckDao.findByImei(imei);
//                    System.out.println(data.getJSONObject(i).get("batteryval").toString());
                    if (device !=null){
                        if (device.getBatteryVoltage() == null && data.getJSONObject(i).get("batteryval").toString() != null){
                            device.setBatteryVoltage(data.getJSONObject(i).get("batteryval").toString());
                            device.setReadValue(new BigDecimal(data.getJSONObject(i).get("ton").toString()));
                            if (data.getJSONObject(i).get("rssi") != null) {
                                device.setRssi(data.getJSONObject(i).get("rssi").toString());
                            }
                            device.setMacAddr(data.getJSONObject(i).get("mac_addr").toString());
                            device.setNbDeviceType(Integer.parseInt(data.getJSONObject(i).get("devicetype").toString()));
                            device.setPinStatus(Integer.parseInt(data.getJSONObject(i).get("pin_status").toString()));
                            device.setReadTime(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()));
                            device.setValve(Integer.valueOf(data.getJSONObject(i).get("switch_status").toString()));
                            deviceCheckDao.save(device);
                        }else
                        if (device.getBatteryVoltage() != null){
                            if ((!device.getReadTime().equals(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()))
                                    ||device.getValve() != (Integer.valueOf(data.getJSONObject(i).get("switch_status").toString()))
                                    ||!device.getBatteryVoltage().equals(data.getJSONObject(i).get("batteryval").toString()))
                                    &&data.getJSONObject(i).get("batteryval").toString() != null) {
                                device.setBatteryVoltage(data.getJSONObject(i).get("batteryval").toString());
                                device.setReadValue(new BigDecimal(data.getJSONObject(i).get("ton").toString()));
                                if (data.getJSONObject(i).get("rssi") != null) {
                                    device.setRssi(data.getJSONObject(i).get("rssi").toString());
                                }
                                device.setMacAddr(data.getJSONObject(i).get("mac_addr").toString());
                                device.setNbDeviceType(Integer.parseInt(data.getJSONObject(i).get("devicetype").toString()));
                                device.setPinStatus(Integer.parseInt(data.getJSONObject(i).get("pin_status").toString()));
                                device.setReadTime(Timestamp.valueOf(data.getJSONObject(i).get("node_updatetime").toString()));
                                device.setValve(Integer.valueOf(data.getJSONObject(i).get("switch_status").toString()));
                                deviceCheckDao.save(device);
                            }
                        }
                    }


                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        Object o = JSONObject.toJSON(object);
//        return o;
    }


    /**
     * 设备控制接口(SZQH
     */
    @ResponseBody
    @PostMapping("/getCommandToDevice")
    public Object getCommandToDevice(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Integer command = jsonObject.getInteger("command");//命令0代表从设备获取数据，1代表初始化底数 2代表阀门控制，1，2需要给参数value01即底数参数或状态参数
        String macAddr = jsonObject.getString("macAddr");
        String url = bigDevicePropUtil.getNbDeviceUrl() + "execute_hbhxznas_cmd.php";
        JSONObject paramMap = new JSONObject();
        String companyalias = "hbhxzn03";
        String loginname = "YWRtaW5oYmh4em4wMw==";
        String password = "YWRtaW5oYmh4em4wMw==";
        paramMap.put("companyalias", companyalias);
        paramMap.put("loginname", loginname);
        paramMap.put("password", password);
        paramMap.put("mac_addr", macAddr);
        RestTemplate restTemplate = new RestTemplate();
        String res = "";
        JSONObject object = new JSONObject();
        try {
            if (command == 0){
                paramMap.put("operatecmd", "getton");
                res = restTemplate.postForEntity(url,paramMap,String.class).getBody();
                object = JSONObject.parseObject(res);
                System.out.println("getton");
            }else if (command == 1){
                Integer value01 = jsonObject.getInteger("value01");
//                String value02 = jsonObject.getString("value02");//备用
                paramMap.put("operatecmd","initton");
                paramMap.put("value01",value01);
                res = restTemplate.postForEntity(url,paramMap,String.class).getBody();
                object = JSONObject.parseObject(res);
                System.out.println("initton");
            }else if (command == 2){
                Integer value01 = jsonObject.getInteger("value01");
                paramMap.put("operatecmd","switchtip");
                paramMap.put("value01",value01);
                res = restTemplate.postForEntity(url,paramMap,String.class).getBody();
                object = JSONObject.parseObject(res);
                //更新阀门状态
                String url1 = "http://118.25.217.87/emac_android_connect/get_hbhxznas_all_data.php";
                paramMap.put("operatecmd","getdatabyaddr");
                String res1 = restTemplate.postForEntity(url1, paramMap, String.class).getBody();
                JSONObject object1 = JSONObject.parseObject(res1);
                JSONArray data = object1.getJSONArray("message");
                try {
                    DeviceCheck device = deviceCheckDao.findByImei(data.getJSONObject(0).get("imei").toString());
                    if (device != null){
                        device.setValve(Integer.valueOf(data.getJSONObject(0).get("switch_status").toString()));
                        deviceCheckDao.save(device);
                    }
                    Device device1 = deviceService.findByImei(data.getJSONObject(0).get("imei").toString());
                    if (device1!= null){
                        device1.setValve(Integer.valueOf(data.getJSONObject(0).get("switch_status").toString()));
                        deviceService.updateDevice(device1);
                    }
                    System.out.println("更新成功");
                }catch (Exception e){
                    e.printStackTrace();
                }


                System.out.println("switchtip");
            }
            jsonMap.put("code","200");
            jsonMap.put("info","命令成功");
            jsonMap.put("data",object);
        }catch (Exception e){
            jsonMap.put("code","-1");
            jsonMap.put("info","命令失败");
        }
        Object o =JSONObject.toJSON(jsonMap);
        return o;

    }

    public void updataSwitch(){

    }
}
