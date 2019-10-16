package com.hust.nb.util;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.util.ClassUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Description:nb
 * Created by Administrator on 2019/9/12
 * 微信支付基本配置类
 */
public class WXConfigUtil implements WXPayConfig {

    private byte[] certData;
    private String APP_ID;
    private String KEY;
    private String MCH_ID;

//    public WXConfigUtil() throws Exception {
//        String certPath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"/weixin/apiclient_cert.p12";//从微信商户平台下载的安全证书存放的路径
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
//    }


    public void setCertData(byte[] certData) {
        this.certData = certData;
    }

    public void setAPP_ID(String APP_ID) {
        this.APP_ID = APP_ID;
    }

    public void setKEY(String KEY) {
        this.KEY = KEY;
    }

    public void setMCH_ID(String MCH_ID) {
        this.MCH_ID = MCH_ID;
    }

    @Override
    public String getAppID() {
        return APP_ID;
    }

    @Override
    public String getMchID() {
        return MCH_ID;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

}
