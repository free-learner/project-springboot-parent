package com.personal.springboot.lock;

import java.util.Collections;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisException;

/**
 * 分布式锁
 * 
 * @Author  LiuBao
 * @Version 2.0
 * @Date 2018年12月27日
 */
@Component
public class RedisDistributedClusterLock {
    private static final Logger logger = LoggerFactory.getLogger(RedisDistributedClusterLock.class);

    @Value("${redis.cache.prefix:dev}")
    private String cachePrefix;

    @Autowired(required=false)
    private JedisCluster jedisCluster;

    public String lockWithTimeout(String locaName, long acquireTimeout, long timeout) {
        String retIdentifier = null;
        int num = 1;
        String identifier = UUID.randomUUID().toString();
        String lockKey = "lock:" + this.cachePrefix + locaName;
        try {
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                String set = this.jedisCluster.set(lockKey, identifier, "NX", "PX", timeout);
                if ("OK".equalsIgnoreCase(set)) {
                    retIdentifier = identifier;
                    String str1 = retIdentifier;
                    return str1;
                }
                num++;
                if (this.jedisCluster.ttl(lockKey).longValue() == -1L)
                    this.jedisCluster.pexpire(lockKey, timeout);
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (JedisException e) {
            logger.error("redis获取锁出现未知异常：" + JSON.toJSONString(e));
        } finally {
        }
        logger.error(locaName + "获取锁失败，获取次数：" + num);
        return retIdentifier;
    }

    public boolean releaseLock(String lockName, String identifier) {
        String lockKey = "lock:" + this.cachePrefix + lockName;
        boolean retFlag = false;
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = this.jedisCluster.eval(script, Collections.singletonList(lockKey), Collections.singletonList(identifier));
            if (1L == ((Long) result).longValue()) {
                retFlag = true;
            }
        } catch (JedisException e) {
            logger.error("redis释放锁出现未知异常：" + JSON.toJSONString(e));
        } finally {
        }
        logger.info("identifier：" + identifier + "释放：" + retFlag);
        return retFlag;
    }
}