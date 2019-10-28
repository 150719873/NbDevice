package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.RepairDao;
import com.hust.nb.Entity.RepairItem;
import com.hust.nb.Service.RepairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public Page<RepairItem> getByCommunityIdAndEnprNo(Integer communityId, String enprNo,Pageable pageable )
    {
        return repairDao.findAllByCommunityIdAndEnprNo(communityId,enprNo,pageable);

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
    public List<RepairItem> getFeedbackByUserNoAndEnprNo(String userNo,String enprNo)
    {
        return repairDao.findAllByUserNoAndEnprNo(userNo,enprNo);
    }


    @Override
    public List<RepairItem> getFeedbackByUserId(Integer userId)
    {
        return repairDao.findAllByUserId(userId);
    }


    @Override
    public Page<RepairItem> getbyEnprNo(String enprNo,Pageable pageable)
    {
        return  repairDao.findByEnprNo(enprNo,pageable);
    }

    @Override
    public void deleteRepairInfo(RepairItem repairItem)
    {
        repairDao.delete(repairItem);
    }

    @Override
    public List<RepairItem> getFeedbackByDeviceNoAndEnprNo(String deviceNo,String enprNo)
    {
        return repairDao.findByDeviceNoAndEnprNo(deviceNo,enprNo);
    }

    @Override
    public String getCommunityNameByCommunityId(Integer communityId)
    {
        return repairDao.findCommunityNameByCommunityId(communityId);
    }

}

