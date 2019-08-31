package com.hust.nb.Controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Config.Constants;
import com.hust.nb.Dao.BlockDao;
import com.hust.nb.Dao.DeviceCheckDao;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.*;
import com.hust.nb.util.ImportExcel;
import com.hust.nb.util.WDWUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * NB导入
 * created by hyJoo on 2019/5/30
 */
@CrossOrigin(origins = "*")
@RestController
public class ImportExcelController {

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    RegionService regionService;

    @Autowired
    CommunityService communityService;

    @Autowired
    ImportExcel importExcel;

    @Autowired
    ImportExcelService importExcelService;

    @Autowired
    UserService userService;

    @Autowired
    DeviceService deviceService;

    @Autowired
    BlockDao blockDao;

    @Autowired
    DeviceCheckDao deviceCheckDao;

    private static Logger logger = LoggerFactory.getLogger(ImportExcelController.class);


    /**
     * @param msg 创建区域
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/NB/addRegion")
    public Object addRegion(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
//        int userType = Integer.parseInt(jsonObject.getString("userType"));
        String regionName = jsonObject.getString("regionName");
        String remark = jsonObject.getString("remark");
        Enterprise enterprise = enterpriseService.findByEnprNo(enprNo);
        List<Region> regions = regionService.getByEnprNo(enprNo);
        for (Region region : regions) {
            if (region.getEnprNo().equals(enprNo) && region.getRegionName().equals(regionName)) {
                jsonMap.put("code", "-1");
                jsonMap.put("info", "此水司已存在同名区域");
                return JSONObject.toJSON(jsonMap);
            }
        }
        Region region = new Region();
//        if (userType == 0 || userType == 1) {
            if (regionName == null) {
                region.setRegionName(enterprise.getEnprName());
            } else {
                region.setRegionName(regionName);
            }
            region.setRemark(remark);
            region.setEnprNo(enprNo);
//        }
        try {
            regionService.saveRegion(region);
            jsonMap.put("code", "200");
            jsonMap.put("info", "导入成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "导入失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * @param msg 创建小区
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/NB/addCommunity")
    public Object addCommunity(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
//        int userType = Integer.parseInt(jsonObject.getString("userType"));
        String communityName = jsonObject.getString("communityName");
        int collectType = Integer.parseInt(jsonObject.getString("collectionType"));
        int regionId = jsonObject.getInteger("regionId");
        List<Community> communities = communityService.getByRegionId(regionId);
        for (Community community : communities) {
            if (community.getCommunityName().equals(communityName)) {
                jsonMap.put("code", "-1");
                jsonMap.put("info", "此区域下已有该小区");
                return JSONObject.toJSON(jsonMap);
            }
        }
        Community community = new Community();
            community.setCollectionType(collectType);
            community.setCommunityName(communityName);
            community.setEnprNo(enprNo);
            community.setRegionId(regionId);
        try {
            communityService.saveCommunity(community);
            jsonMap.put("code", "200");
            jsonMap.put("info", "导入成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "导入失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    @ResponseBody
    @PostMapping("/nbImport")
    public Object importNB(HttpServletRequest request) {
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//        Integer type = Integer.parseInt(params.getParameter("type"));
        String enprNo = params.getParameter("enprNo");
        Integer communityId = Integer.parseInt(params.getParameter("communityId"));
        Map<String, Object> jsonMap = new HashMap<>();
        String fileName = file.getOriginalFilename();
        boolean isExcel2003 = true;
        if (enprNo != null) {
            String blockName = null;
            String userName = null;
            String userTel = null;
            int userNo = 0;
            int size = 0;
            int valve = 0;
            int userType = 0;
            int deviceType = 0;


            if (WDWUtil.isExcel2007(fileName)) {
                isExcel2003 = false;
            }
            InputStream is = null;
            try {
                is = file.getInputStream();
            } catch (IOException e) {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            List<List<String>>[] sheets = importExcel.readSheets(is, isExcel2003);//读取整个EXCEL文件
            List<String> userNoList = importExcelService.findUserNo();
            for (int k = 0; k < sheets.length; k++) {//k代表第k + 1个sheet，blockName和sheet是对应的
                if (sheets[k] != null){
                    blockName = importExcel.blockName[k];//得到blockName
                }
                List<List<String>> dataList = sheets[k];//得到每个sheet数据
                Block block0 = importExcelService.findBlockByCommunityIdAndBlockName(communityId, blockName);
                if (block0 == null) {//如果不存在此楼栋，导入
                    Block blockEntity = new Block();
                    blockEntity.setBlockName(blockName);
                    blockEntity.setCommunityId(communityId);
                    blockEntity.setEnprNo(enprNo);
                    try {
                        importExcelService.saveBlock(blockEntity);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                        jsonMap.put("code", "-1");
                        jsonMap.put("info", "导入楼栋失败");
                    }
                }
                try {
                    Block block = importExcelService.findBlockByCommunityIdAndBlockName(communityId, blockName);
                    for (int i = 2; i < dataList.size(); i++) {//循环每一行，即对应单个用户，sheet表的第一行是填写注释，所以从1开始
                        //插入User表
                        List<String> cellList = dataList.get(i);
                        if (cellList.get(0)!= null && cellList.get(10)!= null){
                            if (!userNoList.contains(cellList.get(16))) {
                                User userEntity = new User();

                                userName = cellList.get(1);
                                userTel = cellList.get(3);
                                userEntity.setUserName(userName);

                                Double f = Double.valueOf(cellList.get(2));
                                userType = (int) Math.ceil(f);
                                userEntity.setUserType(userType);

                                if (cellList.get(4) != null) {
                                    userEntity.setUserPhone(cellList.get(4));
                                }
                                userEntity.setUserTel(userTel);
                                if (!cellList.get(5).isEmpty()){
                                    userEntity.setBankAccount(cellList.get(5));
                                }
                                userEntity.setUserAddr(cellList.get(9));
                                if (!cellList.get(6).isEmpty()){
                                    userEntity.setBankOwner(cellList.get(6));
                                }
                                if (!cellList.get(7).isEmpty()){
                                    userEntity.setBankAddr(cellList.get(7));
                                }
                                userEntity.setBlockId(block.getBlockId());
                                userEntity.setUserNo(cellList.get(16));
                                userEntity.setEnprNo(enprNo);
                                userEntity.setMonthExpense(new BigDecimal("0"));

                                userEntity.setAccountBalance(new BigDecimal("0"));
                                userNoList.add(cellList.get(16));
                                try {
                                    importExcelService.saveImportedExcel(userEntity);
                                    //对于user表，数据库设计的是用户名和block_id,.addr在一起的联合索引，即三个都一样的话就插不进去
                                    //如果插不进去则说明已经存在这个用户，为保证重复批量导入时不会导入同一个用户多次
                                } catch (Exception e) {
                                    logger.error(e.getMessage());
                                    jsonMap.put("code", "-1");
                                    jsonMap.put("info", "导入user表失败");
                                }
                            }
                            //插入device表
                            Device device = deviceService.getByDeviceNoAndEnprNo(cellList.get(8),enprNo);
                            if(device == null ){
                                Device deviceEntity = new Device();
                                try {
                                    String addr = cellList.get(9);
                                    logger.info("addr is : " + addr);
                                    userNo = importExcelService.findUserByUserNameAndUserTelAndAddr(cellList.get(1), cellList.get(3),addr);
                                } catch (Exception e) {
                                    logger.error("根据姓名和电话查找User失败" + e.getMessage());
                                }

                                deviceEntity.setUserId(userNo);
                                deviceEntity.setDeviceNo(cellList.get(8));
                                deviceEntity.setImei(cellList.get(10));
                                Double f2 = Double.valueOf(cellList.get(13));
                                deviceType = (int) Math.ceil(f2);
                                deviceEntity.setWaterType(deviceType);
                                Double f4 = Double.valueOf(cellList.get(11));
                                size = (int) Math.ceil(f4);
                                deviceEntity.setCaliber(size);

                                Double f5 = Double.valueOf(cellList.get(12));
                                valve = (int) Math.ceil(f5);
                                deviceEntity.setValve(valve);
                                deviceEntity.setState(2);
                                deviceEntity.setEnprNo(enprNo);
                                deviceEntity.setDayAmount(new BigDecimal("0"));
                                deviceEntity.setMonthAmount(new BigDecimal("0"));
                                deviceEntity.setDeviceVender(cellList.get(15));
                                deviceEntity.setDeviceType(Constants.DX_NB);
                                deviceEntity.setReadValue(new BigDecimal(cellList.get(17)));
                                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                                java.util.Date d = null;
                                try {
                                    d = date.parse(cellList.get(14));
                                    java.sql.Date date1 = new java.sql.Date(d.getTime());
                                    deviceEntity.setInstallDate(date1);
                                    deviceEntity.setReadTime(Timestamp.valueOf(cellList.get(14)));
                                } catch (Exception e) {
                                    logger.error(e.getMessage());
                                }

                                try {
                                    importExcelService.saveImportedExcelDevice(deviceEntity);
                                } catch (Exception e) {
                                    logger.error(e.getMessage());
                                    jsonMap.put("code", "-1");
                                    jsonMap.put("info", "导入device表失败");
                                }
                            }
                        }




                    }
                    jsonMap.put("code", "200");
                    jsonMap.put("info", "添加成功");
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    jsonMap.put("code", "-1");
                    jsonMap.put("info", "添加失败");
                }
            }
        } else {
            jsonMap.put("code", "-1");
            jsonMap.put("info", "添加失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

    /**
     * NB导入出厂表
     */
    @ResponseBody
    @PostMapping("/importNewDevice")
    public Object importNewDevice(HttpServletRequest request){
        Map<String, Object> jsonMap = new HashMap<>();
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        String enprNo = params.getParameter("enprNo");
        boolean isExcel2003 = true;
        String fileName = file.getOriginalFilename();

        if (WDWUtil.isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        List<List<String>>[] sheets = importExcel.readSheets(is, isExcel2003);//读取整个EXCEL文件
        try {
            for (int i =0; i< sheets.length; i++){
                List<List<String>> lists = sheets[i];
                for (int j = 2; j <lists.size(); j++){
                    List<String> cellList = lists.get(j);
                    DeviceCheck device = deviceCheckDao.findByImeiAndDeviceNo(cellList.get(10),cellList.get(8));
                    if (device == null && cellList.get(8) != null){
                        DeviceCheck d = new DeviceCheck();
                        d.setImei(cellList.get(10));
                        d.setDeviceNo(cellList.get(8));
                        d.setEnprNo(enprNo);
                        try {
                            deviceCheckDao.save(d);

                        }catch (Exception e){
                            logger.error(e.getMessage());
                        }
                    }
                }
            }
            jsonMap.put("code", "200");
            jsonMap.put("info", "添加成功");
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "添加失败");
        }
        Object o = JSONObject.toJSON(jsonMap);
        return o;
    }
    /**
     * NB表出厂导入检测
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/checkNBDevice")
    public Object checkNBDevice(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        String enprNo = params.getParameter("enprNo");
        boolean isExcel2003 = true;
        String fileName = file.getOriginalFilename();

        if (WDWUtil.isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        List<List<String>>[] sheets = importExcel.readSheets(is, isExcel2003);//读取整个EXCEL文件
        StringBuffer errstr = new StringBuffer();
        try {
            Set<String> imeis = new HashSet<>();
            Set<String> deviceNos = new HashSet<>();
        List<DeviceCheck> deviceList = deviceCheckDao.findByEnprNo(enprNo);
        if (!deviceList.isEmpty()){
            for (DeviceCheck deviceCheck : deviceList){
                imeis.add(deviceCheck.getImei());
                imeis.add(deviceCheck.getDeviceNo());
            }
        }
        for (int i = 0; i < sheets.length;i++){
            List<List<String>> lists = sheets[i];
            for (int j = 2; j < lists.size(); j++) {
                    int k = j+1;
                    List<String> cellList = lists.get(j);
                    DeviceCheck deviceName = deviceCheckDao.findByImeiAndDeviceNo(cellList.get(10),cellList.get(8));
                    if (deviceName!= null){
                        if (cellList.get(8).equals(deviceName.getDeviceNo())){
                            continue;
                        }
                    }

                    if ("".equals(cellList.get(10))) {
                        errstr.append("序号为(" + k + ")这一行的IMEI为空，请检查excel！");
                    } else if (!imeis.add(cellList.get(10))) {
                        errstr.append("序号为(" + k + ")这一行的IMEI号已存在，请检查excel！");
                    } else {
                        imeis.add(cellList.get(10));
                    }
                    if ("".equals(cellList.get(8))) {
                        errstr.append("序号为(" + k + ")这一行的表地址为空，请检查excel！");
                    }else if (!deviceNos.add(cellList.get(8))){
                        errstr.append("序号为(" + k + ")这一行的表地址已存在，请检查excel！");
                    }else {
                        deviceNos.add(cellList.get(8));
                    }

            }
        }
            if ("".equals(errstr.toString())){
                errstr.append( "恭喜，资料符合要求");
                jsonMap.put("code", "200");
                jsonMap.put("info", errstr);
            }else {
                jsonMap.put("code", "-1");
                jsonMap.put("info", errstr);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "导入检测失败");
        }
        Object o = JSONArray.toJSON(jsonMap);
        return o;
    }
    /**
     * NB导入检测
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/checkNBFormat")
    public Object checkNBFormat(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        String enprNo = params.getParameter("enprNo");
        Integer communityId = Integer.parseInt(params.getParameter("communityId"));
        boolean isExcel2003 = true;
        String fileName = file.getOriginalFilename();

        if (WDWUtil.isExcel2007(fileName)) {
            isExcel2003 = false;
        }
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        List<List<String>>[] sheets = importExcel.readSheets(is, isExcel2003);//读取整个EXCEL文件
        try {
            //查询现有用户编号
            List<String> userNoList = importExcelService.findUserNo();
            //查询用户电话号码与用户名称与银行户主、银行卡号
            List<User> userInfo = userService.findInfo();
            //查询当前水司表编号
            List<String> deviceNoList = deviceService.findDeviceNoByEnprNo(enprNo);
            //查询IMEI号
            List<String> imeiList = deviceService.findImei();
            //用户电话
            Map<String, String> mapTel = new HashMap<>();
            //银行卡号
            Map<String, String> mapBank = new HashMap<>();
            //创建HashSet判断唯一性
            Set<String> userImei = new HashSet<>();
            Set<String> userNo = new HashSet<>();
            Set<String> deveceN = new HashSet<>();
            deveceN.addAll(deviceNoList);
            userImei.addAll(imeiList);
            userNo.addAll(userNoList);
            for (User user : userInfo) {
                mapTel.put(user.getUserTel(), user.getUserName());
                mapBank.put(user.getBankAccount(), user.getBankOwner());

            }
            StringBuffer errstr = new StringBuffer();
            for (int i = 0; i < sheets.length; i++) {
                List<List<String>> list = sheets[i];
                String blockName = importExcel.blockName[i];
                Block block = blockDao.getAllByCommunityIdAndBlockName(communityId, blockName);
                for (int k = 2; k < list.size(); k++) {
                    List<String> cellList = list.get(k);
                    int j = 1 + k;
                    if ("".equals(cellList.get(1))) {
                        errstr.append("序号为(" + j + ")这一行的用户名为空，请检查excel！");
                    }
                    if ("".equals(cellList.get(2))) {
                        errstr.append("序号为(" + j + ")这一行的用户类型为空，请检查excel！");
                    }

                    User user = userService.findByUserNameAndUserAddrAAndUserTel(cellList.get(1), cellList.get(9), cellList.get(3));
                    Device device = deviceService.findByDeviceNoAndImei(cellList.get(8), cellList.get(10));
                    if (user != null && device != null){
                        boolean isBlockId = user.getBlockId()==block.getBlockId();
                        System.out.println(isBlockId);
                        boolean isUserId = device.getUserId().equals(user.getUserId());
                        System.out.println(isUserId);
                        if (isBlockId && isUserId){
                            if (cellList.get(3).equals(user.getUserTel())
                                    && cellList.get(16).equals(user.getUserNo())
//                                && cellList.get(13).equals(String.valueOf(device.getDeviceType()))
                            ){
                                if (cellList.get(5) != null && cellList.get(6) != null && cellList.get(7) != null){
                                    if (cellList.get(5).equals(user.getBankAccount())
                                            && cellList.get(6).equals(user.getBankOwner())
                                            && cellList.get(7).equals(user.getBankAddr())){
                                        continue;
                                    }
                                }

                                continue;
                            }
                        }
                    }



                    if(block == null){
                        errstr.append("序号为(" + j + ")这一行的用户所属小区与已导入所属小区不符，请检查excel！");
                    }
                    if (user != null && block != null){
                        if (!user.getEnprNo().equals(enprNo)){
                            errstr.append("序号为(" + j + ")这一行的用户所属水司与已导入所属水司不符，请检查excel！");
                        }

                        if (user.getBlockId() != block.getBlockId()){
                            errstr.append("序号为(" + j + ")这一行的用户所属楼栋与已导入所属楼栋不符，请检查excel！");
                        }
                        Block commnityId = blockDao.getByBlockId(user.getBlockId());
                        if (commnityId.getCommunityId() != communityId){
                            errstr.append("序号为(" + j + ")这一行的用户所属小区与已导入所属小区不符，请检查excel！");
                        }
                    }

                    if ("".equals(cellList.get(3))) {
                        errstr.append("序号为(" + j + ")这一行的用户电话为空，请检查excel！");
                    } else if (cellList.get(3).length() != 11 && cellList.get(3).length() < 4 && cellList.get(3).length() > 8) {
                        errstr.append("序号为(" + j + ")这一行的用户电话位数有误，请检查excel！");
                    } else if (mapTel.containsKey(cellList.get(3)) && !mapTel.get(cellList.get(3)).equals(cellList.get(1))) {
                        errstr.append("序号为(" + j + ")这一行的用户电话已存在且与所存在用户名不符，请检查excel！");
                    } else if (!mapTel.containsKey(cellList.get(3))) {
                        mapTel.put(cellList.get(3), cellList.get(1));
                    }
                    if (!cellList.get(5).isEmpty() && !cellList.get(7).isEmpty() && !cellList.get(6).isEmpty()) {
                        if ("".equals(cellList.get(5))) {
                            errstr.append("序号为(" + j + ")这一行的银行账号为空，请检查excel！");
                        } else if (cellList.get(5).length() != 16 && cellList.get(5).length() != 15 && cellList.get(5).length() != 19) {
                            errstr.append("序号为(" + j + ")这一行的银行账号位数有误，请检查excel！");
                        } else if (mapBank.containsKey(cellList.get(5)) && !mapBank.get(cellList.get(5)).equals(cellList.get(6))) {
                            errstr.append("序号为(" + j + ")这一行的银行账号与已导入户主不符，请检查excel！");
                        }else if (!mapBank.containsKey(cellList.get(5))){
                            mapBank.put(cellList.get(5),cellList.get(6));
                        }
                        if ("".equals(cellList.get(6))) {
                            errstr.append("序号为(" + j + ")这一行的银行户主为空，请检查excel！");
                        }
                        if ("".equals(cellList.get(7))) {
                            errstr.append("序号为(" + j + ")这一行的开户行为空，请检查excel！");
                        }
                    }
                    if ("".equals(cellList.get(8))) {
                        errstr.append("序号为(" + j + ")这一行的表地址为空，请检查excel！");
                    }else if (!deveceN.add(cellList.get(8))){
                        errstr.append("序号为(" + j + ")这一行的表地址已存在，请检查excel！");
                    }else {
                        deveceN.add(cellList.get(8));
                    }
                    if ("".equals(cellList.get(9))) {
                        errstr.append("序号为(" + j + ")这一行的用户住址为空，请检查excel！");
                    }

                    if ("".equals(cellList.get(10))) {
                        errstr.append("序号为(" + j + ")这一行的IMEI为空，请检查excel！");
                    }else if (!userImei.add(cellList.get(10))){
                        errstr.append("序号为(" + j + ")这一行的IMEI号已存在，请检查excel！");
                    }else {
                        userImei.add(cellList.get(10));
                    }
                    if ("".equals(cellList.get(11))) {
                        errstr.append("序号为(" + j + ")这一行的口径为空，请检查excel！");
                    }
                    if ("".equals(cellList.get(12))) {
                        errstr.append("序号为(" + j + ")这一行的阀门为空，请检查excel！");
                    }
                    if ("".equals(cellList.get(14))) {
                        errstr.append("序号为(" + j + ")这一行的安装时间为空，请检查excel！");
                    }
                    if ("".equals(cellList.get(15))) {
                        errstr.append("序号为(" + j + ")这一行的生产厂家为空，请检查excel！");
                    }
                    if ("".equals(cellList.get(16))) {
                        errstr.append("序号为(" + j + ")这一行的用户编号为空，请检查excel！");
                    }
                    if (user != null && !user.getUserNo().equals(cellList.get(16))){
                        errstr.append("序号为(" + j + ")这一行的用户已存在但用户编号与已存在用户编号不同，请检查excel！");
                    }
                    if (user == null){
                       if (!userNo.add(cellList.get(16))){
                            errstr.append("序号为(" + j + ")这一行的用户编号已存在，请检查excel！");
                        }else {
                            userNo.add(cellList.get(16));
                        }
                    }

                }
            }

            if ("".equals(errstr.toString())){
                errstr.append( "恭喜，资料符合要求");
                jsonMap.put("code", "200");
                jsonMap.put("info", errstr);
            }else {
                jsonMap.put("code", "-1");
                jsonMap.put("info", errstr);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            jsonMap.put("code", "-1");
            jsonMap.put("info", "检测失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

}
