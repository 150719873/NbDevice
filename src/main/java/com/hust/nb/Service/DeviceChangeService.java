package com.hust.nb.Service;

import com.hust.nb.Entity.Daycount;
import com.hust.nb.Entity.Device;
import com.hust.nb.Entity.DeviceChange;

/**
 * Description:nb
 * Created by Administrator on 2019/5/23
 */
public interface DeviceChangeService {
    void addDevicechange(DeviceChange devicechange);

    void changeDevice(Device oldDevice, Device newDevice, DeviceChange deviceChange);
}
