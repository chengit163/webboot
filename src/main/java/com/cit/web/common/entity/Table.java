package com.cit.web.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Table implements Serializable
{
    /**
     * 表名称
     **/
    private String tableName;
    /**
     * 表备注
     **/
    private String tableComment;
    /**
     * 创建时间
     **/
    private Date createTime;
    /**
     * 列
     **/
    private List<Column> columns;
    /**
     * 类名
     **/
    private String className;
    /**
     * 类名对应的属性名
     */
    private String fieldName;
}
