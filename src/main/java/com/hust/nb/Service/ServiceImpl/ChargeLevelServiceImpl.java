package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.ChargeLevelDao;
import com.hust.nb.Entity.ChargeLevel;
import com.hust.nb.Service.ChargeLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Service
public class ChargeLevelServiceImpl implements ChargeLevelService {

    @Autowired
    ChargeLevelDao chargeLevelDao;

    @Override
    public List<ChargeLevel> getAllChargeLevelByEnprNo(String enprNo) {
        return chargeLevelDao.findChargeLevelByEnprNo(enprNo);
    }

    @Override
    public ChargeLevel getByEnprNoAndType(String enprNo, int type) {
        return chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, type);
    }

    @Override
    public void save(ChargeLevel chargeLevel) {
        chargeLevelDao.save(chargeLevel);
    }
}
