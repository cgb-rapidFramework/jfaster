package org.jeecgframework.web.system.job.quartz;

import org.jeecgframework.web.system.entity.core.JobEntity;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同步的任务工厂类
 */
public class SyncJob implements Job {

    /* 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(SyncJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LOG.info("SyncJob execute");

        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        JobEntity job = (JobEntity) mergedJobDataMap.get("jobParam");

        System.out.println("SyncJob jobName:" + job.getName() + "  " + job);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
