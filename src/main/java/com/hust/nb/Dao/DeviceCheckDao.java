package com.hust.nb.Dao;

import com.hust.nb.Entity.DeviceCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface DeviceCheckDao extends JpaRepository<DeviceCheck,DeviceCheck>, JpaSpecificationExecutor<DeviceCheck> {

    Page<DeviceCheck> findByEnprNo(String enprNo, Pageable pageable);

    List<DeviceCheck> findByEnprNo(String enprNo);

    DeviceCheck findByImeiAndDeviceNo(String imei, String deviceNo);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nb_deviceCheck where enprNo = ?1 and read_time BETWEEN ?2 AND ?3")
    Page<DeviceCheck> findSuccessByEnprNo(String enprNo, Timestamp data, Timestamp date, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nb_deviceCheck where enprNo = ?1 and read_time < ?2")
    Page<DeviceCheck> findfailedByEnprNo(String enprNo, Timestamp data, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nb_deviceCheck where imei = ?1")
    DeviceCheck findByImei(String imei);

    DeviceCheck findByEnprNoAndDeviceNo(String enprNo, String deviceNo);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM mixAll.dbo.nb_deviceCheck where enprNo = ?1")
    Integer selectCounts(String enprNo);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM mixAll.dbo.nb_deviceCheck WHERE enprNo = ?1 AND read_time BETWEEN ?2 AND ?3")
    Integer selectSuccessCounts(String enprNo , Timestamp data, Timestamp date);

    @Transactional
    void deleteByEnprNo(String enprNo);

    @Transactional
    void deleteByImeiAndEnprNo(String imei,String enprNo);
}
