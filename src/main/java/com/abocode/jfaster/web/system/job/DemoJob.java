package com.abocode.jfaster.web.system.job;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * 定时任务初始化
 *
 * Created by liyd on 12/19/14.
 */
@Component
public class DemoJob implements Job {
    /** 日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(DemoJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("job is started....");
    }
}