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
@Table(name = "nb_daycost", schema = "dbo", catalog = "mixAll")
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
    @Column(name = "user_id")
    private Integer userId;

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
    private java.sql.Timestamp costTime;

    /**
     * 前一天用水量
     */
    @Basic
    @Column(name = "day_amount")
    private java.math.BigDecimal dayAmount;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    public Timestamp getCostTime() {
        return costTime;
    }

    public void setCostTime(Timestamp costTime) {
        this.costTime = costTime;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
