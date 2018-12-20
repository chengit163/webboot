package com.cit.web.system.controller;

import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.Code;
import com.cit.web.common.constant.R;
import com.cit.web.common.util.BaseIdUtils;
import com.cit.web.common.util.PageUtils;
import com.cit.web.system.entity.Menu;
import com.cit.web.system.service.MenuService;
import com.cit.web.system.validator.MenuValidator;
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
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "菜单控制器")
@RequestMapping("/system/menu")
@Controller
public class MenuController
{
    @Autowired
    private MenuService menuService;

    @SysLog("查询菜单列表")
    @ApiOperation("查询菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "父菜单ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "url", value = "路径", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "permission", value = "权限", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "icon", value = "图标", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "orders", value = "排序", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "gmtCreate", value = "创建时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "gmtModified", value = "最近修改时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "排序字段", dataType = "string", paramType = "query", allowableValues = "id,pid,name,url,permission,icon,type,orders,gmtCreate,gmtModified"),
            @ApiImplicitParam(name = "order", value = "排序规则", dataType = "string", paramType = "query", allowableValues = "ASC,DESC")
    })
    @RequiresPermissions("system:menu:get")
    @GetMapping
    @ResponseBody
    public Map<String, Object> list(@ApiIgnore @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, Menu.class);
        PageUtils.cleanOrder(map);
        //页码
        int pageNum = PageUtils.getPageNum(map);
        int pageSize = PageUtils.getPageSize(map);
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<Menu> menus = menuService.list(map);
        Page<Menu> page = (Page<Menu>) menus;
        //结果
        Map<String, Object> result = PageUtils.getResult(page.getTotal(), page);
        return result;
    }

    @SysLog("获取菜单详情")
    @ApiOperation("获取菜单详情")
    @ApiImplicitParam(name = "id", value = "菜单ID(主键)", dataType = "int", paramType = "path", required = true)
    @RequiresPermissions("system:menu:get")
    @GetMapping("/{id}")
    @ResponseBody
    public Menu get(@PathVariable("id") Integer id)
    {
        Menu menu = menuService.get(id);
        return menu;
    }

    @SysLog("新增菜单")
    @ApiOperation("新增菜单")
    @RequiresPermissions("system:menu:save")
    @PostMapping
    @ResponseBody
    public R save(Menu menu)
    {
        //校验
        String message = MenuValidator.validate(menu, false);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        int result = menuService.save(menu);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("更新菜单")
    @ApiOperation("更新菜单")
    @RequiresPermissions("system:menu:update")
    @PutMapping
    @ResponseBody
    public R update(Menu menu)
    {
        //校验
        String message = MenuValidator.validate(menu, true);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        int result = menuService.update(menu);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("删除菜单")
    @ApiOperation("删除菜单")
    @ApiImplicitParam(name = "id", value = "菜单ID(主键)", dataType = "int", paramType = "query", required = true)
    @RequiresPermissions("system:menu:remove")
    @DeleteMapping
    @ResponseBody
    public R remove(@RequestParam(value = "id", defaultValue = "0") Integer id)
    {
        if (BaseIdUtils.isBaseMenuId(id))
        {
            String name = menuService.getNameById(id);
            if (name != null)
            {
                return R.bind(Code.INVALID.getCode(), "[" + name + "] 为基础菜单，不能删除!");
            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("pid", Integer.toString(id));
        int count = menuService.count(map);
        if (count > 0)
        {
            return R.bind(Code.INVALID.getCode(), "请先删除子目录！");
        }
        int result = menuService.remove(id);
        return result > 0 ? R.success() : R.unknown();
    }

//    @SysLog("批量删除菜单")
//    @ApiOperation("批量删除菜单")
//    @RequiresPermissions("system:menu:batchRemove")
//    @PostMapping("/batchRemove")
//    @ResponseBody
//    public R batchRemove(@RequestParam("ids[]") Integer[] ids)
//    {
//        for (Integer id : ids)
//        {
//            if (BaseIdUtils.isBaseMenuId(id))
//            {
//                String name = menuService.getNameById(id);
//                if (name != null)
//                {
//                    return R.bind(Code.INVALID.getCode(), "[" + name + "] 为基础菜单，不能删除!");
//                }
//            }
//        }
//        int result = menuService.batchRemove(ids);
//        return result > 0 ? R.success() : R.unknown();
//    }

    @SysLog("所有菜单列表")
    @ApiOperation("所有菜单列表")
    @RequiresPermissions("system:menu:get")
    @GetMapping("/all")
    @ResponseBody
    public Map<String, Object> all(@RequestParam("sort") String sort, @RequestParam("order") String order)
    {
        Map<String, Object> map = PageUtils.cleanSortOrder(sort, order, Menu.class);
        List<Menu> menus = menuService.list(map);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", menus);
        return result;
    }


    private static final String PREFIX = "system/menu/";

    @ApiIgnore
    @RequiresPermissions("system:menu:get")
    @GetMapping("/main")
    public String main()
    {
        return PREFIX + "main";
    }

    @ApiIgnore
    @RequiresPermissions("system:menu:get")
    @GetMapping("/add/{pid}")
    public String add(@PathVariable("pid") Integer pid, Model model)
    {
        Menu pmenu = menuService.get(pid);
        model.addAttribute("pid", pmenu != null ? pmenu.getId() : 0);
        model.addAttribute("pname", pmenu != null ? pmenu.getName() : "根目录");
        return PREFIX + "add";
    }

    @ApiIgnore
    @RequiresPermissions("system:menu:get")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model)
    {
        Menu menu = menuService.get(id);
        Menu pmenu = menuService.get(menu.getPid());
        model.addAttribute("menu", menu);
        model.addAttribute("pname", pmenu != null ? pmenu.getName() : "根目录");
        return PREFIX + "edit";
    }

    @ApiIgnore
    @RequiresPermissions("system:menu:get")
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model)
    {
        Menu menu = menuService.get(id);
        model.addAttribute("menu", menu);
        return PREFIX + "view";
    }

}
