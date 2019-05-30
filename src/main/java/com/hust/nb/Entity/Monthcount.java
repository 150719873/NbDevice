package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * 月水量表
 * Created by Administrator on 2019/5/24
 */
@Entity
@Table(name = "nt_monthcount", schema = "dbo", catalog = "mixAll")
public class Monthcount implements Serializable {

    /**
     * 自增主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 水表编号
     */
    @Basic
    @Column(name = "device_no")
    private String deviceNo;

    /**
     * 开始时间
     */
    @Basic
    @Column(name = "start_time")
    private Timestamp startTime;

    /**
     * 开始读数
     */
    @Basic
    @Column(name = "start_value")
    private BigDecimal startValue;

    /**
     * 结束时间
     */
    @Basic
    @Column(name = "end_time")
    private Timestamp endTime;

    /**
     * 结束读数
     */
    @Basic
    @Column(name = "end_value")
    private BigDecimal endValue;

    /**
     * 月水量
     */
    @Basic
    @Column(name = "month_amount")
    private BigDecimal monthAmount;

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

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
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

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(BigDecimal monthAmount) {
        this.monthAmount = monthAmount;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
