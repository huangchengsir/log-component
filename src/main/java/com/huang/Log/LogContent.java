package com.huang.Log;

import com.huang.Enum.LogType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: log-kafka
 * @author: hjw
 * @create: 2024-11-29 23:56
 * @ClassName:LogContent
 * @Description:
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogContent<T,V> implements Serializable {
    private T paras;
    private V returnValue;
    @Builder.Default
    private String method = "";
    @Builder.Default
    private String serverName = "default";
    @Builder.Default
    private String Key1 = "";
    @Builder.Default
    private String Key2 = "";
    @Builder.Default
    private String Key3 = "";
    @Builder.Default
    private String timePeriod = "";
    @Builder.Default
    private String logLevel = LogType.INFO.name();
    @Builder.Default
    private String stackTrace = "";
    @Builder.Default
    private String ip = "";
    @Builder.Default
    private String serverAddress = "";
    @Builder.Default
    private String TrackID = "";
}
