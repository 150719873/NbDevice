package com.hust.nb.Service.ServiceImpl;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.hust.nb.Dao.WxConfigDao;
import com.hust.nb.Entity.PayHistory;
import com.hust.nb.Entity.WxConfig;
import com.hust.nb.Service.PayHistoryService;
import com.hust.nb.Service.WxService;
import com.hust.nb.util.WXConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/9/12
 */
@Service
public class WxServiceImpl implements WxService {

    private static Logger log = LoggerFactory.getLogger(WxServiceImpl.class);

    @Autowired
    WxConfigDao wxConfigDao;

    @Autowired
    PayHistoryService payHistoryService;

    public static final String SPBILL_CREATE_IP = "222.20.81.173";
    public static final String NOTIFY_URL = "回调接口地址";
    private static final int TRADE_INIT = 0;
    private static final int TRADE_SUCCESS = 1;
    private static final int TRADE_FAILED = -1;

    @Override
    public Map doMicroOrder(String authcode, String enprNo, String fee, String userNo) throws Exception {
        Map<String,Object> jsonMap = new HashMap<>();
        WxConfig wxConfig = wxConfigDao.getWxConfigByEnprNo(enprNo);
        PayHistory payHistory = new PayHistory();
        payHistory.setState(TRADE_INIT);
        payHistory.setEnprNo(enprNo);
        payHistory.setFee(fee);
        String curTime = ""+System.currentTimeMillis()/1000;
        payHistory.setPayTime(curTime);
        String out_trade_no = userNo+curTime;
        payHistory.setTradeNo(out_trade_no);
        payHistory.setUserNo(userNo);
        payHistoryService.transactionProcess(payHistory);
        try {
            WXConfigUtil config = new WXConfigUtil();
            config.setAPP_ID(wxConfig.getAppId());
            config.setMCH_ID(wxConfig.getMchId());
            config.setKEY(wxConfig.getKey());
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<>();
            //生成商户订单号，不可重复
            data.put("appid", config.getAppID());//公众账号ID
            data.put("mch_id", config.getMchID());//商户号
            data.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
            data.put("body", enprNo+"-"+"water");//商品名称
            data.put("out_trade_no", out_trade_no); //商户订单号，自己生成
            data.put("total_fee", fee); //订单金额int 传来的费用,分数形式  默认货币为CNY
            data.put("spbill_create_ip", SPBILL_CREATE_IP);//调用微信支付API的机器IP
            data.put("auth_code", authcode);//扫码支付授权码，设备读取用户微信中的条码或者二维码信息
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                    WXPayConstants.SignType.MD5));
            //使用官方API请求预付订单
            Map<String, String> response = wxpay.microPay(data);
            if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数
                jsonMap.put("appid", response.get("appid"));
                jsonMap.put("mch_id", response.get("mch_id"));
                jsonMap.put("nonce_str", response.get("nonce_str"));
                jsonMap.put("sign", response.get("sign"));
                jsonMap.put("transactionId", response.get("transaction_id"));//微信支付订单号
                jsonMap.put("outTradeNo", response.get("out_trade_no"));
                jsonMap.put("timeEnd", response.get("time_end"));
                String resultCode = response.get("result_code");
                if(resultCode.equals("SUCCESS")){
                    payHistory.setTransactionId(response.get("transaction_id"));
                    payHistory.setState(TRADE_SUCCESS);
                    payHistoryService.transactionProcess(payHistory);
                    jsonMap.put("code", 200);
                    return jsonMap;
                } else {
                    String errCode = response.get("err_code");
                    Map<String, String> res;
                    if(errCode.equals("USERPAYING")){ // 付款中状态
                        double ts = System.currentTimeMillis()/1000;
                        do{
                            Thread.sleep(5000);
                            Map<String, String> req = new HashMap<>();
                            req.put("appid", config.getAppID());
                            req.put("mch_id", config.getMchID());
                            req.put("out_trade_no", out_trade_no);
                            req.put("nonce_str", WXPayUtil.generateNonceStr());
                            req.put("sign", WXPayUtil.generateSignature(req, config.getKey(),
                                    WXPayConstants.SignType.MD5));
                            res = wxpay.orderQuery(req);
                            errCode = res.get("trade_state");
                        } while (errCode != null && errCode.equals("USERPAYING") && (System.currentTimeMillis()/1000 - ts) < 30);
                        if(errCode.equals("SUCCESS")){ // 成功
                            payHistory.setTransactionId(response.get("transaction_id"));
                            payHistory.setState(TRADE_SUCCESS);
                            payHistoryService.transactionProcess(payHistory);
                            jsonMap.put("trade_state_desc", res.get("trade_state_desc"));
                            jsonMap.put("code", 200);
                            return jsonMap;
                        } else { // 没有成功，撤销订单
                            payHistory.setTransactionId(response.get("transaction_id"));
                            payHistory.setState(TRADE_FAILED);
                            payHistoryService.transactionProcess(payHistory);
                            jsonMap.put("code", -1);
                            jsonMap.put("trade_state_desc", res.get("trade_state_desc"));
                            return jsonMap;
                        }
                    } else {
                        payHistory.setTransactionId(response.get("transaction_id"));
                        payHistory.setState(TRADE_FAILED);
                        payHistoryService.transactionProcess(payHistory);
                        jsonMap.put("err_code", errCode);
                        jsonMap.put("code", -1);
                        return jsonMap;
                    }
                }
            } else {
                payHistory.setState(TRADE_FAILED);
                payHistoryService.transactionProcess(payHistory);
                String errMsg = response.get("return_msg");
                jsonMap.put("code", -1);
                jsonMap.put("err_msg", errMsg);
            }
        } catch (Exception e) {
            jsonMap.put("code", -1);
            jsonMap.put("err_msg", "内部错误");
            log.error(e.toString());
        }
        return jsonMap;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis()/1000);
    }

    @Override
    @Transactional
    public void save(WxConfig wxConfig) {
        wxConfigDao.save(wxConfig);
    }

    @Override
    public WxConfig findByEnprNo(String enprNo) {
        return wxConfigDao.getWxConfigByEnprNo(enprNo);
    }

    @Override
    public String payBack(String resXml) {
        WXConfigUtil config = null;
        try {
            config = new WXConfigUtil();
        } catch (Exception e) {
            e.printStackTrace();
        }
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理

                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        // 业务数据持久化
                        log.info("微信手机支付回调成功订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        log.info("微信手机支付回调失败订单号:{}", out_trade_no);
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                //失败的数据要不要存储？
                log.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败", e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }
}
