package com.hust.nb.Dao;

import com.hust.nb.Entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Repository
public interface RegionDao extends JpaRepository<Region,Region>,JpaSpecificationExecutor<Region> {
    List<Region> getAllByEnprNo(String enprNo);

    Region findByRegionId(int regionId);
}

