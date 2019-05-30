package com.hust.nb.ScheduleJob.MonthCountAndCostScheduledJob;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
public class ApplicationStartQuartzJobListener implements ApplicationListener<ContextRefreshedEvent> {

    //用来创建mySchedulerFactoryBean这个bean对象了
    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(ApplicationStartQuartzJobListener.class);

    @Autowired
    private ScheduleConfig scheduleConfig;

    @Autowired
    private JobFactory jobFactory;
    /**
     * 初始启动quartz
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.debug("执行onApplicationEvent..");
        try {
            scheduleConfig.initStartJob();
            log.debug("任务已经启动...");
        } catch (SchedulerException e) {
            log.error("初始化启动错误:{}", e);
        }
    }

    /**
     * 初始注入scheduler
     * @return
     * @throws SchedulerException
     */
    @Bean(name ="mySchedulerFactoryBean")
    public SchedulerFactoryBean scheduler(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setOverwriteExistingJobs(true);
        bean.setStartupDelay(1);
        bean.setJobFactory(jobFactory);
        return bean;
    }
}
