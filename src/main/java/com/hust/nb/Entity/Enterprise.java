package com.hust.nb.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 * 水司表
 */
@Entity
@Table(name = "nb_enterprise", schema = "dbo", catalog = "mixAll")
public class Enterprise {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 水司名称
     */
    @Basic
    @Column(name = "enpr_name")
    private String enprName;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 水司创建时间
     */
    @Basic
    @Column(name = "creat_time")
    private Timestamp createTime;

    /**
     * 扣费类型
     */
    @Basic
    @Column(name = "cost_type")
    private int costType;

    /**
     * 定时字段
     */
    @Basic
    @Column(name = "cron")
    private String cron;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnprName() {
        return enprName;
    }

    public void setEnprName(String enprName) {
        this.enprName = enprName;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getCostType() {
        return costType;
    }

    public void setCostType(int costType) {
        this.costType = costType;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
