package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.RepairDao;
import com.hust.nb.Entity.RepairItem;
import com.hust.nb.Service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairServiceImpl implements RepairService
{
    @Autowired
    RepairDao repairDao;


    @Override
    public void saveRepairItem(RepairItem repairItem)
    {
        repairDao.save(repairItem);
    }

    @Override
    public Integer getCommunityIdByBlockId(Integer blockId)
    {
        return repairDao.findCommunityIdByBlockId(blockId);
    }

    @Override
    public List<RepairItem> getByCommunityId(Integer communityId)
    {
        return repairDao.findAllByCommunityId(communityId);
    }

    @Override
    public String getDeviceNoByUserNoAndEnprNo(String userNo, String enprNo) {
        return repairDao.findDeviceNoByEnprNoAndUserNo(userNo, enprNo);
    }


    @Override
    public RepairItem getbyUserId(Integer userId) {
        return repairDao.findUserByUserId(userId);
    }

    @Override
    public List<RepairItem> getFeedbackByUserNo(String userNo)
    {
        return repairDao.findAllByUserNo(userNo);
    }

//    @Override
//    public Integer getUserIdByDeviceNo(String deviceNo)
//    {
//        return repairDao.findUserIdByDeviceNo(deviceNo);
//    }

    @Override
    public List<RepairItem> getFeedbackByUserId(Integer userId)
    {
        return repairDao.findAllByUserId(userId);
    }


    @Override
    public List<RepairItem> getbyEnprNo(String enprNo)
    {
        return  repairDao.findByEnprNo(enprNo);
    }

    @Override
    public void deleteRepairInfo(RepairItem repairItem)
    {
        repairDao.delete(repairItem);
    }

    @Override
    public List<RepairItem> getFeedbackByDeviceNo(String deviceNo)
    {
        return repairDao.findByDeviceNo(deviceNo);
    }

}

