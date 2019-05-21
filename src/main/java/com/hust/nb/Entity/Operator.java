package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:nb
 * Created by Administrator on 2019/5/21
 */
@Entity
@Table(name = "nt_operator", schema = "dbo", catalog = "mixAll")
public class Operator implements Serializable {

    /**
     * 管理员ID主键
     */
    @Id
    @Column(name = "operator_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer operatorId;

    /**
     * 账号
     */
    @Basic
    @Column(name = "user_name")
    private String userName;

    /**
     * 密码
     */
    @Basic
    @Column(name = "password")
    private String password;

    /**
     * 联系方式
     */
    @Basic
    @Column(name = "phone")
    private String phone;

    /**
     * 管理员类型
     */
    @Basic
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 操作员真实姓名
     */
    @Basic
    @Column(name = "operator_name")
    private String operatorName;

    /**
     * 操作员管理小区
     */
    @Basic
    @Column(name = "manage_community")
    private String manageCommunity;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getManageCommunity() {
        return manageCommunity;
    }

    public void setManageCommunity(String manageCommunity) {
        this.manageCommunity = manageCommunity;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}

