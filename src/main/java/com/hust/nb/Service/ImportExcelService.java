package com.hust.nb.Service;


import com.hust.nb.Entity.*;

import java.util.List;

/**
 * Description:nbqbtt
 * Created by Administrator on 2018/10/21
 */
public interface ImportExcelService {

    Block findBlockByCommunityIdAndBlockName(Integer communityId, String blockName);

    void saveBlock(Block block);

    void saveImportedExcel(User user);

    void saveImportedExcelDevice(Device device);

    void saveImportedExcelMechanicalDevice(MechanicalDevice mechanicalDevice);

    void saveImportedExcelMechanicalDeviceHistory(MechanicalDeviceHistory mdHistory);

    List<String> findUserNo();

    //查询机械表编号
    List<String> findDeviceNo();

    Integer findUserByUserNameAndUserTelAndAddr(String userName, String userTel, String addr);
}
