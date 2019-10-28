package com.hust.nb.Dao;

import com.hust.nb.Entity.Community;
import com.hust.nb.Entity.RepairItem;
import com.hust.nb.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nt_repair where user_no = ?1 and enprNo =?2 order by user_id desc")
    List<RepairItem> findAllByUserNoAndEnprNo(String userNo,String enprNo);

    List<RepairItem> findAllByUserId(Integer userId);

//    @Query(nativeQuery = true, value = "select contents from mixAll.dbo.nt_repair where device_No = ?1")
//    List<String> findFeedbackByDeviceNo(String deviceNo);

//    @Query(nativeQuery = true, value = "select user_id from mixAll.dbo.nt_block where device_no = ?1")
//    Integer findUserIdByDeviceNo(String deviceNo);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nt_repair where enprNo = ?1 order by user_id desc")
    Page<RepairItem> findByEnprNo(String enprNo,Pageable pageable);


    @Query(nativeQuery = true, value = "select community_id from mixAll.dbo.nt_block where block_id = ?1")
    Integer findCommunityIdByBlockId(Integer blockId);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nt_repair where community_id = ?1 and enprNo =?2 order by user_id desc")
    Page<RepairItem> findAllByCommunityIdAndEnprNo(Integer communityId, String enprNo, Pageable pageable);


//    @Query(nativeQuery = true, value = "select user_id from mixAll.dbo.nt_user where enprNo = ?1 and user_no =?2" )
//    Integer findUserIdByEnprNoAndUserNo(String enprNo, String userNo);
//
//    @Query(nativeQuery = true, value = "select device_no from mixAll.dbo.nb_device where user_id = ?1 " )
//    String findDeviceNoByUserId(Integer userId);

    @Query(nativeQuery = true, value = "select d.device_no as device_no from mixAll.dbo.nb_device d left join mixAll.dbo.nt_user u on d.user_id = u.user_id where u.user_no = ?1 and u.enprNo = ?2")
    String findDeviceNoByEnprNoAndUserNo(String userNo, String enprNo);

    @Query(nativeQuery = true, value = "select * from mixAll.dbo.nt_repair where device_no = ?1 and enprNo =?2 order by user_id desc")
    List<RepairItem> findByDeviceNoAndEnprNo(String deviceNo,String enprNo);


    @Query(nativeQuery = true, value = "select community from mixAll.dbo.nt_community where community_id = ?1 " )
    String findCommunityNameByCommunityId(Integer communityId);

}


