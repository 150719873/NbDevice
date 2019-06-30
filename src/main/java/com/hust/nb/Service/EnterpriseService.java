package com.hust.nb.Service;

import com.hust.nb.Entity.Enterprise;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
public interface EnterpriseService {
    Enterprise findByEnprNo(String enprNo);

    void saveEnterpriseCronAndCostType(Enterprise enterprise);

    List<Enterprise> getAllEnpr();

    void addEnpr(Enterprise enterprise);
}
