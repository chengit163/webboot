package com.cit.web.system.dao;

import com.cit.web.system.entity.Rabc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface RabcDao
{
    Set<String> listPermissionByUserId(Integer userId);

    List<Rabc> listMenuByUserId(Integer userId);

    List<Rabc> getMenuOptions();

    Set<Integer> getMenuIdsByRoleId(Integer roleId);

    int removeMenuIdsByRoleId(Integer roleId);

    int saveMenuIdsByRoleId(@Param("roleId") Integer roleId, @Param("menuIds") Integer[] menuIds);


    List<Rabc> getRoleOptions();

    Set<Integer> getRoleIdsByUserId(Integer userId);

    int removeRoleIdsByUserId(Integer userId);

    int saveRoleIdsByUserId(@Param("userId") Integer userId, @Param("roleIds") Integer[] roleIds);
}
