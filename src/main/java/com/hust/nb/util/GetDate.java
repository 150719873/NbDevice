package com.hust.nb.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
public class GetDate {
    public static String getCurrentMonth(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String curMonth = format.format(now);
        return curMonth;
    }

    public static Timestamp getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        Timestamp time = Timestamp.valueOf(format.format(now));
        return time;
    }
    public static String getDate(int d) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, d);
        String d1 = format1.format((cal.getTime()));
        return d1;
    }

    public static String getMonth(){
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

    public static String getBeforMonth(int month){
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMM");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return format1.format((cal.getTime()));
    }

    public static Timestamp getCurrentDay() {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date now = new Date();
        Timestamp day = Timestamp.valueOf(format1.format(now));
        return day;
    }

    public static Timestamp getTomorrowDay() {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        Date now = new Date();
        Timestamp day = Timestamp.valueOf(format1.format(now));
        return day;
    }
    public static Integer getToday(){
        Calendar cal=Calendar.getInstance();
        Integer d=cal.get(Calendar.DATE);
        return d;
    }
}
