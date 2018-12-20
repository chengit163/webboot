package com.cit.web.common.controller;

import com.alibaba.fastjson.JSON;
import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.entity.Table;
import com.cit.web.common.service.GeneratorService;
import com.cit.web.common.util.PageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Api(description = "生成代码控制器")
@RequestMapping("/common/generator")
@Controller
public class GeneratorController
{

    @Autowired
    private GeneratorService generatorService;

    @SysLog("生成代码")
    @ApiOperation("生成代码")
    @GetMapping("/code")
    public void code(HttpServletResponse response, @RequestParam("tableName") String tableName, @RequestParam("module") String module)
            throws IOException
    {
        module = StringUtils.trim(module);
        if (module == null || module.matches("[a-z]*"))
        {
            if (StringUtils.isNotEmpty(tableName))
            {
                byte[] data = generatorService.code(tableName, module);
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("application/octet-stream; charset=UTF-8");
                IOUtils.write(data, response.getOutputStream());
            }
        }

    }

    @SysLog("批量生成代码")
    @ApiOperation("批量生成代码")
    @GetMapping("/batchCode")
    public void batchCode(HttpServletResponse response, @RequestParam("tableNames") String tableNames, @RequestParam("module") String module)
            throws IOException
    {
        module = StringUtils.trim(module);
        if (module == null || module.matches("[a-z]*"))
        {
            String[] _tableNames = new String[]{};
            _tableNames = JSON.parseArray(tableNames).toArray(_tableNames);
            if (_tableNames.length > 0)
            {
                byte[] data = generatorService.batchCode(_tableNames, module);
                response.reset();
                response.setHeader("Content-Disposition", "attachment; filename=\"codes.zip\"");
                response.addHeader("Content-Length", "" + data.length);
                response.setContentType("application/octet-stream; charset=UTF-8");
                IOUtils.write(data, response.getOutputStream());
            }
        }
    }


    @ApiIgnore
    @GetMapping
    @ResponseBody
    public Map<String, Object> list(@ApiIgnore @RequestParam(required = false) Map<String, Object> map)
    {
        //页码
        int pageNum = PageUtils.getPageNum(map);
        int pageSize = PageUtils.getPageSize(map);
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Table> tables = generatorService.list();
        Page<Table> page = (Page<Table>) tables;
        //结果
        Map<String, Object> result = PageUtils.getResult(page.getTotal(), page);
        return result;
    }


    private static final String PREFIX = "/common/generator" ;

    @ApiIgnore
    @GetMapping("/main")
    public String main()
    {
        return PREFIX + "/main" ;
    }

    @ApiIgnore
    @GetMapping("/view/{tableName}")
    public String view(@PathVariable("tableName") String tableName, Model model)
    {
        Table table = generatorService.getWithColumns(tableName);
        model.addAttribute("table", table);
        return PREFIX + "/view" ;
    }

}
