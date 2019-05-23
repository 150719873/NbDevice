package com.hust.nb.Dao;

import com.hust.nb.Entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

    Page<Device> findAllByDeviceNoAndEnprNo(String deviceNo, String enprNo, Pageable pageable);

    List<Device> findAllByUserId(int userId);

    @Transactional
    void deleteByDeviceNoAndEnprNo(String deviceNo, String enprNo);
}

