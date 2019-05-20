package com.hust.nb.Controller;

import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.BlockService;
import com.hust.nb.Service.CommunityService;
import com.hust.nb.Service.DeviceService;
import com.hust.nb.Service.RegionService;
import com.hust.nb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Controller
@RequestMapping("/Operate")
@CrossOrigin
public class OperateController {
    @Autowired
    RegionService regionService;

    @Autowired
    CommunityService communityService;

    @Autowired
    BlockService blockService;

    @Autowired
    UserService userService;

    @Autowired
    DeviceService deviceService;

    /**
     * 获取水司的全部区域信息
     */
    @ResponseBody
    @PostMapping("/GetRegionInfo")
    public Object getRegionInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        try {
            List<Region> regions = regionService.getByEnprNo(enprNo);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", regions);
        } catch (Exception e) {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     * 方法功能描述:根据区域ID小区以及楼栋信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/GetInfoByRegion")
    public Object getAreaInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int regionId = jsonObject.getInteger("regionId");
        Map<String, Object> commuBlockMap = new HashMap<>();
        List<Community> communities = communityService.getByRegionId(regionId);
        if (communities.size() != 0) {
            try {
                for (Community community : communities) {
                    String communityName = community.getCommunityName();
                    List<Block> blocks = blockService.getByCommunityId(community.getCommunityId());
                    commuBlockMap.put(communityName, blocks);
                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "查询成功");
                jsonMap.put("data", commuBlockMap);
            } catch (Exception e) {
                e.printStackTrace();
                jsonMap.put("code", "-1");
                jsonMap.put("info", "查询失败");
            }
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据楼栋获取用户信息
     */
    @ResponseBody
    @PostMapping("/GetUserInfoByBlock")
    @CrossOrigin
    public Object getUserInfoByBlock(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int blockId = Integer.parseInt(jsonObject.getString("blockId"));
        int rows = Integer.parseInt(jsonObject.getString("rows"));
        int page = Integer.parseInt(jsonObject.getString("page"));
        Page<User> userPage = userService.getUserPageByBlockId(blockId, page, rows);
        if (userPage.hasContent()) {
            List<Object> userDetailList = new ArrayList<>();
            long total = userPage.getTotalElements();
            for (User user : userPage.getContent()) {
                userDetailList.add(getFrontInfoByUser(user));
            }
            jsonMap.put("total", total);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", userDetailList);
        } else {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
            jsonMap.put("data", "");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据小区楼栋查询具体住址
     */
    @ResponseBody
    @PostMapping("/GetAddrInfoByBlock")
    @CrossOrigin
    public Object getAddrInfoByBlock(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int blockId = jsonObject.getInteger("blockId");
        try {
            List<String> addrs = userService.getAddrsByBlockId(blockId);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", addrs);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据用户姓名和水司编码查询用户
     */
    @ResponseBody
    @PostMapping("/GetUserByNameAndEnprNo")
    @CrossOrigin
    public Object getUserByNameAndEnprNo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String userName = jsonObject.getString("userName");
        String enprNo = jsonObject.getString("enprNo");
        try {
            List<User> userList = userService.getUserByNameAndEnprNo(userName, enprNo);
            List<Object> detailList = new ArrayList<>();
            for (User user : userList) {
                detailList.add(getFrontInfoByUser(user));
            }
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", detailList);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据表地址和水司编码查询用户
     */
    @ResponseBody
    @PostMapping("/GetUserByAddrAndEnprNo")
    @CrossOrigin
    public Object getUserByAddrAndEnprNo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        try {
            Device device = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo);
            int userId = device.getUserId();
            User user = userService.getByUserId(userId);
            Map detailMap = getFrontInfoByUser(user);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", detailMap);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据小区楼栋和住址查询用户
     */
    @ResponseBody
    @PostMapping("/GetUserByBlockAndAddr")
    @CrossOrigin
    public Object getUserByBlockAndAddr(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int blockId = jsonObject.getInteger("blockId");
        String userAddr = jsonObject.getString("userAddr");
        try {
            User user = userService.getByBLockIdAndAddr(blockId, userAddr);
            Map detailMap = getFrontInfoByUser(user);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", detailMap);
        } catch (Exception e) {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据用户ID查找用户
     */
    @ResponseBody
    @PostMapping("/GetUserUserId")
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
     * 用于前端显示的所有信息
     */
    private Map getFrontInfoByUser(User user) {
        Map<String, Object> detailMap = new HashMap<>();
        String addr = user.getUserAddr();
        int userId = user.getUserId();
        String userName = user.getUserName();

        detailMap.put("addr", addr);
        detailMap.put("userId", userId);
        detailMap.put("userName", userName);
        detailMap.put("monthExpense", user.getMonthExpense());
        detailMap.put("accountBalance", user.getAccountBalance());

        List<Device> deviceList = deviceService.getAllByUserId(userId);
        List<Object> deviceDetailList = new ArrayList<>();
        if (deviceList != null && deviceList.size() > 0) {
            for (Device device : deviceList) {
                Map<String, Object> deviceMap = new HashMap<>();
                //获取表编号
                deviceMap.put("deviceNo", device.getDeviceNo());
                //获取表读数
                deviceMap.put("deviceValue", device.getReadValue());
                //获取日用水量
                deviceMap.put("dailyAmount", device.getDayAmount());
                //获取月用量
                deviceMap.put("monthAmount", device.getMonthAmount());
                //表状态
                deviceMap.put("state", device.getState());
                //阀门状态
                deviceMap.put("valve", device.getValve());
                deviceDetailList.add(deviceMap);
            }
        }
        detailMap.put("deviceDetailList", deviceDetailList);
        return detailMap;
    }
}
