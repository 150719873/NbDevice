package com.hust.nb.Dao;

import com.hust.nb.Entity.Community;
import com.hust.nb.Entity.HistoryDayCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryDayCountDao extends JpaRepository<HistoryDayCount, HistoryDayCount>, JpaSpecificationExecutor<HistoryDayCount>
{

    @Query(nativeQuery = true, value = "select  * from mixAll.dbo.nt_historyDayCount where community_id = ?1 order by id desc")
    Page<HistoryDayCount> getDayAmountsByCommunityId(Integer communityId, Pageable pageable);

}
