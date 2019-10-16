package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: suxinyu
 * @DateTme: 2019/10/14 15:47
 */
@Entity
@Table(name = "nt_payhistory", schema = "dbo", catalog = "mixAll")
public class PayHistory implements Serializable {
    /**
     * 商户订单号
     */
    @Id
    @Column(name = "out_trade_no")
    public String tradeNo;

    /**
     * 用户编号
     */
    @Basic
    @Column(name = "user_no")
    private String userNo;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 付款时间
     */
    @Basic
    @Column(name = "pay_time")
    private String payTime;

    /**
     * 付款状态
     */
    @Basic
    @Column(name = "state")
    private Integer state;

    /**
     * 付款金额
     */
    @Basic
    @Column(name = "fee")
    private String fee;

    /**
     * 微信支付订单号
     */
    @Basic
    @Column(name = "transaction_id")
    private String transactionId;

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }
}
