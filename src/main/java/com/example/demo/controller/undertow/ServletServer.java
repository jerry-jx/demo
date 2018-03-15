package com.example.demo.controller.undertow;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.api.ServletInfo;
import javax.servlet.ServletException;

public class ServletServer {

    public static void main(String[] args) {

        /*
         * 创建ServletInfo，Servelt的最小单位。是对javax.servlet.Servlet具体实现的再次封装。
         * 注意：ServletInfo的name必须是唯一的
         */
        ServletInfo servletInfo1 = Servlets.servlet("MyServlet",
            MyServlet.class);
        // 创建servletInfo的初始化参数
        servletInfo1.addInitParam("message", "This is my first MyServlet!");
        // 绑定映射为/myServlet
        servletInfo1.addMapping("/myServlet");
        /**
         * 创建包部署对象，包含多个servletInfo。可以认为是servletInfo的集合
         */
        DeploymentInfo deploymentInfo1 = Servlets.deployment();
        // 指定ClassLoader
        deploymentInfo1.setClassLoader(ServletServer.class.getClassLoader());
        // 应用上下文(必须与映射路径一致，否则sessionId会出现问题，每次都会新建)
        deploymentInfo1.setContextPath("/myapp");
        // 设置部署包名
        deploymentInfo1.setDeploymentName("myServlet.war");
        // 添加servletInfo到部署对象中
        deploymentInfo1.addServlets(servletInfo1);
        /**
         * 使用默认的servlet容器，并将部署添加至容器
         * 容器，用来管理DeploymentInfo，一个容器可以添加多个DeploymentInfo
         */
        ServletContainer container = Servlets.defaultContainer();
        /**
         * 将部署添加至容器并生成对应的容器管理对象
         * 包部署管理。是对添加到ServletContaint中DeploymentInfo的一个引用，用于运行发布和启动容器
         */
        DeploymentManager manager = container.addDeployment(deploymentInfo1);
        // 实施部署
        manager.deploy();
        /**
         * 分发器：将用户请求分发给对应的HttpHandler
         */
        PathHandler pathHandler = Handlers.path();
        /**
         * servlet path处理器，DeploymentManager启动后返回的Servlet处理器。
         */
        HttpHandler myApp = null;
        try {
            //启动容器，生成请求处理器
            myApp = manager.start();
        } catch (ServletException e) {
            throw new RuntimeException("容器启动失败！");
        }
        //绑定映射关系
        pathHandler.addPrefixPath("/myapp", myApp);

        Undertow server = Undertow.builder().
            //绑定端口号和主机
                addHttpListener(8081, "localhost")
            //设置分发处理器
            .setHandler(pathHandler).build();
        //启动server
        server.start();
    }
}