package com.hust.nb.Dao;

import com.hust.nb.Entity.Dimness;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author: suxinyu
 * @DateTme: 2019/11/4 11:05
 */
@Repository
public interface DimnessDao extends JpaRepository<Dimness,Dimness>, JpaSpecificationExecutor<Dimness> {

}
