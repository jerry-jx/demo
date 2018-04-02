package com.example.demo.controller.HTMLCatch

import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import org.apache.http.ParseException
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.jsoup.Connection
import org.jsoup.Connection.Method
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * <STRONG>类描述</STRONG> :  2345万年历信息爬取工具
 *
 *
 *
 * @version 1.0
 *
 *
 * @author 溯源blog
 *
 * <STRONG>创建时间</STRONG> : 2016年4月11日 下午14:15:44
 *
 *
 * <STRONG>修改历史</STRONG> :
 *
 *
 * <pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
</pre> *
 */
object AlmanacLoginUtilTestKt {
    /**
     * 获取万年历信息
     * @return
     */
    /* String url="http://tools.2345.com/rili.htm";
        String html=pickData(url);
        Almanac almanac=analyzeHTMLByString(html);*/ val almanac: Almanac
        @Throws(IOException::class)
        get() = analyzeHTMLByString()

    /*
     * 获取公历时间,用yyyy年MM月dd日 EEEE格式表示。
     * @return yyyy年MM月dd日 EEEE
     */
    private val solarDate: String
        get() {
            val calendar = Calendar.getInstance()
            val solarDate = calendar.time
            val formatter = SimpleDateFormat("yyyy年MM月dd日 EEEE")
            return formatter.format(solarDate)
        }


    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        almanac
    }

    /*
     * 爬取网页信息
     */
    private fun pickData(url: String): String? {
        val httpclient = HttpClients.createDefault()
        try {
            val httpget = HttpGet(url)
            val response = httpclient.execute(httpget)
            try {
                // 获取响应实体
                val entity = response.entity
                // 打印响应状态
                if (entity != null) {
                    return EntityUtils.toString(entity)
                }
            } finally {
                response.close()
            }
        } catch (e: ClientProtocolException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return null
    }

    /*
     * 使用jsoup解析网页信息
     */
    @Throws(IOException::class)
    private fun analyzeHTMLByString(): Almanac {
        var solarDate: String? = ""
        val lunarDate: String
        val chineseAra: String
        val should: String
        var avoid = " "

        var res: Connection.Response? = null

        res = Jsoup.connect("http://w071.hga020.com/")
                .data("username", "mitra168", "passwd", "mitra168168")
                .method(Method.POST)
                .execute()

        val doc = res!!.parse()
        println("username" + doc.getElementById("username"))
        println(" " + doc.getElementById("passwd"))

        //这儿的SESSIONID需要根据要登录的目标网站设置的session Cookie名字而定
        val sessionId = res.cookie("SESSIONID")

        val document = Jsoup.connect("http://w071.hga020.com/")
                .cookie("SESSIONID", sessionId)
                .get()

        //Document document = Jsoup.parse(html);
        //公历时间
        solarDate = solarDate
        //农历时间
        val eLunarDate = document.getElementById("info_nong")
        lunarDate = eLunarDate.child(0).html().substring(1, 3) + eLunarDate.html().substring(11)
        //天干地支纪年法
        val eChineseAra = document.getElementById("info_chang")
        chineseAra = eChineseAra.text().toString()
        //宜
        should = getSuggestion(document, "yi")
        //忌
        avoid = getSuggestion(document, "ji")
        return Almanac(solarDate, lunarDate, chineseAra, should, avoid)
    }

    /*
     * 获取忌/宜
     */
    private fun getSuggestion(doc: Document, id: String): String {
        val element = doc.getElementById(id)
        val elements = element.getElementsByTag("a")
        val sb = StringBuffer()
        for (e in elements) {
            sb.append(e.text() + " ")
        }
        return sb.toString()
    }

}
/**
 * 单例工具类
 */
