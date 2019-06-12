package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;

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
    @Column(name = "collectionType")
    private Integer collectionType;

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
}
