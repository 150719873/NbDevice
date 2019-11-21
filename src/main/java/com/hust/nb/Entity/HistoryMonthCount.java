package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "nt_historyMonthCount",schema= "dbo",catalog = "mixAll")
public class HistoryMonthCount implements Serializable
{
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 小区名
     */
    @Basic
    @Column(name = "community_name")
    private String communityName;

    /**
     * 小区编号
     */
    @Basic
    @Column(name = "community_id")
    private Integer communityId;


    /**
     * 总月用量
     */
    @Basic
    @Column(name = "month_amounts")
    private java.math.BigDecimal monthAmounts;

    /**
     * 月份
     */
    @Basic
    @Column(name = "month")
    private String month;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public BigDecimal getMonthAmounts() {
        return monthAmounts;
    }

    public void setMonthAmounts(BigDecimal monthAmounts) {
        this.monthAmounts = monthAmounts;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }
}
