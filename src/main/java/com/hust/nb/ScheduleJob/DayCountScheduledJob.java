package com.hust.nb.ScheduleJob;

import com.hust.nb.Controller.DeviceController;
import com.hust.nb.Dao.*;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.ServiceImpl.DaycostService;
import com.hust.nb.util.GetDate;
import com.hust.nb.util.QBTDeviceTmpGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
@Component
public class DayCountScheduledJob {

    private static Logger logger = LoggerFactory.getLogger(DayCountScheduledJob.class);

    @Autowired
    HistoryDao historydataDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DaycountDao daycountDao;

    @Autowired
    QBTDeviceTmpGetter deviceTmpGetter;

    @Autowired
    RegionDao regionDao;

    @Autowired
    BlockDao blockDao;

    @Autowired
    UserDao userDao;

    @Autowired
    CommunityDao communityDao;

    @Autowired
    WarningDao warningDao;

    @Autowired
    EnterpriseDao enterpriseDao;

    @Autowired
    ChargeLevelDao chargeLevelDao;

    @Autowired
    DaycostDao daycostDao;

    @Autowired
    DaycostService daycostService;

    @Autowired
    DeviceController deviceController;

    /**
     * 每天六点更新所有水表，针对每一个水表计算日用水量，插入dayCount
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void update() {
        logger.info("====================日水量统计====================");
        List<Device> deviceList = deviceDao.findAll();
        BigDecimal limit = new BigDecimal(200);
        BigDecimal zero = new BigDecimal(0);
        Calendar calendar = Calendar.getInstance();
        for (Device device : deviceList) {
            try{
                if (device.getImei() != null) {
                    Daycount daycount = new Daycount();
                    daycount.setDeviceNo(device.getDeviceNo());
                    daycount.setEnprNo(device.getEnprNo());
                    daycount.setEndTime(device.getReadTime());
                    daycount.setEndValue(device.getReadValue());
                    daycount.setDate(GetDate.getYesterday());
                    daycount.setDayAmount(device.getReadValue().subtract(device.getPreReadValue()));
                    if (daycount.getStartTime() == daycount.getEndTime()) {
                        daycount.setState(2);
                    } else if (daycount.getDayAmount().compareTo(limit) == 1) {
                        daycount.setState(3);
                    } else if (daycount.getDayAmount().compareTo(zero) == -1) {
                        daycount.setState(1);
                    } else {
                        daycount.setState(0);
                    }
                    device.setPreReadValue(device.getReadValue());
                    device.setPreReadTime(device.getReadTime());
                    if(device.getMonthAmount() == null && device.getMonthAmount().compareTo(new BigDecimal("0")) <= 0){
                        device.setMonthAmount(daycount.getDayAmount());
                    } else {
                        device.setMonthAmount(device.getMonthAmount().add(daycount.getDayAmount()));
                    }
                    daycostService.updateDeviceAndDaycount(device, daycount);
                }
            } catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }

    /**
     * 定时功能，更新水表读数
     */
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void updateNBDevice() {
        Map<String, Object> map = new HashMap<>();
        map.put("flag", 0);
        map.put("position", 0);
        map.put("count", 500);
        map.put("check", 1);
        try {
            deviceController.getSZNBdevice(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 每天七点计算用户水费
     */
    @Scheduled(cron = "0 0 7 * * ?")
    public void calculate() {
        logger.info("====================日水费统计====================");
        Calendar calendar = Calendar.getInstance();
        List<Enterprise> enterprises = enterpriseDao.findAll();
        for (Enterprise enterprise : enterprises) {
            String enprNo = enterprise.getEnprNo();
            List<User> userList = userDao.findAllByEnprNo(enprNo);
            for (User user : userList) {
                try{
                    userProcess(calendar, enprNo, user);
                } catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 根据用户的月水量计算水费插入daycost表，并且检查用户余额，不足发短信
     *
     * @param calendar
     * @param enprNo
     * @param user
     */
    public void userProcess(Calendar calendar, String enprNo, User user) throws Exception {
        Daycost daycost = new Daycost();
        List<Device> deviceList = deviceDao.findAllByUserId(user.getUserId());
        BigDecimal userDayAmount = new BigDecimal("0");
        BigDecimal userMonthAmount = new BigDecimal("0");
        for (Device device : deviceList) {
            Daycount daycount = daycountDao.findLatestRecord(
                    device.getDeviceNo(), device.getEnprNo(), GetDate.getYesterday()
            );
            BigDecimal deviceDayAmount = daycount == null ? new BigDecimal("0") : daycount.getDayAmount();
            userDayAmount = userDayAmount.add(deviceDayAmount);
            BigDecimal preMonthAmount = device.getMonthAmount();
            if(preMonthAmount == null || preMonthAmount.compareTo(new BigDecimal("0")) <= 0){
                preMonthAmount = new BigDecimal("0");
            }
            userMonthAmount = userMonthAmount.add(preMonthAmount);
        }
        int userType = user.getUserType();
        ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, userType);
        calculatePrice(userDayAmount, userMonthAmount, chargelevel, daycost, calendar.get(Calendar.DATE));
        if (calendar.get(Calendar.DATE) == 1) {
            //每月第一天核查完上月水费后清空device的月用水量
            daycostService.userProcessAndDeviceRefresh(user, daycost, deviceList);
        } else {
            daycostService.userProcess(user, daycost);
        }
    }

    /**
     * 阶梯水价:  * minCharge * firstEdge * secondEdge * thirdEdge * fourthEdge * fifthEdge * sixthEdge
     * <p>
     * 阶梯水量:  0 -------- min ------ first ------ second ---- third ------ fourth ---- fifth --------->
     */
    private void calculatePrice(BigDecimal userDayAmount, BigDecimal userMonthAmount, ChargeLevel chargelevel, Daycost daycost, int date) {
        BigDecimal min = chargelevel.getMin();
        BigDecimal minCharge = chargelevel.getMinCharge();
        BigDecimal first = chargelevel.getFirst();
        BigDecimal second = chargelevel.getSecond();
        BigDecimal third = chargelevel.getThird();
        BigDecimal fourth = chargelevel.getFourth();
        BigDecimal fifth = chargelevel.getFifth();
        BigDecimal firstEdge = chargelevel.getFirstEdge();
        BigDecimal secondEdge = chargelevel.getSecondEdge();
        BigDecimal thirdEdge = chargelevel.getThirdEdge();
        BigDecimal fourthEdge = chargelevel.getFourthEdge();
        BigDecimal fifthEdge = chargelevel.getFifthEdge();
        BigDecimal sixthEdge = chargelevel.getSixthEdge();
        if (fifth != null && userMonthAmount.compareTo(fifth) != -1 && userMonthAmount.subtract(userDayAmount).compareTo(fifth) != -1) {
            BigDecimal cost = sixthEdge.multiply(userDayAmount);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if(fifth != null && userMonthAmount.compareTo(fifth) != -1 && userMonthAmount.subtract(userDayAmount).compareTo(fifth) == -1){
            BigDecimal beyond = userMonthAmount.subtract(fifth);
            BigDecimal higher = beyond.subtract(sixthEdge);
            BigDecimal lower = userDayAmount.subtract(beyond).subtract(fifth);
            BigDecimal cost = higher.add(lower);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fifth != null && userMonthAmount.compareTo(fifth) == -1 && userMonthAmount.compareTo(fourth) != -1) {
            BigDecimal total = userDayAmount.add(userMonthAmount);
            BigDecimal beyond = total.subtract(fifth);
            BigDecimal higher = beyond.multiply(sixthEdge);
            BigDecimal lower = userDayAmount.subtract(beyond).multiply(fifthEdge);
            BigDecimal cost = higher.add(lower);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        }  else if (fourth != null && userMonthAmount.compareTo(fourth) == -1 && userMonthAmount.compareTo(third) != -1) {
            BigDecimal total = userDayAmount.add(userMonthAmount);
            BigDecimal beyond = total.subtract(fourth);
            BigDecimal higher = beyond.multiply(fifthEdge);
            BigDecimal lower = userDayAmount.subtract(beyond).multiply(fourthEdge);
            BigDecimal cost = higher.add(lower);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (third != null && userMonthAmount.compareTo(third) == -1 && userMonthAmount.compareTo(second) != -1) {
            BigDecimal total = userDayAmount.add(userMonthAmount);
            BigDecimal beyond = total.subtract(third);
            BigDecimal higher = beyond.multiply(fourthEdge);
            BigDecimal lower = userDayAmount.subtract(beyond).multiply(thirdEdge);
            BigDecimal cost = higher.add(lower);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (second != null && userMonthAmount.compareTo(second) == -1 && userMonthAmount.compareTo(first) != -1) {
            BigDecimal total = userDayAmount.add(userMonthAmount);
            BigDecimal beyond = total.subtract(second);
            BigDecimal higher = beyond.multiply(thirdEdge);
            BigDecimal lower = userDayAmount.subtract(beyond).multiply(secondEdge);
            BigDecimal cost = higher.add(lower);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (first != null && userMonthAmount.compareTo(first) == -1 && userMonthAmount.compareTo(min) != -1) {
            BigDecimal total = userDayAmount.add(userMonthAmount);
            BigDecimal beyond = total.subtract(first);
            BigDecimal higher = beyond.multiply(secondEdge);
            BigDecimal lower = userDayAmount.subtract(beyond).multiply(firstEdge);
            BigDecimal cost = higher.add(lower);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (min != null && userMonthAmount.compareTo(min) == -1) {
            BigDecimal total = userDayAmount.add(userMonthAmount);
            BigDecimal beyond = total.subtract(min);
            BigDecimal higher = beyond.multiply(firstEdge);
            BigDecimal cost = higher.add(minCharge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else {
            //凌晨计算前一天水费，如果是一号代表上个月用水量未超过最低限额，扣费
            //其余日期(直到月底计算前)不扣费
            if (date == 1) {
                daycost.setCostMoney(minCharge);
            } else {
                daycost.setCostMoney(new BigDecimal("0"));
            }
        }
    }


    /**
     * nb故障诊断每日定时分析
     */
    @Scheduled(cron = "0 30 10 * * ?")
    public void warining() {
        Warning warning = new Warning();
        List<Device> deviceList = deviceDao.getFailDevice();

        for (Device device1 : deviceList) {
            if (device1.getImei() != null) {
                Daycount daycount = daycountDao.findLatestDaycountRecord(device1.getDeviceNo(), device1.getEnprNo());
                Block block = blockDao.getByBlockId(userDao.findUserByUserId(device1.getUserId()).getBlockId());
                Community community = communityDao.getByCommunityId(block.getCommunityId());
                Region region = regionDao.findByRegionId(community.getRegionId());
                warning.setWarningType(device1.getState());
                warning.setWarningDate(GetDate.getCurrentDay());
                warning.setDeviceNo(device1.getDeviceNo());
                warning.setCompleted(0);
                warning.setImei(device1.getImei());
                warning.setReadTime(daycount.getEndTime());
                warning.setLastReadtime(daycount.getStartTime());
                warning.setShowValue(daycount.getEndValue());
                warning.setPrShowValue(daycount.getStartValue());
                warning.setBlockName(block.getBlockName());
                warning.setCommunityName(community.getCommunityName());
                warning.setRegionName(region.getRegionName());
                warning.setEnprNo(community.getEnprNo());
                try {
                    warningDao.save(warning);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

}


