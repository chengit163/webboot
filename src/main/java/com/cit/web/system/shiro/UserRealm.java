package com.cit.web.system.shiro;

import com.cit.web.common.constant.Constants;
import com.cit.web.common.util.ShiroUtils;
import com.cit.web.system.entity.User;
import com.cit.web.system.service.RabcService;
import com.cit.web.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Set;

public class UserRealm extends AuthorizingRealm
{
    private static final Logger logger = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RabcService rabcService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        User user = ShiroUtils.getUser();
        logger.debug("用户[" + user.getUsername() + "]授权");
        Set<String> permissions = rabcService.listPermissionByUserId(user.getId());
        //
//        permissions.add("common:log:save");
//        permissions.add("common:log:update");
//        permissions.add("common:log:import");
        //
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        String username = (String) token.getPrincipal();
        String password = new String((char[]) token.getCredentials());

        logger.debug("用户[" + username + "]登陆");
        User user = userService.getByUsername(username);
        //
        if (user == null)
        {
            throw new UnknownAccountException();
        }
        // 密码错误
        String passwd = new Md5Hash(password, Constants.PACKAGE_NAME).toString();
        //logger.debug(passwd);
        if (!passwd.equals(user.getPassword()))
        {
            throw new IncorrectCredentialsException();
        }
        //
        if (user.getStatus() == null || user.getStatus() == 0)
        {
            throw new LockedAccountException();
        }
        logger.debug("用户[" + username + "]登陆成功");
        try
        {
            // 更新最近登陆时间和最近登陆ip
            user.setPassword(null);
            user.setLastLoginTime(new Date());
            user.setLastLoginIp(ShiroUtils.getHost());
            userService.update(user);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return new SimpleAuthenticationInfo(user, password, getName());
    }

    /**
     * 清空授权
     */
    public void clearAuthz()
    {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
