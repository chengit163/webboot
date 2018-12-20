package com.cit.web.system.controller;

import com.cit.web.common.entity.Tree;
import com.cit.web.common.util.ShiroUtils;
import com.cit.web.common.util.TreeUtils;
import com.cit.web.system.entity.Rabc;
import com.cit.web.system.entity.User;
import com.cit.web.system.service.RabcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@RequestMapping("/index")
@Controller
public class IndexController
{
    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    private static final String prefix = "index/";

    @Autowired
    private RabcService rabcService;

    @GetMapping
    public String index(Model model)
    {
        // 用户
        User user = ShiroUtils.getUser();
        Integer userId = ShiroUtils.getUserId();
        // 菜单
        List<Rabc> list = rabcService.listMenuByUserId(userId);
        Tree<Rabc> root = TreeUtils.formatTree(list);
        //
        model.addAttribute("user", user);
        model.addAttribute("menus", root.getChildren());
        return "index";
    }


    @GetMapping("/welcome")
    public String welcome()
    {
        return prefix + "welcome";
    }
}
