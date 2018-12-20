package com.cit.web.common.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PageUtils
{

    private static final String PAGE_NUM = "pageNum";

    private static final String PAGE_SIZE = "pageSize";

    private static final String TOTAL = "total";

    private static final String ROWS = "rows";

    private static final String SORT = "sort";

    private static final String ORDER = "order";


    public static final Pattern PATTERN = Pattern.compile("^[0-9]+$");
    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE_NUMBER = 1;
    /**
     * 默认一页的大小
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    private static int getValue(Object obj, int defaultValue)
    {
        if (obj instanceof Integer)
            return (Integer) obj;
        else if (obj instanceof String)
            if (PATTERN.matcher((String) obj).matches())
                return Integer.parseInt((String) obj);
        return defaultValue;
    }

    public static int getPageNum(Map<String, Object> map)
    {
        if (map == null)
            return DEFAULT_PAGE_NUMBER;
        return getValue(map.get(PAGE_NUM), DEFAULT_PAGE_NUMBER);
    }

    public static int getPageSize(Map<String, Object> map)
    {
        if (map == null)
            return DEFAULT_PAGE_SIZE;
        return getValue(map.get(PAGE_SIZE), DEFAULT_PAGE_SIZE);
    }

    public static Map<String, Object> getResult(long total, Collection<?> list)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(TOTAL, total);
        result.put(ROWS, list);
        return result;
    }

    public static void cleanSort(Map<String, Object> map, Class<?> clazz)
    {
        Object sort = map.get(SORT);
        if (sort != null)
        {
            boolean flag = false;
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields)
            {
                if (field.getName().equals(sort.toString()))
                {
                    map.put(SORT, CamelNameUtils.camel2underscore(field.getName()));
                    flag = true;
                    break;
                }
            }
            if (!flag)
                map.remove(SORT);
        }
    }

    public static void cleanOrder(Map<String, Object> map)
    {
        Object order = map.get(ORDER);
        if (order != null)
        {
            if (!StringUtils.equalsIgnoreCase("asc", order.toString()) &&
                    !StringUtils.equalsIgnoreCase("desc", order.toString()))
            {
                map.remove(ORDER);
            }
        }
    }

    public static Map<String, Object> cleanSortOrder(String sort, String order, Class<?> clazz)
    {
        Map<String, Object> map = null;
        if (StringUtils.isNotEmpty(sort) || StringUtils.isNotEmpty(order))
        {
            map = new HashMap<String, Object>();
            // sort
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields)
            {
                if (field.getName().equals(sort.toString()))
                {
                    map.put(SORT, CamelNameUtils.camel2underscore(field.getName()));
                    map.put(SORT, sort);
                    break;
                }
            }
            //
            if (StringUtils.equalsIgnoreCase("asc", order.toString()) ||
                    StringUtils.equalsIgnoreCase("desc", order.toString()))
            {
                map.put(ORDER, order);
            }
        }
        return map;
    }

}
