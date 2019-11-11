package com.hust.nb.Service;

import com.hust.nb.Dao.DimnessDao;
import com.hust.nb.Entity.Dimness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DimnessService
{
    Page<Dimness> getbyEnprNo(String enprNo, Pageable pageable);
}
