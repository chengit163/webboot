package com.cit.web.common.util;

import com.cit.web.common.entity.Column;
import com.cit.web.common.entity.Table;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GeneratorUtils
{
    private static final String MAPPER_VM = "templates/common/generator/vm/Mapper.xml.vm";
    private static final String ENTITY_VM = "templates/common/generator/vm/Entity.java.vm";
    private static final String VALIDATOR_VM = "templates/common/generator/vm/Validator.java.vm";
    private static final String DAO_VM = "templates/common/generator/vm/Dao.java.vm";
    private static final String SERVICE_VM = "templates/common/generator/vm/Service.java.vm";
    private static final String SERVICE_IMPL_VM = "templates/common/generator/vm/ServiceImpl.java.vm";
    private static final String CONTROLLER_VM = "templates/common/generator/vm/Controller.java.vm";
    private static final String MESSAGES_PROPERTIES_VM = "templates/common/generator/vm/messages.properties.vm";
    private static final String MAIN_HTML_VM = "templates/common/generator/vm/main.html.vm";
    private static final String MAIN_JS_VM = "templates/common/generator/vm/main.js.vm";
    private static final String ADD_HTML_VM = "templates/common/generator/vm/add.html.vm";
    private static final String ADD_JS_VM = "templates/common/generator/vm/add.js.vm";
    private static final String EDIT_HTML_VM = "templates/common/generator/vm/edit.html.vm";
    private static final String EDIT_JS_VM = "templates/common/generator/vm/edit.js.vm";
    private static final String VIEW_HTML_VM = "templates/common/generator/vm/view.html.vm";


    public static Configuration getConfig()
    {
        try
        {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getTemplates()
    {
        List<String> templates = new ArrayList<String>();
        templates.add(MAPPER_VM);
        templates.add(ENTITY_VM);
        templates.add(VALIDATOR_VM);
        templates.add(DAO_VM);
        templates.add(SERVICE_VM);
        templates.add(SERVICE_IMPL_VM);
        templates.add(CONTROLLER_VM);
        templates.add(MESSAGES_PROPERTIES_VM);
        templates.add(MAIN_HTML_VM);
        templates.add(MAIN_JS_VM);
        templates.add(ADD_HTML_VM);
        templates.add(ADD_JS_VM);
        templates.add(EDIT_HTML_VM);
        templates.add(EDIT_JS_VM);
        templates.add(VIEW_HTML_VM);
        return templates;
    }

    public static String getFileName(String template, String packageName, String module, String className, String fieldName)
    {
        StringBuilder sb = new StringBuilder("main");
        sb.append(File.separator);
        switch (template)
        {
            case MAPPER_VM:
                sb.append("resources").append(File.separator);
                sb.append("mybatis").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(className).append("Mapper.xml");
                break;
            case ENTITY_VM:
                sb.append("java").append(File.separator);
                sb.append(packageName.replace(".", File.separator)).append(File.separator);
                sb.append(module).append(File.separator);
                sb.append("entity").append(File.separator);
                sb.append(className).append(".java");
                break;
            case VALIDATOR_VM:
                sb.append("java").append(File.separator);
                sb.append(packageName.replace(".", File.separator)).append(File.separator);
                sb.append(module).append(File.separator);
                sb.append("validator").append(File.separator);
                sb.append(className).append("Validator.java");
                break;
            case DAO_VM:
                sb.append("java").append(File.separator);
                sb.append(packageName.replace(".", File.separator)).append(File.separator);
                sb.append(module).append(File.separator);
                sb.append("dao").append(File.separator);
                sb.append(className).append("Dao.java");
                break;
            case SERVICE_VM:
                sb.append("java").append(File.separator);
                sb.append(packageName.replace(".", File.separator)).append(File.separator);
                sb.append(module).append(File.separator);
                sb.append("service").append(File.separator);
                sb.append(className).append("Service.java");
                break;
            case SERVICE_IMPL_VM:
                sb.append("java").append(File.separator);
                sb.append(packageName.replace(".", File.separator)).append(File.separator);
                sb.append(module).append(File.separator);
                sb.append("service").append(File.separator);
                sb.append("impl").append(File.separator);
                sb.append(className).append("ServiceImpl.java");
                break;
            case CONTROLLER_VM:
                sb.append("java").append(File.separator);
                sb.append(packageName.replace(".", File.separator)).append(File.separator);
                sb.append(module).append(File.separator);
                sb.append("controller").append(File.separator);
                sb.append(className).append("Controller.java");
                break;
            case MESSAGES_PROPERTIES_VM:
                sb.append(className).append("_messages.properties");
                break;
            case MAIN_HTML_VM:
                sb.append("resources").append(File.separator);
                sb.append("templates").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(fieldName).append(File.separator);
                sb.append("main.html");
                break;
            case MAIN_JS_VM:
                sb.append("resources").append(File.separator);
                sb.append("static").append(File.separator);
                sb.append("js").append(File.separator);
                sb.append("app").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(fieldName).append(File.separator);
                sb.append("main.js");
                break;
            case ADD_HTML_VM:
                sb.append("resources").append(File.separator);
                sb.append("templates").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(fieldName).append(File.separator);
                sb.append("add.html");
                break;
            case ADD_JS_VM:
                sb.append("resources").append(File.separator);
                sb.append("static").append(File.separator);
                sb.append("js").append(File.separator);
                sb.append("app").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(fieldName).append(File.separator);
                sb.append("add.js");
                break;
            case EDIT_HTML_VM:
                sb.append("resources").append(File.separator);
                sb.append("templates").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(fieldName).append(File.separator);
                sb.append("edit.html");
                break;
            case EDIT_JS_VM:
                sb.append("resources").append(File.separator);
                sb.append("static").append(File.separator);
                sb.append("js").append(File.separator);
                sb.append("app").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(fieldName).append(File.separator);
                sb.append("edit.js");
                break;
            case VIEW_HTML_VM:
                sb.append("resources").append(File.separator);
                sb.append("templates").append(File.separator);
                sb.append(module).append(File.separator);
                sb.append(fieldName).append(File.separator);
                sb.append("view.html");
                break;
            default:
                sb.setLength(0);
                break;
        }
        return sb.toString();
    }


    private static String getModule(String name)
    {
        return name.substring(0, name.indexOf('_')).toLowerCase();
    }


    public static byte[] generating(List<Table> tables, String _module)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        Configuration config = getConfig();
        //
        // 遍历表
        for (Table table : tables)
        {
            String tableName = table.getTableName();
            if (StringUtils.isNotEmpty(_module))
            {
                int index;
                if ((index = tableName.indexOf('_')) != -1)
                {
                    tableName = tableName.substring(index + 1);
                }
            }
            // 设置类名
            String fieldName = CamelNameUtils.underscore2camel(tableName);
            table.setFieldName(fieldName);
            String className = CamelNameUtils.capitalize(fieldName);
            table.setClassName(className);
            // 遍历列
            List<Column> columns = table.getColumns();
            Column pk = null;
            for (Column column : columns)
            {
                if (pk == null && ("PRI".equals(column.getColumnKey()) || "UNI".equals(column.getColumnKey())))
                {
                    pk = column;
                }
                String name = CamelNameUtils.underscore2camel(column.getColumnName());
                column.setFieldName(name);
                column.setMethodName(CamelNameUtils.capitalize(name));

                String javaType = config.getString("java.type." + column.getDataType(), "UNKNOWN");
                column.setJavaType(javaType);

                String jdbcType = config.getString("jdbc.type." + column.getDataType(), "UNKNOWN");
                column.setJdbcType(jdbcType);

                String swaggerType = config.getString("swagger.type." + column.getDataType(), "UNKNOWN");
                column.setSwaggerType(swaggerType);
                // 分析
                column.analysis();
            }
            if (pk == null)
            {
                pk = columns.get(0);
            }

            //
            String packageName = config.getString("package");
            String module = StringUtils.isNotEmpty(_module) ? _module : getModule(table.getTableName());

            //设置velocity资源加载器
            Properties prop = new Properties();
            prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            Velocity.init(prop);

            //封装模板数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("table", table);
            map.put("pk", pk);
            map.put("packageName", packageName);
            map.put("module", module);
            VelocityContext context = new VelocityContext(map);

            //获取模板列表
            List<String> templates = getTemplates();
            for (String template : templates)
            {
                //渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, "UTF-8");
                tpl.merge(context, sw);
                try
                {
                    //添加到zip
                    String filename = getFileName(template, packageName, module, className, fieldName);
                    if (!StringUtils.isEmpty(filename))
                    {
                        zip.putNextEntry(new ZipEntry(filename));
                        IOUtils.write(sw.toString(), zip, "UTF-8");
                        IOUtils.closeQuietly(sw);
                        zip.closeEntry();
                    }
                } catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
        //
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }
}
