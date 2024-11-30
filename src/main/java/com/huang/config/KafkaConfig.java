package com.huang.config;

import com.huang.Utils.CustomSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @program: log-kafka
 * @author: hjw
 * @create: 2024-11-28 22:49
 * @ClassName:KafkaConfig
 * @Description:
 **/
@Configuration
public class KafkaConfig {
    @Value("${spring.application.name:default}")
    private String clientName;

    @Value("${kafka.server-host:192.168.31.10:9092}")
    private String serverUrl;

    @Bean
    public Producer<String, Object> getProducer(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverUrl);
        // 客户端 ID
        props.put(ProducerConfig.CLIENT_ID_CONFIG, clientName);
        // 序列化设置，消息键和值的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());
        // 其他配置选项（如需要的话）
        props.put(ProducerConfig.ACKS_CONFIG, "1"); // 等待所有副本确认消息已写入
        props.put(ProducerConfig.RETRIES_CONFIG, 3);   // 配置重试次数
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1); // 延迟发送消息时间（毫秒）
        // 设置批量消息的大小为 10MB
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.putIfAbsent(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "10485760"); // 设置为 10MB
        return new KafkaProducer<>(props);
    };

}
