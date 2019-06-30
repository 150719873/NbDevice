package com.hust.nb.ScheduleJob.MonthCountAndCostScheduledJob;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Configurable
@Component
@EnableScheduling
public class RemindTask {
    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(RemindTask.class);
    /**
     * 初始化动态创建定期任务
     *
     * 创建的一个定时任务,每天会执行一遍,代替1中的onApplicationEvent方法
     */
    @Autowired
    private RemindRuleScheduler remindRuleScheduler;

    // 每天执行定时任务:测试使用,每小时执行
    @Scheduled(cron = "0 0 * * * ?")
    public void testGetDemoData() {
        log.debug("RemindTask starting...");
        // A:创建或者更新定期的所有动态任务
        try {
            remindRuleScheduler.initStartJob();
        } catch (SchedulerException e) {
            log.error("RemindTask 初始化定期提醒任务失败.. {}", e);
        }
        log.debug("RemindTask end...");
    }
}
