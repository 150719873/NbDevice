package com.hust.nb.Dao;

import com.hust.nb.Entity.DeviceRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DeviceRelationDao extends JpaRepository<DeviceRelation,DeviceRelation>, JpaSpecificationExecutor<DeviceRelation> {


}
