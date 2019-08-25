package com.hust.nb.Dao;

import com.hust.nb.Entity.Daycost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Description:nb
 * Created by Administrator on 2019/8/25
 */
@Repository
public interface DaycostDao extends JpaRepository<Daycost,Daycost>,JpaSpecificationExecutor<Daycost> {

}
