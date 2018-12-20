package com.cit.web.system.service;

import com.cit.web.system.entity.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService
{

    int count(Map<String, Object> map);

    List<Menu> list(Map<String, Object> map);

    Menu get(Integer id);

    int save(Menu menu);

    int update(Menu menu);

    int remove(Integer id);

    int batchRemove(Integer[] ids);

    String getNameById(Integer id);
}