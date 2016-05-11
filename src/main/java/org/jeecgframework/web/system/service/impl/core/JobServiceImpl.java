package org.jeecgframework.web.system.service.impl.core;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.hibernate.qbc.PagerUtil;
import org.jeecgframework.core.common.model.json.DataGridReturn;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.web.system.entity.core.JobEntity;
import org.jeecgframework.web.system.service.core.JobService;
import org.jeecgframework.web.utils.JobUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("jobService")
@Transactional
public class JobServiceImpl extends CommonServiceImpl implements JobService {
    /** 调度工厂Bean */
    @Autowired
    private Scheduler scheduler;

    public void initJob() throws Exception {
        List<JobEntity> jobList = this.findAll(JobEntity.class);
        if (CollectionUtils.isEmpty(jobList)) {
            return;
        }
        for (JobEntity job : jobList) {
            CronTrigger cronTrigger = JobUtils.getCronTrigger(scheduler, job.getName(),
                    job.getGroup());
            //不存在，创建一个
            if (cronTrigger == null) {
                JobUtils.createScheduleJob(scheduler, job.getName(),job.getGroup(),job.getExpression(),job.getClazz(),null);
                if(job.getStatus().equalsIgnoreCase("1")) {
                    pauseJob(job.getId());
                }
            } else {
                //已存在，先删除，再创建
                JobUtils.deleteJob(scheduler, job.getName(), job.getGroup());
                JobUtils.createScheduleJob(scheduler, job.getName(),job.getGroup(),job.getExpression(),job.getClazz(),null);
                if(job.getStatus().equalsIgnoreCase("1")) {
                    pauseJob(job.getId());
                }
            }
        }
    }

    public void addJob(JobEntity job) throws Exception {
          JobUtils.createScheduleJob(scheduler, job.getName(),job.getGroup(),job.getExpression(),job.getClazz(),null);
         this.save(job);
    }

    public void updateJob(JobEntity job) throws Exception {
        //先删除
        JobUtils.deleteJob(scheduler, job.getName(), job.getGroup());
        //再创建
        JobUtils.createScheduleJob(scheduler, job.getName(),job.getGroup(),job.getExpression(),job.getClazz(),null);
        //数据库直接更新即可
        this.update(job);
    }

    public void deleteJob(String jobId) throws Exception {
        JobEntity job = this.findEntity(JobEntity.class, jobId);
        //删除运行的任务
        JobUtils.deleteJob(scheduler, job.getName(), job.getGroup());
        //删除数据
        this.delete(JobEntity.class, jobId);
    }

    public void runOnceJob(String jobId) throws Exception {
        JobEntity job = this.findEntity(JobEntity.class, jobId);
        JobUtils.runOnce(scheduler, job.getName(), job.getGroup());
    }

    public void pauseJob(String jobId) throws Exception {
        JobEntity job = this.findEntity(JobEntity.class, jobId);
        job.setStatus("1");
        JobUtils.pauseJob(scheduler, job.getName(), job.getGroup());
        this.update(job);
    }

    public void resumeJob(String jobId) throws Exception {
        JobEntity job = this.findEntity(JobEntity.class, jobId);
        job.setStatus("0");
        JobUtils.resumeJob(scheduler, job.getName(), job.getGroup());
        this.update(job);
    }
}