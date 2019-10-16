package com.hust.nb.Service.ServiceImpl;

import com.hust.nb.Dao.PayHistoryDao;
import com.hust.nb.Entity.PayHistory;
import com.hust.nb.Service.PayHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: suxinyu
 * @DateTme: 2019/10/14 16:02
 */
@Service
public class PayHistoryServiceImpl implements PayHistoryService {

    @Autowired
    PayHistoryDao payHistoryDao;

    @Override
    public void transactionProcess(PayHistory payHistory) {
        payHistoryDao.save(payHistory);
    }
}
