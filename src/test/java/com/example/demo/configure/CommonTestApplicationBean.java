package com.example.demo.configure;

import com.jxinternet.platform.common.common.EmptyRedisService;
import com.jxinternet.platform.common.common.RedisService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : cabbage
 */
@Configuration
public class CommonTestApplicationBean {

    @Bean
    public RedisService redisService() {
        return new EmptyRedisService();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
