package com.cit.web.system.dao;


import com.cit.web.system.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RoleDao
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

