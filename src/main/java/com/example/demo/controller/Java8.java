package com.example.demo.controller;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.amazonaws.annotation.SdkTestInternalApi;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jerry-jx on 2017/12/8.
 */
public class Java8
{

    private static final String COMMISSION_DEL_TABLE = "balance.commission_calc,balance.team_statements_day,balance.user_statements,"
        + "balance.team_statements_week,balance.user_statements_week,balance.team_statements_month,balance.user_statements_month,"
        + "balance.effective_bet_event_0,balance.effective_bet_event_1,balance.effective_bet_event_2,balance.effective_bet_event_3,balance.effective_bet_event_4,"
        + "balance.effective_bet_event_5,balance.effective_bet_event_6,balance.effective_bet_event_7,balance.effective_bet_event_8,balance.effective_bet_event_9,"
        + "balance.effective_bet_event_10,balance.effective_bet_event_11,balance.effective_bet_event_12,balance.effective_bet_event_13,balance.effective_bet_event_14,"
        + "balance.effective_bet_event_15,balance.effective_bet_event_16,balance.effective_bet_event_17,balance.effective_bet_event_18,balance.effective_bet_event_19,"
        + "balance.effective_bet_event_20,balance.effective_bet_event_21,balance.effective_bet_event_22,balance.effective_bet_event_23,balance.effective_bet_event_24,"
        + "balance.effective_bet_event_25,balance.effective_bet_event_26,balance.effective_bet_event_27,balance.effective_bet_event_28,balance.effective_bet_event_29,"
        + "account.del_users_audit,account.del_users_his";

    public static void main(String [] args){
        //sortTest();

        /*List<String> delTableList = Arrays.asList(COMMISSION_DEL_TABLE.split(","));

        delTableList.forEach(table -> {
            System.out.println(delTableList.contains("balance.effective_bet_event_16"));
            }
        );*/

        System.out.println(trimPunct("把我的标点符号去掉吧，全科。"));
    }


    public static void sortTest(){
        List<String> names = Arrays.asList("peter", "anna", "mike", "xenia");
        Collections.sort(names,( a,  b) ->
            a.compareTo(b));
        System.out.println(names);
        trimPunct("把我的标点符号去掉吧，全科。");
    }

    /**
     * 删除所有的标点符号
     *
     * @param str 处理的字符串
     */
    public  static String trimPunct(String str) {
        if(isEmpty(str)){
            return "";
        }
        return str.replaceAll("[\\pP\\p{Punct}]", "");
    }

}
