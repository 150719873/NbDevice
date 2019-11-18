package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.CommunityDao;
import com.hust.nb.Entity.Community;
import com.hust.nb.Service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Service
public class CommunityServiceImpl implements CommunityService {

    @Autowired
    CommunityDao communityDao;

    @Override
    public List<Community> getByRegionId(int regionId) {
        return communityDao.getAllByRegionId(regionId);
    }

    @Override
    public void saveCommunity(Community community){
        communityDao.save(community);
    }

    @Override
    public Community getByCommunityNameAndEnprNo(String communityName, String enprNo){
        return communityDao.getByCommunityNameAndEnprNo(communityName, enprNo);
    }

    @Override
    public String getEnprNoByCommunityId(Integer communityId) {
        return communityDao.getEnprNoByCommunityId(communityId);
    }

    @Override
    public void saveCommunityWater(Community community)
    {
        communityDao.save(community);
    }

    @Override
    public BigDecimal getTotalDayAmountByCommunityId(Integer communityId)
    {
        return communityDao.getTotalDayAmountByCommunityId(communityId);
    }

    @Override
    public BigDecimal getTotalMonthAmountByCommunityId(Integer communityId)
    {
        return communityDao.getTotalMonthAmountByCommunityId(communityId);
    }





}
