package com.example.demo.controller.worm;

/**
 * Created by jerry-jx on 2017/10/31.
 */
public class UrlDataHanding implements Runnable {
    /**
     * 下载对应页面并分析出页面对应的URL放在未访问队列中。
     *
     * @param url
     */
    public void dataHanding(String url) {
        HrefOfPage.getHrefOfContent(DownloadPage.getContentFormUrl(url));
    }

    @Override
    public void run() {
        while (!UrlQueue.isEmpty()) {
            dataHanding(UrlQueue.outElem());
        }
    }
}
