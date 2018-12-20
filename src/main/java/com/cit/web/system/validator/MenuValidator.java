package com.cit.web.system.validator;

import com.cit.web.system.entity.Menu;
import org.apache.commons.lang.StringUtils;

/**
 * 菜单验证器
 */
public class MenuValidator
{
    /**
     * 验证: 菜单
     * @param menu
     * @param isNullable (false: save; true: update)
     * @return
     */
    public static String validate(Menu menu, boolean isNullable)
    {
        if (isNullable)
        {
            // 更新时需要判断主键
            if (menu.getId() == null)
            {
                return "菜单ID(主键)不能为空";
            }
        }
        //其他字段
        String message = null;
        String name = StringUtils.trim(menu.getName());
        if ((message = validateName(name, isNullable)) != null)
        {
            return message;
        }
        menu.setName(name);
        String url = StringUtils.trim(menu.getUrl());
        if ((message = validateUrl(url, isNullable)) != null)
        {
            return message;
        }
        menu.setUrl(url);
        String permission = StringUtils.trim(menu.getPermission());
        if ((message = validatePermission(permission, isNullable)) != null)
        {
            return message;
        }
        menu.setPermission(permission);
        String icon = StringUtils.trim(menu.getIcon());
        if ((message = validateIcon(icon, isNullable)) != null)
        {
            return message;
        }
        menu.setIcon(icon);
        return message;
    }

    /**
     * 验证: 名称
     * @param name
     * @return
     */
    public static String validateName(String name, boolean isNullable)
    {
        if (isNullable && name == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(name))
        {
            // 不允许该字段为空
            return "名称不能为空";
        }
        if (StringUtils.length(name) < 1 || StringUtils.length(name) > 50)
        {
            return "名称长度必须在1~50之间";
        }
        return null;
    }

    /**
     * 验证: 路径
     * @param url
     * @return
     */
    public static String validateUrl(String url, boolean isNullable)
    {
        if (isNullable && url == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(url))
        {
            // 允许该字段为空
            return null;
        }
        if (StringUtils.length(url) < 0 || StringUtils.length(url) > 100)
        {
            return "路径长度必须在0~100之间";
        }
        return null;
    }

    /**
     * 验证: 权限
     * @param permission
     * @return
     */
    public static String validatePermission(String permission, boolean isNullable)
    {
        if (isNullable && permission == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(permission))
        {
            // 允许该字段为空
            return null;
        }
        if (StringUtils.length(permission) < 0 || StringUtils.length(permission) > 100)
        {
            return "权限长度必须在0~100之间";
        }
        return null;
    }

    /**
     * 验证: 图标
     * @param icon
     * @return
     */
    public static String validateIcon(String icon, boolean isNullable)
    {
        if (isNullable && icon == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(icon))
        {
            // 允许该字段为空
            return null;
        }
        if (StringUtils.length(icon) < 0 || StringUtils.length(icon) > 100)
        {
            return "图标长度必须在0~100之间";
        }
        return null;
    }

}