package com.example.demo.entity;

import com.example.demo.controller.ExcelField;
import lombok.Data;

/**
 * Created by jerry-jx on 2017/8/8.
 */

@Data
public class User {

    @ExcelField(title = "id")
    private Long id;

    @ExcelField(title = "用户id")
    private Long userId;

    @ExcelField(title = "密码")
    private String pwd;

}