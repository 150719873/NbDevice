package com.hust.nb.Entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/23
 */
@Entity
@Table(name = "nt_devicechange", schema = "dbo", catalog = "mixAll")
public class DeviceChange {

    /**
     * 主键
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
     * 换表日期
     */
    @Basic
    @Column(name = "change_date")
    private java.sql.Timestamp changeDate;

    /**
     * 换表人
     */
    @Basic
    @Column(name = "operator_id")
    private Integer operatorId;

    /**
     * 旧表编号
     */
    @Basic
    @Column(name = "old_no")
    private String oldNo;

    /**
     * 旧表读数
     */
    @Basic
    @Column(name = "old_val")
    private java.math.BigDecimal oldVal;

    /**
     * 新表编号
     */
    @Basic
    @Column(name = "new_no")
    private String newNo;

    /**
     * 新表读数
     */
    @Basic
    @Column(name = "new_val")
    private java.math.BigDecimal newVal;

    /**
     * 重复判断
     * 0为未计算
     * 1为计算过
     */
    @Basic
    @Column(name = "flag")
    private Integer flag;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Timestamp getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Timestamp changeDate) {
        this.changeDate = changeDate;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOldNo() {
        return oldNo;
    }

    public void setOldNo(String oldNo) {
        this.oldNo = oldNo;
    }

    public BigDecimal getOldVal() {
        return oldVal;
    }

    public void setOldVal(BigDecimal oldVal) {
        this.oldVal = oldVal;
    }

    public String getNewNo() {
        return newNo;
    }

    public void setNewNo(String newNo) {
        this.newNo = newNo;
    }

    public BigDecimal getNewVal() {
        return newVal;
    }

    public void setNewVal(BigDecimal newVal) {
        this.newVal = newVal;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
