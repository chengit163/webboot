package com.cit.web.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;

import java.io.Serializable;

/**
 * 在线
 */
@ApiModel("在线")
@Data
public class Online implements Serializable
{
    /**
     * 会话ID
     */
    @ApiModelProperty("会话ID")
    private String id;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private java.util.Date starttimestamp;
    /**
     * 最近访问时间
     */
    @ApiModelProperty("最近访问时间")
    private java.util.Date lastaccesstime;
    /**
     * 超時(ms)
     */
    @ApiModelProperty("超時(ms)")
    private Long timeout;
    /**
     * 主机
     */
    @ApiModelProperty("主机")
    private String host;
    /**
     * 系统
     */
    @ApiModelProperty("系统")
    private String os;
    /**
     * 浏览器
     */
    @ApiModelProperty("浏览器")
    private String browser;

    private Object principals;


    public Online()
    {
    }

    public Online(Session s)
    {
        if (s != null)
        {
            this.id = s.getId().toString();
            this.starttimestamp = s.getStartTimestamp();
            this.lastaccesstime = s.getLastAccessTime();
            this.timeout = s.getTimeout();
            this.host = s.getHost();
            //
            Object os = s.getAttribute("os");
            this.os = os == null ? null : os.toString();
            Object browser = s.getAttribute("browser");
            this.browser = browser == null ? null : browser.toString();
            //
            SimplePrincipalCollection principals = (SimplePrincipalCollection) s.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            this.principals = principals == null ? null : principals.getPrimaryPrincipal();
        }
    }
}