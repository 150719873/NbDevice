package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Entity
@Table(name = "nt_user", schema = "dbo", catalog = "mixAll")
public class User implements Serializable {
    /**
     * 用户ID主键
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    /**
     * 用户姓名
     */
    @Basic
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户类型
     */
    @Basic
    @Column(name = "user_type")
    private int userType;

    /**
     * 用户手机
     */
    @Basic
    @Column(name = "user_tel")
    private String userTel;

    /**
     * 用户备用手机号
     */
    @Basic
    @Column(name = "user_phone")
    private String userPhone;

    /**
     * 用户所在楼栋ID
     */
    @Basic
    @Column(name = "block_id")
    private int blockId;

    /**
     * 用户单元门牌号信息
     */
    @Basic
    @Column(name = "addr")
    private String userAddr;

    /**
     * 银行卡号码
     */
    @Basic
    @Column(name = "bank_account")
    private String bankAccount;

    /**
     * 持卡人
     */
    @Basic
    @Column(name = "bank_owner")
    private String bankOwner;

    /**
     * 开户行
     */
    @Basic
    @Column(name = "bank_addr")
    private String bankAddr;

    /**
     * 月水费
     */
    @Basic
    @Column(name = "month_expense")
    private BigDecimal monthExpense;

    /**
     * 用户账户余额
     */
    @Basic
    @Column(name = "account_balance")
    private BigDecimal accountBalance;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankOwner() {
        return bankOwner;
    }

    public void setBankOwner(String bankOwner) {
        this.bankOwner = bankOwner;
    }

    public String getBankAddr() {
        return bankAddr;
    }

    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }

    public BigDecimal getMonthExpense() {
        return monthExpense;
    }

    public void setMonthExpense(BigDecimal monthExpense) {
        this.monthExpense = monthExpense;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
