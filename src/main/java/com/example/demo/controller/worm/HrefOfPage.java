package com.example.demo.controller.worm;

/**
 * Created by jerry-jx on 2017/10/31.
 */
public class HrefOfPage {
    /**
     * 获得页面源代码中超链接
     */
    public static void getHrefOfContent(String content) {
        System.out.println("开始");
        String[] contents = content.split("<a href=\"");
        for (int i = 1; i < contents.length; i++) {
            int endHref = contents[i].indexOf("\"");

            String aHref = FunctionUtils.getHrefOfInOut(contents[i].substring(
                0, endHref));

            if (aHref != null) {
                String href = FunctionUtils.getHrefOfInOut(aHref);

                if (!UrlQueue.isContains(href)
                    && href.indexOf("/code/explore") != -1
                    && !VisitedUrlQueue.isContains(href)) {
                    UrlQueue.addElem(href);
                }
            }
        }

        System.out.println(UrlQueue.size() + "--抓取到的连接数");
        System.out.println(VisitedUrlQueue.size() + "--已处理的页面数");

    }

}
