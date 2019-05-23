package com.hust.nb.Dao;

import com.hust.nb.Entity.DeviceChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Description:nb
 * Created by Administrator on 2019/5/23
 */
@Repository
public interface DeviceChangeDao extends JpaRepository<DeviceChange,DeviceChange>,JpaSpecificationExecutor<DeviceChange> {

}
