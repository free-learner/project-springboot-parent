package com.personal.springboot.kafka;

import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2018年5月29日
 */
@EnableKafka
@Component
public class TestKafkaConsumer {

    @KafkaListener(topics = KafkaConstants.TEST_TOPIC)
    public void processMessage(String content) {
        System.out.println("name:" + ",operator:" + content);
    }
    
}
