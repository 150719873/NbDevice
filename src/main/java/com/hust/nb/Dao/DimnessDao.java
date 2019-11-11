package com.hust.nb.Dao;

import com.hust.nb.Entity.Dimness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author: suxinyu
 * @DateTme: 2019/11/4 11:05
 */
@Repository
public interface DimnessDao extends JpaRepository<Dimness,Dimness>, JpaSpecificationExecutor<Dimness>
{

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nb_waterquality where enprNo = ?1 order by id desc")
    Page<Dimness> findAllByEnprNo(String enprNo, Pageable pageable);
}
