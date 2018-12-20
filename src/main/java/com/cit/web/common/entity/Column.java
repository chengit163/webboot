package com.cit.web.common.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class Column implements Serializable
{
    /**
     * 列名称
     **/
    private String columnName;
    /**
     * 列备注
     **/
    private String columnComment;
    /**
     * 数据类型
     **/
    private String dataType;
    /**
     * 列类型
     */
    private String columnType;
    /**
     * 能否为空
     */
    private String isNullable;
    /**
     * 键
     **/
    private String columnKey;
    /**
     * auto_increment
     **/
    private String extra;
    /**
     * 属性名
     */
    private String fieldName;
    /**
     * 属性名对应的函数名
     */
    private String methodName;
    /**
     * javaType数据类型
     */
    private String javaType;
    /**
     * jdbcType数据类型
     */
    private String jdbcType;
    /**
     * swaggerType(Swagger)
     */
    private String swaggerType;


    private Rule rule;

    private List<Option> options;


    @Data
    public static class Rule implements Serializable
    {
        private Integer min;

        private Integer max;

        private String regex;

        private String note;

        private String regexp;

        public void load()
        {
            if (regex != null)
            {
                regexp = regex.replace("\\", "\\\\");
            }
        }
    }


    @Data
    public static class Option implements Serializable
    {
        private String key;
        private String value;

        public Option(String key, String value)
        {
            this.key = key;
            this.value = value;
        }
    }


    /**
     * 通过列备注，获取以下属性值
     * options
     * min
     * max
     * regex
     */
    public void analysis()
    {
        if (columnComment != null)
        {
            int index;
            if ((index = columnComment.indexOf("###")) != -1)
            {
                // 校验规则
                String comment = columnComment.substring(0, index);
                try
                {
                    String str = columnComment.substring(index + "###".length());
                    rule = JSON.parseObject(str, Rule.class);
                    rule.load();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                //重新赋值列备注
                columnComment = comment;
            } else if ((index = columnComment.indexOf("@@@")) != -1)
            {
                // 枚举所有值
                String comment = columnComment.substring(0, index);
                try
                {
                    String str = columnComment.substring(index + "@@@".length());
                    LinkedHashMap<String, String> jsonMap = JSON.parseObject(str, new TypeReference<LinkedHashMap<String, String>>()
                    {
                    });
                    if (jsonMap != null && !jsonMap.isEmpty())
                    {
                        options = new ArrayList<Option>();
                        for (Map.Entry<String, String> entry : jsonMap.entrySet())
                        {
                            options.add(new Option(entry.getKey(), entry.getValue()));
                        }
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
                //重新赋值列备注
                columnComment = comment;
            }
        }
    }
}
