package com.hust.nb.ScheduleJob;

import com.hust.nb.Dao.DaycountDao;
import com.hust.nb.Dao.DeviceDao;
import com.hust.nb.Dao.HistoryDao;
import com.hust.nb.Entity.Daycount;
import com.hust.nb.Entity.Device;
import com.hust.nb.Entity.Historydata;
import com.hust.nb.util.GetDate;
import com.hust.nb.util.QBTDeviceTmpGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
@Component
public class DayCountScheduledJob {

    @Autowired
    HistoryDao historydataDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DaycountDao daycountDao;

    @Autowired
    QBTDeviceTmpGetter deviceTmpGetter;

    @Value("${com.QBTT.dbPrefix}")
    private String dbPrefix;

    /**
     * 每天执行一次
     * 10点执行更新，针对每个水表插入dayCount记录,显示的是查看当天前一天的日用水量
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void dayCountUpdate() {
        System.out.println("====================日水量统计====================");
        List<Device> deviceList = deviceDao.findAll();
        BigDecimal limit = new BigDecimal(200);
        BigDecimal zero = new BigDecimal(0);
        Calendar calendar = Calendar.getInstance();
        for(Device device : deviceList) {
            String deviceNo = device.getDeviceNo();
            String enprNo = device.getEnprNo();
            Daycount preDaycount = daycountDao.findLatestDaycountRecord(deviceNo,enprNo);//前一天的记录
            Daycount daycount = new Daycount();
            String imei = device.getImei();
            if(imei != null){
                //说明此水表是NB表
                Historydata historydata = historydataDao.findByImeiOrderByIdDesc(imei);
                if(historydata==null){
                    continue;
                }
                daycount.setDeviceNo(device.getDeviceNo());
                daycount.setEnprNo(device.getEnprNo());
                daycount.setEndTime(historydata.getReadTime());
                daycount.setEndValue(historydata.getDeviceValue());
                daycount.setDate(calendar.get(Calendar.DATE));
                if (preDaycount != null) {
                    daycount.setStartTime(preDaycount.getEndTime());
                    daycount.setStartValue(preDaycount.getEndValue());
                    daycount.setDayAmount(historydata.getDeviceValue().subtract(preDaycount.getEndValue()));
                    if(daycount.getStartTime()==daycount.getEndTime()){
                        daycount.setState(2);
                    }else if(daycount.getDayAmount().compareTo(limit)==1){
                        daycount.setState(3);
                    }else if(daycount.getDayAmount().compareTo(zero)==-1){
                        daycount.setState(1);
                    }else {
                        daycount.setState(0);
                    }
                } else {
                    daycount.setDayAmount(historydata.getDeviceValue());
                    daycount.setState(0);
                }
                /**
                 * 已经在nt_history表编写了触发器，每块表只要来了新数据，就更新nb_device表的数据
                 * 所以nb_device表的数据是实时更新的
                 */
                daycountDao.save(daycount);
                device.setDayAmount(daycount.getDayAmount());
                device.setState(daycount.getState());
                deviceDao.save(device);
            } else {
                //说明此水表是集中器水表，需要调接口
                //查询出此水表的最新读数
                String tableName = dbPrefix + GetDate.getCurrentMonth();
                List queryRes = deviceTmpGetter.getLatestRecord(deviceNo, enprNo, tableName);
                Map map = (Map) queryRes.get(0);
                Timestamp endTime = (Timestamp)map.get("readTime");
                BigDecimal showValue = (BigDecimal) map.get("showValue");
                daycount.setDeviceNo(device.getDeviceNo());
                daycount.setEnprNo(device.getEnprNo());
                daycount.setEndTime(endTime);
                daycount.setEndValue(showValue);
                daycount.setDate(calendar.get(Calendar.DATE));
                if (preDaycount != null) {
                    daycount.setStartTime(preDaycount.getEndTime());
                    daycount.setStartValue(preDaycount.getEndValue());
                    daycount.setDayAmount(showValue.subtract(preDaycount.getEndValue()));
                    if(daycount.getStartTime()==daycount.getEndTime()){
                        daycount.setState(2);
                    }else if(daycount.getDayAmount().compareTo(limit)==1){
                        daycount.setState(3);
                    }else if(daycount.getDayAmount().compareTo(zero)==-1){
                        daycount.setState(1);
                    }else {
                        daycount.setState(0);
                    }
                } else {
                    daycount.setDayAmount(showValue);
                    daycount.setState(0);
                }
                /**
                 * 需要采集在插入tmp表的同时更新t_device表的数据
                 * 以保持t_device表的数据也是实时更新的
                 * 如果没有此功能，那么只能在页面点击查看所有表的时候才会更新数据
                 */
                daycountDao.save(daycount);
                device.setReadTime(endTime);
                device.setReadValue(showValue);
                device.setDayAmount(daycount.getDayAmount());
                device.setState(daycount.getState());
                deviceDao.save(device);
            }
        }
    }
}
