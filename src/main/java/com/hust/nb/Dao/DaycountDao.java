package com.hust.nb.Dao;

import com.hust.nb.Entity.Daycount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
public interface DaycountDao extends JpaRepository<Daycount,Daycount>,JpaSpecificationExecutor<Daycount> {

    Daycount findDaycountByDeviceIdOrderByIdDesc(int deviceId);
}
