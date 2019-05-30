package com.hust.nb.Service;

import com.hust.nb.Entity.Daycount;
import org.springframework.data.domain.Page;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
public interface DaycountService {
    Page<Daycount> findDaycountPage(String deviceNo, String enprNo, int rows, int page);
}
