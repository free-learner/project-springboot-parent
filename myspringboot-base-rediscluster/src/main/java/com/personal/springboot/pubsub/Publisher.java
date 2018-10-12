package com.personal.springboot.pubsub;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 发布者
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月20日
 */
public class Publisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);
    
    private final String channel;
    private final JedisPool jedisPool;

    public Publisher(String channel,JedisPool jedisPool) {
        this.channel = channel;
        this.jedisPool = jedisPool;
    }

    public void startPublish(String message) {
        LOGGER.info("发布消息为:{}",message);
        Jedis jedis = jedisPool.getResource();
        jedis.publish(channel, message);
        jedis.close();
    }
}