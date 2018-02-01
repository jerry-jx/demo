package com.example.demo;

import com.example.demo.configure.CommonTestApplicationBean;
import com.example.demo.configure.DataSourceConfigure;
import com.example.demo.configure.SpringConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by jerry-jx on 2017/12/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
//@ContextConfiguration({"/spring/app*.xml","/spring/service/app*.xml"}) //加载配置文件
//@ContextConfiguration(locations={"classpath:spring-config.xml"})//加载spring配置文件
@ContextConfiguration(classes = {DataSourceConfigure.class, SpringConfig.class, CommonTestApplicationBean.class})
//@ContextConfiguration({"classpath*:/spring/applicationContext*.yml"})
//@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
//@SpringApplicationConfiguration(classes = DemoApplication.class) // 指定我们SpringBoot工程的Application启动类
//@WebAppConfiguration // 由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration。

//------------如果加入以下代码，所有继承该类的测试类都会遵循该配置，也可以不加，在测试类的方法上///控制事务，参见下一个实例
//这个非常关键，如果不加入这个注解配置，事务控制就会完全失效！
//@Transactional
//这里的事务关联到配置文件中的事务控制器（transactionManager = "transactionManager"），同时//指定自动回滚（defaultRollback = true）。这样做操作的数据才不会污染数据库！
//@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
//------------
public class BaseJunit4Test {

}
