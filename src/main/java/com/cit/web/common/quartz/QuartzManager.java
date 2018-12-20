package com.cit.web.common.quartz;

import com.cit.web.common.entity.ScheduleJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class QuartzManager
{
    private static final Logger logger = LoggerFactory.getLogger(QuartzManager.class);
    @Autowired
    private Scheduler scheduler;


    /**
     * 添加任务
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void addJob(ScheduleJob scheduleJob) throws SchedulerException
    {
        logger.debug("addJob: {}", scheduleJob);
        try
        {
            Class<? extends Job> jobClass = (Class<? extends Job>) (Class.forName(scheduleJob.getJobClass()).newInstance().getClass());
            // 任务名，任务组，任务执行类
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(scheduleJob.getName(), scheduleJob.getGroup())// 任务名称和组构成任务key
                    .build();
            // 定义调度触发规则
            // 使用cornTrigger规则
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getName(), scheduleJob.getGroup())// 触发器key
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())).startNow().build();
            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(jobDetail, trigger);
            // 启动
            if (!scheduler.isShutdown())
            {
                scheduler.start();
            }
        } catch (ClassNotFoundException e)
        {
            throw new SchedulerException("ClassNotFoundException");
        } catch (InstantiationException e)
        {
            throw new SchedulerException("InstantiationException");
        } catch (IllegalAccessException e)
        {
            throw new SchedulerException("IllegalAccessException");
        } catch (SchedulerException e)
        {
            throw e;
        }
    }


    /**
     * 删除一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void removeJob(ScheduleJob scheduleJob) throws SchedulerException
    {
        logger.debug("removeJob: {}", scheduleJob);
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getName(), scheduleJob.getGroup());
        scheduler.pauseTrigger(triggerKey);// 停止触发器
        scheduler.unscheduleJob(triggerKey);// 移除触发器
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());// 删除任务
        scheduler.deleteJob(jobKey);
    }

    /**
     * 更新job时间表达式
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void updateJobCron(ScheduleJob scheduleJob) throws SchedulerException
    {
        logger.debug("updateJobCron: {}", scheduleJob);
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getName(), scheduleJob.getGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleJob> getAllJobs() throws SchedulerException
    {
        logger.debug("getAllJobs:");
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<ScheduleJob> jobs = new ArrayList<ScheduleJob>();
        for (JobKey jobKey : jobKeys)
        {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers)
            {
                ScheduleJob job = new ScheduleJob();
                job.setName(jobKey.getName());
                job.setGroup(jobKey.getGroup());
                job.setDescription(trigger.getKey().toString());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setStatus(triggerState.name());
                if (trigger instanceof CronTrigger)
                {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobs.add(job);
            }
        }
        return jobs;
    }

    /**
     * 所有正在运行的job
     *
     * @return
     * @throws SchedulerException
     */
    public List<ScheduleJob> getRunningJobs() throws SchedulerException
    {
        logger.debug("getRunningJobs:");
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJob> jobs = new ArrayList<ScheduleJob>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs)
        {
            ScheduleJob job = new ScheduleJob();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setName(jobKey.getName());
            job.setGroup(jobKey.getGroup());
            job.setDescription(trigger.getKey().toString());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.setStatus(triggerState.name());
            if (trigger instanceof CronTrigger)
            {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobs.add(job);
        }
        return jobs;
    }

    /**
     * 立即执行job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void runJob(ScheduleJob scheduleJob) throws SchedulerException
    {
        logger.debug("runJob: {}", scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 暂停一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void pauseJob(ScheduleJob scheduleJob) throws SchedulerException
    {
        logger.debug("pauseJob: {}", scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param scheduleJob
     * @throws SchedulerException
     */
    public void resumeJob(ScheduleJob scheduleJob) throws SchedulerException
    {
        logger.debug("resumeJob: {}", scheduleJob);
        JobKey jobKey = JobKey.jobKey(scheduleJob.getName(), scheduleJob.getGroup());
        scheduler.resumeJob(jobKey);
    }
}
