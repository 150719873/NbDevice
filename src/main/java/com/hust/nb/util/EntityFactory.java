package com.hust.nb.util;

import com.hust.nb.Entity.Operator;

/**
 * Description:nb
 * Created by Administrator on 2019/5/21
 */
public class EntityFactory {

    public static Operator OperatorFactory(String userName, String password, String phone
    , int userType, String operatorName, String manageCommunity, String enprNo){
        Operator operator = new Operator();
        operator.setUserName(userName);
        operator.setPassword(password);
        operator.setPhone(phone);
        operator.setUserType(userType);
        operator.setOperatorName(operatorName);
        operator.setManageCommunity(manageCommunity);
        operator.setManageCommunity(enprNo);
        return operator;
    }
}
