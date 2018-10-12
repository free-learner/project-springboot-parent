package com.personal.springboot.user.controller.redis;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.pubsub.Publisher;
import com.personal.springboot.pubsub.Subscriber;
import com.personal.springboot.pubsub.SubscriberThread;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * RedisCacheController 相关接口
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月11日
 */
@RestController
@RequestMapping("/static")
public class RedisCacheController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheController.class);

//    @Autowired(required=false)
//    private JedisPoolConfig jedisPoolConfig;
    
    @Autowired
    private JedisCluster jedisCluster;

    private final String channel="MyTopic1";
//    private final String masterIp="10.0.68.213";
//    private final int masterPort=7000;
//    
//    /**
//     * Publish端测试
//     */
//    @RequestMapping(value = "/testPublish/0", method = { RequestMethod.POST })
//    public Object testPublish0(@RequestBody Map<String, Object> requestMap) {
//        String message="This is the message from testPublish0 at time "+DateTimeUtil.formatDate2Str();
//        JedisPool jedisPool = new JedisPool(jedisPoolConfig, masterIp, masterPort);
//        Publisher publisher = new Publisher(channel,jedisPool);
//        publisher.startPublish(message);
//        LOGGER.error("testPublish端测试结束!");
//        return null;
//    }
//    
//    /**
//     * Subscribe端测试
//     */
//    @RequestMapping(value = "/testSubscribe/0", method = { RequestMethod.POST })
//    public Object testSubscribe0(@RequestBody Map<String, Object> requestMap) {
//        JedisPool jedisPool = new JedisPool(jedisPoolConfig, masterIp, masterPort);
//        SubscriberThread subThread = new SubscriberThread(channel,jedisPool);
//        subThread.start();
//        LOGGER.error("testSubscribe0端测试结束!");
//        return null;
//    }
//    
//    @PostConstruct
//    public void subscribePostConstruct() {
//        JedisPool jedisPool = new JedisPool(jedisPoolConfig, masterIp, masterPort);
//        SubscriberThread subThread = new SubscriberThread(channel, jedisPool);
//        subThread.start();
//        LOGGER.warn("subscribePostConstruct执行结束!");
//    }
    
    /**
     * Publish端测试
     */
    @RequestMapping(value = "/testPublish/1", method = { RequestMethod.POST  ,RequestMethod.GET})
    public Object testPublish1(@RequestBody Map<String, Object> requestMap) {
        String message="This is the message from testPublish1 at time "+DateTimeUtil.formatDate2Str();
        Long publish = jedisCluster.publish(channel, message);
        LOGGER.error("Publish端测试结果:{}",publish);
        return null;
    }
    
    /**
     * Subscribe端测试
     */
    @RequestMapping(value = "/testSubscribe/1", method = { RequestMethod.POST ,RequestMethod.GET})
    public Object testSubscribe1(@RequestBody Map<String, Object> requestMap) {
        jedisCluster.subscribe(new Subscriber(), channel);
        return null;
    }
    
    @PostConstruct
    public void init() {
        // jedisCluster.subscribe(new Subscriber(), channel);
        LOGGER.error("Subscriber.PostConstruct执行了:{}", channel);
    }

}
