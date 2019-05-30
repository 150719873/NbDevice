package com.hust.nb.ScheduleJob.MonthCountAndCostScheduledJob;

import com.hust.nb.Dao.EnterpriseDao;
import com.hust.nb.Entity.Enterprise;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;
import java.util.List;

/**
 * Description:nb
 * Created by Administrator on 2019/5/24
 */
@Configuration
public class ScheduleConfig {
    /**
     * 日志
     */
    private final Logger log = LoggerFactory.getLogger(Scheduler.class);

    private final String REMINDRULE = "REMINDRULE";

    @Autowired
    private EnterpriseDao enterpriseDao;

    /**
     * 开始执行所有任务
     * 从数据库查询定时提醒任务,进行更新任务.
     */
    public void initStartJob() throws SchedulerException {
        //从数据库中查到所有提醒
        ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        StdScheduler stdScheduler = (StdScheduler)applicationContext.getBean("mySchedulerFactoryBean");
        Scheduler mySchedule = stdScheduler;

        //0表示定期，该方法仅执行定期
        List<Enterprise> scheduledJobs = enterpriseDao.findAll();
        log.debug("RemindTask 数据库查询出来的定期任务数量:{}", scheduledJobs.size());
        if (scheduledJobs.size() > 0){
            for (int i =0; i <scheduledJobs.size(); i++){
                startJob(mySchedule,scheduledJobs.get(i));
                System.out.println(scheduledJobs.get(i).getCron());
            }
            mySchedule.start();
        }
    }

    private void startJob(Scheduler myScheduler, Enterprise scheduledJob) throws SchedulerException{
        // 创建触发器表达式{0}:表示小时,{1}:表示日,
//        String cron = MessageFormat.format("0 0 {0} {1} * ?",
//                scheduledJob.getOnHour() == null ? 0 : remindRuleObjF04SQL01OM01.getOnHour(),
//                scheduledJob.getOnDay() == null || remindRuleObjF04SQL01OM01.getOnDay() == 0 ? "*" : remindRuleObjF04SQL01OM01.getOnDay());
        log.info(MessageFormat.format("RemindTask 提醒任务Id=[{0}]的cron的表达式:[{1}]", scheduledJob.getId(), scheduledJob.getCron()));
        String cron = scheduledJob.getCron();
        //名字和分组名
        TriggerKey triggerKey = new TriggerKey(scheduledJob.getId()+"",REMINDRULE);
        CronTrigger cronTrigger = (CronTrigger)myScheduler.getTrigger(triggerKey);
        //不存在这个任务，新增任务
        if (cronTrigger == null){
            //通过JobBuilder构建JobDetail实例，JobDetail规定只能是实现Job接口的实例
            JobDetail jobDetail = JobBuilder.newJob(MonthScheduleJob.class)
                    .withIdentity(scheduledJob.getId().toString(),REMINDRULE)
                    .usingJobData("水司编号",scheduledJob.getId())
                    .build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduledJob.getCron());
            //CronTrigger表达式触发器，继承与trigger
            cronTrigger = TriggerBuilder.newTrigger().withIdentity(scheduledJob.getId().toString(),REMINDRULE)
                    .withSchedule(cronScheduleBuilder).build();
            myScheduler.scheduleJob(jobDetail, cronTrigger);
        }else {
            //存在这个任务，判断这个任务触发时间是否被修改过，如果修改过则更新任务
            String oldCron = cronTrigger.getCronExpression();
            // 新配置的cron和之前任务中使用的不一致,则更新
            if (!cron.equals(oldCron)){
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(scheduledJob.getId().toString(),REMINDRULE)
                        .withSchedule(cronScheduleBuilder).build();
                myScheduler.rescheduleJob(triggerKey, trigger);
                log.info("RemindTask 更新任务执行时间成功");
            }else {
                log.info("RemindTask 任务不进行操作");
            }
        }
    }
}
