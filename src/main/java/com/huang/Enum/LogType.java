package com.huang.Enum;

/**
 * @program: log-kafka
 * @author: hjw
 * @create: 2024-11-30 01:08
 * @ClassName:LogType
 * @Description:
 **/
public enum LogType {
    INFO("INFO"),
    WARN("WARN"),
    ERROR("ERROR");


    private String type;
    private LogType(String type) {}

}
