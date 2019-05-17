package com.hust.nb.Service;

import com.hust.nb.Entity.Historydata;
import org.springframework.data.domain.Page;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface HistorydataService {
    Page<Historydata> getHisPageByImei(String imei, int page, int rows);
}
