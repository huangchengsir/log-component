package com.huang.Interceptor;

import com.huang.Log.LogProxy;
import com.huang.Utils.TrackIdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LogInterceptor implements HandlerInterceptor{
    @Resource
    LogProxy logProxy;
    @Value("${spring.application.name:default}")
    private String serverName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip = request.getHeader("X-Forwarded-For"); // 支持通过代理获取真实IP
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        logProxy.setIp(ip);
        // 设置trackId
        HttpSession session = request.getSession();
        session.setAttribute("trackID", TrackIdUtil.generateTrackId(serverName));
        return true;
    }
}
