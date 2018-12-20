package com.cit.web.common.jobs;

import org.quartz.JobExecutionContext;

public class WelcomeJob extends AbstractJob
{
    @Override
    void run(JobExecutionContext context)
    {
        int i = 10;
        while (i-- > 0)
        {
            System.out.println("WelcomeJob: " + i);
            try
            {
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
