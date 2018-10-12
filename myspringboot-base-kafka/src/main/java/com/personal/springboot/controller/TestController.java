package com.personal.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.springboot.kafka.TestKafkaProducer;

/**
 * Created by yue.yuan on 2017/4/17.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestKafkaProducer kafkaProducer;
    
    @Autowired
    KafkaTemplate<?, ?> kafkaTemplate;

    @RequestMapping("/testKafka")
    public void testKafka() {
//        kafkaTemplate.send("", "");
        kafkaProducer.sendMessage();
    }

}
