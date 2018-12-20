package com.cit.web.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 *  角色
 */
@ApiModel("角色")
@Data
public class Role implements Serializable
{
    /**
     * 角色ID(主键)
     */
    @ApiModelProperty("角色ID(主键)")
    private Integer id;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer orders;
    /**
     * 创建用户ID
     */
    @ApiModelProperty("创建用户ID")
    private Integer userIdCreate;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private java.util.Date gmtCreate;
    /**
     * 最近修改时间
     */
    @ApiModelProperty("最近修改时间")
    private java.util.Date gmtModified;
}