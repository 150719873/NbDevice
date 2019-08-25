package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.DaycountDao;
import com.hust.nb.Dao.DeviceChangeDao;
import com.hust.nb.Dao.DeviceDao;
import com.hust.nb.Entity.Daycount;
import com.hust.nb.Entity.Device;
import com.hust.nb.Entity.DeviceChange;
import com.hust.nb.Service.DeviceChangeService;
import com.hust.nb.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:nb
 * Created by Administrator on 2019/5/23
 */
@Service
public class DeviceChangeServiceImpl implements DeviceChangeService {

    @Autowired
    DeviceChangeDao deviceChangeDao;

    @Autowired
    DeviceService deviceService;

    @Autowired
    DeviceChangeService deviceChangeService;

    @Autowired
    DaycountDao daycountDao;

    @Autowired
    DeviceDao deviceDao;

    @Override
    public void addDevicechange(DeviceChange devicechange) {
        deviceChangeDao.save(devicechange);
    }

    @Transactional
    @Override
    public void changeDevice(Device oldDevice, Device newDevice, DeviceChange deviceChange) {
        deviceDao.save(oldDevice);
        deviceDao.save(newDevice);
        deviceChangeService.addDevicechange(deviceChange);
    }


}
