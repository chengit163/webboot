package com.cit.web.common.dao;

import com.cit.web.common.entity.Column;
import com.cit.web.common.entity.Table;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GeneratorMapper
{

    @Select("SELECT COUNT(*) FROM information_schema.TABLES " +
            "WHERE TABLE_SCHEMA = (SELECT DATABASE()) ")
    int count();

    @Select("SELECT TABLE_NAME, TABLE_COMMENT, CREATE_TIME FROM information_schema.TABLES " +
            "WHERE TABLE_SCHEMA = (SELECT DATABASE()) ")
//    @Results({
//            @Result(property = "tableName", column = "TABLE_NAME"),
//            @Result(property = "tableComment", column = "TABLE_COMMENT"),
//            @Result(property = "createTime", column = "CREATE_TIME")
//    })
    List<Table> list();


    @Select("SELECT TABLE_NAME, TABLE_COMMENT, CREATE_TIME FROM information_schema.TABLES " +
            "WHERE TABLE_SCHEMA = (SELECT DATABASE()) AND TABLE_NAME = #{tableName} ")
//    @Results({
//            @Result(property = "tableName", column = "TABLE_NAME"),
//            @Result(property = "tableComment", column = "TABLE_COMMENT"),
//            @Result(property = "createTime", column = "CREATE_TIME")
//    })
    Table get(String tableName);

    @Select("SELECT COLUMN_NAME, COLUMN_COMMENT, DATA_TYPE, COLUMN_TYPE, IS_NULLABLE, COLUMN_KEY, EXTRA FROM information_schema.COLUMNS " +
            "WHERE TABLE_NAME = #{tableName} AND TABLE_SCHEMA = (SELECT DATABASE()) ORDER BY ORDINAL_POSITION ")
//    @Results({
//            @Result(property = "columnName", column = "COLUMN_NAME"),
//            @Result(property = "columnComment", column = "COLUMN_COMMENT"),
//            @Result(property = "dataType", column = "DATA_TYPE"),
//            @Result(property = "columnType", column = "COLUMN_TYPE"),
//            @Result(property = "isNullable", column = "IS_NULLABLE"),
//            @Result(property = "columnKey", column = "COLUMN_KEY"),
//            @Result(property = "extra", column = "EXTRA")
//    })
    List<Column> listColumns(String tableName);
}
