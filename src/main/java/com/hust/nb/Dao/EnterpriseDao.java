package com.hust.nb.Dao;

import com.hust.nb.Entity.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Repository
public interface EnterpriseDao extends JpaRepository<Enterprise,Enterprise>,JpaSpecificationExecutor<Enterprise> {

    @Query(nativeQuery = true,value = "select * from mixAll.dbo.nt_enterprise where id = ?1")
    Enterprise findEnprNoById(Integer id);

    Enterprise findByEnprNo(String enprNo);

}
