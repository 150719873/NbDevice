package com.hust.nb.Dao;

import com.hust.nb.Entity.PayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author: suxinyu
 * @DateTme: 2019/10/14 16:02
 */
@Repository
public interface PayHistoryDao extends JpaRepository<PayHistory,PayHistory>, JpaSpecificationExecutor<PayHistory> {

}
