package com.cit.web.system.controller;

import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.Code;
import com.cit.web.common.constant.R;
import com.cit.web.common.util.BaseIdUtils;
import com.cit.web.common.util.ExcelUtils;
import com.cit.web.common.util.PageUtils;
import com.cit.web.system.entity.Role;
import com.cit.web.system.service.RoleService;
import com.cit.web.system.validator.RoleValidator;
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

@Api(description = "角色控制器")
@RequestMapping("/system/role")
@Controller
public class RoleController
{
    @Autowired
    private RoleService roleService;

    @SysLog("查询角色列表")
    @ApiOperation("查询角色列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "描述", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "userIdCreate", value = "创建用户ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "gmtCreate", value = "创建时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "gmtModified", value = "最近修改时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "排序字段", dataType = "string", paramType = "query", allowableValues = "id,name,description,orders,userIdCreate,gmtCreate,gmtModified"),
            @ApiImplicitParam(name = "order", value = "排序规则", dataType = "string", paramType = "query", allowableValues = "ASC,DESC")
    })
    @RequiresPermissions("system:role:get")
    @GetMapping
    @ResponseBody
    public Map<String, Object> list(@ApiIgnore @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, Role.class);
        PageUtils.cleanOrder(map);
        //页码
        int pageNum = PageUtils.getPageNum(map);
        int pageSize = PageUtils.getPageSize(map);
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Role> roles = roleService.list(map);
        Page<Role> page = (Page<Role>) roles;
        //结果
        Map<String, Object> result = PageUtils.getResult(page.getTotal(), page);
        return result;
    }

    @SysLog("获取角色详情")
    @ApiOperation("获取角色详情")
    @ApiImplicitParam(name = "id", value = "角色ID(主键)", dataType = "int", paramType = "path", required = true)
    @RequiresPermissions("system:role:get")
    @GetMapping("/{id}")
    @ResponseBody
    public Role get(@PathVariable("id") Integer id)
    {
        Role role = roleService.get(id);
        return role;
    }

    @SysLog("新增角色")
    @ApiOperation("新增角色")
    @RequiresPermissions("system:role:save")
    @PostMapping
    @ResponseBody
    public R save(Role role)
    {
        //校验
        String message = RoleValidator.validate(role, false);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        int result = roleService.save(role);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("更新角色")
    @ApiOperation("更新角色")
    @RequiresPermissions("system:role:update")
    @PutMapping
    @ResponseBody
    public R update(Role role)
    {
        //校验
        String message = RoleValidator.validate(role, true);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        int result = roleService.update(role);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("删除角色")
    @ApiOperation("删除角色")
    @ApiImplicitParam(name = "id", value = "角色ID(主键)", dataType = "int", paramType = "query", required = true)
    @RequiresPermissions("system:role:remove")
    @DeleteMapping
    @ResponseBody
    public R remove(@RequestParam(value = "id", defaultValue = "0") Integer id)
    {
        if (BaseIdUtils.isBaseRoleId(id))
        {
            String name = roleService.getNameById(id);
            if (name != null)
            {
                return R.bind(Code.INVALID.getCode(), "[" + name + "] 为基础角色，不能删除!");
            }
        }
        int result = roleService.remove(id);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("批量删除角色")
    @ApiOperation("批量删除角色")
    @RequiresPermissions("system:role:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R batchRemove(@RequestParam("ids[]") Integer[] ids)
    {
        for (Integer id : ids)
        {
            if (BaseIdUtils.isBaseRoleId(id))
            {
                String name = roleService.getNameById(id);
                if (name != null)
                {
                    return R.bind(Code.INVALID.getCode(), "[" + name + "] 为基础角色，不能删除!");
                }
            }
        }
        int result = roleService.batchRemove(ids);
        return result > 0 ? R.success() : R.unknown();
    }


    private static final String PREFIX = "system/role/";

    @ApiIgnore
    @RequiresPermissions("system:role:get")
    @GetMapping("/main")
    public String main()
    {
        return PREFIX + "main";
    }

    @ApiIgnore
    @RequiresPermissions("system:role:get")
    @GetMapping("/add")
    public String add()
    {
        return PREFIX + "add";
    }

    @ApiIgnore
    @RequiresPermissions("system:role:get")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model)
    {
        Role role = roleService.get(id);
        model.addAttribute("role", role);
        return PREFIX + "edit";
    }

    @ApiIgnore
    @RequiresPermissions("system:role:get")
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model)
    {
        Role role = roleService.get(id);
        model.addAttribute("role", role);
        return PREFIX + "view";
    }

    @SysLog("导出角色Excel")
    @ApiIgnore
    @RequiresPermissions("system:role:export")
    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, Role.class);
        PageUtils.cleanOrder(map);
        //
        List<Role> roles = roleService.list(map);
        ExcelUtils.export(response, roles, Role.class);
    }

    @SysLog("下载角色Excel模板")
    @ApiIgnore
    @RequiresPermissions("system:role:import")
    @GetMapping("/template")
    public void excelTemplate(HttpServletResponse response)
    {
        ExcelUtils.export(response, Role.class);
    }

    @SysLog("导入角色Excel")
    @ApiIgnore
    @RequiresPermissions("system:role:import")
    @PostMapping("/import")
    @ResponseBody
    public R excelImport(@RequestParam("file") MultipartFile file)
    {
        String filename = file.getOriginalFilename();
        System.out.println("**********Excel: " + filename);
        return R.success();
    }

}
