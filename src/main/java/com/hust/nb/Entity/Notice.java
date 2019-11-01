package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 * 通知公告
 */
@Entity
@Table(name = "nt_notice", schema = "dbo", catalog = "mixAll")
public class Notice implements Serializable {

    /**
     * ID主键
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 公告标题
     */
    @Basic
    @Column(name = "title")
    private String title;

    /**
     * 公告详细内容
     */
    @Basic
    @Column(name = "content")
    private String content;

    /**
     * 公告发布时间
     */
    @Basic
    @Column(name = "r_time")
    private java.sql.Timestamp releaseTime;


    /**
     * 公告类型
     */
    @Basic
    @Column(name = "type")
    private Integer type;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 公告对象
     */
    @Basic
    @Column(name = "objects")
    private String objects;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Timestamp releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public String getObjects() {
        return objects;
    }

    public void setObjects(String objects) {
        this.objects = objects;
    }
}



