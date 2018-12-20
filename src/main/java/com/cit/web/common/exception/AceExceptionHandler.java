package com.cit.web.common.exception;

import com.cit.web.common.constant.Code;
import com.cit.web.common.constant.R;
import com.cit.web.common.util.HttpUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@RestControllerAdvice
public class AceExceptionHandler
{
    private Logger logger = LoggerFactory.getLogger(AceExceptionHandler.class);

    @ExceptionHandler(AceException.class)
    public R handlerAceException(AceException e)
    {
        e.printStackTrace();
        return R.bind(e.getCode());
    }

    @ExceptionHandler({UnknownAccountException.class, IncorrectCredentialsException.class, LockedAccountException.class})
    public R handleShiroException(ShiroException e)
    {
        e.printStackTrace();
        return R.bind(Code.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationException.class)
    public Object handleAuthorizationException(AuthorizationException e, HttpServletRequest request)
    {
        e.printStackTrace();
        if (HttpUtils.isAjax(request))
        {
            return R.bind(Code.HTTP_403);
        }
        return new ModelAndView("error/403");
    }

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest request)
    {
        e.printStackTrace();
        if (HttpUtils.isAjax(request))
        {
            return R.bind(Code.HTTP_500);
        }
        return new ModelAndView("error/500");
    }

}
