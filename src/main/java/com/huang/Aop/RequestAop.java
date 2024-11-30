package com.huang.Aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.huang.Log.LogUtil;
import com.huang.Utils.ExecutionTimeUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
@Aspect
@Order(2)
public class RequestAop {
    @Resource
    LogUtil logUtil;

    @Pointcut("execution(public * com.huang.controller.*.*(..))")
    public void apiPointCut() {
    }

    @Around("apiPointCut()")
    public Object apiAround(ProceedingJoinPoint JoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        Object[] args = Arrays.stream(JoinPoint.getArgs())
                .filter(arg -> !(arg instanceof HttpServletResponse))
                .filter(arg -> !(arg instanceof HttpServletRequest))
                .toArray();
        ExecutionTimeUtil.start("apiAround");
        Object result = JoinPoint.proceed();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                .create();
        try {
            String resJson = gson.toJson(result);
            String requestJson;
            if (args.length ==1) {
                requestJson = gson.toJson(args[0]);
            }else {
                requestJson = gson.toJson(args);
            }
            logUtil.Info(requestJson,resJson,"interface-record","","",JoinPoint.getSignature().getName(),request.getRequestURI());
        } catch (Exception e) {
            logUtil.Error(e,e.getMessage());
        }
        ExecutionTimeUtil.clear("apiAround");
        return result;
    }
}
