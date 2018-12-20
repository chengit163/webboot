package com.cit.web.system.service;

import com.cit.web.system.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService
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

    int updatePasswordById(Integer id, String password);

}