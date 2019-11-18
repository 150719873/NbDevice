package com.hust.nb.Dao;

import com.hust.nb.Entity.Device;
import com.hust.nb.vo.DeviceOutputVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Repository
public interface DeviceDao extends JpaRepository<Device,Device>,JpaSpecificationExecutor<Device> {

    Device findByDeviceNoAndEnprNo(String deviceNo, String enprNo);


    Device findByImei(String imei);

    Page<Device> findAllByDeviceNoAndEnprNo(String deviceNo, String enprNo, Pageable pageable);

    @Query(nativeQuery = true, value = "select state from mixAll.dbo.nb_device where user_id = ?1")
    List<Integer> findStateByUserId(int userId);

    @Query(value = "select new com.hust.nb.vo.DeviceOutputVO(d.id,d.deviceNo,u.userName,d.imei,d.dayAmount,d.deviceType,d.monthAmount,d.readTime,d.readValue,d.state,d.userId,d.waterType,d.valve,d.batteryVoltage,d.rssi,d.pinStatus,d.macAddr,u.userAddr,u.blockId,u.userNo" +
                ") FROM Device d INNER JOIN User u ON d.userId = u.userId WHERE u.blockId in (SELECT blockId FROM Block b WHERE b.communityId = ?1) AND d.state <> 0 and d.imei is not null ORDER BY u.blockId")
     Page<DeviceOutputVO> getFailDeviceByCommunityId(int communityId, Pageable pageable);

    @Query(value = "select new com.hust.nb.vo.DeviceOutputVO(d.id,d.deviceNo,u.userName,d.imei,d.dayAmount,d.deviceType,d.monthAmount,d.readTime,d.readValue,d.state,d.userId,d.waterType,d.valve,d.batteryVoltage,d.rssi,d.pinStatus,d.macAddr,u.userAddr,u.blockId,u.userNo" +
            ") FROM Device d INNER JOIN User u ON d.userId = u.userId WHERE u.blockId in (SELECT blockId FROM Block b WHERE b.communityId = ?1) ORDER BY u.blockId")
    Page<DeviceOutputVO> getDeviceByCommunityId(int communityId, Pageable pageable);

    @Query(value = "select new com.hust.nb.vo.DeviceOutputVO(d.id,d.deviceNo,u.userName,d.imei,d.dayAmount,d.deviceType,d.monthAmount,d.readTime,d.readValue,d.state,d.userId,d.waterType,d.valve,d.batteryVoltage,d.rssi,d.pinStatus,d.macAddr,u.userAddr,u.blockId,u.userNo" +
            ") FROM Device d INNER JOIN User u ON d.userId = u.userId WHERE u.blockId = ?1 ORDER BY u.blockId")
    Page<DeviceOutputVO> getDeviceByBlockId(int BlockId, Pageable pageable);

    List<Device> findAllByUserId(int userId);

    List<Device> findAllByEnprNo(String enprNo);

    @Transactional
    void deleteByDeviceNoAndEnprNo(String deviceNo, String enprNo);

    @Query(nativeQuery = true, value = "select device_no from mixAll.dbo.nb_device where enprNo = ?1")
    List<String> findDeviceNoByEnprNo(String enprNo);

    @Query(nativeQuery = true, value = "select imei from mixAll.dbo.nb_device")
    List<String> findImei();

    Device findByDeviceNoAndImei(String deviceNo, String imei);

    @Query(nativeQuery = true, value = "select count(id) from mixAll.dbo.nb_device where id in (select device_id from mixAll.dbo.nt_deviceRelation where community_id = ?1 and enprNo = ?2)")
    Integer getCountByCommunityId(int communityId, String enprNo);

    @Query(nativeQuery = true, value = "select count(id) from mixAll.dbo.nb_device where id in (select device_id from mixAll.dbo.nt_deviceRelation where community_id = ?1 and enprNo = ?2) and state = 0")
    Integer getSucCountByCommunityId(int communityId, String enprNo);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nb_device  where state != 0 and imei != ''")
    List<Device> getFailDevice();
}

