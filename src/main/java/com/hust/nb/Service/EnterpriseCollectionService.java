package com.hust.nb.Service;

import com.hust.nb.Entity.EnterCollectType;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/6/29
 */
public interface EnterpriseCollectionService {

    void saveEntity(EnterCollectType enterCollectType);

    List<Integer> selectAllType(String enprNo);

    void deleteAllByEnprNo(String enprNo);
}
