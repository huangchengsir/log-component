package com.huang.Utils;

import java.util.HashMap;
import java.util.Map;

public class ExecutionTimeUtil {

    // ThreadLocal 存储当前线程的多个方法执行开始时间
    private static ThreadLocal<Map<String, Long>> startTimeThreadLocal = new ThreadLocal() {
        @Override
        protected Map<String, Long> initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * 记录方法开始时间
     */
    public static void start(String methodName) {
        startTimeThreadLocal.get().put(methodName, System.nanoTime());
    }

    /**
     * 获取当前方法执行时间（单位：毫秒）
     */
    public static long getElapsedTime(String methodName) {
        long startTime = startTimeThreadLocal.get().getOrDefault(methodName, System.nanoTime());
        long endTime = System.nanoTime();
        return (endTime - startTime) / 1000000; // 纳秒转毫秒
    }

    /**
     * 清理 ThreadLocal
     */
    public static void clear() {
        startTimeThreadLocal.remove();
    }

    public static void clear(String methodName) {
        if (startTimeThreadLocal.get().containsKey(methodName)){
            startTimeThreadLocal.get().remove(methodName);
        }
    }
}
