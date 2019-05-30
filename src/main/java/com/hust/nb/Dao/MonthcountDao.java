package com.hust.nb.Dao;

import com.hust.nb.Entity.Monthcount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
public interface MonthcountDao extends JpaRepository<Monthcount,Monthcount>,JpaSpecificationExecutor<Monthcount> {

    Monthcount findById(int id);

    Page<Monthcount> findAllByDeviceNoAndEnprNo(String deviceNo, String enprNo, Pageable pageable);

    @Query(nativeQuery = true,value = "SELECT * FROM ConnectTest.dbo.nt_monthcount WHERE device_no = ?1 ORDER BY id DESC LIMIT 1")
    Monthcount findMonthcountByDeviceNoOrderByIdDesc(String deviceNo);

    Monthcount findTopByDeviceNoAndEnprNoOrderByIdDesc(String deviceNo, String enprNo);
}
