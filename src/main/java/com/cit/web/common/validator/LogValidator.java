package com.cit.web.common.validator;

import com.cit.web.common.entity.Log;
import org.apache.commons.lang.StringUtils;

/**
 * 日志验证器
 */
public class LogValidator
{
    /**
     * 验证: 日志
     * @param log
     * @param isNullable (false: save; true: update)
     * @return
     */
    public static String validate(Log log, boolean isNullable)
    {
        if (isNullable)
        {
            // 更新时需要判断主键
            if (log.getId() == null)
            {
                return "日志ID(主键)不能为空";
            }
        }
        //其他字段
        String message = null;
        String username = StringUtils.trim(log.getUsername());
        if ((message = validateUsername(username, isNullable)) != null)
        {
            return message;
        }
        log.setUsername(username);
        String ip = StringUtils.trim(log.getIp());
        if ((message = validateIp(ip, isNullable)) != null)
        {
            return message;
        }
        log.setIp(ip);
        String action = StringUtils.trim(log.getAction());
        if ((message = validateAction(action, isNullable)) != null)
        {
            return message;
        }
        log.setAction(action);
        String url = StringUtils.trim(log.getUrl());
        if ((message = validateUrl(url, isNullable)) != null)
        {
            return message;
        }
        log.setUrl(url);
        String args = StringUtils.trim(log.getArgs());
        if ((message = validateArgs(args, isNullable)) != null)
        {
            return message;
        }
        log.setArgs(args);
        String func = StringUtils.trim(log.getFunc());
        if ((message = validateFunc(func, isNullable)) != null)
        {
            return message;
        }
        log.setFunc(func);
        String os = StringUtils.trim(log.getOs());
        if ((message = validateOs(os, isNullable)) != null)
        {
            return message;
        }
        log.setOs(os);
        String browser = StringUtils.trim(log.getBrowser());
        if ((message = validateBrowser(browser, isNullable)) != null)
        {
            return message;
        }
        log.setBrowser(browser);
        return message;
    }

    /**
     * 验证: 用户名
     * @param username
     * @return
     */
    public static String validateUsername(String username, boolean isNullable)
    {
        if (isNullable && username == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(username))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

    /**
     * 验证: IP地址
     * @param ip
     * @return
     */
    public static String validateIp(String ip, boolean isNullable)
    {
        if (isNullable && ip == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(ip))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

    /**
     * 验证: 动作
     * @param action
     * @return
     */
    public static String validateAction(String action, boolean isNullable)
    {
        if (isNullable && action == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(action))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

    /**
     * 验证: 请求URL
     * @param url
     * @return
     */
    public static String validateUrl(String url, boolean isNullable)
    {
        if (isNullable && url == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(url))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

    /**
     * 验证: 参数
     * @param args
     * @return
     */
    public static String validateArgs(String args, boolean isNullable)
    {
        if (isNullable && args == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(args))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

    /**
     * 验证: 函数
     * @param func
     * @return
     */
    public static String validateFunc(String func, boolean isNullable)
    {
        if (isNullable && func == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(func))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

    /**
     * 验证: 系统
     * @param os
     * @return
     */
    public static String validateOs(String os, boolean isNullable)
    {
        if (isNullable && os == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(os))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

    /**
     * 验证: 浏览器
     * @param browser
     * @return
     */
    public static String validateBrowser(String browser, boolean isNullable)
    {
        if (isNullable && browser == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(browser))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

}