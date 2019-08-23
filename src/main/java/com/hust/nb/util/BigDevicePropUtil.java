package com.hust.nb.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "system")//接受application.yaml中myProps下面的属性
public class BigDevicePropUtil {
    public String bigDeviceUrl;
    public String nbDeviceUrl;

    public String getNbDeviceUrl(){
        return nbDeviceUrl;
    }

    public BigDevicePropUtil setNbDeviceUrl(String url){
        this.nbDeviceUrl = url;
        return this;
    }

    public String getBigDeviceUrl(){
        return bigDeviceUrl;
    }

    public BigDevicePropUtil setBigDeviceUrl(String url){
        this.bigDeviceUrl = url;
        return this;
    }
}
