package com.hust.nb.Service;

import com.hust.nb.Entity.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface UserService {
    Page<User> getUserPageByBlockId(int blockId, int page, int rows);

    Page<User> findAllByCommunityId(int communityId, int page, int rows);

    List<String> getAddrsByBlockId(int blockId);

    List<User> getUserByNameAndEnprNo(String userName, String enprNo);

    User getByUserId(int userId);

    User getByBLockIdAndAddr(int blockId, String addr);

    void updateUser(User user);

    List<User> findInfo();

    User findByUserNameAndUserAddrAAndUserTel(String userName, String userAddr, String userTel);

    List<Integer> getUserIdsByBlockId(int blockId);

    User getByUserNoAndPassword(String userNo, String password);

    List<User> getUsersByBlockId(int blockId);
}
