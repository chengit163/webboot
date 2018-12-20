package com.cit.web.common.validator;

import com.cit.web.common.entity.Job;
import org.apache.commons.lang.StringUtils;

/**
 * 任务验证器
 */
public class JobValidator
{
    /**
     * 验证: 任务
     * @param job
     * @param isNullable (false: save; true: update)
     * @return
     */
    public static String validate(Job job, boolean isNullable)
    {
        if (isNullable)
        {
            // 更新时需要判断主键
            if (job.getId() == null)
            {
                return "主键ID不能为空";
            }
        }
        //其他字段
        String message = null;
        String jobClass = StringUtils.trim(job.getJobClass());
        if ((message = validateJobClass(jobClass, isNullable)) != null)
        {
            return message;
        }
        job.setJobClass(jobClass);
        String cronExpression = StringUtils.trim(job.getCronExpression());
        if ((message = validateCronExpression(cronExpression, isNullable)) != null)
        {
            return message;
        }
        job.setCronExpression(cronExpression);
        String description = StringUtils.trim(job.getDescription());
        if ((message = validateDescription(description, isNullable)) != null)
        {
            return message;
        }
        job.setDescription(description);
        return message;
    }

    /**
     * 验证: 执行类
     * @param jobClass
     * @return
     */
    public static String validateJobClass(String jobClass, boolean isNullable)
    {
        if (isNullable && jobClass == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(jobClass))
        {
            // 不允许该字段为空
            return "执行类不能为空";
        }
        if (StringUtils.length(jobClass) < 0 || StringUtils.length(jobClass) > 100)
        {
            return "执行类长度必须在0~100之间";
        }
        return null;
    }

    /**
     * 验证: cron表达式
     * @param cronExpression
     * @return
     */
    public static String validateCronExpression(String cronExpression, boolean isNullable)
    {
        if (isNullable && cronExpression == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(cronExpression))
        {
            // 不允许该字段为空
            return "cron表达式不能为空";
        }
        if (StringUtils.length(cronExpression) < 0 || StringUtils.length(cronExpression) > 100)
        {
            return "cron表达式长度必须在0~100之间";
        }
        return null;
    }

    /**
     * 验证: 描述
     * @param description
     * @return
     */
    public static String validateDescription(String description, boolean isNullable)
    {
        if (isNullable && description == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(description))
        {
            // 允许该字段为空
            return null;
        }
        if (StringUtils.length(description) < 0 || StringUtils.length(description) > 250)
        {
            return "描述长度必须在0~250之间";
        }
        return null;
    }

}