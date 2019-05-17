package com.hust.nb.Dao;

import com.hust.nb.Entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Repository
public interface BlockDao extends JpaRepository<Block,Block>,JpaSpecificationExecutor<Block> {
    List<Block> getAllByCommunityId(int communityId);
}
