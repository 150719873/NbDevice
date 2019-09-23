package com.hust.nb.Service;

import java.util.Map;

/**
 * Description:nb
 * Created by Administrator on 2019/9/12
 */
public interface WxService {
    String payBack(String resXml);

    Map doUnifiedOrder() throws Exception;
}
