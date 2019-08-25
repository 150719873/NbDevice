package com.hust.nb.Dao;

import com.hust.nb.Entity.Daycount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
@Repository
public interface DaycountDao extends JpaRepository<Daycount,Daycount>,JpaSpecificationExecutor<Daycount> {


    @Query(nativeQuery = true, value = "select top 1 * from mixAll.dbo.nt_daycount where device_no = ?1 and enprNo = ?2 order by id desc")
    Daycount findLatestDaycountRecord(String deviceNo, String enprNo);

    @Query(nativeQuery = true, value = "select top 1 * from mixAll.dbo.nt_daycount where device_no = ?1 and enprNo = ?2 order by id asc")
    Daycount findOldestDaycountRecord(String deviceNo, String enprNo);

    Page<Daycount> findByDeviceNoAndEnprNo(String deviceNo, String enprNo, Pageable pageable);

    @Query(nativeQuery = true,value = "select * from mixAll.dbo.nt_daycount where device_no = ?1 and enprNo = ?2 and start_time > ?3 AND end_time < ?4")
    Page<Daycount> findPartDaycountPage(String deviceNo, String enprNo, Timestamp start, Timestamp end, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nt_daycount where device_no = ?1 and enprNo = ?2 and date = ?3")
    Daycount findLatestRecord(String deviceNo, String enprNo, int date);

}
