package com.hust.nb.Dao;

import com.hust.nb.Entity.Historydata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Repository
public interface HistoryDao extends JpaRepository<Historydata,Historydata>,JpaSpecificationExecutor<Historydata> {

    Page<Historydata> findByImei(String imei, Pageable pageable);
}
