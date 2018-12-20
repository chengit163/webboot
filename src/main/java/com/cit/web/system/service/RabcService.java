package com.cit.web.system.service;

import com.cit.web.system.entity.Rabc;

import java.util.List;
import java.util.Set;

public interface RabcService
{
    Set<String> listPermissionByUserId(Integer userId);

    List<Rabc> listMenuByUserId(Integer userId);


    List<Rabc> getMenuOptions();

    Set<Integer> getMenuIdsByRoleId(Integer roleId);

    int saveRoleMenu(Integer roleId, Integer[] menuIds);


    List<Rabc> getRoleOptions();

    Set<Integer> getRoleIdsByUserId(Integer userId);

    int saveUserRole(Integer userId, Integer[] roleIds);
}
