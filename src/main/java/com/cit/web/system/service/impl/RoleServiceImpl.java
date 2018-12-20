package com.cit.web.system.service.impl;

import com.cit.web.system.dao.RoleDao;
import com.cit.web.system.entity.Role;
import com.cit.web.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService
{

    @Autowired
    private RoleDao roleDao;

    @Override
    public int count(Map<String, Object> map)
    {
        return roleDao.count(map);
    }

    @Override
    public List< Role> list(Map<String, Object> map)
    {
        return roleDao.list(map);
    }

    @Override
    public  Role get(Integer id)
    {
        return roleDao.get(id);
    }

    @Override
    public int save(Role role)
    {
        return roleDao.save(role);
    }

    @Override
    public int update(Role role)
    {
        return roleDao.update(role);
    }

    @Override
    public int remove(Integer id)
    {
        return roleDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids)
    {
        return roleDao.batchRemove(ids);
    }

    @Override
    public String getNameById(Integer id)
    {
        return roleDao.getNameById(id);
    }
}