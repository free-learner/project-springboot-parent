package com.personal.springboot.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Subscriber订阅端线程启动类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月21日
 */
public class SubscriberThread extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriberThread.class);
    
	private final String channel;
	private final JedisPool jedisPool;
	private final Subscriber subscriber = new Subscriber();
	
	public SubscriberThread(String channel,JedisPool jedisPool) {
		super("SubThread");
		this.channel = channel;
		this.jedisPool = jedisPool;
	}
	
	@Override
	public void run() {
		LOGGER.info(String.format("subscribe redis, channel %s, thread will be blocked", channel));
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.subscribe(subscriber, channel);
		} catch (Exception e) {
			LOGGER.info(String.format("subsrcibe channel error, %s", e));
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}
}
