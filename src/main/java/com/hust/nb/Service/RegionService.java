package com.hust.nb.Service;

import com.hust.nb.Entity.Region;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface RegionService {
    List<Region> getByEnprNo(String enprNo);

    void saveRegion(Region region);

}
