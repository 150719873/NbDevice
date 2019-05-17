package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.BlockDao;
import com.hust.nb.Entity.Block;
import com.hust.nb.Service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Service
public class BlockServiceImpl implements BlockService {

    @Autowired
    BlockDao blockDao;

    @Override
    public List<Block> getByCommunityId(int communityId) {
        return blockDao.getAllByCommunityId(communityId);
    }
}
