package com.hust.nb.Service;

import com.hust.nb.Entity.Community;
import com.hust.nb.Entity.HistoryDayCount;
import com.hust.nb.Entity.HistoryMonthCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface CommunityService {
    List<Community> getByRegionId(int regionId);

    void saveCommunity(Community community);

    Community getByCommunityNameAndEnprNo(String communityName, String enprNo);

    BigDecimal getTotalDayAmountByCommunityId(Integer communityId);

    String getEnprNoByCommunityId(Integer communityId);

    void saveCommunityWater(Community community);

    BigDecimal getTotalMonthAmountByCommunityId(Integer communityId);

    Page<HistoryDayCount> getDayAmountsByCommunityId(Integer communityId, Pageable pageable);

    Page<HistoryMonthCount> getMonthAmountsByCommunityId(Integer communityId, Pageable pageable);
}
