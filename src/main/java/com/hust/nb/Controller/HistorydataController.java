package com.hust.nb.Controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.Historydata;
import com.hust.nb.Service.DeviceService;
import com.hust.nb.Service.HistorydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Controller
@CrossOrigin
public class HistorydataController {
    @Autowired
    HistorydataService historydataService;

    @Autowired
    DeviceService deviceService;

    /**
     * 方法功能描述:查询任一水表的当月或者上月历史纪录。
     */
    @ResponseBody
    @PostMapping("/NB/GetHistoryData")
    public Object getHistorydataByDeviceNo(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        int isCurMon = jsonObject.getInteger("isCurMon");
        String imei = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo).getImei();
        Object object = null;
        if(!imei.equals("")){
            //该表为NB水表，有iemi号
            try{
                List<Historydata> res;
                if(isCurMon == 1){
                    res = historydataService.getCurMonthData(imei);
                } else {
                    res = historydataService.getPreMonthData(imei);
                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "查询成功");
                jsonMap.put("data", res);
            } catch (Exception e){
                jsonMap.put("code","-1");
                jsonMap.put("info","查询失败");
                jsonMap.put("data","");
            }
            object = JSONObject.toJSON(jsonMap);

        } else {
            //该表为集中器水表
            String qbttResult= HttpRequest.post("http://localhost:8088/QBTT/Device/QueryHistoryData")
                    .body(jsonObject.toString())
                    .execute()
                    .body();
            System.out.println(qbttResult);
            object = qbttResult;
        }
        return object;
    }
}
