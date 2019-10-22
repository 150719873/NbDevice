package com.hust.nb.Dao;

import com.hust.nb.Entity.Community;
import com.hust.nb.Entity.RepairItem;
import com.hust.nb.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
public interface RepairDao extends JpaRepository<RepairItem,RepairItem>,JpaSpecificationExecutor<RepairItem>
{
    RepairItem findUserByUserId(int userId);


    List<RepairItem> findAllByUserNo(String userNo);

    List<RepairItem> findAllByUserId(Integer userId);

//    @Query(nativeQuery = true, value = "select contents from mixAll.dbo.nt_repair where device_No = ?1")
//    List<String> findFeedbackByDeviceNo(String deviceNo);

//    @Query(nativeQuery = true, value = "select user_id from mixAll.dbo.nt_block where device_no = ?1")
//    Integer findUserIdByDeviceNo(String deviceNo);

//    RepairItem findFeedbackByUserId(Integer userId);


    List<RepairItem> findByEnprNo(String enprNo);


    @Transactional
    void deleteByUserId(Integer userId);

    @Query(nativeQuery = true, value = "select community_id from mixAll.dbo.nt_block where block_id = ?1")
    Integer findCommunityIdByBlockId(Integer blockId);

    List<RepairItem> findAllByCommunityId(Integer communityId);


    @Query(nativeQuery = true, value = "select user_id from mixAll.dbo.nt_user where enprNo = ?1 and user_no =?2" )
    Integer findUserIdByEnprNoAndUserNo(String enprNo, String userNo);

    @Query(nativeQuery = true, value = "select device_no from mixAll.dbo.nb_device where user_id = ?1 " )
    String findDeviceNoByUserId(Integer userId);

    @Query(nativeQuery = true, value = "select d.device_no as device_no from mixAll.dbo.nb_device d left join mixAll.dbo.nt_user u on d.user_id = u.user_id where u.user_no = ?1 and u.enprNo = ?2")
    String findDeviceNoByEnprNoAndUserNo(String userNo, String enprNo);


    List<RepairItem> findByDeviceNo(String deviceNo);
}


