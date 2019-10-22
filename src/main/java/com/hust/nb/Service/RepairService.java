package com.hust.nb.Service;

import com.hust.nb.Entity.RepairItem;

import java.util.List;


public interface RepairService
{

    void saveRepairItem(RepairItem repairItem);

    RepairItem getbyUserId(Integer userId);

    List<RepairItem> getFeedbackByUserNo(String userNo);

//     Integer getUserIdByDeviceNo(String deviceNo);

    //List<RepairItem> getbyCommunityId(Integer communityId);

    List<RepairItem> getbyEnprNo(String enproNo);

    void deleteRepairInfo(RepairItem repairItem);

    List<RepairItem> getFeedbackByUserId(Integer userId);

    Integer getCommunityIdByBlockId(Integer blockId);

    List<RepairItem> getByCommunityId(Integer communityId);

    String getDeviceNoByUserNoAndEnprNo(String userNo, String enprNo);

    List<RepairItem> getFeedbackByDeviceNo(String deviceNo);
}

