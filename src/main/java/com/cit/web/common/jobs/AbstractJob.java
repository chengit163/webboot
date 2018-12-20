package com.cit.web.common.jobs;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJob implements org.quartz.Job
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException
    {
        logger.info("调度开始");
        long time = System.currentTimeMillis();
        run(context);
        logger.info("调度结束, 耗时 [{}]ms", (System.currentTimeMillis() - time));
    }

    abstract void run(JobExecutionContext context);
}
