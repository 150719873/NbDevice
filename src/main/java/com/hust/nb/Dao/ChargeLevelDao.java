package com.hust.nb.Dao;

import com.hust.nb.Entity.ChargeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Repository
public interface ChargeLevelDao extends JpaRepository<ChargeLevel,ChargeLevel>,JpaSpecificationExecutor<ChargeLevel> {
    List<ChargeLevel> findChargeLevelByEnprNo(String enprNo);

    ChargeLevel findChargeLevelByEnprNoAndType(String enprNo, int type);
}
