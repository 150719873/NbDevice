package com.hust.nb.Entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/8/25
 */
@Entity
@Table(name = "nt_daycount", schema = "dbo", catalog = "mixAll")
public class Daycost {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * imei
     */
    @Basic
    @Column(name = "imei")
    private String imei;

    /**
     * 用户编号
     */
    @Basic
    @Column(name = "user_no")
    private String userNo;

    /**
     * 扣费
     */
    @Basic
    @Column(name = "cost_money")
    private java.math.BigDecimal costMoney;

    /**
     * 扣费时间
     */
    @Basic
    @Column(name = "cost_time")
    private java.sql.Timestamp costTIme;

    /**
     * 前一天用水量
     */
    @Basic
    @Column(name = "pre_day_amount")
    private java.math.BigDecimal endValue;

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    public Timestamp getCostTIme() {
        return costTIme;
    }

    public void setCostTIme(Timestamp costTIme) {
        this.costTIme = costTIme;
    }

    public BigDecimal getEndValue() {
        return endValue;
    }

    public void setEndValue(BigDecimal endValue) {
        this.endValue = endValue;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
