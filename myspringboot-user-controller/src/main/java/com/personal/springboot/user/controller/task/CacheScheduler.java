package com.personal.springboot.user.controller.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.user.dao.entity.LoanUser;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPubSub;

/**
 * 缓存定时执行类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年8月29日
 */
@Component
@EnableScheduling
@SuppressWarnings("unused")
public class CacheScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheScheduler.class);
    private static final String CRON_PER_DAY = "59 59 23 * * ?";
    private static final String CRON_PER_HOUR = "* * */1 * * ?";
    private static final String CRON_PER_MINUTE = "* */1 * * * ?";
    private static final String CRON_PER_SECOND = "*/1 * * * * ?";
    private static final String CRON_ONCE = "59 59 23 ? * *";

    @Value("${redis.cache.logFlag}")
    private boolean logFlag;
    
    @Autowired
    private JedisCluster jedisCluster;
    
    @Value("${redis.cache.topic}")
    private String topic;
    
    @Autowired
    private RedisCacheAsyncTask redisCacheAsyncTask;

    @Scheduled(initialDelay=6000,fixedRate = 86400000)
    public void SubscriberPostConstruct() {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("当前获取信息[topic={}],[logFlag={}]", topic,logFlag);
        }
        synchronized (CacheScheduler.class) {
            if(logFlag){
                jedisCluster.subscribe(new Subscriber(), topic);
            }
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.warn("SubscriberPostConstruct执行结束了,[topic={}],[logFlag={}]", topic,logFlag);
        }
    }

    private final class Subscriber extends JedisPubSub {
        
        /**
         * try-catch捕获异常
         */
        @Override
        public void onMessage(String channel, String message) {
            LOGGER.info(String.format("Received. Channel: %s, Message: %s", channel, message));
            LoanUser record=JSON.parseObject(message, LoanUser.class);
        }
        
        @Override
        public void onPMessage(String pattern, String channel, String message) {
            LOGGER.info(String.format("PMessage. Pattern: %s, Channel: %s, Msg: %s", pattern, channel, message));
        }
        
        @Override
        public void onSubscribe(String channel, int subscribedChannels) {
            logFlag=Boolean.FALSE;
            LOGGER.info("onSubscribe ... logFlag:{}",logFlag);
        }
        
        @Override
        public void onUnsubscribe(String channel, int subscribedChannels) {
            logFlag=Boolean.TRUE;
            LOGGER.info("onUnsubscribe ... logFlag:{}",logFlag);
        }
        
        @Override
        public void onPUnsubscribe(String pattern, int subscribedChannels) {
            LOGGER.info("onPUnsubscribe");
        }
        
        @Override
        public void onPSubscribe(String pattern, int subscribedChannels) {
            LOGGER.info("onPSubscribe");
        }
        
    }
    
    
}
