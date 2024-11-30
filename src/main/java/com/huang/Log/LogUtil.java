package com.huang.Log;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LogUtil {
    @Resource
    LogProxy log;

    public void Error(Throwable e,String msg){
        log.Error(e,msg);
    }
    public <T,V> void Info(T paras,V returnValue,String key1,String key2,String key3,String address){
        log.Info(paras,returnValue,key1,key2,key3,address);
    }
    public <T,V> void Info(T paras,V returnValue,String key1,String key2,String key3,String method,String address){
        log.Info(paras,returnValue,key1,key2,key3,method,address);
    }
    public <T,V> void Info(T paras,V returnValue,String key1,String key2,String key3){
        log.Info(paras,returnValue,key1,key2,key3);
    }
    public <T,V> void Info(T paras,V returnValue,String key1,String key2){
        log.Info(paras,returnValue,key1,key2);
    }
    public <T,V> void Info(T paras,V returnValue,String key1){
        log.Info(paras,returnValue,key1);
    }
    public <T,V> void Info(T paras,V returnValue){
        log.Info(paras,returnValue);
    }
}
