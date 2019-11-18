package com.hust.nb.Dao;

import com.hust.nb.Entity.Community;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Repository
public interface CommunityDao extends JpaRepository<Community,Community>,JpaSpecificationExecutor<Community> {

    @Query(nativeQuery = true, value = "select  * from mixAll.dbo.nt_community where region_id = ?1")
    List<Community> getAllByRegionId(int regionId);

    Community getByCommunityNameAndEnprNo(String communityName, String enprNo);

    Community getByCommunityId(int communityId);

    @Query(nativeQuery = true, value = "select enprNo from mixAll.dbo.nt_community where community_id = ?1")
    String getEnprNoByCommunityId(Integer communityId);

    @Query(nativeQuery = true, value = "select sum(d.day_amount) as totalDayAmount from mixAll.dbo.nb_daycost d,mixAll.dbo.nt_user u,mixAll.dbo.nt_block b where d.user_id =u.user_id and u.block_id = b.block_id and b.community_id = ?1")
    BigDecimal getTotalDayAmountByCommunityId(Integer communityId);

    @Query(nativeQuery = true, value = "select sum(m.month_amount) as totalMonthAmount from mixAll.dbo.nt_monthcost m,mixAll.dbo.nt_user u,mixAll.dbo.nt_block b where m.user_id =u.user_id and u.block_id = b.block_id and b.community_id = ?1")
    BigDecimal getTotalMonthAmountByCommunityId(Integer communityId);
}
