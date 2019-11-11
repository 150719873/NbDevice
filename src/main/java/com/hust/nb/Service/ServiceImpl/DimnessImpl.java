package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.DimnessDao;
import com.hust.nb.Entity.Dimness;
import com.hust.nb.Entity.RepairItem;
import com.hust.nb.Service.DimnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DimnessImpl implements DimnessService
{
    @Autowired
    DimnessDao dimnessDao;

    @Override
    public Page<Dimness> getbyEnprNo(String enprNo, Pageable pageable )
    {
        return dimnessDao.findAllByEnprNo(enprNo,pageable);
    }

}
