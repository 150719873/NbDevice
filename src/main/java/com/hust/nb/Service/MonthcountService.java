package com.hust.nb.Service;

import com.hust.nb.Entity.Monthcost;
import com.hust.nb.Entity.Monthcount;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
public interface MonthcountService {

    Monthcount findLatestRecordByDeviceNoAndEnprNo(String deviceNo, String enprNo);

    Page<Monthcount> findMonthcountPage(String deviceNo, String enprNo, int rows, int page);

    void save(Monthcount monthcount);

    void addMonthcost(Monthcost tmonthcost);

    void delMonthcostByKey(Monthcost id);

    Monthcost updateMonthcost(Monthcost tmonthcost);

    Monthcost getMonthcostByKey(int id);

    List<Monthcost> findAllByUserIdAndDate(int userId , String date);

    Monthcost findTop1ByUserId(int userId);

    Page<Monthcost> findAllByUserIdOrderByIdDesc(int page , int rows ,int userId  );

    Page<Monthcost> findAllByUserId(int page , int rows ,int userId  );

    Page<Monthcost> findAllByOperatorIdAndDateOrderByIdDesc( int page , int rows ,int operatorId , String data);
}
