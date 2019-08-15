package com.hust.nb.util;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/20
 */
@Component
public class QBTDeviceTmpGetter {

    @PersistenceContext
    EntityManager entityManager;

    public List getLatestRecord(String addr, String enprNo, String tableName) {
        String sql = "select top 1 * from " + tableName + " where addr = '" +addr + "'"
                +" and enprNo = '" + enprNo + "'" + " order by readDate desc";
        System.out.println(sql);
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List res = query.getResultList();
        return res;
    }

    public List queryForCurMonthHistoryData(String addr, int centerId, String tableName){
        String sql = "select * from " + tableName + " where addr = '" + addr + "' and centerId = " + centerId;
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List res = query.getResultList();
        return res;
    }
}
