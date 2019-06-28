package com.hust.nb.Controller;


import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Config.Constants;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.*;
import com.hust.nb.util.ImportExcel;
import com.hust.nb.util.WDWUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
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
                e.printStackTrace();
            }
            List<List<String>>[] sheets = importExcel.readSheets(is, isExcel2003);//读取整个EXCEL文件
            List<String> userNoList = importExcelService.findUserNo();
            for (int k = 0; k < sheets.length; k++) {//k代表第k + 1个sheet，blockName和sheet是对应的
                blockName = importExcel.blockName[k];//得到blockName
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
                        jsonMap.put("code", "-1");
                        jsonMap.put("info", "导入楼栋失败");
                    }
                }
                try {
                    Block block = importExcelService.findBlockByCommunityIdAndBlockName(communityId, blockName);
                    for (int i = 2; i < dataList.size(); i++) {//循环每一行，即对应单个用户，sheet表的第一行是填写注释，所以从1开始
                        //插入User表
                        List<String> cellList = dataList.get(i);
                        if (!userNoList.contains(cellList.get(16))) {
                            User userEntity = new User();
                            Device deviceEntity = new Device();

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
                            userEntity.setBankAccount(cellList.get(5));
                            userEntity.setUserAddr(cellList.get(9));
                            userEntity.setBankOwner(cellList.get(6));
                            userEntity.setBankAddr(cellList.get(7));
                            userEntity.setBlockId(block.getBlockId());
                            userEntity.setUserNo(cellList.get(16));
                            userEntity.setEnprNo(enprNo);
                            userNoList.add(cellList.get(16));
                            try {
                                importExcelService.saveImportedExcel(userEntity);
                                //对于user表，数据库设计的是用户名和block_id,.addr在一起的联合索引，即三个都一样的话就插不进去
                                //如果插不进去则说明已经存在这个用户，为保证重复批量导入时不会导入同一个用户多次
                            } catch (Exception e) {
                                jsonMap.put("code", "-1");
                                jsonMap.put("info", "导入user表失败");
                            }

                            //插入device表
                            try {
                                userNo = importExcelService.findUserByUserNameAndUserTelAndAddr(userName, userTel,userEntity.getUserAddr());
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("根据姓名和电话查找User失败");
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
                            deviceEntity.setEnprNo(enprNo);
                            deviceEntity.setDeviceVender(cellList.get(15));
                            deviceEntity.setDeviceType(Constants.DX_NB);
                            try {
                                importExcelService.saveImportedExcelDevice(deviceEntity);
                            } catch (Exception e) {
                                jsonMap.put("code", "-1");
                                jsonMap.put("info", "导入device表失败");
                            }
                        }
                    }
                    jsonMap.put("code", "200");
                    jsonMap.put("info", "添加成功");
                } catch (Exception e) {
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
     * NB导入检测
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/checkNBFormat")
    public Object checkNBFormat(HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<>();
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//        Integer type = Integer.parseInt(params.getParameter("type"));
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
            e.printStackTrace();
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
                        if (cellList.get(3).equals(user.getUserTel())
                                && cellList.get(5).equals(user.getBankAccount())
                                && cellList.get(6).equals(user.getBankOwner())
                                && cellList.get(7).equals(user.getBankAddr())
//                                && cellList.get(13).equals(String.valueOf(device.getDeviceType()))
                                ){
                            continue;
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
                    }else if (!userNo.add(cellList.get(16))){
                        errstr.append("序号为(" + j + ")这一行的用户编号已存在，请检查excel！");
                    }else {
                        userNo.add(cellList.get(16));
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
            jsonMap.put("code", "-1");
            jsonMap.put("info", "检测失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }

}