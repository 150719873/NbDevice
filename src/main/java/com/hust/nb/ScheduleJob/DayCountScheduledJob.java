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
     * 定时功能，每天凌晨三点更新水表读数
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void updateNBDevice() {
        Map<String, Object> map = new HashMap<>();
        map.put("flag", 0);
        map.put("position", 0);
        map.put("count", 500);
        map.put("check", 1);
        try {
            deviceController.getSZNBdevice(map);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
        }
    }

    /**
     * 每天六点更新所有水表，针对每一个水表计算日用水量，插入dayCount，并且更新device表的日用水量
     * 水表的deviceValue在导入的时候就已经导入过，所以该字段不可能为空，即使是初次使用
     * 另外如果是初次使用，凌晨三点的时候就已经更新过preReadValue，所以该字段也不可能为空
     */
    @Scheduled(cron = "0 0 6 * * ?")
    public void update() {
        logger.info("====================日水量统计====================");
        List<Device> deviceList = deviceDao.findAll();
        BigDecimal limit = new BigDecimal(200);
        BigDecimal zero = new BigDecimal(0);
        for (Device device : deviceList) {
            try {
                if (device.getImei() != null) {
                    Daycount daycount = new Daycount();
                    daycount.setDeviceNo(device.getDeviceNo());
                    daycount.setEnprNo(device.getEnprNo());
                    daycount.setEndTime(device.getReadTime());
                    daycount.setEndValue(device.getReadValue());
                    daycount.setDate(GetDate.getYesterday()); //凌晨六点计算的是前一天的用水量，二号六点计算的是一号的用水量
                    BigDecimal dayAmount = device.getReadValue().subtract(device.getPreReadValue());
                    daycount.setDayAmount(dayAmount);
                    device.setDayAmount(dayAmount);
                    if (daycount.getStartTime() == daycount.getEndTime()) {
                        daycount.setState(2);
                    } else if (daycount.getDayAmount().compareTo(limit) == 1) {
                        daycount.setState(3);
                    } else if (daycount.getDayAmount().compareTo(zero) == -1) {
                        daycount.setState(1);
                    } else {
                        daycount.setState(0);
                    }
                    if (device.getMonthAmount() == null && device.getMonthAmount().compareTo(new BigDecimal("0")) <= 0) {
                        device.setMonthAmount(daycount.getDayAmount());
                    } else {
                        device.setMonthAmount(device.getMonthAmount().add(daycount.getDayAmount())); // 月用水量清零操作在计算水费的时候进行
                    }
                    daycostService.updateDeviceAndDaycount(device, daycount);
                }
            } catch (Exception e) {
                logger.error(e.getStackTrace().toString());
            }
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
                try {
                    userProcess(calendar, enprNo, user);
                } catch (Exception e) {
                    logger.error(e.getStackTrace().toString());
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
    private void userProcess(Calendar calendar, String enprNo, User user) throws Exception {
        Daycost daycost = new Daycost();
        List<Device> deviceList = deviceDao.findAllByUserId(user.getUserId());
        BigDecimal userDayAmount = new BigDecimal("0");
        BigDecimal userMonthAmount = new BigDecimal("0");
        for (Device device : deviceList) {
            userDayAmount = userDayAmount.add(device.getDayAmount());
            userMonthAmount = userMonthAmount.add(device.getMonthAmount());
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
        BigDecimal excludeToday = userMonthAmount.subtract(userDayAmount);
        if (fifth != null && userMonthAmount.compareTo(fifth) != -1 && excludeToday.compareTo(fifth) != -1) {
            BigDecimal cost = sixthEdge.multiply(userDayAmount);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fifth != null && userMonthAmount.compareTo(fifth) != -1 && excludeToday.compareTo(fifth) == -1) {
            BigDecimal cost = getCostBetween(userDayAmount, userMonthAmount, fifthEdge, sixthEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fourth != null && userMonthAmount.compareTo(fourth) != -1 && excludeToday.compareTo(fourth) != -1) {
            BigDecimal cost = userDayAmount.add(fifthEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fourth != null && userMonthAmount.compareTo(fourth) != -1 && excludeToday.compareTo(fourth) == -1) {
            BigDecimal cost = getCostBetween(userDayAmount, userMonthAmount, fourthEdge, fifthEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (third != null && userMonthAmount.compareTo(third) != -1 && excludeToday.compareTo(third) != -1) {
            BigDecimal cost = userDayAmount.multiply(fourthEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (third != null && userMonthAmount.compareTo(third) != -1 && excludeToday.compareTo(third) == -1) {
            BigDecimal cost = getCostBetween(userDayAmount, userMonthAmount, thirdEdge, fourthEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (second != null && userMonthAmount.compareTo(second) != -1 && excludeToday.compareTo(second) != -1) {
            BigDecimal cost = userDayAmount.multiply(thirdEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (second != null && userMonthAmount.compareTo(second) != -1 && excludeToday.compareTo(second) == -1) {
            BigDecimal cost = getCostBetween(userDayAmount, userMonthAmount, secondEdge, thirdEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if(first != null && userMonthAmount.compareTo(first) != -1 && excludeToday.compareTo(first) != -1){
            BigDecimal cost = userDayAmount.multiply(secondEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if(first != null && userMonthAmount.compareTo(first) != -1 && excludeToday.compareTo(first) == -1){
            BigDecimal cost = getCostBetween(userDayAmount, userMonthAmount, firstEdge, secondEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if(min != null && userMonthAmount.compareTo(min) != -1 && excludeToday.compareTo(min) != -1){
            BigDecimal cost = userDayAmount.multiply(firstEdge);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if(min != null && userMonthAmount.compareTo(min) != -1 && excludeToday.compareTo(min) == -1){
            BigDecimal beyond = userMonthAmount.subtract(userDayAmount);
            BigDecimal higherCost = beyond.multiply(firstEdge);
            BigDecimal cost = minCharge.add(higherCost);
            daycost.setCostMoney(cost.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if(min != null && userMonthAmount.compareTo(min) == -1) {
            //凌晨计算前一天水费，如果是一号代表上个月用水量未超过最低限额，扣费
            //其余日期(直到月底计算前)不扣费
            if (date == 1) {
                daycost.setCostMoney(minCharge);
            } else {
                daycost.setCostMoney(new BigDecimal("0"));
            }
        }
    }

    private BigDecimal getCostBetween(BigDecimal userDayAmount, BigDecimal userMonthAmount, BigDecimal preStage, BigDecimal posStage){
        BigDecimal beyond = userMonthAmount.subtract(userDayAmount);
        BigDecimal higherCost = beyond.multiply(posStage);
        BigDecimal lowerCost = userDayAmount.subtract(beyond).multiply(preStage);
        BigDecimal cost = higherCost.add(lowerCost);
        return cost;
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


