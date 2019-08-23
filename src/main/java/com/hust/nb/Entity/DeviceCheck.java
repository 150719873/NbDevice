package com.hust.nb.Entity;


import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "nb_deviceCheck", schema = "dbo", catalog = "mixAll")
public class DeviceCheck {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /**
     * 表编号
     */
    @Basic
    @Column(name = "device_no")
    private String deviceNo;


    /**
     * 抄表读数
     */
    @Basic
    @Column(name = "read_value")
    private java.math.BigDecimal readValue;


    /**
     * 阀门状态（0：关 1：不定或没阀，2：开
     */
    @Basic
    @Column(name = "valve")
    private Integer valve;



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
     * 抄表时间
     */
    @Basic
    @Column(name = "read_time")
    private java.sql.Timestamp readTime;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public BigDecimal getReadValue() {
        return readValue;
    }

    public void setReadValue(BigDecimal readValue) {
        this.readValue = readValue;
    }


    public Integer getValve() {
        return valve;
    }

    public void setValve(Integer valve) {
        this.valve = valve;
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

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }
}
