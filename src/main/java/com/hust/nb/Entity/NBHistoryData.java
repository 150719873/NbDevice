package com.hust.nb.Entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
public class NBHistoryData {

    private String addr;
    private BigDecimal showValue;
    private Timestamp readTime;

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public BigDecimal getShowValue() {
        return showValue;
    }

    public void setShowValue(BigDecimal showValue) {
        this.showValue = showValue;
    }

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }
}
