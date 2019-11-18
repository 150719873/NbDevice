package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.*;
import com.hust.nb.Entity.*;
import com.hust.nb.util.MsgSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/8/25
 */
@Service
public class DaycostService {

    @Autowired
    UserDao userDao;

    @Autowired
    DaycostDao daycostDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DaycountDao daycountDao;

    @Transactional(rollbackFor = Exception.class)
    public void userProcessAndDeviceRefresh(User user, Daycost daycost, List<Device> deviceList) {
        BigDecimal left = user.getAccountBalance().subtract(daycost.getCostMoney());
        if (left.compareTo(new BigDecimal("20")) == -1) {
            MsgSender.send(user.getUserTel(), String.valueOf(left));
        }
        user.setAccountBalance(left);
        userDao.save(user);
        daycostDao.save(daycost);
        for (Device device : deviceList) {
            device.setMonthAmount(new BigDecimal("0"));
            deviceDao.save(device);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void userProcess(User user, Daycost daycost) {
        BigDecimal left = user.getAccountBalance().subtract(daycost.getCostMoney());
//        if (left.compareTo(new BigDecimal("20")) == -1) {
//            MsgSender.send(user.getUserTel(), String.valueOf(left));
//        }
        user.setAccountBalance(left);
        userDao.save(user);
        daycostDao.save(daycost);
    }

    @Transactional
    public void updateDeviceAndDaycount(Device device, Daycount daycount) {
        deviceDao.save(device);
        daycountDao.save(daycount);
    }


}
