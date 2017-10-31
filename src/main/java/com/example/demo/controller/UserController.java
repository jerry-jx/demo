/*
package com.example.demo.controller;

*/
/**
 * Created by jerry-jx on 2017/10/27.
 *//*

import java.util.List;

import org.andy.work.entity.AcctUser;
import org.andy.work.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

*/
/**
 * 创建时间：2015-2-7 上午11:49:00
 * @author andy
 * @version 2.2
 * 描述： 用户Controller
 *//*

@Controller
@RequestMapping(/user)
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(/showInfo/{userId})
    public String showUserInfo(ModelMap modelMap, @PathVariable String userId){
        LOGGER.info(查询用户： + userId);
        AcctUser userInfo = userService.load(userId);
        modelMap.addAttribute(userInfo, userInfo);
        return /user/showInfo;
    }

    @RequestMapping(/showInfos)
    public @ResponseBody List showUserInfos(){
        LOGGER.info(查询用户全部用户);
        List userInfos = userService.findAll();
        return userInfos;
    }


    @RequestMapping(/main)
    public String main(ModelMap modelMap){
        LOGGER.info(显示主页面);
        //后台获取security保存的session中的用户信息

        //获取security的上下文
        SecurityContext securityContext = SecurityContextHolder.getContext();
        //获取认证对象
        Authentication authentication = securityContext.getAuthentication();
        //在认证对象中获取主体对象
        Object principal = authentication.getPrincipal();

        String username = ;
        if(principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }else {
            username = principal.toString();
        }
        modelMap.addAttribute(username, username);
        return /user/main;
    }


    @RequestMapping(/manage)
    public String manage(ModelMap modelMap){
        LOGGER.info(显示主页面);
        modelMap.addAttribute(msg, manage);
        return /user/option;
    }

    @RequestMapping(/save)
    public String save(ModelMap modelMap){
        LOGGER.info(保存);
        modelMap.addAttribute(msg, save);
        return /user/option;
    }

    @RequestMapping(/update)
    public String update(ModelMap modelMap){
        LOGGER.info(修改);
        modelMap.addAttribute(msg, update);
        return /user/option;
    }

    @RequestMapping(/delete)
    public String delete(ModelMap modelMap){
        LOGGER.info(删除);
        modelMap.addAttribute(msg, delete);
        return /user/option;
    }

}*/
