package com.cit.web.common.jobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobUtils
{
    private static List<Map<String, String>> jobs = new ArrayList<Map<String, String>>();

    static
    {
        jobs.add(createExecuteJob("欢迎任务", WelcomeJob.class));
    }

    public static Map<String, String> createExecuteJob(String name, Class<? extends org.quartz.Job> clazz)
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("executeName", name);
        map.put("executeClass", clazz.getName());
        return map;
    }

    public static List<Map<String, String>> getJobs()
    {
        return jobs;
    }
}
