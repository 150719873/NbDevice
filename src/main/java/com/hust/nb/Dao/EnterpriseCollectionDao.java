package com.hust.nb.Dao;

import com.hust.nb.Entity.EnterCollectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/6/29
 */
@Repository
public interface EnterpriseCollectionDao extends JpaRepository<EnterCollectType,EnterCollectType>,JpaSpecificationExecutor<EnterCollectType> {
    @Query(nativeQuery = true,value = "SELECT collection_type_id FROM mixAll.dbo.nt_enterCollectType WHERE enprNo = ?1")
    List<Integer> findAllType(String enprNo);

    @Transactional
    void deleteAllByEnprNo(String enprNo);
}
