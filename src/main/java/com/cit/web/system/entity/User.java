package com.cit.web.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 *  用户
 */
@ApiModel("用户")
@Data
public class User implements Serializable
{
    /**
     * 用户ID(主键)
     */
    @ApiModelProperty("用户ID(主键)")
    private Integer id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;
    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nickname;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;
    /**
     * 最近登陆时间
     */
    @ApiModelProperty("最近登陆时间")
    private java.util.Date lastLoginTime;
    /**
     * 最近登陆IP
     */
    @ApiModelProperty("最近登陆IP")
    private String lastLoginIp;
    /**
     * 创建用户ID
     */
    @ApiModelProperty("创建用户ID")
    private Integer userIdCreate;
    /**
     * 状态
     * 0: 锁定
     * 1: 可用
     */
    @ApiModelProperty("状态")
    private Integer status;
    /**
     * 录入时间
     */
    @ApiModelProperty("录入时间")
    private java.util.Date gmtCreate;
    /**
     * 最近修改时间
     */
    @ApiModelProperty("最近修改时间")
    private java.util.Date gmtModified;
}