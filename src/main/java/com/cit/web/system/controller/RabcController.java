package com.cit.web.system.controller;

import com.alibaba.fastjson.JSON;
import com.cit.web.common.annotation.SysLog;
import com.cit.web.common.constant.R;
import com.cit.web.common.entity.Tree;
import com.cit.web.common.redis.shiro.RedisSessionDAO;
import com.cit.web.common.util.ShiroUtils;
import com.cit.web.common.util.TreeUtils;
import com.cit.web.system.entity.Rabc;
import com.cit.web.system.entity.Role;
import com.cit.web.system.entity.User;
import com.cit.web.system.service.RabcService;
import com.cit.web.system.service.RoleService;
import com.cit.web.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@ApiIgnore
@RequestMapping("/system/rabc")
@Controller
public class RabcController
{
    private static final Logger logger = LoggerFactory.getLogger(RabcController.class);

    private static final String PREFIX = "system/rabc/" ;

    @Autowired
    private RabcService rabcService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionDAO sessionDAO;


    @SysLog("进入[角色-菜单]关系")
    @RequiresPermissions("rabc:role:menu:get")
    @GetMapping("/role-menu/{id}")
    public String toRoleMenu(@PathVariable("id") Integer id, Model model)
    {
        logger.debug("toRoleMenu: roleId = " + id);
        Role role = roleService.get(id);
        model.addAttribute("role", role);
        return PREFIX + "role-menu" ;
    }

    @SysLog("获取[角色-菜单]关系")
    @RequiresPermissions("rabc:role:menu:get")
    @PostMapping("/getRoleMenu")
    @ResponseBody
    public List<Tree<Rabc>> getRoleMenu(@RequestParam("roleId") Integer roleId)
    {
        logger.debug("getRoleMenu: roleId = " + roleId);
        List<Rabc> list = rabcService.getMenuOptions();
        Set<Integer> menuIds = rabcService.getMenuIdsByRoleId(roleId);
        //
        Tree<Rabc> root = TreeUtils.formatTree(list, menuIds);
        return root.getChildren();
    }

    @SysLog("保存[角色-菜单]关系")
    @RequiresPermissions("rabc:role:menu:save")
    @PostMapping("/saveRoleMenu")
    @ResponseBody
    public R saveRoleMenu(@RequestParam("roleId") Integer roleId, @RequestParam("menuIds[]") Integer[] menuIds)
    {
        logger.debug("getRoleMenu: roleId = " + roleId + ", menuIds = " + JSON.toJSONString(menuIds));
        int result = rabcService.saveRoleMenu(roleId, menuIds);
        markAuth();
        return result > 0 ? R.success() : R.unknown();
    }


    @SysLog("进入[用户-角色]关系")
    @RequiresPermissions("rabc:user:role:get")
    @GetMapping("/user-role/{id}")
    public String toUserRole(@PathVariable("id") Integer id, Model model)
    {
        logger.debug("toUserRole: userId = " + id);
        User user = userService.get(id);
        model.addAttribute("user", user);
        return PREFIX + "user-role" ;
    }

    @SysLog("获取[用户-角色]关系")
    @RequiresPermissions("rabc:user:role:get")
    @PostMapping("/getUserRole")
    @ResponseBody
    public List<Tree<Rabc>> getUserRole(@RequestParam("userId") Integer userId)
    {
        logger.debug("getUserRole: userId = " + userId);
        List<Rabc> list = rabcService.getRoleOptions();
        Set<Integer> roleIds = rabcService.getRoleIdsByUserId(userId);
        //
        Tree<Rabc> root = TreeUtils.formatTree(list, roleIds);
        return root.getChildren();
    }

    @SysLog("保存[用户-角色]关系")
    @RequiresPermissions("rabc:user:role:save")
    @PostMapping("/saveUserRole")
    @ResponseBody
    public R saveUserRole(@RequestParam("userId") Integer userId, @RequestParam("roleIds[]") Integer[] roleIds)
    {
        logger.debug("saveUserRole: userId = " + userId + ", roleIds = " + JSON.toJSONString(roleIds));
        int result = rabcService.saveUserRole(userId, roleIds);
        markAuth();
        return result > 0 ? R.success() : R.unknown();
    }

    /**
     * 标记需要重新加载权限
     */
    private void markAuth()
    {
        Collection<Session> sessions = sessionDAO.getActiveSessions();
        if (sessions != null && !sessions.isEmpty())
        {
            for (Session s : sessions)
            {
                ShiroUtils.markAuthBysessionId(s.getId());
            }
        }
    }

}
