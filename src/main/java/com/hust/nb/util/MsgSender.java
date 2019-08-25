package com.hust.nb.util;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/8/25
 */
public class MsgSender {

    public static String send(String tel, String money) {
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("userid", "3717");
        paramsMap.put("account", "利川市清河水厂");
        paramsMap.put("password", "123456");
        paramsMap.put("mobile", tel);
        paramsMap.put("content", "【利川市清河水厂】 您的余额不足" + money + "，请及时充值");
        paramsMap.put("sendTime", "");
        paramsMap.put("action", "send");
        String url = "http://120.77.209.179:8868/sms.aspx?";
        String res = HttpRequest.post(url).header(Header.ACCEPT, "text/json").form(paramsMap).execute().body();
        if (res.indexOf("ok") != -1) {
            return "success";
        }
        return "fail";
    }
}
