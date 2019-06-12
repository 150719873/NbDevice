package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.UserDao;
import com.hust.nb.Entity.User;
import com.hust.nb.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public Page<User> getUserPageByBlockId(int blockId, int page, int rows) {
        Pageable pageable = PageRequest.of(page - 1, rows);
        Page<User> pageList = userDao.findAllByBlockId(blockId ,pageable);
        return pageList;
    }

    @Override
    public List<String> getAddrsByBlockId(int blockId) {
        return userDao.findAddrsByBlockId(blockId);
    }

    @Override
    public List<User> getUserByNameAndEnprNo(String userName, String enprNo) {
        return userDao.findAllByUserNameAndEnprNo(userName, enprNo);
    }

    @Override
    public User getByUserId(int userId) {
        return userDao.findUserByUserId(userId);
    }

    @Override
    public User getByBLockIdAndAddr(int blockId, String addr) {
        return userDao.findUserByBlockIdAndUserAddr(blockId, addr);
    }

    @Override
    public void updateUser(User user) {
        userDao.save(user);
    }


    @Override
    public List<User> findInfo(){
        return userDao.findUserTelAndName();
    }
}
