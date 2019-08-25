/*package com.hust.nb.ScheduleJob.MonthCountAndCostScheduledJob;

import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

*//**
 * Description:nb
 * Created by Administrator on 2019/5/24
 *//*
@Component
public class JobFactory extends AdaptableJobFactory {

    //用来创建JOB实例
    private static final Logger log = LoggerFactory.getLogger(JobFactory.class);

    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception{
        //调用父类方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}*/
