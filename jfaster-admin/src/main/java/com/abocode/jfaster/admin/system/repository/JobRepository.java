package com.abocode.jfaster.admin.system.repository;

import com.abocode.jfaster.core.repository.CommonRepository;
import com.abocode.jfaster.system.entity.Job;

public interface JobRepository extends CommonRepository {
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
