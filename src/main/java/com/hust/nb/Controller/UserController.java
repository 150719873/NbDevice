package com.hust.nb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.User;
import com.hust.nb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
@Controller
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;


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
     * 方法功能描述:根据用户ID查找用户
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
}
