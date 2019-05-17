package com.hust.nb.Service;

import com.hust.nb.Entity.Block;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface BlockService {
    List<Block> getByCommunityId(int communityId);
}
