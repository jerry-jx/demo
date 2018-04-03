package com.example.demo.controller.OperationBrowser;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * Created by jerry-jx on 2018/4/3.
 * Robot04GUI.java
 * create by kin. 2004/11/07.
 * Please enjoy this.
 */


/**Robot04's GUI version.*/
public class Robot04GUI extends JFrame {

    private JButton b = new JButton("Close IE");

    public Robot04GUI() {
        super("Close IE");
        getContentPane().add(b, BorderLayout.CENTER);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Robot04.main(new String[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
       /* Robot04GUI r = new Robot04GUI();
        r.setSize(200, 200);
        r.setVisible(true);*/
        openURL("https://www.baidu.com/");
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