package com.cit.web.system.service.impl;

import com.cit.web.system.dao.RabcDao;
import com.cit.web.system.entity.Rabc;
import com.cit.web.system.service.RabcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class RabcServiceImpl implements RabcService
{
    @Autowired
    private RabcDao rabcDao;

    @Override
    public Set<String> listPermissionByUserId(Integer userId)
    {
        return rabcDao.listPermissionByUserId(userId);
    }

    @Override
    public List<Rabc> listMenuByUserId(Integer userId)
    {
        return rabcDao.listMenuByUserId(userId);
    }


    @Override
    public List<Rabc> getMenuOptions()
    {
        return rabcDao.getMenuOptions();
    }

    @Override
    public Set<Integer> getMenuIdsByRoleId(Integer roleId)
    {
        return rabcDao.getMenuIdsByRoleId(roleId);
    }

    @Transactional
    @Override
    public int saveRoleMenu(Integer roleId, Integer[] menuIds)
    {
        rabcDao.removeMenuIdsByRoleId(roleId);
        return rabcDao.saveMenuIdsByRoleId(roleId, menuIds);
    }


    @Override
    public List<Rabc> getRoleOptions()
    {
        return rabcDao.getRoleOptions();
    }

    @Override
    public Set<Integer> getRoleIdsByUserId(Integer userId)
    {
        return rabcDao.getRoleIdsByUserId(userId);
    }


    @Transactional
    @Override
    public int saveUserRole(Integer userId, Integer[] roleIds)
    {
        rabcDao.removeRoleIdsByUserId(userId);
        return rabcDao.saveRoleIdsByUserId(userId, roleIds);
    }
}
