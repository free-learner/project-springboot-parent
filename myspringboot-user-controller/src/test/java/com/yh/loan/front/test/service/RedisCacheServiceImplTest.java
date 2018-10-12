package com.yh.loan.front.test.service;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.utils.HashShardUtils;
import com.personal.springboot.service.RedisCacheService;
import com.yh.loan.front.test.base.BaseServicesTest;

import redis.clients.jedis.JedisCluster;

public class RedisCacheServiceImplTest extends BaseServicesTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheServiceImplTest.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired(required = false)
    private JedisCluster jedisCluster;

    @Test
    public void testName() throws Exception {
        String sharkKey = "dev_";
        String namespace =sharkKey+ "sysuser";
        
        //String hashKey = HashShardUtils.getShardNamespace(sharkKey, namespace);
        String hashKey = HashShardUtils.getShardNamespace(namespace);
        LOGGER.info("操作信息为hashKey={}", hashKey);
        for (int i = 0; i < 10; i++) {
            String fieldKey = "username"+i;
            String fieldValue = "刘保的值"+i;
            LOGGER.info("操作信息为fieldKey={},fieldValue={}", fieldKey,fieldValue);
            Long hset = jedisCluster.hset(hashKey, fieldKey, fieldValue);
            LOGGER.info("hset-[{}]结果为:{}", i, hset);
            String hget = jedisCluster.hget(hashKey, fieldKey);
            LOGGER.info("hget-[{}]结果为:{}", i,hget);
            if(i==5){
                Long hdel = jedisCluster.hdel(hashKey, fieldKey);
                LOGGER.info("hdel-[{}]结果为:{}", i,hdel);
            }
        }

        Map<String, String> hgetAll = jedisCluster.hgetAll(hashKey);
        LOGGER.info("hgetAll结果为:{}", JSON.toJSONString(hgetAll));
    }

    @Test
    public void testHset() {
        String namespace = "sysuser";
        for (int i = 0; i < 10; i++) {
            String fieldKey = "wf" + i;
            String fieldValue = "刘保的值" + i;
            Long hset = redisCacheService.hset(namespace, fieldKey, fieldValue);
            LOGGER.info("hset结果为:{}", hset);
        }
    }

    @Test
    public void testHget(){
        String namespace ="sysuser";
        String fieldKey="wf";
        String hget = redisCacheService.hget(namespace, fieldKey);
        LOGGER.info("hget结果为:{}", hget);
    }
    
    @Test
    public void testHdel(){
        String namespace ="sysuser";
        String fieldKey="wf5";
        Long hdel = redisCacheService.hdel(namespace, fieldKey);
        LOGGER.info("hdel结果为:{}", hdel);
    }
    
    @Test
    public void testHgetAll(){
        String namespace ="sysuser";
        Map<String, String> hgetAll = redisCacheService.hgetAll(namespace);
        LOGGER.info("hgetAll结果为:{}", JSON.toJSONString(hgetAll));
    }

}
