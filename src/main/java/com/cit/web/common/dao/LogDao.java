package com.cit.web.common.dao;


import com.cit.web.common.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LogDao
{
    int count(Map<String, Object> map);

    List<Log> list(Map<String, Object> map);

    Log get(Long id);

    int save(Log log);

    int update(Log log);

    int remove(Long id);

    int batchRemove(Long[] ids);

}

