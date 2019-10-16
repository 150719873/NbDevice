package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: suxinyu
 * @DateTme: 2019/10/9 16:46
 */
@Entity
@Table(name = "wx_config", schema = "dbo", catalog = "mixAll", uniqueConstraints = {@UniqueConstraint(columnNames = "enprNo")})
public class WxConfig implements Serializable {

    /**
     * 水司编码
     */
    @Id
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 公众账号id
     */
    @Basic
    @Column(name = "app_id")
    private String appId;

    /**
     * key
     */
    @Basic
    @Column(name = "wx_key")
    private String key;

    /**
     * 商户id
     */
    @Basic
    @Column(name = "mch_id")
    private String mchId;

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }
}
