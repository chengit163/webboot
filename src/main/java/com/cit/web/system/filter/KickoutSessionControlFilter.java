package com.cit.web.system.filter;

import com.cit.web.common.util.HttpUtils;
import com.cit.web.common.util.ShiroUtils;
import com.cit.web.system.entity.User;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;


public class KickoutSessionControlFilter extends AccessControlFilter
{
    private static final Logger logger = LoggerFactory.getLogger(KickoutSessionControlFilter.class);

    private String kickoutUrl; //踢出后到的地址
    private boolean kickoutAfter; //踢出之前登录的/之后登录的用户 默认踢出之前登录的用户
    private int maxSession; //同一个帐号最大会话数 默认1
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;


    public void setKickoutUrl(String kickoutUrl)
    {
        this.kickoutUrl = kickoutUrl;
    }

    public void setKickoutAfter(boolean kickoutAfter)
    {
        this.kickoutAfter = kickoutAfter;
    }

    public void setMaxSession(int maxSession)
    {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager)
    {
        this.cache = cacheManager.getCache("shiro-activeSessionCache");
    }

    /**
     * 是否允许访问，返回true表示允许
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception
    {
        return false;
    }

    /**
     * 表示访问拒绝时是否自己处理，如果返回true表示自己不处理且继续拦截器链执行，返回false表示自己已经处理了（比如重定向到另一个页面）。
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered())
        {
            //如果没有登录，直接进行之后的流程
            return true;
        }

        try
        {
            //
            User user = (User) subject.getPrincipal();
            Session session = subject.getSession();
            String username = user.getUsername();
            Serializable sessionId = session.getId();
            //
            ShiroUtils.clearAuthBysessionId(sessionId);

            logger.debug("当前[" + username + " = " + sessionId + "]");

            //读取缓存   没有就存入
            Deque<Serializable> deque = cache.get(username);

            //如果此用户没有session队列，也就是还没有登录过，缓存中没有
            //就new一个空队列，不然deque对象为空，会报空指针
            if (deque == null)
            {
                deque = new LinkedList<Serializable>();
            }

            //如果队列里没有此sessionId，且用户没有被踢出；放入队列
            if (!deque.contains(sessionId) && session.getAttribute("kickout") == null)
            {
                //将sessionId存入队列
                deque.push(sessionId);
                //将用户的sessionId队列缓存
                cache.put(username, deque);
            }

            //如果队列里的sessionId数超出最大会话数，开始踢人
            while (deque.size() > maxSession)
            {
                Serializable kickoutSessionId = null;
                if (kickoutAfter)
                {
                    // 如果踢出后者
                    kickoutSessionId = deque.removeFirst();
                } else
                {
                    // 否则踢出前者
                    kickoutSessionId = deque.removeLast();
                }
                // 更新下缓存队列
                cache.put(username, deque);
                try
                {
                    // 获取被踢出的sessionId的session对象
                    Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                    if (kickoutSession != null)
                    {
                        //设置会话的kickout属性表示踢出了
                        kickoutSession.setAttribute("kickout", true);
                    }
                } catch (Exception e)
                {
                    //ignore exception
                    e.printStackTrace();
                }
            }

            //如果被踢出了，直接退出，重定向到踢出后的地址
            if (session.getAttribute("kickout") != null)
            {

                //会话被踢出了
                try
                {
                    subject.logout();
                } catch (Exception e)
                {
                    //ignore exception
                    e.printStackTrace();
                }
                saveRequest(request);

                logger.debug("踢出[" + username + " = " + sessionId + "] ajax: " + HttpUtils.isAjax((HttpServletRequest) request));
//                if (HttpUtils.isAjax((HttpServletRequest) request))
//                {
//                    R r = R.bind(Code.KICKOUT);
//                    HttpUtils.out((HttpServletResponse) response, r);
//                } else
//                {
//                    //重定向
//                    WebUtils.issueRedirect(request, response, kickoutUrl);
//                }
                //重定向
                WebUtils.issueRedirect(request, response, kickoutUrl);
                return false;
            }

            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
