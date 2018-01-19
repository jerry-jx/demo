package com.example.demo.controller;

import com.example.demo.service.IRegService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jerry-jx on 2017/8/8.
 */
@Controller
@EnableAutoConfiguration
public class HelloWorld {
    @Autowired
    private IRegService regService;


    @ApiOperation(value = "查询用户数",  tags = {"user"})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "查询用户数成功", response = String.class)})
    @RequestMapping(value = "/reg", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public void reg(@RequestParam("userId") String userId ){
       int count = regService.regUser(userId);
        //return String.valueOf(count);
    }

   /* @ApiOperation(value = "用户登录接口",  tags = {"user"})
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "用户登录成功", response = String.class)})
    @RequestMapping(value = "/reg", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public Object login(@RequestParam("loginPwd") String loginPwd, @RequestParam("username") String username ){
        String pwd = creatMD5(username);
        System.out.println(username+":"+username);
        boolean result = regService.userLogin(pwd,username);
        return result;
    }*/

    private String creatMD5(String loginNum){
        // 生成一个MD5加密计算摘要
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(loginNum.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, md.digest()).toString(16);
    }
}