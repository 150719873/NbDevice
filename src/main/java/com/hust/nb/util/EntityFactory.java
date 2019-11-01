package com.hust.nb.util;

import com.hust.nb.Entity.Daycount;
import com.hust.nb.Entity.DeviceChange;
import com.hust.nb.Entity.Notice;
import com.hust.nb.Entity.Operator;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/21
 */
public class EntityFactory {

    public static Operator OperatorFactory(String userName, String password, String phone
    , int userType, String operatorName, String manageCommunity, String enprNo){
        Operator operator = new Operator();
        operator.setUserName(userName);
        operator.setPassword(password);
        operator.setPhone(phone);
        operator.setUserType(userType);
        operator.setOperatorName(operatorName);
        operator.setManageCommunity(manageCommunity);
        operator.setEnprNo(enprNo);
        return operator;
    }

    public static DeviceChange deviceChangeFactory(int userId, Timestamp changeDate, int operatorId, String oldNo
    , BigDecimal oldVal, String newNo, BigDecimal newVal, int flag, String enprNo)
    {
        DeviceChange deviceChange = new DeviceChange();
        deviceChange.setUserId(userId);
        deviceChange.setChangeDate(changeDate);
        deviceChange.setOperatorId(operatorId);
        deviceChange.setOldNo(oldNo);
        deviceChange.setOldVal(oldVal);
        deviceChange.setNewNo(newNo);
        deviceChange.setNewVal(newVal);
        deviceChange.setFlag(flag);
        deviceChange.setEnprNo(enprNo);
        return deviceChange;
    }

    public static Daycount daycountFactory(String deviceNo, Timestamp startTime, BigDecimal startValue, Timestamp endTime
    , BigDecimal endValue, BigDecimal dayAmount, String date, int state)
    {
        Daycount daycount = new Daycount();
        daycount.setDeviceNo(deviceNo);
        daycount.setStartTime(startTime);
        daycount.setStartValue(startValue);
        daycount.setEndTime(endTime);
        daycount.setEndValue(endValue);
        daycount.setDayAmount(dayAmount);
        daycount.setDate(date);
        daycount.setState(state);
        return daycount;
    }

    public static Notice inputNoticeFactory(String title, String content, Timestamp rTime, Integer type, String enprNo,String objects)
    {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContent(content);
        notice.setReleaseTime(rTime);
        notice.setType(type);
        notice.setEnprNo(enprNo);
        notice.setObjects(objects);
        return notice;
    }
}
