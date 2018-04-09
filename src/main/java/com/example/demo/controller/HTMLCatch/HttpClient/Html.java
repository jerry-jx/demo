package com.example.demo.controller.HTMLCatch.HttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * HttpURLConnection 和HttpClient+Jsoup处理标签抓取页面和模拟登录
 */
public class Html {
    private static final String loginURL = "http://login.goodjobs.cn/index.php/action/UserLogin";
    private static final String forwardURL = "http://user.goodjobs.cn/dispatcher.php/module/Personal/?skip_fill=1";

    /**
     * 获取登录页面请求
     * @param /loginUrl登录URL
     * @param /params登录用户名/密码参数
     * @throws Exception
     */
    public static String  createHtml(String...params)throws Exception{
        URL url = new URL(loginURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        loginHtml(conn, params);
        return forwardHtml(conn,url);
    }
    /**
     * 登录页面
     * @param conn
     * @param /params登录用户名/密码参数
     * @throws Exception
     */
    private static void loginHtml(HttpURLConnection conn, String... params)
        throws Exception {
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "GBK");
        StringBuffer buff=new StringBuffer();
        buff.append("memberName="+URLEncoder.encode(params[0], "UTF-8"));//页面用户名
        buff.append("&password="+URLEncoder.encode(params[1],"UTF-8"));//页面密码
        out.write(buff.toString());//填充参数
        out.flush();
        out.close();
    }
    /**
     * 转向到定向的页面
     * @param /conn连接对象
     * @param /url重新定向请求URL
     * @param /toUrl定向到页面请求URL
     * @throws Exception
     */
    public static String forwardHtml(HttpURLConnection conn,URL url)throws Exception{
        //重新打开一个连接
        String cookieVal = conn.getHeaderField("Set-Cookie");
        url = new URL(forwardURL);
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("User-Agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; Foxy/1; .NET CLR 2.0.50727;MEGAUPLOAD 1.0)");
        conn.setFollowRedirects(false);//置此类是否应该自动执行 HTTP 重定向
        // 取得cookie,相当于记录了身份,供下次访问时使用
        if (cookieVal != null) {
            //发送cookie信息上去,以表明自己的身份,否则会被认为没有权限
            conn.setRequestProperty("Cookie", cookieVal);
        }
        conn.connect();
        InputStream in = conn.getInputStream();
        BufferedReader buffReader = new BufferedReader( new InputStreamReader(in,"GBK"));
        String line = null;
        String content = "";
        while ((line = buffReader.readLine()) != null) {
            content +="\n" +line;
        }
        //IOUtils.write(result, new FileOutputStream("d:/index.html"),"GBK");
        write(content, "d:/forward.html");
        buffReader.close();
        return content;
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


    public static void main(String[] args)throws Exception{
        String [] params={"admin","admin12"};
        System.out.println(createHtml(params));
    }
}
