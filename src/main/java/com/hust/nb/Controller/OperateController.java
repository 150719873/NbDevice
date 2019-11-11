package com.hust.nb.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Config.Constants;
import com.hust.nb.Dao.OperatorDao;
import com.hust.nb.Dao.RepairDao;
import com.hust.nb.Entity.*;
import com.hust.nb.Entity.Notice;
import com.hust.nb.Service.*;
import com.hust.nb.util.EntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Controller
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

    @Autowired
    OperatorDao operatorDao;

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    WxService wxService;

    @Autowired
    RepairService repairService;

    @Autowired
    RepairDao repairDao;

    @Autowired
    NoticeService noticeService;


    private static Logger logger = LoggerFactory.getLogger(OperateController.class);


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
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     * 方法功能描述:根据区域ID获取小区以及楼栋信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/GetInfoByRegion")
    public Object getAreaInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int regionId = jsonObject.getInteger("regionId");
        List<Map> res = new ArrayList<>();
        List<Community> communities = communityService.getByRegionId(regionId);
        try {
            for (Community community : communities) {
                Map<String, Object> commuBlockMap = new HashMap<>();
                String communityName = community.getCommunityName();
                List<Block> blocks = blockService.getByCommunityId(community.getCommunityId());
                commuBlockMap.put(communityName, blocks);
                res.add(commuBlockMap);
            }
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", res);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:根据区域ID获取小区信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/GetCommunityInfoByRegion")
    public Object getCommunityInfoByRegion(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int regionId = jsonObject.getInteger("regionId");
        List<Community> communities = null;
        try{
            communities = communityService.getByRegionId(regionId);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", communities);
        } catch (Exception e){
            logger.error(e.getStackTrace().toString());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
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
        try {
            List<Object> userDetailList = new ArrayList<>();
            long total = userPage.getTotalElements();
            for (User user : userPage.getContent()) {
                userDetailList.add(getFrontInfoByUser(user));
            }
            jsonMap.put("total", total);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", userDetailList);
        } catch (Exception e){
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
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
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     * 方法功能描述:登录
     */
    @ResponseBody
    @PostMapping("/Login")
    public Object checkOperatorInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String userName = jsonObject.getString("userName");
        String password = jsonObject.getString("password");
        try {
            Operator operator = operatorDao.findOperatorByUserNameAndPassword(userName, password);
            if (operator != null) {
                if(operator.getUserType() == 0){
                    jsonMap.put("code", "201");
                    jsonMap.put("info", "查询成功");
                    jsonMap.put("operator", operator);
                } else {
                    jsonMap.put("code", "200");
                    jsonMap.put("info", "查询成功");
                    jsonMap.put("operator", operator);
                    String enprNo = operator.getEnprNo();
                    Enterprise enterprise = enterpriseService.findByEnprNo(enprNo);
                    jsonMap.put("enterprise", enterprise);
                    jsonMap.put("wxConfig",wxService.findByEnprNo(enprNo));
                }
            } else {
                jsonMap.put("code", "-1");
                jsonMap.put("info", "登录失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 方法功能描述:更改水司信息
     */
    @ResponseBody
    @PostMapping("/updateCompanyInfo")
    @CrossOrigin
    public Object updateCompanyInfo(@RequestBody String company) {
        return null;
    }

    /**
     * 获取所有管理员信息
     */
    @ResponseBody
    @PostMapping("/GetOperatorList")
    public Object getOperatorList(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        try {
            List<Operator> res = operatorDao.findAllByEnprNo(enprNo);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", res);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 增加或修改管理员
     *
     * @param msg
     * @return
     */
    @ResponseBody
    @PostMapping("/AddOrModifyOperator")
    @CrossOrigin
    public Object addOrModifyOperator(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Integer operatorId = jsonObject.getInteger("operatorId");
        String userName = jsonObject.getString("userName");
        String password = jsonObject.getString("password");
        String phone = jsonObject.getString("phone");
        int userType = jsonObject.getInteger("userType");
        String operatorName = jsonObject.getString("operatorName");
        String manageCommunity = jsonObject.getString("manageCommunity");
        String enprNo = jsonObject.getString("enprNo");
        try{
            if(operatorId == null){
                //新增
                Operator operator = EntityFactory.OperatorFactory(userName, password, phone,
                        userType, operatorName, manageCommunity, enprNo);
                operatorDao.save(operator);
            } else {
                //修改
                Operator operator = JSON.parseObject(msg, Operator.class);
                operatorDao.save(operator);
            }
            jsonMap.put("code", "200");
            jsonMap.put("info", "成功");
        } catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 删除管理员
     */
    @ResponseBody
    @PostMapping("/DeleteOperator")
    @CrossOrigin
    public Object deleteOperator(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int operatorId = jsonObject.getInteger("operatorId");
        try {
            operatorDao.deleteByOperatorId(operatorId);
            jsonMap.put("code", "200");
            jsonMap.put("info", "删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "删除失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * 修改个人信息
     */
    @ResponseBody
    @PostMapping("/UpdatePrivateInfo")
    @CrossOrigin
    public Object updatePrivateInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String operatorName = jsonObject.getString("operatorName");
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");
        int operatorId = Integer.parseInt(jsonObject.getString("operatorId"));
        Operator operator = operatorDao.findByOperatorId(operatorId);
        operator.setOperatorName(operatorName);
        operator.setPhone(phone);
        operator.setPassword(password);
        try {
            operatorDao.save(operator);
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
        detailMap.put("accountBalance", user.getAccountBalance());
        detailMap.put("userType", user.getUserType());
        detailMap.put("userNo", user.getUserNo());

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

    /**
     * 编辑维修师傅信息
     */
    @ResponseBody
    @PostMapping("/Repairman")
    @CrossOrigin
    public Object Repairman(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String repairmanName = jsonObject.getString("repairmanName");
        String repairmanTel = jsonObject.getString("repairmanTel");
        Integer userId = jsonObject.getInteger("userId");
        Integer state =2;
        RepairItem repairItem = repairService.getbyUserId(userId);
        repairItem.setRepairmanName(repairmanName);
        repairItem.setRepairmanTel(repairmanTel);
        repairItem.setState(state);
        try {
            repairService.saveRepairItem(repairItem);
            jsonMap.put("code", "200");
            jsonMap.put("info", "编辑成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "导入失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * * 录入维修完成时间
     */
    @ResponseBody
    @PostMapping("/RepairFinish")
    @CrossOrigin
    public Object RepairFinish(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Timestamp endTime = Timestamp.valueOf(LocalDateTime.now());
        Integer userId = jsonObject.getInteger("userId");
//        Integer state =jsonObject.getInteger("state");
        Integer state = 0;
        RepairItem repairItem = repairService.getbyUserId(userId);
        repairItem.setEndTime(endTime);
        repairItem.setState(state);
        try {
            repairService.saveRepairItem(repairItem);
            jsonMap.put("code", "200");
            jsonMap.put("info", "导入成功");
            jsonMap.put("data",endTime);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "导入失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     *  根据用户编号查询反馈信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getFeedbackByUserNoAndEnprNo")
    public Object getFeedbackByUserNoAndEnprNo(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String userNo = jsonObject.getString("userNo");
        String enprNo = jsonObject.getString("enprNo");
        try {
            List<RepairItem> list01=repairService.getFeedbackByUserNoAndEnprNo(userNo,enprNo);
            jsonMap.put("code","200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data",list01);
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     *  根据表地址查询反馈信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getFeedbackByDeviceNoAndEnprNo")
    public Object getFeedbackByDeviceNoAndEnprNo(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String deviceNo = jsonObject.getString("deviceNo");
        String enprNo = jsonObject.getString("enprNo");
        try {
            List<RepairItem> list02=repairService.getFeedbackByDeviceNoAndEnprNo(deviceNo,enprNo);
            jsonMap.put("code","200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data",list02);
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     *  根据小区Id查询所有信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getFeedbackByCommunityIdAndEnprNo")
    public Object getFeedbackByCommunityIdAndEnprNo(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Integer communityId = jsonObject.getInteger("communityId");
        String enprNo = jsonObject.getString("enprNo");
        int rows = jsonObject.getInteger("rows");
        int page = Integer.parseInt(jsonObject.getString("page"));
        try {
            Pageable pageable = PageRequest.of(page - 1, rows);
            Page<RepairItem> page01 = repairService.getByCommunityIdAndEnprNo(communityId,enprNo,pageable);
            jsonMap.put("code","200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data",page01);
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     *  根据水司编码查询所有信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/getFeedbackByEnprNo")
    public Object getFeedbackByEnprNo(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        int rows = jsonObject.getInteger("rows");
        int page = Integer.parseInt(jsonObject.getString("page"));
        try {
            Pageable pageable = PageRequest.of(page - 1, rows);
            Page <RepairItem> page02=repairService.getbyEnprNo(enprNo,pageable);
            jsonMap.put("code","200");
            jsonMap.put("info","查询成功");
            jsonMap.put("data", page02);
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code","-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     *  删除报修记录
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteFeedbackInfo")
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
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "删除失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     *  录入（编辑）公告内容
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/inputNotice")
    public Object inputNotice(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String title = jsonObject.getString("title");
        String content =jsonObject.getString("content");
        Timestamp rTime = Timestamp.valueOf(LocalDateTime.now());
        Integer type = jsonObject.getInteger("type");
        String enprNo =jsonObject.getString("enprNo");
        String objects =jsonObject.getString("objects");
        Notice notice =EntityFactory.inputNoticeFactory(title,content,rTime,type,enprNo,objects);
        try {
            noticeService.saveNotice(notice);
            jsonMap.put("code", "200");
            jsonMap.put("info", "录入成功");
            jsonMap.put("data",notice);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "录入失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     *  删除某条公告
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/deleteNotice")
    public Object deleteNotice(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        Integer id =jsonObject.getInteger("id");
        Notice notice =noticeService.getNoticeById(id);
        try {
            noticeService.deleteNotice(notice);
            jsonMap.put("code", "200");
            jsonMap.put("info", "删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "删除失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     *  显示历史公告记录
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/outputNotice")
    public Object outputNotice(@RequestBody String msg)
    {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo =jsonObject.getString("enprNo");
        int rows = jsonObject.getInteger("rows");
        int page = Integer.parseInt(jsonObject.getString("page"));
        try {
            Pageable pageable = PageRequest.of(page - 1, rows);
            Page <Notice> page03=noticeService.getByEnprNo(enprNo,pageable);
            jsonMap.put("code","200");
            jsonMap.put("info","显示成功");
            jsonMap.put("data", page03);
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "显示失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

}
