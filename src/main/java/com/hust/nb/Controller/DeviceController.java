package com.hust.nb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.Device;
import com.hust.nb.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Controller
@CrossOrigin
@RequestMapping("/Device")
public class DeviceController {

    @Autowired
    DeviceService deviceService;

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
        Device device = deviceService.getByDeviceNoAndEnprNo(enprNo, deviceNo);
        if (device!=null){
            jsonMap.put("code","200");
            jsonMap.put("info","查询成功");
            jsonMap.put("data",device);
        }else{
            jsonMap.put("code","-1");
            jsonMap.put("info","查询失败");
            jsonMap.put("data","");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:查询水表的列表。
     * 分页
     */
    @ResponseBody
    @PostMapping("/GetDevicesInfo")
    public Object queryForList(@RequestBody String msg) {
        Map<String,Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        int page = jsonObject.getInteger("page");
        int rows = jsonObject.getInteger("rows");
        Page<Device> pages = deviceService.getDevicePageByDeviceNoAndEnprNo(deviceNo, enprNo, page, rows);
        if(pages.getSize() != 0){
            jsonMap.put("code","200");
            jsonMap.put("info","查询成功");
            jsonMap.put("data",pages);
        } else {
            jsonMap.put("code","-1");
            jsonMap.put("info","查询失败");
            jsonMap.put("data","");
        }
        return JSONObject.toJSON(jsonMap);
    }

    /**
     * 方法功能描述:修改水表信息
     */
    @ResponseBody
    @PostMapping("/NB/UpdateDeviceDetailInfo")
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
}
