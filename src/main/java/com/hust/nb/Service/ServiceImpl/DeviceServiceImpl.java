package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.DeviceDao;
import com.hust.nb.Entity.Device;
import com.hust.nb.Service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    DeviceDao deviceDao;

    @Override
    public Device getByDeviceNoAndEnprNo(String deviceNo, String enprNo) {
        return deviceDao.findByDeviceNoAndEnprNo(deviceNo, enprNo);
    }

    @Override
    public Page<Device> getDevicePageByDeviceNoAndEnprNo(String deviceNo, String enprNo, int page, int rows) {
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<Device> pageList = deviceDao.findAllByDeviceNoAndEnprNo(deviceNo, enprNo, pageable);
        return pageList;
    }

    @Override
    public void updateDevice(Device device) {
        deviceDao.save(device);
    }

    @Override
    public List<Device> getAllByUserId(int userId) {
        return deviceDao.findAllByUserId(userId);
    }

    @Override
    public void addDevice(Device device) {
        deviceDao.save(device);
    }

    @Override
    public void delDeviceByDeviceNoAndEnprNo(String deviceNo, String enprNo) {
        deviceDao.deleteByDeviceNoAndEnprNo(deviceNo, enprNo);
    }

    @Override
    public List<Device> findAllByEnprNo(String enprNo) {
        return deviceDao.findAllByEnprNo(enprNo);
    }
}
