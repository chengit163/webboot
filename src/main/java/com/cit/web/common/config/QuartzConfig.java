package com.cit.web.common.config;

import com.cit.web.common.quartz.JobFactory;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig
{
    @Autowired
    JobFactory jobFactory;


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean()
    {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        try
        {
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            schedulerFactoryBean.setQuartzProperties(quartzProperties());
            schedulerFactoryBean.setJobFactory(jobFactory);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return schedulerFactoryBean;
    }

    // 指定quartz.properties
    @Bean
    public Properties quartzProperties() throws IOException
    {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/config/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    // 创建schedule
    @Bean(name = "scheduler")
    public Scheduler scheduler()
    {
        return schedulerFactoryBean().getScheduler();
    }
}
