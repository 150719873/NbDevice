package com.hust.nb.Service;

import com.hust.nb.Entity.Community;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface CommunityService {
    List<Community> getByRegionId(int regionId);

    void saveCommunity(Community community);

    Community getByCommunityNameAndEnprNo(String communityName, String enprNo);
}
