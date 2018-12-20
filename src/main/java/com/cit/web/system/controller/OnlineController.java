package com.cit.web.system.controller;

import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.R;
import com.cit.web.common.redis.online.OnlineManager;
import com.cit.web.system.entity.Online;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "在线控制器")
@RequestMapping("/system/online")
@Controller
public class OnlineController
{

    @Autowired
    private OnlineManager onlineManager;

    @SysLog("查询在线")
    @ApiOperation("查询在线")
    @RequiresPermissions("system:online:get")
//    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    @GetMapping
    @ResponseBody
    public Map<String, Object> list()
    {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("time", System.currentTimeMillis());
        List<Online> list = onlineManager.listOnline();
        result.put("total", list.size());
        result.put("data", list);
        return result;
    }

    @SysLog("强制下线")
    @ApiOperation("强制下线")
    @RequiresPermissions("system:online:remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "会话ID", dataType = "string", paramType = "query")
    })
//    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.PUT})
    @DeleteMapping
    @ResponseBody
    public R remove(@RequestParam(value = "id") String id)
    {
        return onlineManager.removeOnline(id) ? R.success() : R.unknown();
    }


    private static final String PREFIX = "system/online/";

    @ApiIgnore
    @RequiresPermissions("system:online:get")
    @GetMapping("/main")
    public String main()
    {
        return PREFIX + "main";
    }
}
