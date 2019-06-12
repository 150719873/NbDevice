package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.RegionDao;
import com.hust.nb.Entity.Region;
import com.hust.nb.Service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    RegionDao regionDao;

    @Override
    public List<Region> getByEnprNo(String enprNo) {
        return regionDao.getAllByEnprNo(enprNo);
    }

    @Override
    public void saveRegion(Region region){
        regionDao.save(region);
    }
}

