package org.jeecgframework.web.system.service.core;

import org.jeecgframework.core.common.service.CommonService;
import org.jeecgframework.web.system.entity.core.JobEntity;

import java.util.List;

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
    public String addJob(JobEntity job) throws Exception;

    /**
     * 删除重新创建方式
     *
     * @param job
     */
    public void updateJob(JobEntity job) throws Exception;

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

    /**
     * 获取任务对象
     *
     * @param jobId
     * @return
     */
    public JobEntity getJob(String jobId);

    /**
     * 查询任务列表
     *
     * @param job
     * @return
     */
    public List<JobEntity> queryJobList(JobEntity job);

    /**
     * 获取运行中的任务列表
     *
     * @return
     */
    public List<JobEntity> queryExecutingJobList();
}
