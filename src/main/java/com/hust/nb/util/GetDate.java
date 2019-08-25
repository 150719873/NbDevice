package com.hust.nb.util;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
public class GetDate {

    public static void main(String[] args) {
        System.out.println(getYesterday());
    }

    public static String getCurrentMonth() {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");//201908
        String curMonth = format.format(now);
        return curMonth;
    }

    public static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyyMMdd ").format(cal.getTime());
        return yesterday;
    }

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String d1 = format.format(now);
        return d1;
    }

    public static String getDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String d1 = format1.format(now);
        return d1;
    }

    public static String getMonth() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
        Calendar cal = Calendar.getInstance();
        String d = format1.format((cal.getTime()));
        return d;
    }

    public static String getLastMonth() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return format1.format((cal.getTime()));
    }

    public static String getBeforMonth(int month) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMM");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return format1.format((cal.getTime()));
    }

    public static Timestamp getCurrentDay() {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//2019-08-25 00:00:00.0
        Date now = new Date();
        Timestamp day = Timestamp.valueOf(format1.format(now));
        return day;
    }

    public static Timestamp getTomorrowDay() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");//2019-08-25 23:59:59.0
        Date now = new Date();
        Timestamp day = Timestamp.valueOf(format1.format(now));
        return day;
    }

    public static Integer getToday() {
        Calendar cal = Calendar.getInstance();//25
        Integer d = cal.get(Calendar.DATE);
        return d;
    }
}
