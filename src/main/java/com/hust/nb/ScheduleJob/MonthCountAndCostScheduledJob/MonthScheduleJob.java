package com.hust.nb.ScheduleJob.MonthCountAndCostScheduledJob;

import com.hust.nb.Dao.*;
import com.hust.nb.Entity.*;
import com.hust.nb.Service.DeviceChangeService;
import com.hust.nb.Service.MonthcountService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Slf4j
public class MonthScheduleJob implements Job {

    @Autowired
    MonthcountService monthcountService;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DaycountDao daycountDao;

    @Autowired
    UserDao userDao;

    @Autowired
    MonthcostDao monthcostDao;

    @Autowired
    DeviceChangeService devicechangeService;

    @Autowired
    DeviceChangeDao devicechangeDao;

    @Autowired
    ChargeLevelDao chargeLevelDao;

    @Autowired
    EnterpriseDao enterpriseDao;

    private final Logger log = LoggerFactory.getLogger(MonthScheduleJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //任务具体操作
        try {
            Integer id = jobExecutionContext.getJobDetail().getJobDataMap().getInt("水司编号");
            log.debug("RemindTask 1.1执行定期提醒任务信息Id:{}", id);
            System.out.println("hello，水司" + id);

            Enterprise enterprise = enterpriseDao.findEnprNoById(id);
            String enprNo = enterprise.getEnprNo();
            //未修改
            List<Device> deviceList = deviceDao.findAllByEnprNo(enprNo);
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            /**
             * 遍历水司下的所有水表，更新这些水表的月用水量以及向月用水量表插入数据
             */
            for (Device device : deviceList) {
                String deviceNo = device.getDeviceNo();
                DeviceChange devicechange = devicechangeDao.findByEnprNoAndNewNo(enprNo, deviceNo);
                String imei = device.getImei();
                if(imei == null){
                    //集中器水表
                    //TODO: 2019/5/29 集中器水表的表读数并没有写触发器，需要解决实时更新问题

                } else {
                    //nb水表
                    /**
                     * 经历过换表
                     * while循环解决一月换多次表
                     */
                    if(devicechange != null){
                        BigDecimal total = new BigDecimal("0");
                        Monthcount newMonCnt = new Monthcount();
                        newMonCnt.setDeviceNo(deviceNo);
                        while (devicechange != null && devicechange.getFlag() == 0) {
                            // 计算旧表部分,起始读数为上个月旧表的monthCount，结束读数为旧表的最新daycount
                            // (因为换表即插入最新数据，同时删除旧表，那么下一次每日定时任务插入daycount就不会插入旧表，也就不会更新)
                            String oldNo = devicechange.getOldNo();
                            String newNo = devicechange.getNewNo();
                            Daycount daycount = daycountDao.findLatestDaycountRecord(oldNo, enprNo);
                            BigDecimal oldEndValue = daycount.getEndValue();
                            Monthcount monthcount = monthcountService.findLatestRecordByDeviceNoAndEnprNo(oldNo, enprNo);
                            BigDecimal oldStartValue = monthcount == null ? new BigDecimal("0") : monthcount.getEndValue();
                            newMonCnt.setStartValue(oldStartValue);
                            newMonCnt.setStartTime(monthcount == null ? null : monthcount.getEndTime());
                            BigDecimal oldClear = oldEndValue.subtract(oldStartValue);
                            //计算新表部分，起始读数为换表时插入的新表的daycount，结束读数为device表的最新读数
                            BigDecimal newEndValue = daycountDao.findLatestDaycountRecord(newNo, enprNo).getEndValue();
                            Daycount daycount2 = daycountDao.findOldestDaycountRecord(newNo, enprNo);
                            BigDecimal newStartValue = daycount2.getStartValue();
                            //可以在这里做检测，如果新读数减去上次读数为负，那么有问题
                            BigDecimal newClear = newEndValue.subtract(newStartValue);
                            //合并插入
                            total = total.add(newClear.add(oldClear));
                            //改变状态
                            devicechange.setFlag(1);
                            try {
                                devicechangeDao.save(devicechange);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            devicechange = devicechangeDao.findByEnprNoAndNewNo(enprNo, oldNo);
                        }
                        newMonCnt.setEndTime(timestamp);
                        newMonCnt.setEndValue(daycountDao.findLatestDaycountRecord(deviceNo, enprNo).getEndValue());
                        newMonCnt.setMonthAmount(total);
                        Device device1 = deviceDao.findByDeviceNoAndEnprNo(deviceNo, enprNo);
                        device1.setMonthAmount(total);
                        try {
                            monthcountService.save(newMonCnt);
                            deviceDao.save(device1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        /**
                         * 未经历换表
                         */
                        Monthcount monthcount = monthcountService.findLatestRecordByDeviceNoAndEnprNo(deviceNo, enprNo);
                        Monthcount newMonCnt = new Monthcount();
                        BigDecimal newest = deviceDao.findByDeviceNoAndEnprNo(deviceNo, enprNo).getReadValue();
                        newMonCnt.setDeviceNo(deviceNo);
                        newMonCnt.setEndTime(timestamp);
                        newMonCnt.setEndValue(newest);
                        if (monthcount != null) {
                            newMonCnt.setStartTime(monthcount.getEndTime());
                            newMonCnt.setStartValue(monthcount.getEndValue());
                            newMonCnt.setMonthAmount(newMonCnt.getEndValue().subtract(newMonCnt.getStartValue()));
                        } else {
                            newMonCnt.setMonthAmount(newMonCnt.getEndValue());
                        }
                        monthcountService.save(newMonCnt);
                        device.setMonthAmount(newMonCnt.getMonthAmount());
                        deviceDao.save(device);
                    }
                }

            }
            /**
             * 计算用户月水费插入到Monthcost当中
             */
            List<User> userList = userDao.findAllByEnprNo(enprNo);
            for (User user : userList) {
                List<Device> deviceList1 = deviceDao.findAllByUserId(user.getUserId());
                BigDecimal userAmount = new BigDecimal("0");
                for (Device device : deviceList1) {
                    userAmount = userAmount.add(device.getMonthAmount());
                }
                Monthcost monthcost = new Monthcost();
                monthcost.setUserId(user.getUserId());
                monthcost.setDate(timestamp);
                monthcost.setMonthAmount(userAmount);
                int userType = user.getUserType();
                if(userType == 1){//居民用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 1);
                    monthcost = calculatePrice(userAmount,chargelevel,monthcost);
                }else if(userType == 2){//商业用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 4);
                    monthcost = calculatePrice(userAmount,chargelevel,monthcost);
                }else if(userType == 3){//工业用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 2);
                    monthcost = calculatePrice(userAmount,chargelevel,monthcost);
                }else{//特种行业用水
                    ChargeLevel chargelevel = chargeLevelDao.findChargeLevelByEnprNoAndType(enprNo, 5);
                    monthcost = calculatePrice(userAmount,chargelevel,monthcost);
                }
                monthcost.setAccountBalance(user.getAccountBalance().add(monthcost.getMoneyChange()));
                monthcostDao.save(monthcost);
                user.setMonthExpense(monthcost.getMoneyChange());
                user.setAccountBalance(monthcost.getAccountBalance());
                userDao.save(user);
            }
        }catch (Exception e) {
            log.error("RemindTask 1.9执行任务,获取任务信息Id出错,任务信息:{}", jobExecutionContext);
        }
    }


    /**
     * 根据用水性质，用水总量计算水费并加到表里面
     */
    private static Monthcost calculatePrice(BigDecimal userAmount, ChargeLevel chargelevel, Monthcost monthcost) {
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
            monthcost.setMoneyChange(fiveRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fifthEdge != null && userAmount.compareTo(fourth) != -1 && userAmount.compareTo(fifth) == -1) {
            //用户水量大于第四阶梯并且小于第五阶梯
            BigDecimal fourRest = userAmount.subtract(fourth).multiply(fifthEdge);
            BigDecimal three = fourth.subtract(third).multiply(fourthEdge);
            BigDecimal two = third.subtract(second).multiply(thirdEdge);
            BigDecimal one = second.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            fourRest = fourRest.add(three).add(two).add(one).add(zero);
            monthcost.setMoneyChange(fourRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (fourthEdge != null && userAmount.compareTo(third) != -1 && userAmount.compareTo(fourth) == -1) {
            //用户水量大于第三阶梯并且小于第四阶梯
            BigDecimal threeRest = userAmount.subtract(third).multiply(fourthEdge);
            BigDecimal two = third.subtract(second).multiply(thirdEdge);
            BigDecimal one = second.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            threeRest = threeRest.add(two).add(one).add(zero);
            monthcost.setMoneyChange(threeRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (thirdEdge != null && userAmount.compareTo(second) != -1 && userAmount.compareTo(third) == -1) {
            //用户水量大于第二阶梯并且小于第三阶梯
            BigDecimal twoRest = userAmount.subtract(second).multiply(thirdEdge);
            BigDecimal one = second.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            twoRest = twoRest.add(one).add(zero);
            monthcost.setMoneyChange(twoRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (secondEdge != null && userAmount.compareTo(first) != -1 && userAmount.compareTo(second) == -1) {
            //用户水量大于第一阶梯并且小于第二阶梯
            BigDecimal oneRest = userAmount.subtract(first).multiply(secondEdge);
            BigDecimal zero = first.multiply(firstEdge);
            oneRest = oneRest.add(zero);
            monthcost.setMoneyChange(oneRest.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else if (firstEdge != null && userAmount.compareTo(first) == -1 && userAmount.compareTo(min) == 1) {
            //用户水量小于第一阶梯并且大于门槛水量
            BigDecimal one = userAmount.multiply(firstEdge);
            monthcost.setMoneyChange(one.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        } else {
            //用户水量小于门槛水量
            monthcost.setMoneyChange(minCharge.setScale(2, BigDecimal.ROUND_HALF_UP).negate());
        }
        return monthcost;
    }
}
