package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.MonthcostDao;
import com.hust.nb.Dao.UserDao;
import com.hust.nb.Entity.Monthcost;
import com.hust.nb.Entity.User;
import com.hust.nb.Service.MonthcostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
@Service
public class MonthcostServiceImpl implements MonthcostService {

    @Autowired
    MonthcostDao monthcostDao;

    @Autowired
    UserDao userDao;

    @Override
    public Page<Monthcost> getMonthcostPage(int userId, int rows, int page) {
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<Monthcost> pageList = monthcostDao.findAllByUserId(userId, pageable);
        return pageList;
    }

    @Override
    public Page<Monthcost> getAccountPage(int userId, int rows, int page) {
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<Monthcost> pageList = monthcostDao.findAllByUserIdOrderByIdDesc(userId, pageable);
        return pageList;
    }

    @Override
    @Transactional
    public void updateAccountBanlance(User user, Monthcost monthcost) {
        monthcostDao.save(monthcost);
        userDao.save(user);
    }
}
