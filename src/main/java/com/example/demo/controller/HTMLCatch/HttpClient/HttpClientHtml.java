package com.example.demo.controller.HTMLCatch.HttpClient;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 *  HttpClient抓取页面 未处理样式的
 */
public class HttpClientHtml {
    private static final String SITE = "login.goodjobs.cn";
    private static final int PORT = 80;
    private static final String loginAction = "/index.php/action/UserLogin";
    private static final String forwardURL = "http://user.goodjobs.cn/dispatcher.php/module/Personal/?skip_fill=1";

    /**
     * 模拟等录
     * @param LOGON_SITE
     * @param LOGON_PORT
     * @param login_Action
     * @param params
     * @throws Exception
     */
    private static HttpClient loginHtml(String LOGON_SITE, int LOGON_PORT,String login_Action,String ...params) throws Exception {
        HttpClient client = new HttpClient();
        client.getHostConfiguration().setHost(LOGON_SITE, LOGON_PORT);
        // 模拟登录页面
        PostMethod post = new PostMethod(login_Action);
        NameValuePair userName = new NameValuePair("memberName",params[0] );
        NameValuePair password = new NameValuePair("password",params[1] );
        post.setRequestBody(new NameValuePair[] { userName, password });
        client.executeMethod(post);
        post.releaseConnection();
        // 查看cookie信息
        CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
        Cookie[] cookies = cookiespec.match(LOGON_SITE, LOGON_PORT, "/", false,
            client.getState().getCookies());
        if (cookies != null)
            if (cookies.length == 0) {
                System.out.println("Cookies is not Exists ");
            } else {
                for (int i = 0; i < cookies.length; i++) {
                    System.out.println(cookies[i].toString());
                }
            }
        return client;
    }
    /**
     * 模拟等录 后获取所需要的页面
     * @param client
     * @param newUrl
     * @throws Exception
     */
    private static void createHtml(HttpClient client, String newUrl)
        throws  Exception {
        PostMethod post = new PostMethod(newUrl);
        client.executeMethod(post);
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
        String content= post.getResponseBodyAsString();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        //IOUtils.write(content, new FileOutputStream("d:/"+format.format(new Date())+".html"),"GBK");
        write(content,"d:/"+format.format(new Date())+".html");
        post.releaseConnection();
    }



    public static void main(String[] args) throws Exception {
        String [] params={"admin","admin123"};
        HttpClient client = loginHtml(SITE, PORT, loginAction,params);
        // 访问所需的页面
        createHtml(client, forwardURL);

        //System.out.println(UUID.randomUUID());
    }


    /**
     *
     * @param content
     * @param htmlPath
     * @return
     */
    public static boolean write(String content, String htmlPath) {
        boolean flag = true;
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlPath), "GBK"));
            out.write("\n" + content);
            out.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return flag;
    }
}
