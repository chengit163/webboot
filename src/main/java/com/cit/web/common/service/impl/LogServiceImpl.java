package com.cit.web.common.service.impl;

import com.cit.web.common.dao.LogDao;
import com.cit.web.common.entity.Log;
import com.cit.web.common.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogServiceImpl implements LogService
{

    @Autowired
    private LogDao logDao;

    @Override
    public int count(Map<String, Object> map)
    {
        return logDao.count(map);
    }

    @Override
    public List< Log> list(Map<String, Object> map)
    {
        return logDao.list(map);
    }

    @Override
    public  Log get(Long id)
    {
        return logDao.get(id);
    }

    @Override
    public int save(Log log)
    {
        return logDao.save(log);
    }

    @Override
    public int update(Log log)
    {
        return logDao.update(log);
    }

    @Override
    public int remove(Long id)
    {
        return logDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids)
    {
        return logDao.batchRemove(ids);
    }

}