package com.cit.web.common.util;

import com.cit.web.system.entity.User;
import com.cit.web.system.shiro.UserRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ShiroUtils
{

    private static final String CAPTCHA = "captcha";

    private static final String OS = "os";

    private static final String BROWSER = "browser";

    public static Subject getSubjct()
    {
        return SecurityUtils.getSubject();
    }

    /**
     * 保存验证码
     *
     * @param code
     */
    public static void putCaptcha(String code)
    {
        Session session = getSubjct().getSession();
        session.setAttribute(CAPTCHA, code);
    }

    /**
     * 验证验证码
     *
     * @param code
     * @return
     */
    public static boolean checkCaptcha(String code)
    {
        Session session = getSubjct().getSession();
        return code.equals(session.getAttribute(CAPTCHA));
    }

    /**
     * 清空验证码
     */
    public static void cleanCaptcha()
    {
        Session session = getSubjct().getSession();
        session.removeAttribute(CAPTCHA);
    }

    /**
     * 登陆
     *
     * @param username
     * @param password
     */
    public static void login(String username, String password)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        getSubjct().login(token);
    }

    /**
     * 登出
     */
    public static void logout()
    {
        getSubjct().logout();
    }

    /**
     * 获取IP
     *
     * @return
     */
    public static String getHost()
    {
        Session session = getSubjct().getSession();
        return session.getHost();
    }

    /**
     * 获取用户对象
     *
     * @return
     */
    public static User getUser()
    {
        return (User) getSubjct().getPrincipal();
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static Integer getUserId()
    {
        User user = getUser();
        return user != null ? user.getId() : null;
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getUsername()
    {
        User user = getUser();
        return user != null ? user.getUsername() : null;
    }

    /**
     * 设置登陆系统
     *
     * @param os
     */
    public static void setOs(String os)
    {
        Session session = getSubjct().getSession();
        session.setAttribute(OS, os);
    }

    /**
     * 获取登陆系统
     *
     * @return
     */
    public static String getOs()
    {
        Session session = getSubjct().getSession();
        return (String) session.getAttribute(OS);
    }

    /**
     * 设置登陆浏览器
     *
     * @param browser
     */
    public static void setBrowser(String browser)
    {
        Session session = getSubjct().getSession();
        session.setAttribute(BROWSER, browser);
    }

    /**
     * 获取登陆浏览器
     *
     * @return
     */
    public static String getBrowser()
    {
        Session session = getSubjct().getSession();
        return (String) session.getAttribute(BROWSER);
    }

    /**
     * 设置登陆系统和浏览器
     *
     * @param os
     * @param browser
     */
    public static void setOsBrowser(String os, String browser)
    {
        Session session = getSubjct().getSession();
        session.setAttribute(OS, os);
        session.setAttribute(BROWSER, browser);
    }


    /**
     * 存放需要跟新权限的sessionId
     */
    private static Set<Serializable> authIds = new HashSet<Serializable>();

    public static void markAuthBysessionId(Serializable sessionId)
    {
        authIds.add(sessionId);
    }

    /**
     * 检验权限是否发生变化
     */
    public static void clearAuthBysessionId(Serializable sessionId)
    {
        if (authIds.remove(sessionId))
        {
            clearAuth();
        }
    }

    /**
     * 清空授权
     */
    private static void clearAuth()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        realm.clearAuthz();
    }

}
