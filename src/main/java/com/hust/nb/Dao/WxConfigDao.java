package com.hust.nb.Dao;

import com.hust.nb.Entity.WxConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author: suxinyu
 * @DateTme: 2019/10/9 16:50
 */
@Repository
public interface WxConfigDao extends JpaRepository<WxConfig,WxConfig>, JpaSpecificationExecutor<WxConfig> {
    WxConfig getWxConfigByEnprNo(String enprNo);
}
