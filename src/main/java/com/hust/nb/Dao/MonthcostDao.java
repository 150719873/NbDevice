package com.hust.nb.Dao;

import com.hust.nb.Entity.Monthcost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Repository
public interface MonthcostDao extends JpaRepository<Monthcost,Monthcost>,JpaSpecificationExecutor<Monthcost> {

    @Query(nativeQuery = true,value = "SELECT * FROM mixAll.dbo.nt_monthcost WHERE date LIKE %?2% and user_id = ?1 ORDER BY id DESC")
    List<Monthcost> findAllByUserIdAndDate(int userId , String date);

    Monthcost findTop1ByUserId(int userId);

    Monthcost findById(int id);

    @Query(nativeQuery = true,value = "select * from mixAll.dbo.nt_monthcost where user_id = ?1 and money_change < 0 ORDER BY id DESC ")
    Page<Monthcost> findAllByUserId(int userId , Pageable pageable);

    Page<Monthcost> findAllByUserIdOrderByIdDesc(int userId , Pageable pageable);

    @Query(nativeQuery = true,value = "SELECT * FROM mixAll.dbo.nt_monthcost WHERE date LIKE %?1% and operator_id = ?2 ORDER BY id DESC")
    Page<Monthcost> findAllByOperatorIdAndDateOrderByIdDesc(String date, int operatorId ,  Pageable pageable1);
}
