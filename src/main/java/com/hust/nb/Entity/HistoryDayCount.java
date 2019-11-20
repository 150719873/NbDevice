package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "nt_historyDayCount",schema= "dbo",catalog = "mixAll")
public class HistoryDayCount implements Serializable
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
     * 总日用量
     */
    @Basic
    @Column(name = "day_amounts")
    private java.math.BigDecimal dayAmounts;

    /**
     * 日期
     */
    @Basic
    @Column(name = "date")
    private String readDate;

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

    public BigDecimal getDayAmounts() {
        return dayAmounts;
    }

    public void setDayAmounts(BigDecimal dayAmounts) {
        this.dayAmounts = dayAmounts;
    }

    public String getReadDate() {
        return readDate;
    }

    public void setReadDate(String readDate) {
        this.readDate = readDate;
    }


}
