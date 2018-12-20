package com.cit.web.system.controller;

import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.Code;
import com.cit.web.common.constant.Constants;
import com.cit.web.common.constant.R;
import com.cit.web.common.util.BaseIdUtils;
import com.cit.web.common.util.ExcelUtils;
import com.cit.web.common.util.PageUtils;
import com.cit.web.common.util.ShiroUtils;
import com.cit.web.system.entity.User;
import com.cit.web.system.service.UserService;
import com.cit.web.system.validator.UserValidator;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(description = "用户控制器")
@RequestMapping("/system/user")
@Controller
public class UserController
{
    @Autowired
    private UserService userService;

    @SysLog("查询用户列表")
    @ApiOperation("查询用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "nickname", value = "昵称", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "lastLoginTime", value = "最近登陆时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "lastLoginIp", value = "最近登陆IP", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "userIdCreate", value = "创建用户ID", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "gmtCreate", value = "录入时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "gmtModified", value = "最近修改时间", dataType = "date-time", paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "页码", dataType = "int", paramType = "query", defaultValue = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", dataType = "int", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "sort", value = "排序字段", dataType = "string", paramType = "query", allowableValues = "id,username,nickname,phone,email,lastLoginTime,lastLoginIp,userIdCreate,status,gmtCreate,gmtModified"),
            @ApiImplicitParam(name = "order", value = "排序规则", dataType = "string", paramType = "query", allowableValues = "ASC,DESC")
    })
    @RequiresPermissions("system:user:get")
    @GetMapping
    @ResponseBody
    public Map<String, Object> list(@ApiIgnore @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, User.class);
        PageUtils.cleanOrder(map);
        //页码
        int pageNum = PageUtils.getPageNum(map);
        int pageSize = PageUtils.getPageSize(map);
        //分页
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userService.list(map);
        Page<User> page = (Page<User>) users;
        //结果
        Map<String, Object> result = PageUtils.getResult(page.getTotal(), page);
        return result;
    }

    @SysLog("获取用户详情")
    @ApiOperation("获取用户详情")
    @ApiImplicitParam(name = "id", value = "用户ID(主键)", dataType = "int", paramType = "path", required = true)
    @RequiresPermissions("system:user:get")
    @GetMapping("/{id}")
    @ResponseBody
    public User get(@PathVariable("id") Integer id)
    {
        User user = userService.get(id);
        return user;
    }

    @SysLog("新增用户")
    @ApiOperation("新增用户")
    @RequiresPermissions("system:user:save")
    @PostMapping
    @ResponseBody
    public R save(User user)
    {
        //校验
        String message = UserValidator.validate(user, false);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        String username = user.getUsername();
        if (userService.getByUsername(username) != null)
        {
            return R.bind(Code.INVALID.getCode(), "用户名已存在");
        }
        // 密码加盐
        String password = user.getPassword();
        password = new Md5Hash(password, username).toString();
        password = new Md5Hash(password, Constants.PACKAGE_NAME).toString();
        user.setPassword(password);
        //
        user.setStatus(1);
        user.setLastLoginTime(null); // 最近登陆时间，不能人为修改
        user.setLastLoginIp(null);// 最近登陆IP，不能人为修改
        user.setUserIdCreate(ShiroUtils.getUserId());//创建用户ID，不能人为修改
        user.setGmtCreate(new Date());// 创建时间，不能人为修改
        user.setGmtModified(null);
        //
        int result = userService.save(user);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("更新用户")
    @ApiOperation("更新用户")
    @RequiresPermissions("system:user:update")
    @PutMapping
    @ResponseBody
    public R update(User user)
    {
        //校验
        String message = UserValidator.validate(user, true);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        // 密码加盐
        String password = user.getPassword();
        if (StringUtils.isNotEmpty(password))
        {
            String username = userService.getUsernameById(user.getId());
            password = new Md5Hash(password, username).toString();
            password = new Md5Hash(password, Constants.PACKAGE_NAME).toString();
            user.setPassword(password);
        }
        user.setLastLoginTime(null); // 最近登陆时间，不能人为修改
        user.setLastLoginIp(null);// 最近登陆IP，不能人为修改
        user.setUserIdCreate(null);//创建用户ID，不能人为修改
        user.setGmtCreate(null);// 创建时间，不能人为修改
        user.setGmtModified(new Date());
        int result = userService.update(user);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("删除用户")
    @ApiOperation("删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID(主键)", dataType = "int", paramType = "query", required = true)
    @RequiresPermissions("system:user:remove")
    @DeleteMapping
    @ResponseBody
    public R remove(@RequestParam(value = "id", defaultValue = "0") Integer id)
    {
        if (BaseIdUtils.isBaseUserId(id))
        {
            String username = userService.getUsernameById(id);
            if (username != null)
            {
                return R.bind(Code.INVALID.getCode(), "[" + username + "] 为基础用户，不能删除!");
            }
        }
        int result = userService.remove(id);
        return result > 0 ? R.success() : R.unknown();
    }

    @SysLog("批量删除用户")
    @ApiOperation("批量删除用户")
    @RequiresPermissions("system:user:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    public R batchRemove(@RequestParam("ids[]") Integer[] ids)
    {
        for (Integer id : ids)
        {
            if (BaseIdUtils.isBaseUserId(id))
            {
                String username = userService.getUsernameById(id);
                if (username != null)
                {
                    return R.bind(Code.INVALID.getCode(), "[" + username + "] 为基础用户，不能删除!");
                }
            }
        }
        int result = userService.batchRemove(ids);
        return result > 0 ? R.success() : R.unknown();
    }


    private static final String PREFIX = "system/user/";

    @ApiIgnore
    @RequiresPermissions("system:user:get")
    @GetMapping("/main")
    public String main()
    {
        return PREFIX + "main";
    }

    @ApiIgnore
    @RequiresPermissions("system:user:get")
    @GetMapping("/add")
    public String add()
    {
        return PREFIX + "add";
    }

    @ApiIgnore
    @RequiresPermissions("system:user:get")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model)
    {
        User user = userService.get(id);
        model.addAttribute("user", user);
        return PREFIX + "edit";
    }

    @ApiIgnore
    @RequiresPermissions("system:user:get")
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Integer id, Model model)
    {
        User user = userService.get(id);
        model.addAttribute("user", user);
        return PREFIX + "view";
    }

    @SysLog("导出用户Excel")
    @ApiIgnore
    @RequiresPermissions("system:user:export")
    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam(required = false) Map<String, Object> map)
    {
        PageUtils.cleanSort(map, User.class);
        PageUtils.cleanOrder(map);
        //
        List<User> users = userService.list(map);
        ExcelUtils.export(response, users, User.class);
    }

    @SysLog("下载用户Excel模板")
    @ApiIgnore
    @RequiresPermissions("system:user:import")
    @GetMapping("/template")
    public void excelTemplate(HttpServletResponse response)
    {
        ExcelUtils.export(response, User.class);
    }

    @SysLog("导入用户Excel")
    @ApiIgnore
    @RequiresPermissions("system:user:import")
    @PostMapping("/import")
    @ResponseBody
    public R excelImport(@RequestParam("file") MultipartFile file)
    {
        String filename = file.getOriginalFilename();
        System.out.println("**********Excel: " + filename);
        return R.success();
    }


    // validate
    @ApiIgnore
    @RequiresPermissions("system:user:get")
    @PostMapping("/validateUsername")
    @ResponseBody
    public R validateUsername(String username)
    {
        User user = userService.getByUsername(username);
        return user == null ? R.validOk() : R.validError();
    }


    // password
    @SysLog("进入修改密码")
    @ApiIgnore
    @RequiresPermissions("system:user:password")
    @GetMapping("/password/{id}")
    public String password(@PathVariable("id") Integer id, Model model)
    {
        String username = userService.getUsernameById(id);
        model.addAttribute("id", id);
        model.addAttribute("username", username);
        return PREFIX + "password";
    }

    @SysLog("保存修改密码")
    @ApiIgnore
    @RequiresPermissions("system:user:password")
    @PostMapping("/password")
    @ResponseBody
    public R password(@RequestParam("id") Integer id, @RequestParam("password") String password)
    {
        //校验
        password = StringUtils.trim(password);
        String message = UserValidator.validatePassword(password, false);
        if (message != null)
        {
            return R.bind(Code.INVALID.getCode(), message);
        }
        // 密码加盐
        String username = userService.getUsernameById(id);
        password = new Md5Hash(password, username).toString();
        password = new Md5Hash(password, Constants.PACKAGE_NAME).toString();
        int result = userService.updatePasswordById(id, password);
        return result > 0 ? R.success() : R.unknown();
    }

}
