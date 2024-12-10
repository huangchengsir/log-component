package com.huang.logkafka;

import com.huang.Log.LogContent;
import com.huang.Log.LogUtil;
import com.huang.Log.test.Metest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class LogKafkaApplicationTests {

//    @Autowired
//    LogUtil logUtil;
//    @Resource
//    Metest metest;

    @Test
    void contextLoads() {
//        LogContent<String, String> test = LogContent.<String, String>builder()
//                .paras("{\"code\":200,\"message\":\"操作成功\",\"data\":{\"object\":[{\"id\":8,\"name\":\"元旦特惠\"")
//                .Key1("测试")
//                .returnValue("null")
//                .logLevel("INFO")
//                .Key3("测试+1")
//                .Key2("阿斯顿撒旦")
//                .stackTrace("")
//                .build();
//        logUtil.Info(test,test);
    }
    @Test
    void test(){
//        metest.test();
    }

}
