package com.hust.nb.Dao;

import com.hust.nb.Entity.Historydata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Repository
public interface HistoryDao extends JpaRepository<Historydata,Historydata>,JpaSpecificationExecutor<Historydata> {

    Page<Historydata> findByImei(String imei, Pageable pageable);

    Historydata findByImeiOrderByIdDesc(String imei);

    @Query(nativeQuery = true,
            value = "select * from mixAll.dbo.nt_historydata " +
                    "where DateDiff(dd,read_time,getdate())<=30 " +
                    "and imei = ?1")
    List<Historydata> find30daysData(String imei);

    @Query(nativeQuery = true,
            value = "SELECT * FROM mixAll.dbo.nt_historydata where DATEDIFF(month,read_time,GETDATE())=0 and imei = ?1")
    List<Historydata> getCurMonthData(String imei);

    @Query(nativeQuery = true,
            value = "SELECT * FROM mixAll.dbo.nt_historydata where DATEDIFF(month,read_time,GETDATE())=1 and imei = ?1")
    List<Historydata> getPreMonthData(String imei);

    @Query(nativeQuery = true, value = "SELECT top 1 * FROM mixAll.dbo.nt_historydata where imei = " +
            "(select imei from mixAll.dbo.nb_device where device_no = ?1 and enprNo = ?2) order by id desc")
    Historydata getLatestRecord(String deviceNo, String enprNo);
}
