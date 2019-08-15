package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "nb_bigDevice", schema = "dbo", catalog = "mixAll")
public class BigDevice implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     *
     */
    @Basic
    @Column(name = "M_PipeDn")
    private String MPipeDn;


    /**
     *
     */
    @Basic
    @Column(name = "RealValue")
    private BigDecimal RealValue;

    /**
     *
     */
    @Basic
    @Column(name = "M_SortCode")
    private Integer MSortCode;

    /**
     *
     */
    @Basic
    @Column(name = "CelVal")
    private java.math.BigDecimal CelVal;

    /**
     *
     */
    @Basic
    @Column(name = "M_Tag")
    private String MTag;

    /**
     *
     */
    @Basic
    @Column(name = "ParentListId")
    private String ParentListId;

    /**
     *
     */
    @Basic
    @Column(name = "M_UserType")
    private String MUserType;

    /**
     *
     */
    @Basic
    @Column(name = "M_DoorNo")
    private String MDoorNo;

    /**
     *
     */
    @Basic
    @Column(name = "ToValue")
    private java.math.BigDecimal ToValue;

    /**
     *
     */
    @Basic
    @Column(name = "CreateTime")
        private Timestamp CreateTime;

    /**
     *
     */
    @Basic
    @Column(name = "OrgName")
    private String OrgName;

    /**
     *
     */
    @Basic
    @Column(name = "AddressCode")
    private String AddressCode;

    /**
     *
     */
    @Basic
    @Column(name = "M_Type")
    private String MType;

    /**
     *
     */
    @Basic
    @Column(name = "M_InstallAddress")
    private String MInstallAddress;

    /**
     *
     */
    @Basic
    @Column(name = "MeterId")
    private String MeterId;

    /**
     *
     */
    @Basic
    @Column(name = "OrganizeId")
    private String OrganizeId;

    /**
     *
     */
    @Basic
    @Column(name = "ForValue")
    private BigDecimal ForValue;

    /**
     *
     */
    @Basic
    @Column(name = "WarnStatus")
    private String WarnStatus;

    /**
     *
     */
    @Basic
    @Column(name = "M_Material")
    private String MMaterial;

    /**
     *
     */
    @Basic
    @Column(name = "PhoneNo")
    private String PhoneNo;

    /**
     *
     */
    @Basic
    @Column(name = "RevValue")
    private BigDecimal RevValue;

    /**
     *
     */
    @Basic
    @Column(name = "MeterName")
    private String MeterName;

    /**
     *
     */
    @Basic
    @Column(name = "CurStatus")
    private String CurStatus;

    /**
     *
     */
    @Basic
    @Column(name = "PressValue")
    private BigDecimal PressValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public BigDecimal getRealValue() {
        return RealValue;
    }

    public void setRealValue(BigDecimal realValue) {
        RealValue = realValue;
    }


    public BigDecimal getCelVal() {
        return CelVal;
    }

    public void setCelVal(BigDecimal celVal) {
        CelVal = celVal;
    }


    public String getParentListId() {
        return ParentListId;
    }

    public void setParentListId(String parentListId) {
        ParentListId = parentListId;
    }




    public BigDecimal getToValue() {
        return ToValue;
    }

    public void setToValue(BigDecimal toValue) {
        ToValue = toValue;
    }

    public Timestamp getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(Timestamp createTime) {
        CreateTime = createTime;
    }

    public String getOrgName() {
        return OrgName;
    }

    public void setOrgName(String orgName) {
        OrgName = orgName;
    }

    public String getAddressCode() {
        return AddressCode;
    }

    public void setAddressCode(String addressCode) {
        AddressCode = addressCode;
    }

    public String getMeterId() {
        return MeterId;
    }

    public void setMeterId(String meterId) {
        MeterId = meterId;
    }

    public String getOrganizeId() {
        return OrganizeId;
    }

    public void setOrganizeId(String organizeId) {
        OrganizeId = organizeId;
    }

    public BigDecimal getForValue() {
        return ForValue;
    }

    public void setForValue(BigDecimal forValue) {
        ForValue = forValue;
    }

    public String getWarnStatus() {
        return WarnStatus;
    }

    public void setWarnStatus(String warnStatus) {
        WarnStatus = warnStatus;
    }


    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public BigDecimal getRevValue() {
        return RevValue;
    }

    public void setRevValue(BigDecimal revValue) {
        RevValue = revValue;
    }

    public String getMeterName() {
        return MeterName;
    }

    public void setMeterName(String meterName) {
        MeterName = meterName;
    }

    public String getCurStatus() {
        return CurStatus;
    }

    public void setCurStatus(String curStatus) {
        CurStatus = curStatus;
    }

    public BigDecimal getPressValue() {
        return PressValue;
    }

    public void setPressValue(BigDecimal pressValue) {
        PressValue = pressValue;
    }

    public String getMPipeDn() {
        return MPipeDn;
    }

    public void setMPipeDn(String MPipeDn) {
        this.MPipeDn = MPipeDn;
    }

    public Integer getMSortCode() {
        return MSortCode;
    }

    public void setMSortCode(Integer MSortCode) {
        this.MSortCode = MSortCode;
    }

    public String getMTag() {
        return MTag;
    }

    public void setMTag(String MTag) {
        this.MTag = MTag;
    }

    public String getMUserType() {
        return MUserType;
    }

    public void setMUserType(String MUserType) {
        this.MUserType = MUserType;
    }

    public String getMDoorNo() {
        return MDoorNo;
    }

    public void setMDoorNo(String MDoorNo) {
        this.MDoorNo = MDoorNo;
    }

    public String getMType() {
        return MType;
    }

    public void setMType(String MType) {
        this.MType = MType;
    }

    public String getMInstallAddress() {
        return MInstallAddress;
    }

    public void setMInstallAddress(String MInstallAddress) {
        this.MInstallAddress = MInstallAddress;
    }

    public String getMMaterial() {
        return MMaterial;
    }

    public void setMMaterial(String MMaterial) {
        this.MMaterial = MMaterial;
    }
}
