package com.hust.nb.Entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 * 楼栋表
 */
@Entity
@Table(name = "nt_block", schema = "dbo", catalog = "mixAll")
public class Block implements Serializable {
    /**
     * 楼栋ID主键
     */
    @Id
    @Column(name = "block_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blockId;

    /**
     * 楼栋名称
     */
    @Basic
    @Column(name = "block")
    private String blockName;

    /**
     * 水司编码
     */
    @Basic
    @Column(name = "enprNo")
    private String enprNo;

    /**
     * 区域ID
     */
    @Basic
    @Column(name = "community_id")
    private int communityId;

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getEnprNo() {
        return enprNo;
    }

    public void setEnprNo(String enprNo) {
        this.enprNo = enprNo;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }
}
