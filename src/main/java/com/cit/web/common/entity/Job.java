package com.cit.web.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 *  任务
 */
@ApiModel("任务")
@Data
public class Job implements Serializable
{
    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    private Integer id;
    /**
     * 执行类
     */
    @ApiModelProperty("执行类")
    private String jobClass;
    /**
     * cron表达式
     */
    @ApiModelProperty("cron表达式")
    private String cronExpression;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;
    /**
     * 状态
     * 0: 关闭
     * 1: 启动
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