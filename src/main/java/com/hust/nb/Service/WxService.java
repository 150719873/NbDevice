package com.hust.nb.Service;

import com.hust.nb.Entity.WxConfig;

import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/9/12
 */
public interface WxService {
    String payBack(String resXml);

    Map doMicroOrder(String authcode, String enprNo, String fee, String userNo) throws Exception;

    void save(WxConfig wxConfig);

    WxConfig findByEnprNo(String enprNo);
}
