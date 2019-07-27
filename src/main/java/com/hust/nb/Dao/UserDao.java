package com.hust.nb.Dao;

import com.hust.nb.Entity.Block;
import com.hust.nb.Entity.Device;
import com.hust.nb.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Repository
public interface UserDao extends JpaRepository<User,User>,JpaSpecificationExecutor<User> {
    Page<User> findAllByBlockId(int bolckId ,Pageable pageable);

    @Query(nativeQuery = true, value = "select addr from mixAll.dbo.nt_user where block_id = ?1")
    List<String> findAddrsByBlockId(int blockId);

    List<User> findAllByUserNameAndEnprNo(String  userName, String enprNo);

    User findUserByUserId(int userId);

    User findUserByBlockIdAndUserAddr(int blockId, String addr);

    List<User> findAllByEnprNo(String enprNo);

    @Query(nativeQuery = true, value = "select user_no from mixAll.dbo.nt_user")
    List<String> findUserNo();

    @Query(nativeQuery = true, value = "select user_id from mixAll.dbo.nt_user where user_name = ?1 and user_tel = ?2 and addr = ?3")
    Integer findUserByUserNameAndUserTelAndAddr(String userName, String userTel, String addr);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nt_user")
    List<User> findUserTelAndName();

    User findByUserNameAndUserAddrAndUserTel(String userName, String userAddr, String userTel);

    @Query(nativeQuery = true, value = "select user_id from mixAll.dbo.nt_user where block_id = ?1")
    List<Integer> getUserIdsByBlockId(int blockId);

    @Query(nativeQuery = true, value = "select * FROM mixAll.dbo.nt_user WHERE block_id in (SELECT block_id FROM mixAll.dbo.nt_block WHERE community_id = ?1)")
    Page<User> findAllByCommunityId(int communityId, Pageable pageable);
}
