package org.jeecgframework.web.utils;

import org.jeecgframework.web.system.entity.core.JobEntity;
import org.jeecgframework.web.system.job.quartz.AsyncJob;
import org.jeecgframework.web.system.job.quartz.SyncJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务辅助类
 */
public class JobUtils {
    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(JobUtils.class);

    /**
     * 获取触发器key
     * 
     * @param jobName
     * @param jobGroup
     * @return
     */
    public static TriggerKey getTriggerKey(String jobName, String jobGroup) {
        return TriggerKey.triggerKey(jobName, jobGroup);
    }

    /**
     * 获取表达式触发器
     *
     * @param scheduler the scheduler
     * @param jobName the job name
     * @param jobGroup the job group
     * @return cron trigger
     */
    public static CronTrigger getCronTrigger(Scheduler scheduler, String jobName, String jobGroup) throws Exception {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (Exception e) {
            LOG.error("获取定时任务CronTrigger出现异常", e);
            throw new Exception("获取定时任务CronTrigger出现异常");
        }
    }

    /**
     * 创建任务
     *
     * @param scheduler
     * @param job
     */
    public static void createJob(Scheduler scheduler, JobEntity job) throws Exception {
        createScheduleJob(scheduler, job.getName(), job.getGroup(), job.getExpression(), job.getIsSync(), job);
    }

    /**
     * 创建定时任务
     *
     * @param scheduler
     * @param name
     * @param group
     * @param expression
     * @param isSync
     * @param param
     */
    public static void createScheduleJob(Scheduler scheduler, String name, String group,
                                         String expression, boolean isSync, Object param) throws Exception {
        Class<? extends Job> jobClass = isSync ? SyncJob.class : AsyncJob.class;
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
        jobDetail.getJobDataMap().put("jobParam", param);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(expression);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group)
            .withSchedule(cronScheduleBuilder).build();
        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (Exception e) {
            LOG.error("创建定时任务失败", e);
            throw new Exception("创建定时任务失败");
        }
    }

    /**
     * 运行一次任务
     *
     * @param scheduler
     * @param name
     * @param group
     */
    public static void runOnce(Scheduler scheduler, String name, String group) throws Exception {
        JobKey jobKey = JobKey.jobKey(name, group);
        try {
            scheduler.triggerJob(jobKey);
        } catch (Exception e) {
            LOG.error("运行一次定时任务失败", e);
            throw new Exception("运行一次定时任务失败");
        }
    }

    /**
     * 暂停任务
     *
     * @param scheduler
     * @param name
     * @param group
     */
    public static void pauseJob(Scheduler scheduler, String name, String group) throws Exception {
        JobKey jobKey = JobKey.jobKey(name, group);
        try {
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            LOG.error("暂停定时任务失败", e);
            throw new Exception("暂停定时任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param scheduler
     * @param name
     * @param group
     */
    public static void resumeJob(Scheduler scheduler, String name, String group) throws Exception {
        JobKey jobKey = JobKey.jobKey(name, group);
        try {
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            LOG.error("暂停定时任务失败", e);
            throw new Exception("暂停定时任务失败");
        }
    }

    /**
     * 获取jobKey
     *
     * @param name
     * @param group
     * @return the job key
     */
    public static JobKey getJobKey(String name, String group) {
        return JobKey.jobKey(name, group);
    }

    /**
     * 删除定时任务
     *
     * @param scheduler
     * @param name
     * @param group
     */
    public static void deleteJob(Scheduler scheduler, String name, String group) throws Exception {
        try {
            scheduler.deleteJob(getJobKey(name, group));
        } catch (Exception e) {
            LOG.error("删除定时任务失败", e);
            throw new Exception("删除定时任务失败");
        }
    }
}
