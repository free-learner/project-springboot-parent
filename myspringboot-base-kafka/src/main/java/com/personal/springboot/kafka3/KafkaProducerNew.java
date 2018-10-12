package com.personal.springboot.kafka3;
import org.apache.kafka.clients.producer.KafkaProducer;  
import org.apache.kafka.clients.producer.ProducerConfig;  
import org.apache.kafka.clients.producer.ProducerRecord;  
import org.apache.kafka.common.serialization.StringSerializer;  
  
import java.util.Properties;  
  
/** 
* producer: org.apache.kafka.clients.producer.KafkaProducer; 
 * */  
  
public class KafkaProducerNew {  
  
    private final KafkaProducer<String, String> producer;  
  
    public final static String TOPIC = "didi-topic-test";  
  
    private KafkaProducerNew() {  
        Properties props = new Properties();  
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.52.140:9092,192.168.52.140:9093,192.168.52.140:9094");  
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());  
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());  
  
        producer = new KafkaProducer<String, String>(props);  
    }  
  
    public void produce() {  
        int messageNo = 1;  
        final int COUNT = 10;  
  
        while(messageNo < COUNT) {  
            String key = String.valueOf(messageNo);  
            String data = String.format("hello KafkaProducer message %s", key);  
              
            try {  
                producer.send(new ProducerRecord<String, String>(TOPIC, data));  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
  
            messageNo++;  
        }  
          
        producer.close();  
    }  
  
    public static void main(String[] args) {  
        new KafkaProducerNew().produce();  
    }  
  
}  