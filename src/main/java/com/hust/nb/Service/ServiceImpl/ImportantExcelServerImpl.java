package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.*;
import com.hust.nb.Entity.*;
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

    @Autowired
    MechanicalDeviceDao mechanicalDeviceDao;

    @Autowired
    MechanicalDeviceHistoryDao mechanicalDeviceHistoryDao;

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
    public List<String> findDeviceNo(){ return mechanicalDeviceDao.findDeviceNo();}

    @Override
    public Integer findUserByUserNameAndUserTelAndAddr(String userName, String userTel, String addr){
        return userDao.findUserByUserNameAndUserTelAndAddr(userName, userTel, addr);
    }

    @Override
    public void saveImportedExcelDevice(Device device){
        deviceDao.save(device);
    }

    @Override
    public void saveImportedExcelMechanicalDevice(MechanicalDevice mechanicaldevice){
        mechanicalDeviceDao.save(mechanicaldevice);
    }

    @Override
    public void saveImportedExcelMechanicalDeviceHistory(MechanicalDeviceHistory mdHistory){
        mechanicalDeviceHistoryDao.save(mdHistory);
    }
}