package com.example.demo.controller.HTMLCatch

import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.NameValuePair
import org.apache.commons.httpclient.cookie.CookiePolicy
import org.apache.commons.httpclient.methods.PostMethod
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException

/**
 * 爬取新安人才网信息
 */
object HgHtml {

    /*private val SITE = "w357.hga020.com"
    private val PORT = 80

    private val loginAction = "/login.php"
    private val forwardURL = "http://w357.hga020.com/"
*/
    private val SITE = "login.goodjobs.cn"
    private val PORT = 80

    private val loginAction = "/index.php/action/UserLogin"
    private val forwardURL = "http://user.goodjobs.cn/dispatcher.php/module/Personal/"
    /* private static final String toUrl = "d:\\jsoup_test\\";
    private static final String hostCss = "d:\\jsoup_test\\style.txt";
    private static final String Img = "http://user.goodjobs.cn/images";
    private static final String _JS = "http://user.goodjobs.cn/scripts/fValidate/fValidate.one.js";*/

    /**
     * 模拟登陆
     */
    @Throws(Exception::class)
    private fun loginHtml(LOGON_SITE: String, LOGON_PORT: Int, login_Action: String, vararg params: String): Array<String>? {
        var result: Array<String>? = null
        val client = HttpClient()
        client.hostConfiguration.setHost(LOGON_SITE, LOGON_PORT)
        // 模拟登录页面
        val post = PostMethod(login_Action)
        val userName = NameValuePair("username", params[0])
        val password = NameValuePair("passwd", params[1])
        val langx = NameValuePair("langx", params[2])
        val auto = NameValuePair("auto", params[3])
        val blackbox = NameValuePair("blackbox", params[4])
        post.setRequestBody(arrayOf(userName, password, langx, auto, blackbox))
        client.executeMethod(post)
        println("执行状态：" + client.state)
        post.releaseConnection()
        // 查看 cookie 信息
        val cookiespec = CookiePolicy.getDefaultSpec()
        val cookies = cookiespec.match(LOGON_SITE, LOGON_PORT, "/", false,
                client.state.cookies)
        if (cookies != null) {
            if (cookies.size == 0) {
                println("Cookies is not Exists ")
            } else {
                for (i in cookies.indices) {
                    println("----------------------------------------------------")
                    println(cookies[i].toString())
                    result = cookies[i].toString().split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    println("----------------------------------------------------")
                }
            }
        }

        return result
    }

