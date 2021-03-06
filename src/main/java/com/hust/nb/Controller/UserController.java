package com.hust.nb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Dao.UserDao;
import com.hust.nb.Entity.Monthcost;
import com.hust.nb.Entity.Notice;
import com.hust.nb.Entity.RepairItem;
import com.hust.nb.Entity.User;
import com.hust.nb.Service.MonthcostService;
import com.hust.nb.Service.NoticeService;
import com.hust.nb.Service.RepairService;
import com.hust.nb.Service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    @Autowired
    RepairService repairService;

    @Autowired
    NoticeService noticeService;


    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    /**
     * 方法功能描述:更改单个用户详细信息
     */
    @ResponseBody
    @PostMapping("/查看")
    @CrossOrigin
    public Object updateUser(@RequestBody String user) {
        User UserEntity = JSON.parseObject(user, User.class);
        Map<String, Object> jsonMap = new HashMap<>();
        try {
            userService.updateUser(UserEntity);
            jsonMap.put("code", "200");
            jsonMap.put("info", "编辑成功");
        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "编辑失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * @param msg 报修受理
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/RepairItem")
    public Object inputRepairInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        String userNo = jsonObject.getString("userNo");
        String userName = jsonObject.getString("userName");
        String userTel = jsonObject.getString("userTel");
        Integer feedbackType = jsonObject.getInteger("feedbackType");
        String contents = jsonObject.getString("contents");
        Integer blockId = jsonObject.getInteger("blockId");
        Integer state = 1;
        String addr = jsonObject.getString("addr");
        Timestamp repairTime = Timestamp.valueOf(LocalDateTime.now()) ;
        Integer communityId = repairService.getCommunityIdByBlockId(blockId);
        String communityName = repairService.getCommunityNameByCommunityId(communityId);
        String deviceNo = repairService.getDeviceNoByUserNoAndEnprNo(userNo, enprNo);
        RepairItem repairItem =new RepairItem();
        repairItem.setUserTel(userTel);
        repairItem.setFeedbackType(feedbackType);
        repairItem.setContents(contents);
        repairItem.setRepairTime(repairTime);
        repairItem.setEnprNo(enprNo);
        repairItem.setUserNo(userNo);
        repairItem.setUserName(userName);
        repairItem.setState(state);
        repairItem.setCommunityId(communityId);
        repairItem.setDeviceNo(deviceNo);
        repairItem.setAddr(addr);
        repairItem.setBlockId(blockId);
        repairItem.setCommunityName(communityName);
        try {
            repairService.saveRepairItem(repairItem);
            jsonMap.put("code", "200");
            jsonMap.put("info", "报修受理成功");
            jsonMap.put("data",repairItem);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "导入失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }



    /**
     * @param msg 显示用户报修信息
     */
    @ResponseBody
    @PostMapping("/showRepairInfo")
    @CrossOrigin
    public Object showRepairInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Integer userId = jsonObject.getInteger("userId");
        RepairItem repairItem = repairService.getbyUserId(userId);
        try {
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data",repairItem);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * @param msg 撤回用户报修信息
     */
    @ResponseBody
    @PostMapping("/deleteRepairInfo")
    @CrossOrigin
    public Object deleteRepairInfo(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Integer userId = jsonObject.getInteger("userId");
        RepairItem repairItem = repairService.getbyUserId(userId);
        try {
            repairService.deleteRepairInfo(repairItem);
            jsonMap.put("code", "200");
            jsonMap.put("info", "删除成功");

        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "删除失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * @param msg 查看公告
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getNotice")
    public Object getNotice(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        int rows = jsonObject.getInteger("rows");
        int page = Integer.parseInt(jsonObject.getString("page"));
        try {
            Pageable pageable = PageRequest.of(page - 1, rows);
            Page<Notice> page04 = noticeService.getByEnprNo(enprNo, pageable);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查看成功");
            jsonMap.put("data", page04);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查看失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 用户登录
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/UserLogin")
    public Object userLogin(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        String userNo = jsonObject.getString("userNo");
        String password = jsonObject.getString("password");
        try {
            User user = userService.getByUserNoAndPassword(userNo, password);
            if(user != null){
                jsonMap.put("code", "200");
                jsonMap.put("info", user);
            } else {
                jsonMap.put("code", "-1");
                jsonMap.put("info", "登陆失败，不存在此用户");
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查看失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 用户密码修改
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/modifyUserPass")
    public Object modifyUserPass(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int userId = jsonObject.getInteger("userPassId");
        String newPass1 = jsonObject.getString("newPassword1");
        String newPass2 = jsonObject.getString("newPassword2");
        if(StringUtils.isEmpty(newPass1) || StringUtils.isEmpty(newPass2) || !newPass1.equals(newPass2)){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "修改失败");
        } else {
            User user = userService.getByUserId(userId);
            user.setPassword(newPass1);
            try{
                userService.updateUser(user);
                jsonMap.put("code", "200");
                jsonMap.put("info", "修改成功");
            } catch (Exception e){
                jsonMap.put("code", "-1");
                jsonMap.put("info", "修改失败");
            }
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }




}
