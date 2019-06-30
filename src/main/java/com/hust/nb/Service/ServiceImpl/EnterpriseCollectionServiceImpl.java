package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.EnterpriseCollectionDao;
import com.hust.nb.Entity.EnterCollectType;
import com.hust.nb.Entity.Enterprise;
import com.hust.nb.Service.EnterpriseCollectionService;
import com.hust.nb.Service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/6/29
 */
@Service
public class EnterpriseCollectionServiceImpl implements EnterpriseCollectionService {

    @Autowired
    EnterpriseCollectionDao enterpriseCollectionDao;

    public void saveEntity(EnterCollectType enterpriseCollectionType){
        enterpriseCollectionDao.save(enterpriseCollectionType);
    }

    @Override
    public List<Integer> selectAllType(String enprNo){
        return enterpriseCollectionDao.findAllType(enprNo);
    }

    @Override
    public void deleteAllByEnprNo(String enprNo){
        enterpriseCollectionDao.deleteAllByEnprNo(enprNo);
    }
}
