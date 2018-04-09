package com.example.demo.controller.HTMLCatch.HttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import com.app.comom.FileUtil;

/**
 *  HttpClient抓取页面处理样式的页面效果（从网站下载以txt格式文件写入html处理的css）
 */
public class HttpClientHtml2 {
    private static final String SITE = "login.goodjobs.cn";
    private static final int PORT = 80;
    private static final String loginAction = "/index.php/action/UserLogin";
    private static final String forwardURL = "http://user.goodjobs.cn/dispatcher.php/module/Personal/?skip_fill=1";
    private static final String toUrl = "d:\\test\\";
    private static final String hostCss  = "d:\\test\\style.txt";
    private static final String Img = "http://user.goodjobs.cn/images";
    private static final String _JS = "http://user.goodjobs.cn/scripts/fValidate/fValidate.one.js";
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
    private static String  createHtml(HttpClient client, String newUrl) throws  Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String filePath = toUrl + format.format(new Date() )+ "_" + 1 + ".html";
        PostMethod post = new PostMethod(newUrl);
        client.executeMethod(post);
        //设置编码
        post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "GBK");
        String content= post.getResponseBodyAsString();
     //   FileUtil.write(content, filePath);
        System.out.println("\n写入文件成功!");
        post.releaseConnection();
        return filePath;
    }
    /**
     * 解析html代码
     * @param filePath
     * @param random
     * @return
     */
    private static String JsoupFile(String filePath, int random) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        File infile = new File(filePath);
        String url = toUrl + format.format(new Date()) + "_new_" + random+ ".html";

        try {
            File outFile = new File(url);
            Document doc = Jsoup.parse(infile, "GBK");
            String html="<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>";
            StringBuffer sb = new StringBuffer();
            sb.append(html).append("\n");
            sb.append("<html>").append("\n");
            sb.append("<head>").append("\n");
            sb.append("<title>欢迎使用新安人才网个人专区</title>").append("\n");
            Elements meta = doc.getElementsByTag("meta");
            sb.append(meta.toString()).append("\n");
            /////////////////////////////本地css////////////////////////////

            File cssFile = new File(hostCss);
            BufferedReader in = new BufferedReader(new FileReader(cssFile));
            Writer out = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(outFile), "gbk"));
            String content=in.readLine();
            while(content!=null){
                //System.out.println(content);
                sb.append(content+"\n");
                content=in.readLine();
            }

            in.close();
            ////////////////////////////处理body标签//////////////////////////
            Elements body = doc.getElementsByTag("body");


            ////////////////////////////处理script标签//////////////////////////
            Elements scripts = doc.select("script");//对script标签
            for (Element js : scripts) {
                String jsrc = js.attr("src");
                if (jsrc.contains("/fValidate.one.js")) {
                    String oldJS="/scripts/fValidate/fValidate.one.js";//之前的css
                    jsrc = jsrc.replace(oldJS,_JS);
                    Element val=js.attr("src", jsrc);//修改href的属性值
                    sb.append(val.toString()).append("\n").append("</head>");
                }
            }

            ////////////////////////////处理所有src的属性值//////////////////////////
            Elements tags = body.select("*");//对所有标签有src的路径都作处理
            for (Element tag : tags) {
                String src = tag.attr("src");
                if (src.contains("/images")) {
                    src = src.replace("/images",Img);
                    tag.attr("src", src);//修改src的属性值
                }
            }

            sb.append(body.toString());
            sb.append("</html>");

            out.write(sb.toString());
            in.close();

            System.out.println("页面已经爬完");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static void main(String[] args) throws Exception {
        String [] params={"admin","admin123"};
        HttpClient client = loginHtml(SITE, PORT, loginAction,params);
        // 页面生成
        String path=createHtml(client, forwardURL);
        System.out.println( JsoupFile(path,1));
    }

}