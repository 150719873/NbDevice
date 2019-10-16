package com.hust.nb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Dao.OperatorDao;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.ChargeLevelService;
import com.hust.nb.Service.EnterpriseCollectionService;
import com.hust.nb.Service.EnterpriseService;
import com.hust.nb.Service.WxService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.sql.Timestamp;
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

    @Autowired
    EnterpriseCollectionService enterpriseCollectionService;

    @Autowired
    OperatorDao operatorDao;

    @Autowired
    WxService wxService;

    private static Logger logger = LoggerFactory.getLogger(EnterpriseController.class);

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
            logger.error(e.getMessage());
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
        if (chargeLevelEntity == null){
             chargeLevelEntity = new ChargeLevel();
        }
        chargeLevelEntity.setType(changeEntity.getType());
        chargeLevelEntity.setEnprNo(enprNo);
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "设置修改失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/GetAllEnprMsg")
    public Object getAllEnprMsg(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int userType = jsonObject.getInteger("userType");
        if (userType == 0) {
            List<Enterprise> enterprises = enterpriseService.getAllEnpr();
            List<Map> mapList = new ArrayList<>();
            if (enterprises.size() != 0) {
                for (Enterprise e : enterprises) {
                    List<Integer> list = enterpriseCollectionService.selectAllType(e.getEnprNo());
                    List<Operator> operators = operatorDao.findByEnprNoAndUserType(e.getEnprNo(), 1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("enprNo", e.getEnprNo());
                    map.put("createTime", e.getCreateTime());
                    map.put("enprName", e.getEnprName());
                    map.put("id", e.getId());
                    map.put("collectionType", list);
                    map.put("operatorName",operators.get(0).getOperatorName());
                    map.put("password", operators.get(0).getPassword());
                    map.put("userName", operators.get(0).getUserName());
                    map.put("operatorName2",operators.get(1).getOperatorName());
                    map.put("password2", operators.get(1).getPassword());
                    map.put("userName2", operators.get(1).getUserName());
                    mapList.add(map);
                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "查询成功");
                jsonMap.put("data", mapList);
            } else {
                logger.warn("无水司");
                jsonMap.put("code", "-2");
                jsonMap.put("info", "无水司");
            }
        } else {
            logger.warn("权限不足");
            jsonMap.put("code", "-1");
            jsonMap.put("info", "权限不足");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    @CrossOrigin
    @ResponseBody
    @PostMapping("/AddOrModifyEnprMsg")
    public Object addOrModifyEnprMsg(@RequestBody String operatorEntity) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(operatorEntity);
        int userType = Integer.parseInt(jsonObject.getString("userType"));
        if (userType == 0) {
            String enprNo = jsonObject.getString("enprNo");
            String enprName = jsonObject.getString("enprName");
            String collectionType = jsonObject.getString("collectionType");
            String[] collections = collectionType.split("");
            Enterprise enterprise = enterpriseService.findByEnprNo(enprNo);
            if (enterprise == null) {//不存在，为添加操作
                Enterprise newENpr = new Enterprise();
                newENpr.setEnprNo(enprNo);
                newENpr.setEnprName(enprName);
                newENpr.setCostType(0);
                newENpr.setCron("0 0 0 28 * ?");
                Timestamp createTime = new Timestamp(System.currentTimeMillis());
                newENpr.setCreateTime(createTime);
                try {
                    enterpriseService.addEnpr(newENpr);
                } catch (Exception e) {
                    e.printStackTrace();
                    jsonMap.put("code", "-2");
                    jsonMap.put("info", "添加失败");
                }
                //为新水司添加配套的水司管理员
                Operator operator = new Operator();
                Operator operator2 = new Operator();
                String userName = enprNo + "Admin";
                operator.setUserName(userName);
                userName = enprNo + "Admin2";
                operator2.setUserName(userName);
                String password = enprNo + "123";
                operator.setPassword(password);
                operator2.setPassword(password);
                operator.setUserType(1);
                operator2.setUserType(1);
                operator.setManageCommunity("所有");
                operator2.setManageCommunity("所有");
                operator.setEnprNo(enprNo);
                operator2.setEnprNo(enprNo);
                try {
                    operatorDao.save(operator);
                    operatorDao.save(operator2);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    jsonMap.put("code", "-2");
                    jsonMap.put("info", "添加失败");
                }
                int len = collections.length;
                for (int i = 0; i < len; i++) {
                    EnterCollectType ec = new EnterCollectType();
                    ec.setEnprNo(enprNo);
                    ec.setTypeId(Integer.valueOf(collections[i]));
                    enterpriseCollectionService.saveEntity(ec);
                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "添加成功");
            } else {//存在，为修改操作
                //修改支持的集中器类型
                enterpriseCollectionService.deleteAllByEnprNo(enprNo);
                int len = collections.length;
                for (int i = 0; i < len; i++) {
                    EnterCollectType ec = new EnterCollectType();
                    ec.setEnprNo(enprNo);
                    ec.setTypeId(Integer.valueOf(collections[i]));
                    enterpriseCollectionService.saveEntity(ec);
                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "修改成功");
            }
        } else {
            logger.warn("权限不足");
            jsonMap.put("code", "-1");
            jsonMap.put("info", "权限不足");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 设置水司微信支付信息
     */
    @ResponseBody
    @PostMapping("/wxConfig")
    public Object uptWxConfig(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        String appId = jsonObject.getString("appId");
        String key = jsonObject.getString("key");
        String mchId = jsonObject.getString("mchId");
        try{
            if(StringUtils.isNotEmpty(enprNo)){
                WxConfig wxConfig = wxService.findByEnprNo(enprNo);
                if(wxConfig != null){
                    wxConfig.setMchId(mchId);
                    wxConfig.setKey(key);
                    wxConfig.setEnprNo(enprNo);
                    wxConfig.setAppId(appId);
                    wxService.save(wxConfig);
                    jsonMap.put("code", "200");
                    jsonMap.put("info", "修改成功");

                } else {
                    WxConfig wxConfig1 = new WxConfig();
                    wxConfig1.setEnprNo(enprNo);
                    wxConfig1.setAppId(appId);
                    wxConfig1.setKey(key);
                    wxConfig1.setMchId(mchId);
                    wxService.save(wxConfig);
                    jsonMap.put("code", "200");
                    jsonMap.put("info", "修改成功");
                }
            } else {
                jsonMap.put("code", "-1");
                jsonMap.put("info", "参数错误");
            }
        } catch (Exception e){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "内部错误");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

}

