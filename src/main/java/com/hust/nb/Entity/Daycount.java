package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
@Entity
@Table(name = "t_daycount")
public class Daycount implements Serializable {

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * device表对应的ID
     */
    @Basic
    @Column(name = "device_no")
    private String deviceNo;

    /**
     * 起始时间
     */
    @Basic
    @Column(name = "start_time")
    private java.sql.Timestamp startTime;

    /**
     * 起始读数
     */
    @Basic
    @Column(name = "start_value")
    private java.math.BigDecimal startValue;

    /**
     * 终止时间
     */
    @Basic
    @Column(name = "end_time")
    private java.sql.Timestamp endTime;

    /**
     * 终止读数
     */
    @Basic
    @Column(name = "end_value")
    private java.math.BigDecimal endValue;

    /**
     * 日用量
     */
    @Basic
    @Column(name = "day_amount")
    private java.math.BigDecimal dayAmount;

    /**
     * 日期
     */
    @Basic
    @Column(name = "date")
    private Integer date;

    /**
     * 状态
     */
    @Basic
    @Column(name = "state")
    private Integer state;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getStartValue() {
        return startValue;
    }

    public void setStartValue(BigDecimal startValue) {
        this.startValue = startValue;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getEndValue() {
        return endValue;
    }

    public void setEndValue(BigDecimal endValue) {
        this.endValue = endValue;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
