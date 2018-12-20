package com.cit.web.common.service.impl;

import com.cit.web.common.dao.GeneratorMapper;
import com.cit.web.common.entity.Column;
import com.cit.web.common.entity.Table;
import com.cit.web.common.service.GeneratorService;
import com.cit.web.common.util.GeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratorServiceImpl implements GeneratorService
{
    @Autowired
    private GeneratorMapper generatorMapper;

    @Override
    public int count()
    {
        return generatorMapper.count();
    }

    @Override
    public List<Table> list()
    {
        return generatorMapper.list();
    }

    @Override
    public Table get(String tableName)
    {
        return generatorMapper.get(tableName);
    }

    @Override
    public List<Column> listColumns(String tableName)
    {
        return generatorMapper.listColumns(tableName);
    }


    @Override
    public Table getWithColumns(String tableName)
    {
        Table table = generatorMapper.get(tableName);
        if (table != null)
        {
            List<Column> columns = generatorMapper.listColumns(tableName);
            if (columns != null)
            {
                for (Column column : columns)
                {
                    column.analysis();
                }
                table.setColumns(columns);
            }
        }
        return table;
    }

    @Override
    public byte[] code(String tableName, String module)
    {
        return generatingCode(new String[]{tableName}, module);
    }

    @Override
    public byte[] batchCode(String[] tableNames, String module)
    {
        return generatingCode(tableNames, module);
    }


    private byte[] generatingCode(String[] tableNames, String module)
    {
        ArrayList<Table> tables = new ArrayList<Table>();
        for (String tableName : tableNames)
        {
            Table table = generatorMapper.get(tableName);
            if (table != null)
            {
                List<Column> columns = generatorMapper.listColumns(table.getTableName());
                if (columns != null && !columns.isEmpty())
                {
                    table.setColumns(columns);
                    tables.add(table);
                }
            }
        }
        return GeneratorUtils.generating(tables, module);
    }
}
