/*
package com.hust.nb.ScheduleJob.MonthCountAndCostScheduledJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class InitRemindRuleScheduler implements ApplicationListener<ContextRefreshedEvent> {

    */
/**
     * 日志
     *//*

    private final Logger log = LoggerFactory.getLogger(InitRemindRuleScheduler.class);

    @Autowired
    private RemindRuleScheduler remindRuleScheduler;

    @Autowired
    private JobFactory jobFactory;

    @Bean(name ="mySchedulerFactoryBean")
    public SchedulerFactoryBean mySchedulerFactory() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(1);
        bean.setJobFactory(jobFactory);
        return bean;
    }

    */
/**
     * 项目初始化的时候启动quartz
     *//*

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        log.debug("执行onApplicationEvent..");
//        try {
//            remindRuleScheduler.initStartJob();
//            log.debug("任务已经启动...");
//        } catch (SchedulerException e) {
//            log.error("初始化启动错误:{}", e);
//        }
    }
}
*/
