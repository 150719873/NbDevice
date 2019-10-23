package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 * 项目报修
 */
@Entity
@Table(name = "nt_repair", schema = "dbo", catalog = "mixAll")
public class RepairItem implements Serializable {

    /**
     * ID主键
     */
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    /**
     * 用户编号
     */
    @Basic
    @Column(name = "user_no")
    private String userNo;

    /**
     * 用户电话
     */
    @Basic
    @Column(name = "user_tel")
    private String userTel;

    /**
     * 操作员姓名
     */
    @Basic
    @Column(name = "repairman_name")
    private String repairmanName;

    /**
     * 操作员电话
     */
    @Basic
    @Column(name = "repairman_tel")
    private String repairmanTel;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 报修状态
     */
    @Basic
    @Column(name = "state")
    private Integer state;

    /**
     * 维修时间
     */
    @Basic
    @Column(name = "repair_time")
    private java.sql.Timestamp repairTime;

    /**
     * 终止时间
     */
    @Basic
    @Column(name = "end_time")
    private java.sql.Timestamp endTime ;

    /**
     * 反馈类型
     */
    @Basic
    @Column(name = "feedbackType")
    private Integer feedbackType ;

    /**
     * 反馈内容
     */
    @Basic
    @Column(name = "contents")
    private String contents ;

    /**
     * 小区Id
     */
    @Basic
    @Column(name = "community_id")
    private Integer communityId ;

    /**
     * 表地址
     */
    @Basic
    @Column(name = "device_no")
    private String deviceNo ;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getRepairmanName() {
        return repairmanName;
    }

    public void setRepairmanName(String repairmanName) {
        this.repairmanName = repairmanName;
    }

    public String getRepairmanTel() {
        return repairmanTel;
    }

    public void setRepairmanTel(String repairmanTel) {
        this.repairmanTel = repairmanTel;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Timestamp getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Timestamp repairTime) {
        this.repairTime = repairTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
}

