package com.example.demo.controller;

import com.amazonaws.annotation.SdkTestInternalApi;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jerry-jx on 2017/12/8.
 */
public class Java8
{

    public static void main(String [] args){
        sortTest();
    }


    public static void sortTest(){
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        Collections.sort(names,( a,  b) ->
            a.compareTo(b));
        System.out.println(names);
    }


}
