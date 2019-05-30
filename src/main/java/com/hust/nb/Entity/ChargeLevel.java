package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 * 阶梯水价表
 */
@Entity
@Table(name = "nt_chargelevel", schema = "dbo", catalog = "mixAll")
public class ChargeLevel implements Serializable {

    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用水类型
     */
    @Basic
    @Column(name = "type")
    private Integer type;

    /**
     * 最低水量
     */
    @Basic
    @Column(name = "min")
    private BigDecimal min;

    /**
     * 最低水费
     */
    @Basic
    @Column(name = "min_charge")
    private BigDecimal minCharge;

    /**
     * 一级阶梯
     */
    @Basic
    @Column(name = "first")
    private BigDecimal first;

    /**
     * 二级阶梯
     */
    @Basic
    @Column(name = "second")
    private BigDecimal second;

    /**
     * 三级阶梯
     */
    @Basic
    @Column(name = "third")
    private BigDecimal third;

    /**
     * 四级阶梯
     */
    @Basic
    @Column(name = "fourth")
    private BigDecimal fourth;

    /**
     * 五级阶梯
     */
    @Basic
    @Column(name = "fifth")
    private BigDecimal fifth;

    /**
     * 一级阶梯计算界限
     */
    @Basic
    @Column(name = "first_edge")
    private BigDecimal firstEdge;

    /**
     * 二级阶梯计算界限
     */
    @Basic
    @Column(name = "second_edge")
    private BigDecimal secondEdge;

    /**
     * 三级阶梯计算界限
     */
    @Basic
    @Column(name = "third_edge")
    private BigDecimal thirdEdge;

    /**
     * 四级阶梯计算界限
     */
    @Basic
    @Column(name = "fourth_edge")
    private BigDecimal fourthEdge;

    /**
     * 五级阶梯计算界限
     */
    @Basic
    @Column(name = "fifth_edge")
    private BigDecimal fifthEdge;

    /**
     * 六级阶梯计算界限
     */
    @Basic
    @Column(name = "sixth_edge")
    private BigDecimal sixthEdge;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMinCharge() {
        return minCharge;
    }

    public void setMinCharge(BigDecimal minCharge) {
        this.minCharge = minCharge;
    }

    public BigDecimal getFirst() {
        return first;
    }

    public void setFirst(BigDecimal first) {
        this.first = first;
    }

    public BigDecimal getSecond() {
        return second;
    }

    public void setSecond(BigDecimal second) {
        this.second = second;
    }

    public BigDecimal getThird() {
        return third;
    }

    public void setThird(BigDecimal third) {
        this.third = third;
    }

    public BigDecimal getFourth() {
        return fourth;
    }

    public void setFourth(BigDecimal fourth) {
        this.fourth = fourth;
    }

    public BigDecimal getFifth() {
        return fifth;
    }

    public void setFifth(BigDecimal fifth) {
        this.fifth = fifth;
    }

    public BigDecimal getFirstEdge() {
        return firstEdge;
    }

    public void setFirstEdge(BigDecimal firstEdge) {
        this.firstEdge = firstEdge;
    }

    public BigDecimal getSecondEdge() {
        return secondEdge;
    }

    public void setSecondEdge(BigDecimal secondEdge) {
        this.secondEdge = secondEdge;
    }

    public BigDecimal getThirdEdge() {
        return thirdEdge;
    }

    public void setThirdEdge(BigDecimal thirdEdge) {
        this.thirdEdge = thirdEdge;
    }

    public BigDecimal getFourthEdge() {
        return fourthEdge;
    }

    public void setFourthEdge(BigDecimal fourthEdge) {
        this.fourthEdge = fourthEdge;
    }

    public BigDecimal getFifthEdge() {
        return fifthEdge;
    }

    public void setFifthEdge(BigDecimal fifthEdge) {
        this.fifthEdge = fifthEdge;
    }

    public BigDecimal getSixthEdge() {
        return sixthEdge;
    }

    public void setSixthEdge(BigDecimal sixthEdge) {
        this.sixthEdge = sixthEdge;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
