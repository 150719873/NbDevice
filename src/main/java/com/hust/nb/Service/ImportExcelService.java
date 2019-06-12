package com.hust.nb.Service;


import com.hust.nb.Entity.Block;
import com.hust.nb.Entity.Device;
import com.hust.nb.Entity.User;

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

    List<String> findUserNo();

    Integer findUserByUserNameAndUserTelAndAddr(String userName, String userTel, String addr);
}
