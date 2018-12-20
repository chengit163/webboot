package com.cit.web.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 *  菜单
 */
@ApiModel("菜单")
@Data
public class Menu implements Serializable
{
    /**
     * 菜单ID(主键)
     */
    @ApiModelProperty("菜单ID(主键)")
    private Integer id;
    /**
     * 父菜单ID
     */
    @ApiModelProperty("父菜单ID")
    private Integer pid;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 路径
     */
    @ApiModelProperty("路径")
    private String url;
    /**
     * 权限
     */
    @ApiModelProperty("权限")
    private String permission;
    /**
     * 图标
     */
    @ApiModelProperty("图标")
    private String icon;
    /**
     * 类型
     * -1: 其他
     * 0: 目录
     * 1: 菜单
     * 2: 按钮
     */
    @ApiModelProperty("类型")
    private Integer type;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer orders;
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