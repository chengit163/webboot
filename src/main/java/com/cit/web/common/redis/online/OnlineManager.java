package com.cit.web.common.redis.online;

import com.cit.web.system.entity.Online;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OnlineManager
{
    private SessionDAO sessionDAO;

    public OnlineManager(SessionDAO sessionDAO)
    {
        this.sessionDAO = sessionDAO;
    }


    /**
     * 所有在线用户
     * @return
     */
    public List<Online> listOnline()
    {
        List<Online> list = new ArrayList<Online>();
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        Iterator<Session> iterator = sessions.iterator();
        while (iterator.hasNext())
        {
            Session session = iterator.next();
            if (available(session))
                list.add(new Online(session));
        }
        return list;
    }

    /**
     * 移除在线用户
     * @param id
     * @return
     */
    public boolean removeOnline(String id)
    {
        try
        {
            Session session = sessionDAO.readSession(id);
            sessionDAO.delete(session);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 是否是一个有效的session
     *
     * @param session
     * @return
     */
    private boolean available(Session session)
    {
        return session != null
                && session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null
                && session.getAttribute("kickout") == null;
    }

}
