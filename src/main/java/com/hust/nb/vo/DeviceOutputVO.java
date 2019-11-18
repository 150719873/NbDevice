package com.hust.nb.vo;

import java.math.BigDecimal;
import java.util.Date;

public class DeviceOutputVO {
    private Integer id;
    private String deviceNo;
    private String userName;
    private String imei;
    private BigDecimal dayAmount;
    private Integer deviceType;
    private BigDecimal monthAmount;
    private Date readTime;
    private BigDecimal readValue;
    private Integer state;
    private Integer userId;
    private Integer waterType;
    private Integer valve;
    private String userAddr;
    private Integer blockId;
    private String userNo;
    private String batteryVoltage;
    private String rssi;
    private Integer pinStatus;
    private String macAddr;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public BigDecimal getDayAmount() {
        return dayAmount;
    }

    public void setDayAmount(BigDecimal dayAmount) {
        this.dayAmount = dayAmount;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public void setMonthAmount(BigDecimal monthAmount) {
        this.monthAmount = monthAmount;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public BigDecimal getReadValue() {
        return readValue;
    }

    public void setReadValue(BigDecimal readValue) {
        this.readValue = readValue;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWaterType() {
        return waterType;
    }

    public void setWaterType(Integer waterType) {
        this.waterType = waterType;
    }

    public Integer getValve() {
        return valve;
    }

    public void setValve(Integer valve) {
        this.valve = valve;
    }

    public String getUserAddr() {
        return userAddr;
    }

    public void setUserAddr(String userAddr) {
        this.userAddr = userAddr;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
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

    public Integer getPinStatus() {
        return pinStatus;
    }

    public void setPinStatus(Integer pinStatus) {
        this.pinStatus = pinStatus;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public DeviceOutputVO(Integer id, String deviceNo, String userName, String imei, BigDecimal dayAmount, Integer deviceType, BigDecimal monthAmount, Date readTime, BigDecimal readValue, Integer state, Integer userId, Integer waterType, Integer valve, String batteryVoltage, String rssi, Integer pinStatus, String macAddr , String userAddr, Integer blockId, String userNo) {
        this.id = id;
        this.deviceNo = deviceNo;
        this.userName = userName;
        this.imei = imei;
        this.dayAmount = dayAmount;
        this.deviceType = deviceType;
        this.monthAmount = monthAmount;
        this.readTime = readTime;
        this.readValue = readValue;
        this.state = state;
        this.userId = userId;
        this.waterType = waterType;
        this.valve = valve;
        this.userAddr = userAddr;
        this.blockId = blockId;
        this.userNo = userNo;
        this.batteryVoltage = batteryVoltage;
        this.rssi = rssi;
        this.pinStatus = pinStatus;
        this.macAddr = macAddr;
    }
}
