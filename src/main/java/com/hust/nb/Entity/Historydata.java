package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 * NB表数据推送表
 */
@Entity
@Table(name = "nt_historydata",schema= "dbo",catalog = "mixAll")
public class Historydata implements Serializable {



    /**
     * 抄表时间
     */
    @Basic
    @Column(name = "read_time")
    private java.sql.Timestamp readTime;

    /**
     * 表读数
     */
    @Basic
    @Column(name = "device_value")
    private java.math.BigDecimal deviceValue;

    /**
     * 信号强度
     */
    @Basic
    @Column(name = "signal_quality")
    private Integer signalQuality;

    /**
     * 水表IMEI号
     */
    @Basic
    @Column(name = "imei")
    private String imei;

    /**
     * 表编号
     */
    @Basic
    @Column(name = "device_no")
    private String deviceNo;

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }

    public BigDecimal getDeviceValue() {
        return deviceValue;
    }

    public void setDeviceValue(BigDecimal deviceValue) {
        this.deviceValue = deviceValue;
    }

    public Integer getSignalQuality() {
        return signalQuality;
    }

    public void setSignalQuality(Integer signalQuality) {
        this.signalQuality = signalQuality;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
