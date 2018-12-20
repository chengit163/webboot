package com.cit.web.common.controller;

import com.cit.web.common.entity.Log;
import com.cit.web.common.service.LogService;
import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.R;
import com.cit.web.common.util.ExcelUtils;
import com.cit.web.common.util.PageUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(description = "日志控制器")
@RequestMapping("/common/log")
@Controller
public class LogController
{
    @Autowired
    private LogService logService;

    @SysLog("查询日志列表")
    @ApiOperation("查询日志列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "ip", value = "IP地址", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "action", value = "动作", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "请求URL", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "method", value = "方法", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "args", value = "参数", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "func", value = "函数", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "os", value = "系统", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "browser", value = "浏览器", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cost", value = "耗时", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "happen", value = "时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "排序字段", dataType = "string", paramType = "query", allowableValues = "id,username,ip,action,url,method,args,func,os,browser,cost,happen"),
            @ApiImplicitParam(name = "order", value = "排序规则", dataType = "string", paramType = "query", allowableValues = "ASC,DESC")
    })
    @RequiresPermissions("common:log:get")
    @GetMapping
    @ResponseBody
    public Map<String, Object> list(@ApiIgnore @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, Log.class);
        PageUtils.cleanOrder(map);
        //页码
        int pageNum = PageUtils.getPageNum(map);
        int pageSize = PageUtils.getPageSize(map);
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Log> logs = logService.list(map);
        Page<Log> page = (Page<Log>) logs;
        //结果
        Map<String, Object> result = PageUtils.getResult(page.getTotal(), page);
        return result;
    }

    @SysLog("获取日志详情")
    @ApiOperation("获取日志详情")
    @ApiImplicitParam(name = "id", value = "日志ID(主键)", dataType = "long", paramType = "path", required = true)
    @RequiresPermissions("common:log:get")
    @GetMapping("/{id}")
    @ResponseBody
    public Log get(@PathVariable("id") Long id)
    {
        Log log = logService.get(id);
        return log;
    }

//    @SysLog("新增日志")
//    @ApiOperation("新增日志")
//    @RequiresPermissions("common:log:save")
//    @PostMapping
//    @ResponseBody
//    public R save(Log log)
//    {
//        //校验
//        String message = LogValidator.validate(log, false);
//        if (message != null)
//        {
//            return R.bind(Code.INVALID.getCode(), message);
//        }
//        int result = logService.save(log);
//        return result > 0 ? R.success() : R.unknown();
//    }

//    @SysLog("更新日志")
//    @ApiOperation("更新日志")
//    @RequiresPermissions("common:log:update")
//    @PutMapping
//    @ResponseBody
//    public R update(Log log)
//    {
//        //校验
//        String message = LogValidator.validate(log, true);
//        if (message != null)
//        {
//            return R.bind(Code.INVALID.getCode(), message);
//        }
//        int result = logService.update(log);
//        return result > 0 ? R.success() : R.unknown();
//    }

    @SysLog("删除日志")
    @ApiOperation("删除日志")
    @ApiImplicitParam(name = "id", value = "日志ID(主键)", dataType = "long", paramType = "query", required = true)
    @RequiresPermissions("common:log:remove")
    @DeleteMapping
    @ResponseBody
    public R remove(@RequestParam(value = "id", defaultValue = "0") Long id)
    {
        int result = logService.remove(id);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("批量删除日志")
    @ApiOperation("批量删除日志")
    @RequiresPermissions("common:log:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R batchRemove(@RequestParam("ids[]") Long[] ids)
    {
        int result = logService.batchRemove(ids);
        return result > 0 ? R.success() : R.unknown();
    }


    private static final String PREFIX = "common/log/";

    @ApiIgnore
    @RequiresPermissions("common:log:get")
    @GetMapping("/main")
    public String main()
    {
        return PREFIX + "main";
    }

    @ApiIgnore
    @RequiresPermissions("common:log:get")
    @GetMapping("/add")
    public String add()
    {
        return PREFIX + "add";
    }

    @ApiIgnore
    @RequiresPermissions("common:log:get")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model)
    {
        Log log = logService.get(id);
        model.addAttribute("log", log);
        return PREFIX + "edit";
    }

    @ApiIgnore
    @RequiresPermissions("common:log:get")
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model)
    {
        Log log = logService.get(id);
        model.addAttribute("log", log);
        return PREFIX + "view";
    }

    @SysLog("导出日志Excel")
    @ApiIgnore
    @RequiresPermissions("common:log:export")
    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, Log.class);
        PageUtils.cleanOrder(map);
        //
        List<Log> logs = logService.list(map);
        ExcelUtils.export(response, logs, Log.class);
    }

    @SysLog("下载日志Excel模板")
    @ApiIgnore
    @RequiresPermissions("common:log:import")
    @GetMapping("/template")
    public void excelTemplate(HttpServletResponse response)
    {
        ExcelUtils.export(response, Log.class);
    }

    @SysLog("导入日志Excel")
    @ApiIgnore
    @RequiresPermissions("common:log:import")
    @PostMapping("/import")
    @ResponseBody
    public R excelImport(@RequestParam("file") MultipartFile file)
    {
        String filename = file.getOriginalFilename();
        System.out.println("**********Excel: " + filename);
        return R.success();
    }
}
