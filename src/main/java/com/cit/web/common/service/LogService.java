package com.cit.web.common.service;

import com.cit.web.common.entity.Log;

import java.util.List;
import java.util.Map;

public interface LogService
{

    int count(Map<String, Object> map);

    List<Log> list(Map<String, Object> map);

    Log get(Long id);

    int save(Log log);

    int update(Log log);

    int remove(Long id);

    int batchRemove(Long[] ids);

}