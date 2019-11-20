package com.hust.nb.Dao;


import com.hust.nb.Entity.HistoryDayCount;
import com.hust.nb.Entity.HistoryMonthCount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryMonthCountDao extends JpaRepository<HistoryMonthCount, HistoryMonthCount>, JpaSpecificationExecutor<HistoryMonthCount>
{
    @Query(nativeQuery = true, value = "select  * from mixAll.dbo.nt_historyMonthCount where community_name = ?1")
    Page<HistoryMonthCount> getMonthAmountsByCommunityName(String communityName, Pageable pageable);
}
