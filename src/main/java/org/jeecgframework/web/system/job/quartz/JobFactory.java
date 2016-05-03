package org.jeecgframework.web.system.job.quartz;

import org.jeecgframework.web.system.entity.core.JobEntity;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 异步的工厂类
 */
@DisallowConcurrentExecution
public class JobFactory implements Job {

    /* 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(JobFactory.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {

        LOG.info("JobFactory execute");

        JobEntity job = (JobEntity) context.getMergedJobDataMap().get("jobParam");

        System.out.println("JobFactory jobName:" + job.getName() + "  " + job);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
