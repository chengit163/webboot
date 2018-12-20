package com.cit.web.common.dao;


import com.cit.web.common.entity.Job;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface JobDao
{
    int count(Map<String, Object> map);

    List<Job> list(Map<String, Object> map);

    Job get(Integer id);

    int save(Job job);

    int update(Job job);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

}

