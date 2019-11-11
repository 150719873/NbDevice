package com.hust.nb.Entity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author: suxinyu
 * @DateTme: 2019/11/4 11:01
 */
@Entity
@Table(name = "nb_waterquality", schema = "dbo", catalog = "mixAll")
public class Dimness {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    /**
     * 浊度仪地址
     */
    @Basic
    @Column(name = "address")
    private String address;

    /**
     * 浊度
     */
    @Basic
    @Column(name = "turbidity")
    private Double turbidity;

    /**
     * 时间
     */
    @Basic
    @Column(name = "dateline")
    private String dateline;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTurbidity() {
        return turbidity;
    }

    public void setTurbidity(Double turbidity) {
        this.turbidity = turbidity;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
