package com.hust.nb.Controller;

import com.alibaba.fastjson.JSONObject;
import com.hust.nb.Entity.Block;
import com.hust.nb.Entity.Community;
import com.hust.nb.Entity.Region;
import com.hust.nb.Service.BlockService;
import com.hust.nb.Service.CommunityService;
import com.hust.nb.Service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
@Controller
@RequestMapping("/Operate")
@CrossOrigin
public class OperateController {
    @Autowired
    RegionService regionService;

    @Autowired
    CommunityService communityService;

    @Autowired
    BlockService blockService;

    /**
     * 获取水司的全部区域信息
     */
    @ResponseBody
    @PostMapping("/GetRegionInfo")
    public Object getRegionInfo(@RequestBody String msg){
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String enprNo = jsonObject.getString("enprNo");
        try{
            List<Region> regions = regionService.getByEnprNo(enprNo);
            jsonMap.put("code", "200");
            jsonMap.put("info", "查询成功");
            jsonMap.put("data", regions);
        } catch (Exception e){
            jsonMap.put("code", "-1");
            jsonMap.put("info", "查询失败");
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }


    /**
     * 方法功能描述:根据区域ID小区以及楼栋信息
     */
    @CrossOrigin
    @ResponseBody
    @PostMapping("/GetInfoByRegion")
    public Object getAreaInfo(@RequestBody String msg) {
        Map<String, Object> jsonMap = new HashMap<>();
        JSONObject jsonObject = JSONObject.parseObject(msg);
        int regionId = jsonObject.getInteger("regionId");
        Map<String, Object> commuBlockMap = new HashMap<>();
        List<Community> communities = communityService.getByRegionId(regionId);
        if(communities.size() != 0){
            try{
                for(Community community : communities){
                    String communityName = community.getCommunityName();
                    List<Block> blocks = blockService.getByCommunityId(community.getCommunityId());
                    commuBlockMap.put(communityName, blocks);
                }
                jsonMap.put("code", "200");
                jsonMap.put("info", "查询成功");
                jsonMap.put("data", commuBlockMap);
            } catch (Exception e){
                e.printStackTrace();
                jsonMap.put("code", "-1");
                jsonMap.put("info", "查询失败");
            }
        }
        Object object = JSONObject.toJSON(jsonMap);
        return object;
    }
}
