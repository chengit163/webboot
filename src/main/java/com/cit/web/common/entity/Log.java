package com.cit.web.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 *  日志
 */
@ApiModel("日志")
@Data
public class Log implements Serializable
{
    /**
     * 日志ID(主键)
     */
    @ApiModelProperty("日志ID(主键)")
    private Long id;
    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;
    /**
     * IP地址
     */
    @ApiModelProperty("IP地址")
    private String ip;
    /**
     * 动作
     */
    @ApiModelProperty("动作")
    private String action;
    /**
     * 请求URL
     */
    @ApiModelProperty("请求URL")
    private String url;
    /**
     * 方法
     * -1: UNKNOWN
     * 0: GET
     * 1: POST
     * 2: PUT
     * 3: DELETE
     * 4: HEADER
     * 5: OPTIONS
     */
    @ApiModelProperty("方法")
    private Integer method;
    /**
     * 参数
     */
    @ApiModelProperty("参数")
    private String args;
    /**
     * 函数
     */
    @ApiModelProperty("函数")
    private String func;
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
    /**
     * 耗时
     */
    @ApiModelProperty("耗时")
    private Integer cost;
    /**
     * 时间
     */
    @ApiModelProperty("时间")
    private java.util.Date happen;
}