package com.hust.nb.Service;

import com.hust.nb.Entity.Historydata;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/17
 */
public interface HistorydataService {
    Page<Historydata> getHisPageByImei(String imei, int page, int rows);

    List<Historydata> get30DaysData(String imei);

    List<Historydata> getCurMonthData(String imei);

    List<Historydata> getPreMonthData(String imei);
}
