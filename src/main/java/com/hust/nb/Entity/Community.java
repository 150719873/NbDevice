package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 * 小区表
 */
@Entity
@Table(name = "nt_community", schema = "dbo", catalog = "mixAll")
public class Community implements Serializable {

    /**
     * 小区ID主键
     */
    @Id
    @Column(name = "community_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer communityId;

    /**
     * 小区名称
     */
    @Basic
    @Column(name = "community")
    private String communityName;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 区域ID
     */
    @Basic
    @Column(name = "region_id")
    private int regionId;

    /**
     *所属抄表类型
     */
    @Basic
    @Column(name = "collection_type")
    private Integer collectionType;

    /**
     * 总日用量
     */
    @Basic
    @Column(name = "total_dayamount")
    private java.math.BigDecimal dayAmounts;

    /**
     * 总月用量
     */
    @Basic
    @Column(name = "total_monthamount")
    private java.math.BigDecimal monthAmounts;

    @Transient
    private Integer deviceCount;

    @Transient
    private Integer sucessCount;

    @Transient
    private String regionName;

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public Integer getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(Integer collectionType) {
        this.collectionType = collectionType;
    }

    public Integer getSucessCount() {
        return sucessCount;
    }

    public void setSucessCount(Integer sucessCount) {
        this.sucessCount = sucessCount;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }


    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public BigDecimal getDayAmounts() {
        return dayAmounts;
    }

    public void setDayAmounts(BigDecimal dayAmounts) {
        this.dayAmounts = dayAmounts;
    }

    public BigDecimal getMonthAmounts() {
        return monthAmounts;
    }

    public void setMonthAmounts(BigDecimal monthAmounts) {
        this.monthAmounts = monthAmounts;
    }
}
