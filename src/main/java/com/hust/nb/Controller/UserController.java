package com.hust.nb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.Monthcost;
import com.hust.nb.Entity.User;
import com.hust.nb.Service.MonthcostService;
import com.hust.nb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
@Controller
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    MonthcostService monthcostService;


    /**
     * 方法功能描述:更改单个用户详细信息
     */
    @ResponseBody
    @PostMapping("/UpdateUserDetailInfo")
    @CrossOrigin
    public Object updateUser(@RequestBody String user) {
        User UserEntity = JSON.parseObject(user, User.class);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            userService.updateUser(UserEntity);
            jsonMap.put("code", "200");
            jsonMap.put("info", "编辑成功");
        } catch (Exception e) {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "编辑失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据用户ID获取用户详细信息
     */
    @ResponseBody
    @PostMapping("/GetUserById")
    @CrossOrigin
    public Object getUserUserId(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int userId = jsonObject.getInteger("userId");
        try {
            User user = userService.getByUserId(userId);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", user);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 查询用户的月水费记录
     */
    @ResponseBody
    @PostMapping("/getUserMonthcostPage")
    public Object getUserMonthcostPage(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int userId = jsonObject.getInteger("userId");
        int page = jsonObject.getInteger("page");
        int rows = jsonObject.getInteger("rows");
        try{
            Page<Monthcost> monthcosts = monthcostService.getMonthcostPage(userId, rows ,page);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", monthcosts);
        } catch (Exception e){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 查询用户的账目详情
     */
    @ResponseBody
    @PostMapping("/GetAccountHistory")
    public Object GetHistoryData(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int userId = Integer.parseInt(jsonObject.getString("userId"));
        int rows = jsonObject.getInteger("rows");
        int page = jsonObject.getInteger("page");
        try{
            Page<Monthcost> monthcosts = monthcostService.getAccountPage(userId, rows, page);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", monthcosts);
        } catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 用户缴费
     */
    @ResponseBody
    @PostMapping("/UpdateAccountBanlance")
    public Object updateMonthcount(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int userId = jsonObject.getInteger("userId");
        int saveMethod = jsonObject.getInteger("saveMethod");
        int operatorId = jsonObject.getInteger("operatorId");
        BigDecimal moneyChange = jsonObject.getBigDecimal("moneyChange");
        Date date = new Date();
        Timestamp time = new Timestamp(date.getTime());
        Monthcost monthcost = new Monthcost();
        monthcost.setUserId(userId);
        monthcost.setSaveMethod(saveMethod);
        monthcost.setOperatorId(operatorId);
        monthcost.setMoneyChange(moneyChange);
        User user = userService.getByUserId(monthcost.getUserId());
        monthcost.setAccountBalance(user.getAccountBalance().add(monthcost.getMoneyChange()));
        user.setAccountBalance(monthcost.getAccountBalance());
        monthcost.setDate(time);
        try {
            monthcostService.updateAccountBanlance(user, monthcost);
            jsonMap.put("code", "200");
            jsonMap.put("info", "编辑成功");
        } catch (Exception e) {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "编辑失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }
}
