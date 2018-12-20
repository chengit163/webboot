package com.cit.web.system.validator;

import com.cit.web.system.entity.Role;
import org.apache.commons.lang.StringUtils;

/**
 * 角色验证器
 */
public class RoleValidator
{
    /**
     * 验证: 角色
     * @param role
     * @param isNullable (false: save; true: update)
     * @return
     */
    public static String validate(Role role, boolean isNullable)
    {
        if (isNullable)
        {
            // 更新时需要判断主键
            if (role.getId() == null)
            {
                return "角色ID(主键)不能为空";
            }
        }
        //其他字段
        String message = null;
        String name = StringUtils.trim(role.getName());
        if ((message = validateName(name, isNullable)) != null)
        {
            return message;
        }
        role.setName(name);
        String description = StringUtils.trim(role.getDescription());
        if ((message = validateDescription(description, isNullable)) != null)
        {
            return message;
        }
        role.setDescription(description);
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
     * 验证: 描述
     * @param description
     * @return
     */
    public static String validateDescription(String description, boolean isNullable)
    {
        if (isNullable && description == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(description))
        {
            // 允许该字段为空
            return null;
        }
        if (StringUtils.length(description) < 0 || StringUtils.length(description) > 250)
        {
            return "描述长度必须在0~250之间";
        }
        return null;
    }

}