package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jerry-jx on 2018/1/2.
 */
public class regxTest {
    public static void main(String[] args){
        //regxChinese();
        getContext2();
    }
    /*public static void regxChinese(){
        // 要匹配的字符串
        String source = "<span title='5 星级酒店' class='dx dx5'>";
        // 将上面要匹配的字符串转换成小写
        // source = source.toLowerCase();
        // www.111cn.net 匹配的字符串的正则表达式
        String reg_charset = "<span[^>]*?title='([0-9]*[\s|\S]*[u4E00-u9FA5]*)'[\s|\S]

            *class='[a-z]*[\s|\S]*[a-z]*[0-9]*'";

        Pattern p = Pattern.compile(reg_charset);
        Matcher m = p.matcher(source);
        while (m.find()) {
            System.out.println(m.group(1));
        }
    }*/

    public static void  getContext2() {
//String html="kk<p>123456</p>ssss";
        String html="ss。kk帖子。ssss。wqewq帖子。ssss帖子。gfhfgh帖子。ss";
        List<String> resultList = new ArrayList<String>();
        Pattern p = Pattern.compile("。(.*?)。");//匹配<p>开头，</p>结尾的文档
        Matcher m = p.matcher(html );//开始编译
        while (m.find()) {
            String str=m.group(1);
            if(str.contains("帖子")){
                resultList.add(str);//获取被匹配的部分
            }
        }
        System.out.println(resultList);
    }
}
