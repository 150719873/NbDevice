package com.hust.nb.Service;

import com.hust.nb.Entity.Monthcost;
import com.hust.nb.Entity.User;
import org.springframework.data.domain.Page;

/**
 * Description:nb
 * Created by Administrator on 2019/5/30
 */
public interface MonthcostService {
    Page<Monthcost> getMonthcostPage(int userId, int rows, int page);

    Page<Monthcost> getAccountPage(int userId, int rows, int page);

    void updateAccountBanlance(User user, Monthcost monthcost);
}
