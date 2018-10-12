package com.personal.springboot.kafka2;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SimpleKafkaProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
//        props.put("group.id", "mygroup");
        // broker地址
        props.put("bootstrap.servers", "192.168.52.140:9092,192.168.52.140:9092,192.168.52.140:9093,192.168.52.140:9094");
        // 请求时候需要验证
        props.put("acks", "all");
        // 请求失败时候需要重试
        props.put("retries", 0);
        // 内存缓存区大小
        props.put("buffer.memory", 33554432);
        // 指定消息key序列化方式
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 指定消息本身的序列化方式
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 10; i++)
            producer.send(new ProducerRecord("my-multi-topic", Integer.toString(i)));
            //producer.send(new ProducerRecord<>("my-multi-topic", Integer.toString(i), Integer.toString(i)));
        System.out.println("Message sent successfully");
        producer.close();
    }
    
    
}