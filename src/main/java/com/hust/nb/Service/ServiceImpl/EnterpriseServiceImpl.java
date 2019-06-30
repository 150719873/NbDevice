package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.EnterpriseDao;
import com.hust.nb.Entity.Enterprise;
import com.hust.nb.Service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    EnterpriseDao enterpriseDao;

    @Override
    public Enterprise findByEnprNo(String enprNo) {
        return enterpriseDao.findByEnprNo(enprNo);
    }

    @Override
    public void saveEnterpriseCronAndCostType(Enterprise enterprise){
        enterpriseDao.save(enterprise);
    }

    @Override
    public List<Enterprise> getAllEnpr() {
        return enterpriseDao.findAll();
    }

    @Override
    public void addEnpr(Enterprise enterprise) {
        enterpriseDao.save(enterprise);
    }
}
