package com.example.demo.service;

/**
 * Created by jerry-jx on 2017/8/8.
 */
public interface IRegService {
    int regUser(String uerId);
    boolean userLogin(String userPassword,String username);
}
