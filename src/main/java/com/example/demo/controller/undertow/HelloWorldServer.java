package com.example.demo.controller.undertow;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * Created by jerry-jx on 2018/3/15.
 */


public class HelloWorldServer {

    public static void main(String[] args) {
        Undertow server = Undertow.builder()
            .addHttpListener(8080, "localhost").setHandler(new HttpHandler() {
                //设置HttpHandler的回调方法
                @Override
                public void handleRequest(HttpServerExchange exchange)
                    throws Exception {
                    exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("This is my first insert server!");
                }
            }).build();
        server.start();
    }
}

