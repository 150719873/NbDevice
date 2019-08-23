package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.HistoryDao;
import com.hust.nb.Entity.Historydata;
import com.hust.nb.Service.HistorydataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Service
public class HistorydateServiceImpl implements HistorydataService {

    @Autowired
    HistoryDao historyDao;

    @Override
    public Page<Historydata> getHisPageByImei(String imei, int page, int rows) {
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<Historydata> pageList = historyDao.findByImei(imei, pageable);
        return pageList;
    }

    @Override
    public List<Historydata> get30DaysData(String imei) {
        return historyDao.find30daysData(imei);

    }

    @Override
    public List<Historydata> getCurMonthData(String imei) {
        return historyDao.getCurMonthData(imei);
    }

    @Override
    public List<Historydata> getPreMonthData(String imei) {
        return historyDao.getPreMonthData(imei);
    }

    @Override
    public List<Historydata> getDataBetweenTime(String imei, Timestamp start, Timestamp end){
        return historyDao.getDataBetweenTime(imei,start,end);
    }

    @Override
    public Historydata getLatestRecord(String deviceNo, String enprNo) {
        return historyDao.getLatestRecord(deviceNo, enprNo);
    }

    @Override
    public void save(Historydata historydata){
        historyDao.save(historydata);
    }
}

