package com.hust.nb.Entity;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 产品订单信息
 * 创建者 科帮网
 * 创建时间	2017年7月27日
 */
@Entity
@Table(name = "nb_product", schema = "dbo", catalog = "mixAll")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 管理员ID主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Basic
    @Column(name = "enprNo")
	private String enprNo;// 水司编码

    @Basic
    @Column(name = "subject")
	private String subject;//订单名称

    @Basic
    @Column(name = "body")
	private String body;// 商品描述

    @Basic
    @Column(name = "total_fee")
	private String totalFee;// 总金额(单位是分)

    @Basic
    @Column(name = "trade_no")
	private String outTradeNo;// 订单号(唯一)

    @Basic
    @Column(name = "spbill_createIp")
	private String spbillCreateIp;// 发起人IP地址

    @Basic
    @Column(name = "attach")
	private String attach;// 附件数据主要用于商户携带订单的自定义数据

    @Basic
    @Column(name = "pay_type")
	private Integer payType;// 支付类型(1:支付宝 2:微信)

    @Basic
    @Column(name = "pay_way")
	private Integer payWay;// 支付方式 (1：PC,平板 2：手机)

    @Basic
    @Column(name = "front_url")
	private String frontUrl;// 前台回调地址  非扫码支付使用

    @Basic
    @Column(name = "usr_no")
    private String usrNo;

    @Basic
    @Column(name = "usr_tel")
    private String usrTel;

    @Basic
    @Column(name = "pay_time")
    private Timestamp payTime;// 支付时间

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getFrontUrl() {
		return frontUrl;
	}
	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getUsrTel() {
        return usrTel;
    }

    public void setUsrTel(String usrTel) {
        this.usrTel = usrTel;
    }

    public String getUsrNo() {
        return usrNo;
    }

    public void setUsrNo(String usrNo) {
        this.usrNo = usrNo;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }
}
