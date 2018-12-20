package com.cit.web.common.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScheduleJob implements Serializable
{
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_NOT_RUNNING = 0;

//    public static final String CONCURRENT_IS = "1";
//    public static final String CONCURRENT_NOT = "0";


    private static final String NAME = "NAME_";
    private static final String GROUP = "GROUP_";
    /**
     * 名
     */
    private String name;
    /**
     * 组
     */
    private String group;
    /**
     * 执行类
     */
    private String jobClass;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     */
    private String status;

    public static class Builder
    {

        private String name;
        private String group;
        private String jobClass;
        private String cronExpression;

        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder setName(int name)
        {
            this.name = NAME + name;
            return this;
        }

        public Builder setGroup(String group)
        {
            this.group = group;
            return this;
        }

        public Builder setGroup(int group)
        {
            this.group = GROUP + group;
            return this;
        }

        public Builder setJobClass(String jobClass)
        {
            this.jobClass = jobClass;
            return this;
        }

        public Builder setCronExpression(String cronExpression)
        {
            this.cronExpression = cronExpression;
            return this;
        }

        public ScheduleJob build()
        {
            ScheduleJob scheduleJob = new ScheduleJob();
            scheduleJob.setName(name);
            scheduleJob.setGroup(group);
            scheduleJob.setJobClass(jobClass);
            scheduleJob.setCronExpression(cronExpression);
            return scheduleJob;
        }
    }
}
