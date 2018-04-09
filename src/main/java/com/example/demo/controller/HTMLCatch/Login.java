package com.example.demo.controller.HTMLCatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by jerry-jx on 2018/4/5.
 */
@Component
public class Login   {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 获取当月第一天 
     *
     * @return
     */
    public String getFirstDayOfMonth() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号  
        return sdf.format(lastDate.getTime());

    }

    /**
     * 计算当月最后一天
     * @return
     */
    public String getLastDayOfMonth() {

        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号  
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号  
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天  
        return sdf.format(lastDate.getTime());

    }

    //测试
    public static void main(String args[]) throws Exception {
        Login l = new Login();
        l.login();
    }


    private static CookieStore cs = new BasicCookieStore();

    //利用spring定时器 每天早上10点一刻抓取并发邮件
    @Scheduled(cron = "0 15 10 ? * *")
    public void login() throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        // 创建一个本地上下文信息
        HttpContext localContext = new BasicHttpContext();
        // 在本地上下问中绑定一个本地存储
        localContext.setAttribute(ClientContext.COOKIE_STORE, cs);
        // 目标地址
        HttpPost httppost = new HttpPost(
            "http://***/userLogin.do");
        // 传参
        StringEntity reqEntity = new StringEntity(
            "userName=jianancun&password=123456&randNum=565656");
        // 设置类型
        reqEntity.setContentType("application/x-www-form-urlencoded");
        // 设置请求的数据
        httppost.setEntity(reqEntity);
        // 执行
        HttpResponse response = httpclient.execute(httppost);
        //取得所有头内容
        Header[] headers = response.getAllHeaders();
        for (Header h : headers) {
            String name = h.getName();
            String value = h.getValue();
            System.out.println("header : " + h.getName() + ":" + h.getValue());
            if ("Set-Cookie".equalsIgnoreCase(name)) {
                String[] strs = value.split(";");
                for (String str : strs) {
                    String[] cookies = str.split("=");
                    //输出cookie名称及标题
                    // System.out.println("=============== : " + cookies[0] + ":" + cookies[1]);
                    cs.addCookie(new BasicClientCookie(cookies[0], cookies[1]));
                }
                cs.addCookie(new BasicClientCookie("userId", "8888"));
                cs.addCookie(new BasicClientCookie("userName", "jiannancun"));
                cs.addCookie(new BasicClientCookie("state", "0"));
                cs.addCookie(new BasicClientCookie("iAdmin", "0"));
                cs.addCookie(new BasicClientCookie("depCode", "0"));
            }
        }
        HttpEntity entity = response.getEntity();
        // 显示结果
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            entity.getContent(), "UTF-8"));
        String line = null;
        //返回是否登录成功的内容 忽略
        while ((line = reader.readLine()) != null) {
            //  System.out.println(line);
        }



        //可以添加多个用户
        String jlc[] ={URLEncoder.encode("贱男春"),"jiannancun@*.cn","888888"};
        List<String[]> list = new ArrayList<String[]>();
        list.add(jlc);

        for(String []u:list){
            //查询本月考勤内容
            String logPath = "http://**.cn/timeCard.jsp?nickName="+u[0]+"&eplTimeCard="
                +u[2]+"&begDate="+getFirstDayOfMonth()+"&endDate="+getLastDayOfMonth();
            String content= getContent(logPath);
            Document doc = Jsoup.parse(content);
            Elements tds = doc.select("table td");
            int i =0;
            //返回的内容
            String html =""; ;
            for (Element td : tds) {
                //取前25行内容   前25行内容显示的是昨天和今天的考勤记录 
                if(i<25){
                    html+=td.text().replace(u[2], URLDecoder.decode(u[0]))+"<br>" ;
                } else {
                    break;
                }

                i++;
            }
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("mailTo",u[1]);
            map.put("mailTitle","考勤提醒");
            map.put("messageBody",html);
            //发送邮件
            //sendMail(map);
        }
    }
    /**
     *  Function: 请求地址并返回页面内容
     *  @author JNC
     *  @param url
     *  @return
     *  @throws Exception
     */
    private String getContent(String url) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String cookieStr = "";
        List<Cookie> list = cs.getCookies();
        for (Cookie cookie : list) {
            cookieStr += cookie.getName() + "=" + cookie.getValue() + ";";
        }
        // 请求目标地址并带上cookie
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Cookie", cookieStr);
        // 执行
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String resultStr ="";
        BufferedReader reader = new BufferedReader(new InputStreamReader(
            entity.getContent(), "UTF-8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            resultStr+=line;
        }
        return resultStr;
    }
}
