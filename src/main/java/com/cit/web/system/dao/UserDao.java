package com.cit.web.system.dao;


import com.cit.web.system.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDao
{
    int count(Map<String, Object> map);

    List<User> list(Map<String, Object> map);

    User get(Integer id);

    int save(User user);

    int update(User user);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    User getByUsername(String username);

    String getUsernameById(Integer id);

    int updatePasswordById(@Param("id") Integer id, @Param("password") String password);

}

