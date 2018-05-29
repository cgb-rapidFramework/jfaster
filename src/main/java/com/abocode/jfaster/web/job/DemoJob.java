package com.abocode.jfaster.web.job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
/**
 * 定时任务初始化
 *
 * Created by liyd on 12/19/14.
 */
@Component
@EnableScheduling
public class DemoJob {
    /** 日志对象 */
    private static final Logger logger = LoggerFactory.getLogger(DemoJob.class);

}