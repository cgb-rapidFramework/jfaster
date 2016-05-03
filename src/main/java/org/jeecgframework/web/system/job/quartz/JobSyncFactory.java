package org.jeecgframework.web.system.job.quartz;

import org.jeecgframework.web.system.entity.core.JobEntity;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 同步的任务工厂类
 */
public class JobSyncFactory implements Job {

    /* 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(JobSyncFactory.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        LOG.info("JobSyncFactory execute");

        JobDataMap mergedJobDataMap = jobExecutionContext.getMergedJobDataMap();
        JobEntity job = (JobEntity) mergedJobDataMap.get("jobParam");

        System.out.println("JobSyncFactory jobName:" + job.getName() + "  " + job);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
