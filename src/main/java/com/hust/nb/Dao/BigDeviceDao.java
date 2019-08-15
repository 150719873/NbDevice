package com.hust.nb.Dao;

import com.hust.nb.Entity.BigDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BigDeviceDao extends JpaRepository<BigDevice, BigDevice>, JpaSpecificationExecutor<BigDevice> {

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nb_bigDevice where M_PipeDn = ?1")
    BigDevice findByMPipeDn(String MPipeDn);
}
