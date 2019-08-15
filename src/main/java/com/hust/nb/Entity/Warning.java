package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "nt_warning", schema = "dbo", catalog = "mixAll")
public class Warning implements Serializable {

    /**
     * 用户ID主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 区域名称
     */
    @Basic
    @Column(name = "region_name")
    private String regionName;

    /**
     * 小区名称
     */
    @Basic
    @Column(name = "community_name")
    private String communityName;

    /**
     * 楼栋
     */
    @Basic
    @Column(name = "block_name")
    private String blockName;

    /**
     * 备注
     */
    @Basic
    @Column(name = "remark")
    private String remark;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 表编号
     */
    @Basic
    @Column(name = "device_no")
    private String deviceNo;

    /**
     * imei
     */
    @Basic
    @Column(name = "imei")
    private String imei;

    /**
     * 起始时间
     */
    @Basic
    @Column(name = "last_readtime")
    private java.sql.Timestamp lastReadtime;

    /**
     * 起始读数
     */
    @Basic
    @Column(name = "pr_show_value")
    private java.math.BigDecimal prShowValue;

    /**
     * 终止时间
     */
    @Basic
    @Column(name = "read_time")
    private java.sql.Timestamp readTime;

    /**
     * 终止读数
     */
    @Basic
    @Column(name = "show_value")
    private java.math.BigDecimal showValue;

    /**
     * 告警类型
     * 0：正常  1：水量负值  2：无数据  3：水量超大
     */
    @Basic
    @Column(name = "warning_type")
    private Integer warningType;

    /**
     * 终止时间
     */
    @Basic
    @Column(name = "warning_date")
    private java.sql.Timestamp warningDate;

    /**
     * 终止读数
     * 0 未处理 1 处理
     */
    @Basic
    @Column(name = "completed")
    private Integer completed;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Timestamp getLastReadtime() {
        return lastReadtime;
    }

    public void setLastReadtime(Timestamp lastReadtime) {
        this.lastReadtime = lastReadtime;
    }

    public BigDecimal getPrShowValue() {
        return prShowValue;
    }

    public void setPrShowValue(BigDecimal prShowValue) {
        this.prShowValue = prShowValue;
    }

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }

    public BigDecimal getShowValue() {
        return showValue;
    }

    public void setShowValue(BigDecimal showValue) {
        this.showValue = showValue;
    }

    public Integer getWarningType() {
        return warningType;
    }

    public void setWarningType(Integer warningType) {
        this.warningType = warningType;
    }

    public Timestamp getWarningDate() {
        return warningDate;
    }

    public void setWarningDate(Timestamp warningDate) {
        this.warningDate = warningDate;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
