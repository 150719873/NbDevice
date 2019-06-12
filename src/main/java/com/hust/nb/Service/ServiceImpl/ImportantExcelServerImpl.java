package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.BlockDao;
import com.hust.nb.Dao.DeviceDao;
import com.hust.nb.Dao.UserDao;
import com.hust.nb.Entity.Block;
import com.hust.nb.Entity.Device;
import com.hust.nb.Entity.User;
import com.hust.nb.Service.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImportantExcelServerImpl implements ImportExcelService {

    @Autowired
    BlockDao blockDao;

    @Autowired
    UserDao userDao;

    @Autowired
    DeviceDao deviceDao;

    @Override
    public Block findBlockByCommunityIdAndBlockName(Integer communityId, String blockName){
        return blockDao.getAllByCommunityIdAndBlockName(communityId, blockName);
    }

    @Override
    public void saveBlock(Block block){
        blockDao.save(block);
    }

    @Override
    public void saveImportedExcel(User user){
        userDao.save(user);
    }

    @Override
    public List<String> findUserNo(){
        return userDao.findUserNo();
    }

    @Override
    public Integer findUserByUserNameAndUserTelAndAddr(String userName, String userTel, String addr){
        return userDao.findUserByUserNameAndUserTelAndAddr(userName, userTel, addr);
    }

    @Override
    public void saveImportedExcelDevice(Device device){
        deviceDao.save(device);
    }
}
