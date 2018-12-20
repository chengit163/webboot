package com.cit.web.system.validator;

import com.cit.web.system.entity.User;
import org.apache.commons.lang.StringUtils;
import java.util.regex.Pattern;

/**
 * 用户验证器
 */
public class UserValidator
{
    /**
     * 验证: 用户
     * @param user
     * @param isNullable (false: save; true: update)
     * @return
     */
    public static String validate(User user, boolean isNullable)
    {
        if (isNullable)
        {
            // 更新时需要判断主键
            if (user.getId() == null)
            {
                return "用户ID(主键)不能为空";
            }
        }
        //其他字段
        String message = null;
        String username = StringUtils.trim(user.getUsername());
        if ((message = validateUsername(username, isNullable)) != null)
        {
            return message;
        }
        user.setUsername(username);
        String password = StringUtils.trim(user.getPassword());
        if ((message = validatePassword(password, isNullable)) != null)
        {
            return message;
        }
        user.setPassword(password);
        String nickname = StringUtils.trim(user.getNickname());
        if ((message = validateNickname(nickname, isNullable)) != null)
        {
            return message;
        }
        user.setNickname(nickname);
        String phone = StringUtils.trim(user.getPhone());
        if ((message = validatePhone(phone, isNullable)) != null)
        {
            return message;
        }
        user.setPhone(phone);
        String email = StringUtils.trim(user.getEmail());
        if ((message = validateEmail(email, isNullable)) != null)
        {
            return message;
        }
        user.setEmail(email);
        String lastLoginIp = StringUtils.trim(user.getLastLoginIp());
        if ((message = validateLastLoginIp(lastLoginIp, isNullable)) != null)
        {
            return message;
        }
        user.setLastLoginIp(lastLoginIp);
        return message;
    }

    /**
     * 验证: 用户名
     * @param username
     * @return
     */
    public static String validateUsername(String username, boolean isNullable)
    {
        if (isNullable && username == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(username))
        {
            // 不允许该字段为空
            return "用户名不能为空";
        }
        if (!Pattern.matches("^[a-zA-Z][a-zA-Z0-9_-]{1,17}$", username))
        {
            return "用户名必须以字母开头，长度在2~18之间，只能包含字母、数字、下划线、减号";
        }
        return null;
    }

    /**
     * 验证: 密码
     * @param password
     * @return
     */
    public static String validatePassword(String password, boolean isNullable)
    {
        if (isNullable && password == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(password))
        {
            // 不允许该字段为空
            return "密码不能为空";
        }
        if (!Pattern.matches("^[a-zA-Z]\\w{5,17}$", password))
        {
            return "密码必须以字母开头，长度在6~18之间，只能包含字符、数字、下划线";
        }
        return null;
    }

    /**
     * 验证: 昵称
     * @param nickname
     * @return
     */
    public static String validateNickname(String nickname, boolean isNullable)
    {
        if (isNullable && nickname == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(nickname))
        {
            // 允许该字段为空
            return null;
        }
        if (StringUtils.length(nickname) < 0 || StringUtils.length(nickname) > 50)
        {
            return "昵称长度必须在0~50之间";
        }
        return null;
    }

    /**
     * 验证: 手机号
     * @param phone
     * @return
     */
    public static String validatePhone(String phone, boolean isNullable)
    {
        if (isNullable && phone == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(phone))
        {
            // 允许该字段为空
            return null;
        }
        if (!Pattern.matches("^1[3|4|5|7|8]\\d{9}$", phone))
        {
            return "手机号格式不合法";
        }
        return null;
    }

    /**
     * 验证: 邮箱
     * @param email
     * @return
     */
    public static String validateEmail(String email, boolean isNullable)
    {
        if (isNullable && email == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(email))
        {
            // 允许该字段为空
            return null;
        }
        if (StringUtils.length(email) < 0 || StringUtils.length(email) > 150)
        {
            return "邮箱长度必须在0~150之间";
        }
        if (!Pattern.matches("^[A-Za-z0-9._%-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$", email))
        {
            return "邮箱格式不合法";
        }
        return null;
    }

    /**
     * 验证: 最近登陆IP
     * @param lastLoginIp
     * @return
     */
    public static String validateLastLoginIp(String lastLoginIp, boolean isNullable)
    {
        if (isNullable && lastLoginIp == null)
        {
            // 更新时允许字段为null
            return null;
        }
        if (StringUtils.isEmpty(lastLoginIp))
        {
            // 允许该字段为空
            return null;
        }
        return null;
    }

}