package com.huang.config;

import com.huang.Aop.TimeAop;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaDynamicAopConfig {

    @Value("${kafka.time-aop:com.huang}")
    private String packageName;  // 从配置文件中获取包路径

    @Bean
    public AspectJExpressionPointcut aspectJExpressionPointcut() {
        // 动态构建切点表达式
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        String expression = "execution(* " + packageName + "..*(..)) && !execution(* com.huang.Utils..*(..))";
        pointcut.setExpression(expression);  // 设置动态切点表达式
        return pointcut;
    }

    @Bean
    public Advisor advisor(AspectJExpressionPointcut pointcut, TimeAop timeAop) {
        // 使用 DefaultPointcutAdvisor，将切点和动态 AOP 逻辑关联
        return new DefaultPointcutAdvisor(pointcut, timeAop);
    }
}