    /**
     *
     * @param cookies
     * @return
     */
    fun getHtmlDocument(cookies: Array<String>): Document? {
        try {
            return Jsoup.connect(forwardURL).cookie(cookies[0], cookies[1]).get()
        } catch (e: IOException) {
            println("页面获取异常！")
            e.printStackTrace()
        }

        return null
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val params = arrayOf("mitra168", "mitra168168", "zh-cn", "IEICHA", "0400AKHeWb1CT4UHcCiyOFFxUKwYH#Vy2s1xUzPtu#hsUXx8PrL/sgEx7mZ7#EnMfC4LgId6dDaNkNU3koT6Hdqn9fi8fptuT4V#57LnmsP38HaJ#KmAMZ21YUzZey/rZNEc4037zRqlee5yFcSkqakryzwj6OrTK/Zzi7pxCD5kB99#UiRMTdA2wBLYhOWdwS8Vex1TLARybUr4X35U0eVzPZXAAYR3cfgv3nZEBQ8rALMtAB9loDuNKLAcesNbcdJjGc#x7ZkkKyrh7d/W78RPOnlWmCRBdNDNnZsKRSTH#INXmndwxC1qdooOq2/rQwMyA6uGXP/Zo2xUgBbu4NCNcz0YLXuOWeFzKxa#pMoKut/vUSbJ#Ltz3R3#1lLYqHZEIIY9S3wRy21KdlUrnClO5EntMwDARHuVzdfxLylYBibsEVBWO8XQIqpa9NvP9z6vQUif7vGFwmKtvx1tsepzjTntEcVmW1kMV3qhf8WVO3WSnrbJSNyIsamwXX0ch5PoiQcp7l8EoPVZOWa7AmiMkUuHuH9BhO0teGRJquTjspCrgecSo06izVzqeMmBCH/i9cmKrLQxcxA5OE2KkOZe/0jXzk77ILZ/eUsQ7RNrLro1kTKIs1496YkpIh3A707lm2e25SQbo1N9hSupCh0nrVUQezWVtLMElodtJBCn1dvmY1XpDGBtyWF8hRwW7Jsss29L0sFkQPnkjHloApNDPJIEFaG7TsuZF23lTgKl7AgMpOFU/WEDIKX1jfI9hOyMHRH29E2Pl68vN0XpeFtRx/cjdbgHxEkRmgkRq1Qs48sbX9QC8nOTD0ntb6FcJyEOEOVzmJtDqimkzDq#SXR1/zMqdQxZs8xyaPS8h4JzIZckxWd#O6V36vzndlpWeDminWumEkZF9YX97CRKDwrr9AQy/m2yvWeDb21o9D86B2RkSzpCKDvWrYPUdLssWSM02MLTnpCtk#xjjZSkBTBViHxgPVj4eN/NsYCocoX3ay/Q78T0AGn/16h1fJQrFSBW1n74tpcOq4iPaqS7dmS/OQAz11sFE541B9tAPWn4oRxXqLz#1vlqUAr9dE2jcfl0TIBnmyVMxonijONFLK06QvJVg5zBIetJQ3Xefa#YB7ITNEE/Z3YwxutPPQ2GfVUszo#TM5ojb8St1HpNGLhyHYVrcXo3NEvSJzAvMrpxMV88VRbOX0n7afwwgPIxn5M#piIn/4g7jAkiUi5StOu7r2IPArjDreTC6uFgohzG6tjKuzMci2qsloCpuYJp7FoGjofZbR2X0CLFRTyPlvo4qRQugOFx8rWemSYrWCTNFTS256rBMb0RBnEGZc6#zs3861I89#pWVnE=;0400nyhffR#vPCUNf94lis1ztpUkBF3gMqJVUvQPAuo2LgKAC3SfR7deSLSUZpybFcXA9SHJSmUeiL2574HgzbCSorEfVk9EjAzqJqKR0TY3#IFLuAeH2h6vGuixLXGBICA18sQyiQagYnGrecmfaqukfbi8jBePvEwYYDvethm0ixXKgDzSlcJpdrCvB/zy9rIL0LVQmADUFBlDgm50c6V/UpKetslI3IixYH1H5YVrp93GJ/KtOFGi8RKePA1UZdKAZDwic#y5/r#SkyAbziDM7k8xAXTS4l7D1erHMnjL6rgY44y5eNeVl4RNv8N11IP4QMzDc2oqeO6WMCVbBcdxP6GA3q0kDinWcD6T1dGUjL9Tx9vW#aqm51UhqgCTPJTYD1MDRzqtKfHhdC9rpz98UdJKw47skt93lCdHmAnjxfTED3HepEpuM1LfCGZRLg#J3eln9a65pRCWooNhUWLUMBc9kBNJU3x5VzgroUySL66/146#zK9MFVUYrroRcS5oPRgte45Z4XMrFr6kygq63#9RJsn4u3PdHf7WUtiodkQghj1LfBHLbUp2VSucKU7k72B5V9rdDiApQs/EGzmZL8oln8UkWIICa/9MigwYM7w59KV7rLUwqgQxSxzPuRtycb0H5IjHkCcKSs4KVYKB4sYRtsxPIW0X#KRnCtOZRo5HDo0vYYDC32sBOC7Bb3k7bazR87feXj5NK8155#SfnP10F4hyuT#ZFiKry47CadKyPr0t8ztVFisjUV4dJsOym9ceHDKRCiK4xI1RTIYC8ouD71qCKcmZqa#c5UMfdLNXqLz#1vlqUAr9dE2jcfl0wgroQBfpyuIcH#e41nTPEqFV8BKCqA3jVTw2C4oQ2p6vWc20/w4QKST/riUqiozfAOitx40UDzaLaxNWMM2S8UTjbKzZpUNBxKb7FG#fia#fFCEvMT9cc6XakoCa7XCW5#Cltm6/m0VPMQF00uJew0LT2BH9Dx8Z6yFodg/w6rqT5xVcmJXbIoCZ40cfr7DqE6#TWDNi9oOXZ43Cwh0Wj5pA33#ZJO0whHpm2mLpmw1R/U9LfT1jtR4tEKxU4tgxHL9kFqpl7W0encqbarltPwR5jUC3gQiT0wNzZWacGR8Bunr0Z2CucHYoqRVEt#uvWrjg3IZNlqx9izplUCi1HWHk3C7XHo8gbzNKEJnsgkqBGA35nwcAWEYXN3#bqydWqLZcnDBAS9SM8lXpMISOf7T7130BIPii0VbAzVM5zPuCJUUDvOkg9DiEuyx#fdZIO0PdhRirnrsPOiZrP2SiOWzybLfWtqZltZ#EUJmjlGcEMv5tsr1ng29taPQ/OgdkZEs6Qig71q2D1HS7LFkjNHqxoWxtenyOiuv6Xcf4KsJnI85D1o/3#B#zfbJNab7KwaOC9uuNAaklQfetdppWAdw1o1VDnMmzuWdNoDc8kApFQnLgM2I5gVCxQ8rMs1JXNfCWMXMAnRmbqUR#IVlSYDw#70xPXUTWSLJV9gmLYppQW1uDeetLDHuhnX7dRdMClU99UoH2xknRstzS6WvL3mytmiMaal9nIsvm/xjvxYGZvbhXcNmZNE0HaxbgbGHWco7gVRRd7iI=")
        var strings: Array<String>?
        try {
            strings = loginHtml(SITE, PORT, loginAction, *params)
            if (strings!!.size > 0) {
                val htmlDocument = getHtmlDocument(strings)
                println(htmlDocument)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}