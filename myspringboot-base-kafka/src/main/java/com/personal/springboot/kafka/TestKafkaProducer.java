package com.personal.springboot.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * Created by yue.yuan on 2017/4/17.
 */
@Component
public class TestKafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage() {
//        kafkaTemplate.send("", "");
//        kafkaTemplate.send(KafkaConstants.TEST_TOPIC, gson.toJson(log));
    }
    
}
