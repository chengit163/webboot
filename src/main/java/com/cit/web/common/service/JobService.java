package com.cit.web.common.service;

import com.cit.web.common.entity.Job;
import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

public interface JobService
{

    int count(Map<String, Object> map);

    List<Job> list(Map<String, Object> map);

    Job get(Integer id);

    int save(Job job);

    int update(Job job);

    int remove(Integer id);

    int batchRemove(Integer[] ids);


    void initSchedule() throws SchedulerException;

    boolean run(Integer id);
}