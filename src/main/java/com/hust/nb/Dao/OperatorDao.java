package com.hust.nb.Dao;

import com.hust.nb.Entity.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/21
 */
@Repository
public interface OperatorDao extends JpaRepository<Operator,Operator>,JpaSpecificationExecutor<Operator> {
    Operator findOperatorByUserNameAndPassword(String userName, String password);

    List<Operator> findAllByEnprNo(String enprNo);

    @Transactional
    void deleteByOperatorId(int operatorId);

    @Query(nativeQuery = true, value = "select id from mixAll.dbo.nt_operator where id = ?1")
    int findIdById(int operatorId);
}
