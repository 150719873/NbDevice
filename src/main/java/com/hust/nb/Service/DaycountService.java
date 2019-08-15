package com.hust.nb.Service;

import com.hust.nb.Entity.Daycount;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
public interface DaycountService {
    Page<Daycount> findDaycountPage(String deviceNo, String enprNo, int rows, int page);

    Page<Daycount> findPartDaycountPage(String deviceNo, String enprNo, Timestamp start,Timestamp end, int rows, int page);
}
