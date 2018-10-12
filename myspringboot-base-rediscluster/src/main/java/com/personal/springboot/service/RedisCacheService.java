package com.personal.springboot.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.JedisCluster;

/**
 * RedisCacheService服务定义接口类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public interface RedisCacheService {

    <T> String addByKey(String key, T object);

    <T> String addExByKey(String key, int seconds, T object);
    
    <T> String addExAtByKey(String key, long unixTime, T object);
    
    <T> Long addListKey(Map<String, T> map) ;

    Long incrementBy(String key);
    
    Long incrementExBy(String key, int seconds);
    
    Long incrementExAtBy(String key, long unixTime);
    
    Long getLong(String key);
    
    String getString(String key) ;

    <T> T getObject(String key, Class<T> clazz);

    Long deleteByKey(String key);

    /**
     * 批量删除
     */
    Long batchDelete(List<String> keyList) ;

    Long expireByKey(String key, int seconds);
    
    Long expireAtByKey(String key, long unixTime) ;

    Long ttlByKey(String key);

    Boolean existsByKey(String key);

    /**
     * 缓存key列表模糊查询
     */
    Set<String> keys(String pattern);
    
    //<T extends List<Object>> Long addListKey(Map<String, T> keyValue);
    /**
     * 批量保存
     */
    Long batchAddByKey(Map<String, Object> keyValue);
    /**
     * 批量保存
     */
    Map<String, String> batchGetByKey(Set<String> keys);
    
    Long pubMessage(String message);
    
    Long pubMessage(String topic,String message) ;
    
    Long hset(String namespace, String fieldKey, String fieldValue);

    String hget(String namespace, String fieldKey);

    Map<String, String> hgetAll(String namespace);

    Long hdel(String namespace, String... fieldsKey);

    JedisCluster getJedisCluster();

}
