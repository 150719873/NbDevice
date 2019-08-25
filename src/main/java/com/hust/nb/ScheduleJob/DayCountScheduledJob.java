package com.hust.nb.ScheduleJob;

import com.hust.nb.Dao.*;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.RegionService;
import com.hust.nb.util.GetDate;
import com.hust.nb.util.QBTDeviceTmpGetter;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//    @Value("${com.QBTT.dbPrefix}")
//    private String dbPrefix;

    /**
     * 每天零点十分更新所有水司  dayCount dayCost
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void update() {
        logger.info("====================日水量统计====================");
        List<Device> deviceList = deviceDao.findAll();
        BigDecimal limit = new BigDecimal(200);
        BigDecimal zero = new BigDecimal(0);
        Calendar calendar = Calendar.getInstance();
        for (Device device : deviceList) {
            String deviceNo = device.getDeviceNo();
            String enprNo = device.getEnprNo();
            String imei = device.getImei();
            if (imei != null) {
                Daycount daycount = new Daycount();
                daycount.setDeviceNo(device.getDeviceNo());
                daycount.setEnprNo(device.getEnprNo());
                daycount.setEndTime(device.getReadTime());
                daycount.setEndValue(device.getReadValue());
                daycount.setDate(calendar.get(Calendar.DATE) - 1);
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
                int today = calendar.get(Calendar.DATE);
                //当月最后一天数据不加入计算
                if (today == 1) {
                    device.setMonthAmount(new BigDecimal("0"));
                } else {
                    device.setMonthAmount(device.getDayAmount().add(daycount.getDayAmount()));
                }
                deviceDao.save(device);
                daycountDao.save(daycount);
            }
//            else {
//                //说明此水表是集中器水表，需要调接口
//                //查询出此水表的最新读数
////                String tableName = dbPrefix + GetDate.getCurrentMonth();
//                String tableName = "mixAll.dbo.t_deviceTmp" + GetDate.getCurrentMonth();
//                List queryRes = deviceTmpGetter.getLatestRecord(deviceNo, enprNo, tableName);
//                Map map = (Map) queryRes.get(0);
//                Timestamp endTime = (Timestamp)map.get("readTime");
//                BigDecimal showValue = (BigDecimal) map.get("showValue");
//                daycount.setDeviceNo(device.getDeviceNo());
//                daycount.setEnprNo(device.getEnprNo());
//                daycount.setEndTime(endTime);
//                daycount.setEndValue(showValue);
//                daycount.setDate(calendar.get(Calendar.DATE));
//                if (preDaycount != null) {
//                    daycount.setStartTime(preDaycount.getEndTime());
//                    daycount.setStartValue(preDaycount.getEndValue());
//                    daycount.setDayAmount(showValue.subtract(preDaycount.getEndValue()));
//                    if(daycount.getStartTime()==daycount.getEndTime()){
//                        daycount.setState(2);
//                    }else if(daycount.getDayAmount().compareTo(limit)==1){
//                        daycount.setState(3);
//                    }else if(daycount.getDayAmount().compareTo(zero)==-1){
//                        daycount.setState(1);
//                    }else {
//                        daycount.setState(0);
//                    }
//                } else {
//                    daycount.setDayAmount(showValue);
//                    daycount.setState(0);
//                }
//                /**
//                 * 需要采集在插入tmp表的同时更新t_device表的数据
//                 * 以保持t_device表的数据也是实时更新的
//                 * 如果没有此功能，那么只能在页面点击查看所有表的时候才会更新数据
//                 */
//                daycountDao.save(daycount);
//                device.setReadTime(endTime);
//                device.setReadValue(showValue);
//                device.setDayAmount(daycount.getDayAmount());
//                device.setState(daycount.getState());
//                deviceDao.save(device);
//            }
        }
    }

    /**
     * 每天两点计算前一天用户水费
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void calculate() {
        logger.info("====================日水费统计====================");
        Calendar calendar = Calendar.getInstance();
        List<Enterprise> enterprises = enterpriseDao.findAll();
        for (Enterprise enterprise : enterprises) {
            String enprNo = enterprise.getEnprNo();
            List<User> userList = userDao.findAllByEnprNo(enprNo);
            for (User user : userList) {
                Daycost daycost = new Daycost();
                List<Device> deviceList = deviceDao.findAllByUserId(user.getUserId());
                BigDecimal userAmount = new BigDecimal("0");
                for (Device device : deviceList) {
                    Daycount daycount = daycountDao.findLatestRecord(
                            device.getDeviceNo(), device.getEnprNo(), calendar.get(Calendar.DATE) - 1);
                    BigDecimal dayAmount = daycount == null ? new BigDecimal("0") : daycount.getDayAmount();
                    userAmount = userAmount.add(dayAmount);
                }
                int userType = user.getUserType();
                if (userType == 1) {//居民用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 1);
                    calculatePrice(userAmount, chargelevel, daycost);
                } else if (userType == 2) {//商业用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 4);
                    calculatePrice(userAmount, chargelevel, daycost);
                } else if (userType == 3) {//工业用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 2);
                    calculatePrice(userAmount, chargelevel, daycost);
                } else if (userType == 5) {//特种行业用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 5);
                    calculatePrice(userAmount, chargelevel, daycost);
                    //todo 新增其他用水
                } else {//其他用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 6);
                    calculatePrice(userAmount, chargelevel, daycost);
                }
            }

        }

    }

    private Daycost calculatePrice(BigDecimal userAmount, ChargeLevel chargelevel, Daycost daycost) {
        BigDecimal min = chargelevel.getMin();
        BigDecimal minCharge = chargelevel.getMinCharge();
        BigDecimal first = chargelevel.getFirst();//一级阶梯截止，二级阶梯起始
        BigDecimal second = chargelevel.getSecond();//二级阶梯截止，三级阶梯起始
        BigDecimal third = chargelevel.getThird();//三级阶梯截止，四级阶梯起始
        BigDecimal fourth = chargelevel.getFourth();//四级阶梯截止，五级阶梯起始
        BigDecimal fifth = chargelevel.getFifth();//五级阶梯截止，六级阶梯起始
        BigDecimal firstEdge = chargelevel.getFirstEdge();
        BigDecimal secondEdge = chargelevel.getSecondEdge();
        BigDecimal thirdEdge = chargelevel.getThirdEdge();
        BigDecimal fourthEdge = chargelevel.getFourthEdge();
        BigDecimal fifthEdge = chargelevel.getFifthEdge();
        BigDecimal sixthEdge = chargelevel.getSixthEdge();
        if (sixthEdge != null && userAmount.compareTo(fifth) != -1) {//用户水量大于等于第五阶梯
            BigDecimal fiveRest = userAmount.subtract(fifth).multiply(sixthEdge);
            BigDecimal four = fifth.subtract(fourth).multiply(fifthEdge);
            BigDecimal three = fourth.subtract(third).multiply(fourthEdge);
            BigDecimal two = third.subtract(second).multiply(thirdEdge);
            BigDecimal one = second.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            fiveRest = fiveRest.add(four).add(three).add(two).add(one).add(zero);
            daycost.setCostMoney(fiveRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fifthEdge != null && userAmount.compareTo(fourth) != -1 && userAmount.compareTo(fifth) == -1) {
            //用户水量大于第四阶梯并且小于第五阶梯
            BigDecimal fourRest = userAmount.subtract(fourth).multiply(fifthEdge);
            BigDecimal three = fourth.subtract(third).multiply(fourthEdge);
            BigDecimal two = third.subtract(second).multiply(thirdEdge);
            BigDecimal one = second.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            fourRest = fourRest.add(three).add(two).add(one).add(zero);
            daycost.setCostMoney(fourRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fourthEdge != null && userAmount.compareTo(third) != -1 && userAmount.compareTo(fourth) == -1) {
            //用户水量大于第三阶梯并且小于第四阶梯
            BigDecimal threeRest = userAmount.subtract(third).multiply(fourthEdge);
            BigDecimal two = third.subtract(second).multiply(thirdEdge);
            BigDecimal one = second.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            threeRest = threeRest.add(two).add(one).add(zero);
            daycost.setCostMoney(threeRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (thirdEdge != null && userAmount.compareTo(second) != -1 && userAmount.compareTo(third) == -1) {
            //用户水量大于第二阶梯并且小于第三阶梯
            BigDecimal twoRest = userAmount.subtract(second).multiply(thirdEdge);
            BigDecimal one = second.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            twoRest = twoRest.add(one).add(zero);
            daycost.setCostMoney(twoRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (secondEdge != null && userAmount.compareTo(first) != -1 && userAmount.compareTo(second) == -1) {
            //用户水量大于第一阶梯并且小于第二阶梯
            BigDecimal oneRest = userAmount.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            oneRest = oneRest.add(zero);
            daycost.setCostMoney(oneRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (firstEdge != null && userAmount.compareTo(first) == -1 && userAmount.compareTo(min) == 1) {
            //用户水量小于第一阶梯并且大于门槛水量
            BigDecimal one = userAmount.multiply(firstEdge);
            daycost.setCostMoney(one.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else {
            //用户水量小于门槛水量
            daycost.setCostMoney(minCharge.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        }
        return daycost;
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
                    e.printStackTrace();
                }
            }
        }
    }

}


