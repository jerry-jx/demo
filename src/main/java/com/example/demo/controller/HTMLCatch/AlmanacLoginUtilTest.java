package com.example.demo.controller.HTMLCatch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *<STRONG>类描述</STRONG> :  2345万年历信息爬取工具<p>
 *
 * @version 1.0 <p>
 * @author 溯源blog
 *
 * <STRONG>创建时间</STRONG> : 2016年4月11日 下午14:15:44<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 *</pre>
 */
public class AlmanacLoginUtilTest {

    /**
     * 单例工具类
     */
    private AlmanacLoginUtilTest() {
    }


    public static void main(String args[]) throws IOException {
        getAlmanac();
    }
    /**
     * 获取万年历信息
     * @return
     */
    public static Almanac getAlmanac() throws IOException {
       /* String url="http://tools.2345.com/rili.htm";
        String html=pickData(url);
        Almanac almanac=analyzeHTMLByString(html);*/
        Almanac almanac=analyzeHTMLByString();
        return almanac;
    }

    /*
     * 爬取网页信息
     */
    private static String pickData(String url) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                // 打印响应状态
                if (entity != null) {
                    return EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 使用jsoup解析网页信息
     */
    private static Almanac analyzeHTMLByString() throws IOException {
        String solarDate,lunarDate,chineseAra,should,avoid=" ";

        Connection.Response res = null;

            res = Jsoup.connect("http://w071.hga020.com/")
                .data("username", "mitra168", "passwd", "mitra168168")
                .method(Method.POST)
                .execute();

            Document doc = res.parse();
        System.out.println("username"+doc.getElementById("username"));
        System.out.println(" "+doc.getElementById("passwd"));

//这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
        String sessionId = res.cookie("SESSIONID");

        Document document = Jsoup.connect("http://w071.hga020.com/")
            .cookie("SESSIONID", sessionId)
            .get();

        //Document document = Jsoup.parse(html);
        //公历时间
        solarDate=getSolarDate();
        //农历时间
        Element eLunarDate=document.getElementById("info_nong");
        lunarDate=eLunarDate.child(0).html().substring(1,3)+eLunarDate.html().substring(11);
        //天干地支纪年法
        Element eChineseAra=document.getElementById("info_chang");
        chineseAra=eChineseAra.text().toString();
        //宜
        should=getSuggestion(document,"yi");
        //忌
        avoid=getSuggestion(document,"ji");
        Almanac almanac=new Almanac(solarDate,lunarDate,chineseAra,should,avoid);
        return almanac;
    }
    /*
     * 获取忌/宜
     */
    private static String getSuggestion(Document doc,String id){
        Element element=doc.getElementById(id);
        Elements elements=element.getElementsByTag("a");
        StringBuffer sb=new StringBuffer();
        for (Element e : elements) {
            sb.append(e.text()+" ");
        }
        return sb.toString();
    }

    /*
     * 获取公历时间,用yyyy年MM月dd日 EEEE格式表示。
     * @return yyyy年MM月dd日 EEEE
     */
    private static String getSolarDate() {
        Calendar calendar = Calendar.getInstance();
        Date solarDate = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
        return formatter.format(solarDate);
    }

}
