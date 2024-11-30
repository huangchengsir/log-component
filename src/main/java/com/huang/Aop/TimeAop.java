package com.huang.Aop;

import com.huang.Utils.ExecutionTimeUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

/**
 * @program: log-kafka
 * @author: hjw
 * @create: 2024-11-30 12:43
 * @ClassName:TimeAop
 * @Description:
 **/
@Aspect
@Component
@Order(1)
public class TimeAop implements MethodInterceptor {

//    @Value("${kafka.time-aop:com.huang}")
//    private String packageName;
//
//    // 匹配所有子包方法
//    @Pointcut("execution(* com.huang..*(..))")
//    public void pointCut(){}
//
//    @Around("pointCut()") //
//    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        // 获取当前方法名
//        String methodName = joinPoint.getSignature().getName();
//
//        // 记录方法开始时间
//        ExecutionTimeUtil.start(methodName);
//
//        // 执行目标方法
//        Object proceed = joinPoint.proceed();
//
//        // 清理 ThreadLocal
//        ExecutionTimeUtil.clear();
//
//        return proceed;
//    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object target = invocation.getThis();
        assert target != null;
        Class<?> targetClass = target.getClass();
        // 如果目标类是 final 类，直接跳过 AOP
        if (Modifier.isFinal(targetClass.getModifiers())) {
            return invocation.proceed(); // 直接调用方法
        }

        // 记录方法开始时间
        ExecutionTimeUtil.start(invocation.getMethod().getName());
        Object proceed = invocation.proceed();  // 执行目标方法
        // 清理 ThreadLocal
        ExecutionTimeUtil.clear(invocation.getMethod().getName());
        return proceed;
    }
}
