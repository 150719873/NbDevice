package com.hust.nb.Service;

import com.hust.nb.Entity.RepairItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RepairService
{

    void saveRepairItem(RepairItem repairItem);

    RepairItem getbyUserId(Integer userId);

    List<RepairItem> getFeedbackByUserNoAndEnprNo(String userNo,String enprNo);

//     Integer getUserIdByDeviceNo(String deviceNo);

    //List<RepairItem> getbyCommunityId(Integer communityId);

    Page<RepairItem> getbyEnprNo(String enproNo,Pageable pageable);

    void deleteRepairInfo(RepairItem repairItem);

    List<RepairItem> getFeedbackByUserId(Integer userId);

    Integer getCommunityIdByBlockId(Integer blockId);


    Page<RepairItem> getByCommunityIdAndEnprNo(Integer communityId, String enprNo, Pageable pageable);

    String getDeviceNoByUserNoAndEnprNo(String userNo, String enprNo);

    List<RepairItem> getFeedbackByDeviceNoAndEnprNo(String deviceNo,String enprNo);

    String getCommunityNameByCommunityId(Integer communityId);
}

