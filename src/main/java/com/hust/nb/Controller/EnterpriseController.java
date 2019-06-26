package com.hust.nb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.ChargeLevel;
import com.hust.nb.Entity.Enterprise;
import com.hust.nb.Service.ChargeLevelService;
import com.hust.nb.Service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@CrossOrigin
@Controller
public class EnterpriseController {

    @Autowired
    ChargeLevelService chargeLevelService;

    @Autowired
    EnterpriseService enterpriseService;

    /**
     * 获取该水司下的所有阶梯水价
     */
    @ResponseBody
    @PostMapping("/GetPriceInfoByEnprNo")//获取所有信息
    public Object getPriceInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        List<ChargeLevel> levelList;
        String enprNo = jsonObject.getString("enprNo");
        try {
            levelList = chargeLevelService.getAllChargeLevelByEnprNo(enprNo);
            jsonMap.put("code", "200");
            jsonMap.put("info", "获取成功");
            jsonMap.put("data", levelList);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "获取失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 修改阶梯水价
     */
    @ResponseBody
    @PostMapping("/UpdateChargeLevelInfo")//修改阶梯水价
    public Object updatePriceInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String msg1 = jsonObject.getString("waterPrice");
        ChargeLevel changeEntity = JSON.parseObject(msg1, ChargeLevel.class);
        String enprNo = changeEntity.getEnprNo();
        int type = changeEntity.getType();
        ChargeLevel chargeLevelEntity = chargeLevelService.getByEnprNoAndType(enprNo, type);
        chargeLevelEntity.setMin(changeEntity.getMin());
        chargeLevelEntity.setMinCharge(changeEntity.getMinCharge());
        chargeLevelEntity.setFirst(changeEntity.getFirst());
        chargeLevelEntity.setSecond(changeEntity.getSecond());
        chargeLevelEntity.setThird(changeEntity.getThird());
        chargeLevelEntity.setFourth(changeEntity.getFourth());
        chargeLevelEntity.setFifth(changeEntity.getFifth());
        chargeLevelEntity.setFirstEdge(changeEntity.getFirstEdge());
        chargeLevelEntity.setSecondEdge(changeEntity.getSecondEdge());
        chargeLevelEntity.setThirdEdge(changeEntity.getThirdEdge());
        chargeLevelEntity.setFourthEdge(changeEntity.getFourthEdge());
        chargeLevelEntity.setFifthEdge(changeEntity.getFifthEdge());
        chargeLevelEntity.setSixthEdge(changeEntity.getSixthEdge());
        try {
            chargeLevelService.save(chargeLevelEntity);
            jsonMap.put("code", "200");
            jsonMap.put("info", "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "修改失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     * 设置和修改计费时间和收费类型
     */
    @ResponseBody
    @PostMapping("/updateCronAndType")
    public Object updateCronAndType(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        int cron = jsonObject.getInteger("cron");
        int costType = jsonObject.getInteger("costType");
        String enprName = jsonObject.getString("enprName");
        Enterprise enterprise = enterpriseService.findByEnprNo(enprNo);

        if (cron != 0){
            enterprise.setCron("0 0 0 "+cron+" * ?");
            enterprise.setCostType(costType);
            enterprise.setEnprName(enprName);
        }else{
            enterprise.setCron("0 0 0 L * ?");
            enterprise.setCostType(costType);
            enterprise.setEnprName(enprName);
        }
        try {
            enterpriseService.saveEnterpriseCronAndCostType(enterprise);
            jsonMap.put("code", "200");
            jsonMap.put("info", "设置修改成功");
        }catch (Exception e){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "设置修改失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

}

