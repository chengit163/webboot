package com.cit.web.common.service;

import com.cit.web.common.entity.Column;
import com.cit.web.common.entity.Table;

import java.util.List;

public interface GeneratorService
{
    int count();

    List<Table> list();

    Table get(String tableName);

    List<Column> listColumns(String tableName);

    //
    Table getWithColumns(String tableName);

    byte[] code(String tableName, String module);

    byte[] batchCode(String[] tableNames, String module);
}
