package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:nb
 * Created by Administrator on 2019/6/29
 */
@Entity
@Table(name = "nt_enterCollectType", schema = "dbo", catalog = "mixAll")
public class EnterCollectType implements Serializable {
    /**
     * 主键
     */
    @javax.persistence.Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    /**
     * 水司编号
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 支持采集类型
     */
    @Basic
    @Column(name = "collection_type_id")
    private Integer typeId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
