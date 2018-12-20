package com.cit.web.common.service.impl;

import com.cit.web.common.dao.JobDao;
import com.cit.web.common.entity.Job;
import com.cit.web.common.entity.ScheduleJob;
import com.cit.web.common.quartz.QuartzManager;
import com.cit.web.common.service.JobService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService
{

    @Autowired
    private JobDao jobDao;

    @Autowired
    private QuartzManager quartzManager;

    @Override
    public int count(Map<String, Object> map)
    {
        return jobDao.count(map);
    }

    @Override
    public List<Job> list(Map<String, Object> map)
    {
        return jobDao.list(map);
    }

    @Override
    public Job get(Integer id)
    {
        return jobDao.get(id);
    }

    @Override
    public int save(Job job)
    {
        int n = jobDao.save(job);
        if (job.getStatus() == ScheduleJob.STATUS_RUNNING)
        {
            addJob(job);
        }
        return n;
    }

    @Override
    public int update(Job job)
    {
        Job old = jobDao.get(job.getId());
        int n = jobDao.update(job);
        job.setJobClass(old.getJobClass());
        if (job.getCronExpression() == null)
            job.setCronExpression(old.getCronExpression());
        if (job.getStatus() == null)
            job.setStatus(old.getStatus());
        if (old.getStatus() == ScheduleJob.STATUS_RUNNING)
        {
            // 已经是开启的任务，判断cron表达式
            if (job.getStatus() == ScheduleJob.STATUS_RUNNING)
            {
                if (!StringUtils.equals(old.getCronExpression(), job.getCronExpression()))
                {
                    updateJob(job);
                }
            } else
            {
                removeJob(job);
            }
        } else
        {
            // 不是开启的任务,判断是否开启
            if (job.getStatus() == ScheduleJob.STATUS_RUNNING)
            {
                addJob(job);
            }
        }
        return n;
    }

    @Override
    public int remove(Integer id)
    {
        Job job = jobDao.get(id);
        if (job.getStatus() == ScheduleJob.STATUS_RUNNING)
        {
            removeJob(job);
        }
        return jobDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids)
    {
        for (int id : ids)
        {
            Job job = jobDao.get(id);
            if (job.getStatus() == ScheduleJob.STATUS_RUNNING)
            {
                removeJob(job);
            }
        }
        return jobDao.batchRemove(ids);
    }


    @Override
    public void initSchedule() throws SchedulerException
    {
        // 查询开启状态的调度任务
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", String.valueOf(ScheduleJob.STATUS_RUNNING));
        List<Job> jobs = jobDao.list(map);
        if (jobs != null && !jobs.isEmpty())
        {
            for (Job job : jobs)
            {
                ScheduleJob scheduleJob = getScheduleJob(job);
                quartzManager.addJob(scheduleJob);
            }
        }
    }

    @Override
    public boolean run(Integer id)
    {
        Job job = jobDao.get(id);
        if (job.getStatus() == ScheduleJob.STATUS_RUNNING)
        {
            try
            {
                ScheduleJob scheduleJob = getScheduleJob(job);
                quartzManager.runJob(scheduleJob);
                return true;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }


    private void addJob(Job job)
    {
        try
        {
            ScheduleJob scheduleJob = getScheduleJob(job);
            quartzManager.addJob(scheduleJob);
        } catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    private void removeJob(Job job)
    {
        try
        {
            ScheduleJob scheduleJob = getScheduleJob(job);
            quartzManager.removeJob(scheduleJob);
        } catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    private void updateJob(Job job)
    {
        try
        {
            ScheduleJob scheduleJob = getScheduleJob(job);
            quartzManager.updateJobCron(scheduleJob);
        } catch (SchedulerException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * com.cit.web.common.entity.Job to com.cit.web.common.entity.ScheduleJob
     *
     * @param job
     * @return
     */
    private ScheduleJob getScheduleJob(Job job)
    {
        ScheduleJob scheduleJob = new ScheduleJob.Builder()
                .setName(job.getId())
                .setGroup(job.getJobClass().hashCode())
                .setJobClass(job.getJobClass())
                .setCronExpression(job.getCronExpression())
                .build();
        return scheduleJob;
    }
}