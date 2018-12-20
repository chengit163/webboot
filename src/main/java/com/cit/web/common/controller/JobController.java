package com.cit.web.common.controller;

import com.cit.web.common.entity.Job;
import com.cit.web.common.entity.ScheduleJob;
import com.cit.web.common.jobs.JobUtils;
import com.cit.web.common.service.JobService;
import com.cit.web.common.validator.JobValidator;
import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.Code;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Api(description = "任务控制器")
@RequestMapping("/common/job")
@Controller
public class JobController
{
    @Autowired
    private JobService jobService;

    @SysLog("查询任务列表")
    @ApiOperation("查询任务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jobClass", value = "执行类", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "cronExpression", value = "cron表达式", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "描述", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "gmtCreate", value = "录入时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "gmtModified", value = "最近修改时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "排序字段", dataType = "string", paramType = "query", allowableValues = "id,jobClass,cronExpression,description,status,gmtCreate,gmtModified"),
            @ApiImplicitParam(name = "order", value = "排序规则", dataType = "string", paramType = "query", allowableValues = "ASC,DESC")
    })
    @RequiresPermissions("common:job:get")
    @GetMapping
    @ResponseBody
    public Map<String, Object> list(@ApiIgnore @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, Job.class);
        PageUtils.cleanOrder(map);
        //页码
        int pageNum = PageUtils.getPageNum(map);
        int pageSize = PageUtils.getPageSize(map);
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Job> jobs = jobService.list(map);
        Page<Job> page = (Page<Job>) jobs;
        //结果
        Map<String, Object> result = PageUtils.getResult(page.getTotal(), page);
        return result;
    }

    @SysLog("获取任务详情")
    @ApiOperation("获取任务详情")
    @ApiImplicitParam(name = "id", value = "主键ID", dataType = "int", paramType = "path", required = true)
    @RequiresPermissions("common:job:get")
    @GetMapping("/{id}")
    @ResponseBody
    public Job get(@PathVariable("id") Integer id)
    {
        Job job = jobService.get(id);
        return job;
    }

    @SysLog("新增任务")
    @ApiOperation("新增任务")
    @RequiresPermissions("common:job:save")
    @PostMapping
    @ResponseBody
    public R save(Job job)
    {
        //校验
        String message = JobValidator.validate(job, false);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        int result = jobService.save(job);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("更新任务")
    @ApiOperation("更新任务")
    @RequiresPermissions("common:job:update")
    @PutMapping
    @ResponseBody
    public R update(Job job)
    {
        //校验
        String message = JobValidator.validate(job, true);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        job.setJobClass(null); // 任务类型不允许修改
        int result = jobService.update(job);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("删除任务")
    @ApiOperation("删除任务")
    @ApiImplicitParam(name = "id", value = "主键ID", dataType = "int", paramType = "query", required = true)
    @RequiresPermissions("common:job:remove")
    @DeleteMapping
    @ResponseBody
    public R remove(@RequestParam(value = "id", defaultValue = "0") Integer id)
    {
        int result = jobService.remove(id);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("批量删除任务")
    @ApiOperation("批量删除任务")
    @RequiresPermissions("common:job:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R batchRemove(@RequestParam("ids[]") Integer[] ids)
    {
        int result = jobService.batchRemove(ids);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("执行任务")
    @ApiOperation("执行任务")
    @ApiImplicitParam(name = "id", value = "主键ID", dataType = "int", paramType = "query", required = true)
    @RequiresPermissions("common:job:remove")
    @PostMapping("/run")
    @ResponseBody
    public R run(@RequestParam(value = "id", defaultValue = "0") Integer id)
    {
        return jobService.run(id) ? R.success() : R.unknown();
    }


    private static final String PREFIX = "common/job/";

    @ApiIgnore
    @RequiresPermissions("common:job:get")
    @GetMapping("/main")
    public String main(Model model)
    {
        model.addAttribute("executeJobs", JobUtils.getJobs());
        return PREFIX + "main";
    }

    @ApiIgnore
    @RequiresPermissions("common:job:get")
    @GetMapping("/add")
    public String add(Model model)
    {
        model.addAttribute("executeJobs", JobUtils.getJobs());
        return PREFIX + "add";
    }

    @ApiIgnore
    @RequiresPermissions("common:job:get")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model)
    {
        Job job = jobService.get(id);
        model.addAttribute("job", job);
        model.addAttribute("executeJobs", JobUtils.getJobs());
        return PREFIX + "edit";
    }

    @ApiIgnore
    @RequiresPermissions("common:job:get")
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model)
    {
        Job job = jobService.get(id);
        model.addAttribute("job", job);
        return PREFIX + "view";
    }

    @SysLog("导出任务Excel")
    @ApiIgnore
    @RequiresPermissions("common:job:export")
    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, Job.class);
        PageUtils.cleanOrder(map);
        //
        List<Job> jobs = jobService.list(map);
        ExcelUtils.export(response, jobs, Job.class);
    }

    @SysLog("下载任务Excel模板")
    @ApiIgnore
    @RequiresPermissions("common:job:import")
    @GetMapping("/template")
    public void excelTemplate(HttpServletResponse response)
    {
        ExcelUtils.export(response, Job.class);
    }

    @SysLog("导入任务Excel")
    @ApiIgnore
    @RequiresPermissions("common:job:import")
    @PostMapping("/import")
    @ResponseBody
    public R excelImport(@RequestParam("file") MultipartFile file)
    {
        String filename = file.getOriginalFilename();
        System.out.println("**********Excel: " + filename);
        return R.success();
    }
}
