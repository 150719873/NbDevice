package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 * 区域表
 */
@Entity
@Table(name = "nt_region", schema = "dbo", catalog = "mixAll")
public class Region implements Serializable {
    /**
     * 区域ID主键
     */
    @Id
    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer regionId;

    /**
     * 区域名称
     */
    @Basic
    @Column(name = "region")
    private String regionName;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    public Integer getRegionId() {
        return regionId;
    }

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
