package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.CommunityDao;
import com.hust.nb.Entity.Community;
import com.hust.nb.Service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
