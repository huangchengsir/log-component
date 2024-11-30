package com.huang.Utils;

import com.huang.Log.LogContent;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @program: log-kafka
 * @author: hjw
 * @create: 2024-11-30 11:45
 * @ClassName:ProducerUtil
 * @Description:
 **/
@Component
public class ProducerUtil {
    @Resource
    Producer<String,Object > producer;

    public void send(LogContent value) {
        producer.send(new ProducerRecord<>("kafka_log", "JSON", value), (metadata, exception) -> {
            if (exception != null) {
                System.out.println("Message failed to send: " + exception.getMessage());
            } else {
                System.out.println("Message sent successfully to topic: " + metadata.topic() + " partition: " + metadata.partition());
            }
        });
    }
}
