package com.hust.nb.Service;

import com.hust.nb.Entity.Device;
import org.springframework.data.domain.Page;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface DeviceService {
    Device getByDeviceNoAndEnprNo(String deviceNo, String enprNo);

    Page<Device> getDevicePageByDeviceNoAndEnprNo(String deviceNo, String enprNo, int page, int rows);

    void updateDevice(Device device);
}
