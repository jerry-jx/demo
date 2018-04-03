package com.example.demo.controller.OperationBrowser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * @ClassName：OpenBrowse
 * @Description：使用java代码打开关闭浏览器（指定的浏览器或者计算机默认的浏览器）
 *               获取网页字节流内容信息，
 * @date：2017年7月28日
 * 修改备注：
 */
public class OpenBrowse {


    private static String[] urls = {"www.baidu.com","www.baidu.com"};


    /**
     * @Description:判断URL指定的页面是否存在
     * @date: 2017年8月2日 下午2:43:33
     * @修改备注:
     */
    public static boolean urlExists(String urlStr) {
        //urlStr = "http://blog.csdn.net/x1617044578/article/details/866863222";
        try {
            //设置此类是否应该自动执行 HTTP 重定向（响应代码为 3xx 的请求）。
            HttpURLConnection.setFollowRedirects(false);
            //到 URL 所引用的远程对象的连接
            HttpURLConnection con = (HttpURLConnection) new URL(urlStr).openConnection();
            //设置 URL 请求的方法， GET POST HEAD OPTIONS PUT DELETE TRACE 以上方法之一是合法的，具体取决于协议的限制。
            con.setRequestMethod("HEAD");
            //从 HTTP 响应消息获取状态码
            con.connect();
            con.getHeaderFields();
            if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
                System.out.println("****存在");
                return true;
            }else {
                System.out.println("****bu存在");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("****异常");
            return false;
        }

    }


    /**
     * @Description: 使用IE浏览器访问指定URL的页面
     * @date: 2017年7月28日 下午2:29:49
     * @修改备注:
     */
    public static void openIEBrowser(){
        //启用cmd运行IE的方式来打开网址。
        for (String url : urls) {
            try {
                Runtime.getRuntime().exec(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 打开计算机默认的浏览器访问指定的url页面
     * @date: 2017年7月28日 下午2:30:24
     * @修改备注:
     */
    public static void openBrowse(){
        for (String url : urls) {
            if(java.awt.Desktop.isDesktopSupported()){
                try{
                    //创建一个URI实例,注意不是URL
                    java.net.URI uri=java.net.URI.create(url);
                    //获取当前系统桌面扩展
                    java.awt.Desktop dp=java.awt.Desktop.getDesktop();
                    //判断系统桌面是否支持要执行的功能
                    if(dp.isSupported(java.awt.Desktop.Action.BROWSE)){
                        //获取系统默认浏览器打开链接
                        dp.browse(uri);
                    }
                }catch(java.lang.NullPointerException e){
                    //此为uri为空时抛出异常
                }catch(java.io.IOException e){
                    //此为无法获取系统默认浏览器
                }
            }
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @Description: 关闭浏览器（关闭指定的浏览器，在此处是强行关闭浏览器，强行杀死进程）
     * @date: 2017年7月27日 下午8:31:34
     * @修改备注:
     */
    public static void closeBrowse(){
        try {
            //Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
            Runtime.getRuntime().exec("taskkill /F /IM iexplorer.exe");
            Runtime.getRuntime().exec("taskkill /F /IM 360se.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        openBrowse();
        openIEBrowser();
        closeBrowse();



    }

}
