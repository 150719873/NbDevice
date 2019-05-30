package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.MonthcountDao;
import com.hust.nb.Entity.Device;
import com.hust.nb.Entity.Monthcost;
import com.hust.nb.Entity.Monthcount;
import com.hust.nb.Service.MonthcountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Service
public class MonthcountServiceImpl implements MonthcountService {

    @Autowired
    MonthcountDao monthcountDao;

    @Override
    public Monthcount findLatestRecordByDeviceNoAndEnprNo(String deviceNo, String enprNo) {
        return monthcountDao.findTopByDeviceNoAndEnprNoOrderByIdDesc(deviceNo, enprNo);
    }

    @Override
    public Page<Monthcount> findMonthcountPage(String deviceNo, String enprNo, int rows, int page) {
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<Monthcount> pageList = monthcountDao.findAllByDeviceNoAndEnprNo(deviceNo, enprNo, pageable);
        return pageList;
    }

    @Override
    public void save(Monthcount monthcount) {
        monthcountDao.save(monthcount);
    }

    @Override
    public void addMonthcost(Monthcost tmonthcost) {

    }

    @Override
    public void delMonthcostByKey(Monthcost id) {

    }

    @Override
    public Monthcost updateMonthcost(Monthcost tmonthcost) {
        return null;
    }

    @Override
    public Monthcost getMonthcostByKey(int id) {
        return null;
    }

    @Override
    public List<Monthcost> findAllByUserIdAndDate(int userId, String date) {
        return null;
    }

    @Override
    public Monthcost findTop1ByUserId(int userId) {
        return null;
    }

    @Override
    public Page<Monthcost> findAllByUserIdOrderByIdDesc(int page, int rows, int userId) {
        return null;
    }

    @Override
    public Page<Monthcost> findAllByUserId(int page, int rows, int userId) {
        return null;
    }

    @Override
    public Page<Monthcost> findAllByOperatorIdAndDateOrderByIdDesc(int page, int rows, int operatorId, String data) {
        return null;
    }
}
