package com.cit.web.system.service.impl;

import com.cit.web.system.dao.UserDao;
import com.cit.web.system.entity.User;
import com.cit.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    private UserDao userDao;

    @Override
    public int count(Map<String, Object> map)
    {
        return userDao.count(map);
    }

    @Override
    public List<User> list(Map<String, Object> map)
    {
        return userDao.list(map);
    }

    @Override
    public User get(Integer id)
    {
        return userDao.get(id);
    }

    @Override
    public int save(User user)
    {
        return userDao.save(user);
    }

    @Override
    public int update(User user)
    {
        return userDao.update(user);
    }

    @Override
    public int remove(Integer id)
    {
        return userDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids)
    {
        return userDao.batchRemove(ids);
    }


    @Override
    public User getByUsername(String username)
    {
        return userDao.getByUsername(username);
    }

    @Override
    public String getUsernameById(Integer id)
    {
        return userDao.getUsernameById(id);
    }

    @Override
    public int updatePasswordById(Integer id, String password)
    {
        return userDao.updatePasswordById(id, password);
    }
}