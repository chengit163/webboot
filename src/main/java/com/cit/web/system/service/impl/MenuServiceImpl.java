package com.cit.web.system.service.impl;

import com.cit.web.system.dao.MenuDao;
import com.cit.web.system.entity.Menu;
import com.cit.web.system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl implements MenuService
{

    @Autowired
    private MenuDao menuDao;

    @Override
    public int count(Map<String, Object> map)
    {
        return menuDao.count(map);
    }

    @Override
    public List<Menu> list(Map<String, Object> map)
    {
        return menuDao.list(map);
    }

    @Override
    public Menu get(Integer id)
    {
        return menuDao.get(id);
    }

    @Override
    public int save(Menu menu)
    {
        return menuDao.save(menu);
    }

    @Override
    public int update(Menu menu)
    {
        return menuDao.update(menu);
    }

    @Override
    public int remove(Integer id)
    {
        return menuDao.remove(id);
    }

    @Override
    public int batchRemove(Integer[] ids)
    {
        return menuDao.batchRemove(ids);
    }

    @Override
    public String getNameById(Integer id)
    {
        return menuDao.getNameById(id);
    }
}