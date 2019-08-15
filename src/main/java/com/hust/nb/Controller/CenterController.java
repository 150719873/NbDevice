package com.hust.nb.Controller;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Dao.*;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.*;
import com.hust.nb.util.Adapter;
import com.hust.nb.util.GetDate;
import com.hust.nb.vo.DeviceOutputVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*")
@Controller
public class CenterController {

    @Autowired
    CommunityService communityService;

    @Autowired
    RegionService regionService;

    @Autowired
    BlockDao blockDao;

    @Autowired
    UserService userService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    OperatorDao operatorDao;

    @Autowired
    DeviceRelationDao deviceRelationDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    WarningDao warningDao;

    /**
     *
     *  列出小区超表界面
     * @return
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/searchCenter")
    public Object searchCenter(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        int operatorId = jsonObject.getInteger("operatorId");
        System.out.println("列出小区超表界面");
        Operator operator = operatorDao.findByOperatorId(operatorId);
        List<Community> communityList = new ArrayList<>();
        try {
            if (operator.getUserType() == 1){
                List<Region> regions = regionService.getByEnprNo(enprNo);
                for (Region region : regions){
                    List<Community> communities = communityService.getByRegionId(region.getRegionId());
                    communityList.addAll(communities); //得到所有小区
                }
                for (Community community : communityList){
                    Region region = regionService.findByRegionId(community.getRegionId());
                    List<Block> blocks = blockDao.getAllByCommunityId(community.getCommunityId());
                    int count = 0;
                    int sucessCount = 0;
                    for (Block block : blocks){
                        List<Integer> userIds = userService.getUserIdsByBlockId(block.getBlockId());
                        for (Integer id : userIds){
                            List<Integer> device = deviceService.findStateByUserId(id);
                            for (Integer device1 : device){
                                count++;
                                if (device1 == 0){
                                    sucessCount++;
                                }
                            }
                        }
                    }
                    community.setDeviceCount(count);
                    community.setSucessCount(sucessCount);
                    community.setRegionName(region.getRegionName());
                }

            }else if (operator.getUserType() ==2){
                Community community = communityService.getByCommunityNameAndEnprNo(operator.getManageCommunity(), enprNo);
                communityList.add(community);
                List<Block> blocks = blockDao.getAllByCommunityId(community.getCommunityId());
                int count = 0;
                int sucessCount = 0;
                for (Block block : blocks){
                    List<Integer> userIds = userService.getUserIdsByBlockId(block.getBlockId());
                    for (Integer id : userIds){
                        List<Integer> device = deviceService.findStateByUserId(id);
                        for (Integer device1 : device){
                            count++;
                            if (device1 == 0){
                                sucessCount++;
                            }
                        }
                    }
                }
                community.setDeviceCount(count);
                community.setSucessCount(sucessCount);
            }
            jsonMap.put("code", "200");
            jsonMap.put("info", communityList);
            System.out.println("列出小区超表界面成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }


        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }
    /**
     * 查看小区下楼栋
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getBlockList")
    public Object getBlockList(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int communityId = jsonObject.getInteger("communityId");
        try {
            List<Block> blockList = blockDao.getAllByCommunityId(communityId);
            jsonMap.put("code", "200");
            jsonMap.put("info", blockList);
        }catch (Exception e){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object o = JSONObject.toJSON(jsonMap);
        return o;
    }

    /**
     * 查看小区下所有表
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deviceList")
    public Object deviceList(@RequestBody String msg){
        //两个方法都用这个，1.直接从小区查询表，2.查询楼栋下得表
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int communityId = jsonObject.getInteger("communityId");
        String blockName = jsonObject.getString("blockName");
        String enprNo = jsonObject.getString("enprNo");
        int rows = jsonObject.getInteger("rows");
        int page = Integer.parseInt(jsonObject.getString("page"));
        try {
            if (communityId != 0 && blockName == null){
                //根据小区ID查询表
                Pageable pageable = PageRequest.of(page - 1, rows);
                Page<DeviceOutputVO> userDetailList = deviceDao.getDeviceByCommunityId(communityId, pageable);
                List<Block> blockList = blockDao.getAllByCommunityId(communityId);
                long total = userDetailList.getTotalElements();
                jsonMap.put("total", total);
                jsonMap.put("data", userDetailList);
                jsonMap.put("blockList", blockList);
            }else if (blockName != null){
                //查看楼栋下得表
                Block block = blockDao.getAllByCommunityIdAndBlockName(communityId, blockName);
                Pageable pageable = PageRequest.of(page - 1, rows);
                Page<DeviceOutputVO> userDetailList = deviceDao.getDeviceByBlockId(block.getBlockId(), pageable);
                long total = userDetailList.getTotalElements();
                jsonMap.put("total", total);
                jsonMap.put("data", userDetailList);
            }
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            System.out.println("查看小区下所有表成功");

        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
            jsonMap.put("data", "");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 查看失败表
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getFailDevice")
    public Object getFailDevice(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int communityId = jsonObject.getInteger("communityId");
        String enprNo = jsonObject.getString("enprNo");
        int rows = Integer.parseInt(jsonObject.getString("rows"));
        int page = Integer.parseInt(jsonObject.getString("page"));
        try {
            Pageable pageable = PageRequest.of(page - 1, rows);
            Page<DeviceOutputVO> userPage = deviceService.getFailDeviceByCommunityId(communityId, pageable);
            List<Block> blockList = blockDao.getAllByCommunityId(communityId);
            long total = userPage.getTotalElements();
            jsonMap.put("code","200");
            jsonMap.put("info", userPage);
            jsonMap.put("total", total);
            jsonMap.put("blockList", blockList);
        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object o = JSONObject.toJSON(jsonMap);
        return o;
    }

    /**
     *根据表地址查询表信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getDeviceByDeviceNo")
    public Object getDeviceByDeviceNo(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        String deviceNo = jsonObject.getString("deviceNo");
        try {
            Device device = deviceService.getByDeviceNoAndEnprNo(deviceNo, enprNo);
            User user = userService.getByUserId(device.getUserId());
            Object object = getFrontInfoByUser(user);
            jsonMap.put("code","200");
            jsonMap.put("info", object);
            System.out.println("根据表地址查询表信息成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object o = JSONObject.toJSON(jsonMap);
        return o;
    }

    /**
     *根据用户名查询表信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getDeviceByUserName")
    public Object getDeviceByUserName(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        String userName = jsonObject.getString("userName");
        try {
            List<Object> userList = new ArrayList<>();
            List<User> users = userService.getUserByNameAndEnprNo(userName,enprNo);
            for (User user: users){
                userList.add(getFrontInfoByUser(user));
            }
            jsonMap.put("code","200");
            jsonMap.put("info", userList);
        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object o = JSONObject.toJSON(jsonMap);
        return o;
    }

    /**
     * 根据用户住址查询表信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getDeviceByUserAddr")
    public Object getDeviceByUserAddr(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String userAddr = jsonObject.getString("userAddr");
        int blockId = jsonObject.getInteger("blockId");
        try {
            User user = userService.getByBLockIdAndAddr(blockId, userAddr);
            Object object = getFrontInfoByUser(user);
            jsonMap.put("code","200");
            jsonMap.put("info", object);
        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object o = JSONObject.toJSON(jsonMap);
        return o;
    }
    /**
     * 显示小区故障表
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getWarningDevice")
    public Object getWarningDevice(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        int flag = jsonObject.getInteger("flag");//标识字段，0则代表查询小区故障表，1则代表水司故障表
        List<Warning> warnings = new ArrayList<>();
        Timestamp cur = GetDate.getCurrentDay();
        if (flag == 1){
            String communityName = jsonObject.getString("communityName");
            warnings = warningDao.findByEnprNoAndCommunityNameAndWarningDate(enprNo, communityName, cur);
        }else {
            warnings = warningDao.findByEnprNoAndWarningDate(enprNo,cur);
        }
        try {
            jsonMap.put("code","200");
            jsonMap.put("info","成功");
            jsonMap.put("data", warnings);
        }catch (Exception e){
            e.printStackTrace();
            jsonMap.put("code","-1");
            jsonMap.put("info", "失败");
        }
        Object o= JSONObject.toJSON(jsonMap);
        return o;
    }

    /**
     * 用于前端显示的所有信息
     */
    private Map getFrontInfoByUser(User user) {
        Map<String, Object> detailMap = new HashMap<>();
        String addr = user.getUserAddr();
        int userId = user.getUserId();
        String userName = user.getUserName();
        Block block = blockDao.getByBlockId(user.getBlockId());



        List<Device> deviceList = deviceService.getAllByUserId(userId);
        List<Object> deviceDetailList = new ArrayList<>();
        if (deviceList != null && deviceList.size() > 0) {
            for (Device device : deviceList) {
                Map<String, Object> deviceMap = new HashMap<>();
                //获取表编号
                deviceMap.put("deviceNo", device.getDeviceNo());
                //获取表读数
                deviceMap.put("readValue", device.getReadValue());
                //获取日用水量
                deviceMap.put("dayCount", device.getDayAmount());
                //获取月用量
                deviceMap.put("monthAmount", device.getMonthAmount());
                //表状态
                deviceMap.put("state", device.getState());
                //阀门状态
                deviceMap.put("valve", device.getValve());
                //读取时间
                deviceMap.put("readTime", device.getReadTime());
                //imei
                deviceMap.put("imei", device.getImei());
                //表类型
                deviceMap.put("deviceType",device.getDeviceType());
                deviceMap.put("waterType",device.getWaterType());
                deviceDetailList.add(deviceMap);
                deviceMap.put("userAddr", addr);
                deviceMap.put("userId", userId);
                deviceMap.put("userName", userName);
                deviceMap.put("blockName",block.getBlockName());
                deviceMap.put("userNo",user.getUserNo());
            }
        }
        detailMap.put("deviceDetailList", deviceDetailList);
        return detailMap;
    }
}
