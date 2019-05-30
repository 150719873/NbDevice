package com.hust.nb.Service;

import com.hust.nb.Entity.ChargeLevel;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
public interface ChargeLevelService {
    List<ChargeLevel> getAllChargeLevelByEnprNo(String enprNo);

    ChargeLevel getByEnprNoAndType(String enprNo, int type);

    void save(ChargeLevel chargeLevel);
}
