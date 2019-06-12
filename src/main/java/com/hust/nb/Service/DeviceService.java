package com.hust.nb.Service;

import com.hust.nb.Entity.Device;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface DeviceService {
    Device getByDeviceNoAndEnprNo(String deviceNo, String enprNo);

    Page<Device> getDevicePageByDeviceNoAndEnprNo(String deviceNo, String enprNo, int page, int rows);

    void updateDevice(Device device);

    List<Device> getAllByUserId(int userId);

    void addDevice(Device device);

    void delDeviceByDeviceNoAndEnprNo(String deviceNo, String enprNo);

    List<Device> findAllByEnprNo(String enprNo);

    List<String> findDeviceNoByEnprNo(String enprNo);

    List<String> findImei();
}
