package com.huang.config;

import com.huang.Interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @program: log-kafka
 * @author: hjw
 * @create: 2024-11-30 11:18
 * @ClassName:WebConfig
 * @Description:
 **/
@Configuration
public class KafkaWebConfig implements WebMvcConfigurer {
    @Resource
    LogInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns();
    }
}
