package com.abocode.jfaster.web.system.service;

import com.abocode.jfaster.core.common.service.CommonService;
import com.abocode.jfaster.web.system.entity.Job;

public interface JobService extends CommonService{
    /**
     * 初始化定时任务
     */
    public void initJob() throws Exception;

    /**
     * 新增
     *
     * @param job
     * @return
     */
    public void addJob(Job job) throws Exception;

    /**
     * 删除重新创建方式
     *
     * @param job
     */
    public void updateJob(Job job) throws Exception;

    /**
     * 删除
     *
     * @param jobId
     */
    public void deleteJob(String jobId) throws Exception;

    /**
     * 运行一次任务
     *
     * @param jobId
     * @return
     */
    public void runOnceJob(String jobId) throws Exception;

    /**
     * 暂停任务
     *
     * @param jobId
     * @return
     */
    public void pauseJob(String jobId) throws Exception;

    /**
     * 恢复任务
     *
     * @param jobId
     * @return
     */
    public void resumeJob(String jobId) throws Exception;
}
