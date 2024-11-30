package com.huang.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huang.Enum.LogType;
import com.huang.Utils.ExecutionTimeUtil;
import com.huang.Utils.ProducerUtil;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class LogProxy implements Logger {

    private Logger originalLogger; // 原始的 Logger 实例

    @Resource
    ProducerUtil producerUtil;

    @Resource
    private ObjectMapper objectMapper;

    @Value("${spring.application.name:default}")
    private String serverName;

    private ThreadLocal<String> threadLocalIp = new ThreadLocal<>();

    private String timePeriod;

    public String getCaller(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // 获取调用 LogUtil 方法的调用者（一般来说调用 LogUtil 方法的栈帧位于 3 位置）
        StackTraceElement caller = stackTrace[31];
        // 获取方法名称、类名、文件名等信息
        String callerMethodName = caller.getMethodName(); // 当前调用方法的名称
        String callerClassName = caller.getClassName();   // 当前调用方法所在的类名
        int lineNumber = caller.getLineNumber();           // 调用方法所在行号
        return callerMethodName;
    }
    public <T,V> LogContent<T,V> getLogContent(LogType logType){
        LogContent<T, V> log = new LogContent<>();
        log.setLogLevel(LogType.INFO.name());
        log.setIp(getIp());
        log.setMethod(getCaller());
        log.setServerName(serverName);
        // 设置trackId
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            if (request.getSession() != null) {
                Object object = request.getSession().getAttribute("trackID");
                log.setTrackID(object instanceof String ? object.toString() : "");
            }
        }
        long elapsedTime = ExecutionTimeUtil.getElapsedTime(getCaller());
        log.setTimePeriod(String.valueOf(elapsedTime));
        return log;
    }
    // 自定义异常日志
    public void Error(Throwable e,String msg) {
        LogContent<Object, Object> log = getLogContent(LogType.ERROR);
        log.setStackTrace(ExceptionUtils.getStackTrace(e));
        producerUtil.send(log);
    }

    public <T,V> void Info(T paras,V returnValue,String key1,String key2,String key3,String address) {
        LogContent<Object, Object> log = getLogContent(LogType.INFO);
        try {
            setPV(log,paras,returnValue);
            log.setKey1(key1);
            log.setKey2(key2);
            log.setKey3(key3);
            log.setServerAddress(address);
            producerUtil.send(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public <T,V> void Info(T paras,V returnValue,String key1,String key2,String key3) {
        LogContent<Object, Object> log = getLogContent(LogType.INFO);
        try {
            setPV(log,paras,returnValue);
            log.setKey1(key1);
            log.setKey2(key2);
            log.setKey3(key3);
            producerUtil.send(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public <T,V> void Info(T paras,V returnValue,String key1,String key2) {
        LogContent<Object, Object> log = getLogContent(LogType.INFO);
        try {
            setPV(log,paras,returnValue);
            log.setKey1(key1);
            log.setKey2(key2);
            producerUtil.send(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public <T,V> void Info(T paras,V returnValue,String key1,String key2,String key3,String method,String address) {
        LogContent<Object, Object> log = getLogContent(LogType.INFO);
        try {
            setPV(log,paras,returnValue);
            log.setKey1(key1);
            log.setKey2(key2);
            log.setKey3(key3);
            log.setMethod(method);
            log.setServerAddress(address);
            producerUtil.send(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T, V> void setPV(LogContent<Object, Object> log, T paras, V returnValue) {
        try{
            if (!(paras instanceof String)) {
                log.setParas(objectMapper.writeValueAsString(paras));

            }else {
                log.setParas(paras);
            }
            if (!(returnValue instanceof String)) {
                log.setReturnValue(objectMapper.writeValueAsString(returnValue));
            }else {
                log.setReturnValue(returnValue);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T,V> void Info(T paras,V returnValue,String key1) {
        LogContent<Object, Object> log = getLogContent(LogType.INFO);
        try {
            setPV(log,paras,returnValue);
            log.setKey1(key1);
            producerUtil.send(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T,V> void Info(T paras,V returnValue) {
        LogContent<Object, Object> log = getLogContent(LogType.INFO);
        try {
            setPV(log,paras,returnValue);
            producerUtil.send(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T,V> void Info(T paras) {
        try {
            LogContent<Object, Object> log = getLogContent(LogType.INFO);
            setPV(log,paras,"");
            producerUtil.send(log);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void setLogger(Class<?> clazz) {
        this.originalLogger = LoggerFactory.getLogger(clazz);
    }

    @Override
    public String getName() {
        return originalLogger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return originalLogger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        originalLogger.trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        originalLogger.trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        originalLogger.trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        originalLogger.trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        originalLogger.trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return originalLogger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        originalLogger.trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        originalLogger.trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        originalLogger.trace(marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        originalLogger.trace(marker, format, argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        originalLogger.trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return originalLogger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        originalLogger.debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        originalLogger.debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        originalLogger.debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        originalLogger.debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        originalLogger.debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return originalLogger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        originalLogger.debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        originalLogger.debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        originalLogger.debug(marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        originalLogger.debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        originalLogger.debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return originalLogger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        originalLogger.info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        originalLogger.info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        originalLogger.info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        originalLogger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        originalLogger.info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return originalLogger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        originalLogger.info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        originalLogger.info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        originalLogger.info(marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        originalLogger.info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        originalLogger.info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return originalLogger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        originalLogger.warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        originalLogger.warn(format, arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        originalLogger.warn(format, arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        originalLogger.warn(format, arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        originalLogger.warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return originalLogger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        originalLogger.warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        originalLogger.warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        originalLogger.warn(marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        originalLogger.warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        originalLogger.warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return originalLogger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        originalLogger.error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        originalLogger.error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        originalLogger.error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        originalLogger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        originalLogger.error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return originalLogger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        originalLogger.error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        originalLogger.error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        originalLogger.error(marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        originalLogger.error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        originalLogger.error(marker, msg, t);
    }

    public void setIp(String ip) {
        threadLocalIp.set(ip);
    }
    private String getIp() {
        return threadLocalIp.get();
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }
}
