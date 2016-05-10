package org.jeecgframework.web.system.job.quartz;

import javax.annotation.PostConstruct;

import org.jeecgframework.web.system.service.core.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 定时任务初始化
 *
 * Created by liyd on 12/19/14.
 */
@Component
public class JobInit {

    /** 日志对象 */
    private static final Logger LOG = LoggerFactory.getLogger(JobInit.class);

    /** 定时任务service */
    @Autowired
    private JobService jobService;

    /**
     * 项目启动时初始化
     */
    @PostConstruct
    public void init() {

        if (LOG.isInfoEnabled()) {
            LOG.info("init");
        }
        try {
            jobService.initJob();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("end");
        }
    }

}
