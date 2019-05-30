package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 * 月水费表
 */
@Entity
@Table(name = "nt_monthcost", schema = "dbo", catalog = "mixAll")
public class Monthcost implements Serializable {

    /**
     * 自增主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户ID
     */
    @Basic
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 记录生成时间
     */
    @Basic
    @Column(name = "date")
    private Timestamp date;

    /**
     * 月用水量
     */
    @Basic
    @Column(name = "month_amount")
    private BigDecimal monthAmount;

    /**
     * 月水费
     */
    @Basic
    @Column(name = "money_change")
    private BigDecimal moneyChange;

    /**
     * 支付方式
     */
    @Basic
    @Column(name = "save_method")
    private Integer saveMethod;

    /**
     * 账户余额
     */
    @Basic
    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    /**
     * 管理员ID
     */
    @Basic
    @Column(name = "operator_id")
    private Integer operatorId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(BigDecimal monthAmount) {
        this.monthAmount = monthAmount;
    }

    public BigDecimal getMoneyChange() {
        return moneyChange;
    }

    public void setMoneyChange(BigDecimal moneyChange) {
        this.moneyChange = moneyChange;
    }

    public Integer getSaveMethod() {
        return saveMethod;
    }

    public void setSaveMethod(Integer saveMethod) {
        this.saveMethod = saveMethod;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }
}
