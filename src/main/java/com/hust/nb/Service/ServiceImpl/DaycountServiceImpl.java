package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.DaycountDao;
import com.hust.nb.Entity.Daycount;
import com.hust.nb.Service.DaycountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
@Service
public class DaycountServiceImpl implements DaycountService {

    @Autowired
    DaycountDao daycountDao;

    @Override
    public Page<Daycount> findDaycountPage(String deviceNo, String enprNo, int rows, int page) {
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<Daycount> pageList = daycountDao.findByDeviceNoAndEnprNo(deviceNo, enprNo, pageable);
        return pageList;
    }

    @Override
    public Page<Daycount> findPartDaycountPage(String deviceNo, String enprNo, Timestamp start, Timestamp end, int rows, int page){
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<Daycount> pageList = daycountDao.findPartDaycountPage(deviceNo, enprNo,start, end, pageable);
        return pageList;
    }
}
