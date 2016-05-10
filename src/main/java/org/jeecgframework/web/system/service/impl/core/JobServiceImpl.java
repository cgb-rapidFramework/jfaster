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
                JobUtils.createJob(scheduler, job);
                if(job.getStatus().equalsIgnoreCase("1")) {
                    pauseJob(job.getId());
                }
            } else {
                //已存在，先删除，再创建
                JobUtils.deleteJob(scheduler, job.getName(), job.getGroup());
                JobUtils.createJob(scheduler, job);
                if(job.getStatus().equalsIgnoreCase("1")) {
                    pauseJob(job.getId());
                }
            }
        }
    }

    public String addJob(JobEntity job) throws Exception {
        JobUtils.createJob(scheduler, job);
        return  (String) this.save(job);
    }

    public void updateJob(JobEntity job) throws Exception {
        //先删除
        JobUtils.deleteJob(scheduler, job.getName(), job.getGroup());
        //再创建
        JobUtils.createJob(scheduler, job);
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

    public JobEntity getJob(String jobId) {
        JobEntity job = this.findEntity(JobEntity.class, jobId);
        return job;
    }

    public DataGridReturn findDataGridReturn(final CriteriaQuery cq, final boolean isOffset) {
        Criteria criteria = cq.getDetachedCriteria().getExecutableCriteria(
                getSession());
        CriteriaImpl impl = (CriteriaImpl) criteria;
        // 先把Projection和OrderBy条件取出来,清空两者来执行Count操作
        Projection projection = impl.getProjection();
        final int allCounts = ((Long) criteria.setProjection(
                Projections.rowCount()).uniqueResult()).intValue();
        criteria.setProjection(projection);
        if (projection == null) {
            criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        }
        if (StringUtils.isNotBlank(cq.getDataGrid().getSort())) {
            cq.addOrder(cq.getDataGrid().getSort(), cq.getDataGrid().getOrder());
        }

        // 判断是否有排序字段
        if (!cq.getOrdermap().isEmpty()) {
            cq.setOrder(cq.getOrdermap());
        }
        int pageSize = cq.getPageSize();// 每页显示数
        int curPageNO = PagerUtil.getcurPageNo(allCounts, cq.getCurPage(),
                pageSize);// 当前页
        int offset = PagerUtil.getOffset(allCounts, curPageNO, pageSize);
        if (isOffset) {// 是否分页
            criteria.setFirstResult(offset);
            criteria.setMaxResults(cq.getPageSize());
        } else {
            pageSize = allCounts;
        }
        // DetachedCriteriaUtil.selectColumn(cq.getDetachedCriteria(),
        // cq.getField().split(","), cq.getClass1(), false);
        List<JobEntity> list = criteria.list();
        try {
            for (JobEntity jobEntity : list) {
                JobKey jobKey = JobUtils.getJobKey(jobEntity.getName(), jobEntity.getGroup());
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (CollectionUtils.isEmpty(triggers)) {
                    continue;
                }
                //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
                Trigger trigger = triggers.iterator().next();
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                jobEntity.setRunStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    jobEntity.setExpression(cronExpression);
                }
            }
        } catch (SchedulerException e) {

        }
        cq.getDataGrid().setResults(list);
        cq.getDataGrid().setTotal(allCounts);
        return new DataGridReturn(allCounts, list);
    }

    public List<JobEntity> queryJobList(JobEntity job) {
        List<JobEntity> jobList = this.findAll(JobEntity.class);
        try {
            for (JobEntity jobEntity : jobList) {
                JobKey jobKey = JobUtils.getJobKey(jobEntity.getName(), jobEntity.getGroup());
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                if (CollectionUtils.isEmpty(triggers)) {
                    continue;
                }
                //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
                Trigger trigger = triggers.iterator().next();
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                jobEntity.setStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    jobEntity.setExpression(cronExpression);
                }
            }
        } catch (SchedulerException e) {

        }
        return jobList;
    }

    public List<JobEntity> queryExecutingJobList() {
        try {
            List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
            List<JobEntity> jobList = new ArrayList<JobEntity>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                JobEntity job = new JobEntity();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                job.setName(jobKey.getName());
                job.setGroup(jobKey.getGroup());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setExpression(cronExpression);
                }
                jobList.add(job);
            }
            return jobList;
        } catch (SchedulerException e) {

            return null;
        }

    }
}