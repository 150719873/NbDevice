package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 * 水表表
 */
@Entity
@Table(name = "nb_device", schema = "dbo", catalog = "mixAll")
public class Device implements Serializable,Cloneable {

    //重写克隆方法，用于换表时进行克隆
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Object object = super.clone();
        return object;
    }

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 口径
     */
    @Basic
    @Column(name = "caliber")
    private Integer caliber;


    /**
     * 表编号
     */
    @Basic
    @Column(name = "device_no")
    private String deviceNo;

    /**
     * 表类型
     */
    @Basic
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 日用量
     */
    @Basic
    @Column(name = "day_amount")
    private java.math.BigDecimal dayAmount;

    /**
     * 生产厂家
     */
    @Basic
    @Column(name = "device_vender")
    private String deviceVender;

    /**
     * 抄表时间
     */
    @Basic
    @Column(name = "read_time")
    private java.sql.Timestamp readTime;

    /**
     * 用户编号
     */
    @Basic
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 月用量
     */
    @Basic
    @Column(name = "month_amount")
    private java.math.BigDecimal monthAmount;

    /**
     * 抄表读数
     */
    @Basic
    @Column(name = "read_value")
    private java.math.BigDecimal readValue;

    /**
     * 安装时间
     */
    @Basic
    @Column(name = "install_date")
    private java.sql.Date installDate;

    /**
     * 阀门状态（0：关 1：不定或没阀，2：开
     */
    @Basic
    @Column(name = "valve")
    private Integer valve;

    /**
     * 表状态
     */
    @Basic
    @Column(name = "state")
    private Integer state;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * imei号
     */
    @Basic
    @Column(name = "imei")
    private String imei;

    /**
     * 用水类型
     * 	1 居民生活用水 2 工业用水 3 行政事业单位用水 4 经营用水 5 特种行业用水 6 其他用水
     */
    @Basic
    @Column(name = "water_type")
    private Integer waterType;

    /**
     * 电池电量
     */
    @Basic
    @Column(name = "battery_voltage")
    private String batteryVoltage;


    /**
     * 信号值
     */
    @Basic
    @Column(name = "rssi")
    private String rssi;

    /**
     * nb设备地址
     */
    @Basic
    @Column(name = "mac_addr")
    private String macAddr;


    /**
     * nb表类型
     */
    @Basic
    @Column(name = "nb_device_type")
    private Integer nbDeviceType;

    /**
     * 干簧管状态（0-7
     */
    @Basic
    @Column(name = "pin_status")
    private Integer pinStatus;

    /**
     * 前一天读取时间
     */
    @Basic
    @Column(name = "pre_read_time")
    private Timestamp preReadTime;

    /**
     * 前一天读数
     */
    @Basic
    @Column(name = "pre_read_value")
    private BigDecimal preReadValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaliber() {
        return caliber;
    }

    public void setCaliber(Integer caliber) {
        this.caliber = caliber;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
    }

    public String getDeviceVender() {
        return deviceVender;
    }

    public void setDeviceVender(String deviceVender) {
        this.deviceVender = deviceVender;
    }

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(BigDecimal monthAmount) {
        this.monthAmount = monthAmount;
    }

    public BigDecimal getReadValue() {
        return readValue;
    }

    public void setReadValue(BigDecimal readValue) {
        this.readValue = readValue;
    }

    public Date getInstallDate() {
        return installDate;
    }

    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    public Integer getValve() {
        return valve;
    }

    public void setValve(Integer valve) {
        this.valve = valve;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Integer getWaterType() {
        return waterType;
    }

    public void setWaterType(Integer waterType) {
        this.waterType = waterType;
    }

    public String getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(String batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public Integer getNbDeviceType() {
        return nbDeviceType;
    }

    public void setNbDeviceType(Integer nbDeviceType) {
        this.nbDeviceType = nbDeviceType;
    }

    public Integer getPinStatus() {
        return pinStatus;
    }

    public void setPinStatus(Integer pinStatus) {
        this.pinStatus = pinStatus;
    }

    public Timestamp getPreReadTime() {
        return preReadTime;
    }

    public void setPreReadTime(Timestamp preReadTime) {
        this.preReadTime = preReadTime;
    }

    public BigDecimal getPreReadValue() {
        return preReadValue;
    }

    public void setPreReadValue(BigDecimal preReadValue) {
        this.preReadValue = preReadValue;
    }
}
