/*
package com.example.demo.controller.HTMLCatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import sun.net.www.http.HttpClient;

*/
/**
 * Created by jerry-jx on 2018/3/20.
 *//*

public class HttpHTMLClient {

    // 第一种方法
//这种方法是用apache提供的包,简单方便
//但是要用到以下包:commons-codec-1.4.jar
// commons-httpclient-3.1.jar
// commons-logging-1.0.4.jar
    public static String createhttpClient(String url, String param) {
        HttpClient client = new HttpClient();
        String response = null;
        String keyword = null;
        PostMethod postMethod = new PostMethod(url);
//  try {
//   if (param != null)
//    keyword = new String(param.getBytes("gb2312"), "ISO-8859-1");
//  } catch (UnsupportedEncodingException e1) {
//   // TODO Auto-generated catch block
//   e1.printStackTrace();
//  }
        // NameValuePair[] data = { new NameValuePair("keyword", keyword) };
        // // 将表单的值放入postMethod中
        // postMethod.setRequestBody(data);
        // 以上部分是带参数抓取,我自己把它注销了．大家可以把注销消掉研究下
        try {
            int statusCode = client.executeMethod(postMethod);
            response = new String(postMethod.getResponseBodyAsString()
                .getBytes("ISO-8859-1"), "gb2312");
            //这里要注意下 gb2312要和你抓取网页的编码要一样
            String p = response.replaceAll("//&[a-zA-Z]{1,10};", "")
                .replaceAll("<[^>]*>", "");//去掉网页中带有html语言的标签
            System.out.println(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    // 第二种方法
// 这种方法是JAVA自带的URL来抓取网站内容
    public String getPageContent(String strUrl, String strPostRequest,
        int maxLength) {
        // 读取结果网页
        StringBuffer buffer = new StringBuffer();
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "5000");
        try {
            URL newUrl = new URL(strUrl);
            HttpURLConnection hConnect = (HttpURLConnection) newUrl
                .openConnection();
            // POST方式的额外数据
            if (strPostRequest.length() > 0) {
                hConnect.setDoOutput(true);
                OutputStreamWriter out = new OutputStreamWriter(hConnect
                    .getOutputStream());
                out.write(strPostRequest);
                out.flush();
                out.close();
            }
            // 读取内容
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                hConnect.getInputStream()));
            int ch;
            for (int length = 0; (ch = rd.read()) > -1
                && (maxLength <= 0 || length < maxLength); length++) {
                buffer.append((char) ch);
                String s = buffer.toString();
                s.replaceAll("//&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");
                System.out.println(s);
            }
            rd.close();
            hConnect.disconnect();
            return buffer.toString().trim();
        } catch (Exception e) {
            // return "错误:读取网页失败！";
            //
            return null;
        }
    }

    public static void main(String[] args) {
        String url = "http://www.jb51.net";
        String keyword = "脚本之家";
        HttpClient p = new HttpClient();
        String response = p.createhttpClient(url, keyword);
        // 第一种方法
        // p.getPageContent(url, "post", 100500);//第二种方法
    }
}
*/
