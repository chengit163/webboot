package com.cit.web.common.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.Constants;
import com.cit.web.common.constant.Method;
import com.cit.web.common.entity.Log;
import com.cit.web.common.service.LogService;
import com.cit.web.common.util.HttpUtils;
import com.cit.web.common.util.ShiroUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Aspect
@Component
public class WebLogAspect
{

    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Autowired
    private LogService logService;


    @Pointcut("execution(* *..controller..*.*(..))")// 两个..代表所有子目录，最后括号里的两个..代表所有参数
    public void logPointCut()
    {
    }

//    @Before("logPointCut()")
//    public void doBefore(JoinPoint joinPoint) throws Throwable
//    {
//    }
//
//    @AfterReturning(returning = "result", pointcut = "logPointCut()")
//    public void doAfterReturning(Object result) throws Throwable
//    {
//    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable
    {
        long start = System.currentTimeMillis(); // 开始
        Object result = pjp.proceed();
        long end = System.currentTimeMillis(); // 结束
        //
        saveLog(pjp, start, end);
        return result;
    }

    // 时间戳；密码；确认密码
    private PropertyFilter filter = (o, s, o1) -> !"_".equals(s) && !"password".equals(s) && !"confirmPassword".equals(s);


    /**
     * 保存日志
     *
     * @param pjp
     * @param start
     * @param end
     */
    private void saveLog(ProceedingJoinPoint pjp, long start, long end)
    {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        Method method = Method.getMethod(request.getMethod());
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 保存log日志
        Log log = new Log();
        log.setUsername(ShiroUtils.getUsername());
        log.setIp(HttpUtils.getAddress(request));
        log.setUrl(request.getServletPath());
        log.setMethod(method.getKey());
        log.setOs(userAgent.getOperatingSystem().getName());
        log.setBrowser(userAgent.getBrowser().getName() + " " + userAgent.getBrowserVersion());
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        log.setFunc(signature.getDeclaringTypeName().replaceFirst(Constants.PACKAGE_NAME, "") + "#" + signature.getName());
        try
        {
            if (Method.GET.equals(method))
            {
                String params = request.getQueryString();
                String args = JSON.toJSONString(params, filter).replaceAll("\"", "");
                log.setArgs(args);
            } else
            {
                Map<String, String[]> params = request.getParameterMap();
                String args = JSON.toJSONString(params, filter).replaceAll("\"", "");
                log.setArgs(args);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        log.setCost((int) (end - start));
        log.setHappen(new Date(start));
        // 记录注解日志
        SysLog sysLog = signature.getMethod().getAnnotation(SysLog.class);
        if (sysLog != null)
        {
            log.setAction(sysLog.value());
            logService.save(log);
        }
        logger.info(JSON.toJSONString(log));
    }
}
