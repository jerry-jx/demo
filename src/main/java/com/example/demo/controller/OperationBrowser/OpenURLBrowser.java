package com.example.demo.controller.OperationBrowser;

import java.io.IOException;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * Created by jerry-jx on 2018/4/3.
 */
@Slf4j
@Component
public class OpenURLBrowser {

  

    public OpenURLBrowser(TaskScheduler taskScheduler) {
        taskScheduler.schedule(this::startBalanceTask, new CronTrigger("0 0/10 * * * *"));
    }

    private void startBalanceTask() {
        log.info("开始准备清理代理佣金历史数据");
        openFireFoxBrowser();
    }
    /**
     * 打开FireFox浏览器访问页面
     */
    public static void openFireFoxBrowser(){
        //启用cmd运行IE的方式来打开网址。
        String str = "cmd /c start Firefox https://www.multcloud.com/index.jsp?rl=zh-CN#cloudType=baiDu&tokenId=Z7lRz6WjbhZ0waVhoPlJvP6NNX8KbRcT002f2zmKNBRgj002bJ7oc2ez2q13upmlT8qOL002b&fileId=/apps";
        try {
            Runtime.getRuntime().exec(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args){
        int i= 1;
        do{
            openFireFoxBrowser();
            try {
                log.info("刷新{}次",i);
                Thread.sleep(5 * 60 * 1000);
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while(1==1);

    }

    /**
     * J2SE 5及之前可使用以下代码<br>
     */
    private static void openURL(String url) {
        String osName = System.getProperty("os.name");
        System.out.println("###osName:" + osName);
// System.gc();
        Process p = null;
        int exitCode = 1;

        try {
            if (osName.startsWith("Mac")) {// Mac OS
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL",
                    new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });
            } else if (osName.startsWith("Windows")) {// Windows
                p = Runtime.getRuntime().exec(
                    "rundll32 url.dll,FileProtocolHandler " + url);

// System.out.println("###p:" + p);
// System.out.println("###p.hashCode():" + p.hashCode());

                exitCode = p.waitFor();
                System.out.println("###exitCode:" + exitCode);
            } else { // Unix or Linux
                String[] browsers = { "firefox", "opera", "konqueror",
                    "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++) {
                    if (Runtime.getRuntime().exec(
                        new String[] { "which", browsers[count] })
                        .waitFor() == 0) {
                        browser = browsers[count];
                    }
                }
                if (browser == null) {
                    throw new Exception("Could not find web browser");
                } else {
                    Runtime.getRuntime().exec(new String[] { browser, url });
                }
            }
            Thread.sleep(2000);
            p.destroy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
