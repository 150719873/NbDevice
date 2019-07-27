package com.hust.nb.util;

import com.hust.nb.Entity.Historydata;
import com.hust.nb.Entity.NBHistoryData;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
public class Adapter {

    public static NBHistoryData rawHisToNBHis(Historydata historydata){
        NBHistoryData nbHistoryData = new NBHistoryData();
        nbHistoryData.setAddr(historydata.getDeviceNo());
        nbHistoryData.setShowValue(historydata.getDeviceValue());
        nbHistoryData.setReadTime(historydata.getReadTime());
        return nbHistoryData;
    }
}
