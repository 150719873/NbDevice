package com.hust.nb.Controller;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.Historydata;
import com.hust.nb.Service.HistorydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Controller
public class HistorydataController {
    @Autowired
    HistorydataService historydataService;

    /**
     * 方法功能描述:分页查询某一水表历史纪录。
     */
    @ResponseBody
    @PostMapping("/NB/GetHistoryData")
    public Object getHistorydataByDeviceNo(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String qbttResult= HttpRequest.post("http://localhost:8088/QBTT/Device/QueryHistoryData")
                .body(jsonObject.toString())
                .execute()
                .body();
        System.out.println(qbttResult);
        String imei = jsonObject.getString("imei");
        int page = jsonObject.getInteger("page");
        int rows = jsonObject.getInteger("rows");
        Page<Historydata> pages = historydataService.getHisPageByImei(imei, page ,rows);
        if (pages!=null){
            jsonMap.put("code","200");
            jsonMap.put("info","查询成功");
            jsonMap.put("data",pages);
        }else{
            jsonMap.put("code","-1");
            jsonMap.put("info","查询失败");
            jsonMap.put("data","");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }
}
