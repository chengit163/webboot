package com.cit.web.common.listenner;


import com.cit.web.common.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ScheduleJobInitListener implements CommandLineRunner
{

    @Autowired
    private JobService jobService;

    @Override
    public void run(String... strings) throws Exception
    {
        try
        {
            jobService.initSchedule();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}