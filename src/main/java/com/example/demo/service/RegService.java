package com.example.demo.service;

import com.example.demo.mapper.UserMapper;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jerry-jx on 2017/8/8.
 */
@Service()
public class RegService implements IRegService {
    //@Autowired
    @Resource
    private UserMapper userMapper;

    @Override
    public int regUser(String userId) {
        int count;
        try {
            count = userMapper.getUserCount(userId);
        }catch (Exception e){
            return 0;
        }
        return count;
    }

    @Override
    public boolean userLogin(String userPassword, String username) {
        return false;
    }



}
