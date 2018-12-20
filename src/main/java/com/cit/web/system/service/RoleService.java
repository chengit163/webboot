package com.cit.web.system.service;

import com.cit.web.system.entity.Role;

import java.util.List;
import java.util.Map;

public interface RoleService
{

    int count(Map<String, Object> map);

    List<Role> list(Map<String, Object> map);

    Role get(Integer id);

    int save(Role role);

    int update(Role role);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    String getNameById(Integer id);
}