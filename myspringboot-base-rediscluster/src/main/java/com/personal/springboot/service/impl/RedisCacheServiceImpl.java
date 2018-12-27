package com.personal.springboot.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.utils.HashShardUtils;
import com.personal.springboot.common.utils.RegexUtils;
import com.personal.springboot.rediscluster.JedisClusterPipeline;
import com.personal.springboot.service.RedisCacheService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 *  RedisCacheService服务定义接口实现类
 *  
 *  如果在多线程中使用,可考虑使用多例@Scope("prototype"),當前通過調整redis綫程池参数，可以使用单例
 *  
 *  100W数据在key和value都在10Byte左右时，占用空间100M左右，若使用Hash的压缩列表特性，内存占用减少到1/5.
 *  
 * @Author  LiuBao
 * @Version 2.0
 *   2017年6月24日
 */
@Scope("prototype")
@Repository("redisCacheService")
public class RedisCacheServiceImpl implements RedisCacheService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheServiceImpl.class);
    
    @Value("${redis.cache.prefix:dev}")
    private String cachePrefix;
    
    @Value("${redis.cache.topic:topic}")
    private String topic;
    
    @Value("${redis.cache.batchSyncSize:1000}")
    private int  batchSyncSize;

    @Autowired(required=false)
    private JedisCluster jedisCluster;
    
    @Override
    public JedisCluster getJedisCluster() {
        return this.jedisCluster;
    }

    @Override
    public <T> String addByKey(String key, T value) {
        key=cachePrefix+key;
        String object2JsonString = null;
        if(value instanceof String){
            object2JsonString =value.toString();
        }else{
            object2JsonString = JSON.toJSONString(value);
        }
        String result = jedisCluster.set(key, object2JsonString);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("addByKey信息为:[key={},value={},result={}]",key,value,result);
        }
        return result;
    }
    
    @Override
    public <T> String addExByKey(String key, int seconds, T value) {
        key=cachePrefix+key;
        String object2JsonString=null;
        if(value instanceof String){
            object2JsonString =value.toString();
        }else{
            object2JsonString = JSON.toJSONString(value);
        }
        String result = jedisCluster.setex(key, seconds, object2JsonString);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("addExByKey信息为:[key={},value={},result={}]",key,value,result);
        }
        return result;
    }
    
    @Override
    public <T> String addExAtByKey(String key, long unixTime, T value) {
        String result = addByKey(key, value);
        Long expireAt = expireAtByKey(key, unixTime);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("addExAtByKey信息为:[key={},unixTime={},result={},expireAt={}]",key,unixTime,result,expireAt);
        }
        return result;
    }
    
    @Override
    public Long expireAtByKey(String key, long unixTime) {
        key=cachePrefix+key;
        Long result = jedisCluster.expireAt(key, unixTime);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("expireAtByKey信息为:[key={},unixTime={},result={}]",key,unixTime,result);
        }
        return result;
    }
    
    @Override
    public Long expireByKey(String key, int seconds) {
        key=cachePrefix+key;
        Long result = jedisCluster.expire(key, seconds);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("expireByKey信息为:[key={},seconds={},result={}]",key,seconds,result);
        }
        return result;
    }
    
    @Override
    public <T> Long addListKey(Map<String, T> map){
        Long sum = (long) 0;
        Iterator<Entry<String, T>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, T> entry = iterator.next();
            String key = entry.getKey();
            //key=cachePrefix+key;
            T value = entry.getValue();
            addByKey(key, value);
            sum = sum + 1;
        }
        return sum;
    }
    
    @Override
    public Long incrementBy(String key) {
        return incrementBy(key,null);
    }
    
    @Override
    public Long incrementExBy(String key,  int seconds){
        Long result = incrementBy(key,null);
        Long expireByKey = expireByKey(key, seconds);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("incrementExBy信息为:[key={},seconds={},expireByKey={},result={}]",cachePrefix+key,seconds,expireByKey,result);
        }
        return result;
    }
    
    @Override
    public Long incrementExAtBy(String key, long unixTime){
        Long result = incrementBy(key,null);
        Long expireAtByKey = expireAtByKey(key, unixTime);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("incrementExAtBy信息为:[key={},unixTime={},expireAtByKey={},result={}]",cachePrefix+key,unixTime,expireAtByKey,result);
        }
        return result;
    }
    
    private Long incrementBy(String key, Long defaultValue){
        key=cachePrefix+key;
        long result = 0L;
        if(null==defaultValue){
            result = jedisCluster.incr(key);
        }else{
            result = jedisCluster.incrBy(key, defaultValue);
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("incrementBy defaultValue信息为:[key={},defaultValue={},result={}]",key,defaultValue,result);
        }
        return result;
    }
    
    @Override
    public Long getLong(String key){
        key=cachePrefix+key;
        String result = jedisCluster.get(key);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("getLong信息为:[key={},result={}]",key,result);
        }
        return result==null?null:Long.valueOf(result);
    }
    
    @Override
    public String getString(String key) {
        key=cachePrefix+key;
        String result = jedisCluster.get(key);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("getObject信息为:[key={},result={}]",key,result);
        }
        return result;
    }
    
    @Override
    public  <V> V getObject(String key,Class<V> clazz){
        key=cachePrefix+key;
        String value = jedisCluster.get(key);
        V result = JSON.parseObject(value, clazz);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("getObjectClass信息为:[key={},result={}]",key,result);
        }
        return result;
    }

    @Override
    public Boolean existsByKey(String key){
        key=cachePrefix+key;
        Boolean result = jedisCluster.exists(key);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("existsByKey信息为:[key={},result={}]",key,result);
        }
        return result;
    }
    
    @Override
    public Long ttlByKey(String key){
        key=cachePrefix+key;
        long result = jedisCluster.ttl(key);
        if(result==-1){
            result=Integer.MAX_VALUE;
        }else if(result==-2){
            result=0;
        }else if(result>0){
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("ttlByKey信息为:[key={},result={}]",key,result);
        }
        return result;
    }
    
    @Override
    public Long deleteByKey(String key){
        key=cachePrefix+key;
        Long result = jedisCluster.del(key);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("deleteByKey信息为:[key={},result={}]",key,result);
        }
        return result;
    }

    /**
     * 模糊查找特殊通配符为普通字符方式:
     * keys *[/*][/*][/*][/*][/*]*
     */
    @Override
    public Set<String> keys(String pattern) {
        LOGGER.info("Start getting keys...");  
        TreeSet<String> keys = new TreeSet<>();  
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();  
        for(String nodeKey : clusterNodes.keySet()){
            JedisPool jedisPool = clusterNodes.get(nodeKey);  
            Jedis jedis = jedisPool.getResource();  
            try {  
                keys.addAll(jedis.keys(pattern));  
            } catch(Exception e){  
                LOGGER.error("Getting keys error: {}", e);  
            } finally{  
                LOGGER.info("Connection closed.");  
                //用完一定要close这个链接！！！  
                jedis.close();
            }  
        }  
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("keys信息为:pattern={},keys={}", pattern, JSON.toJSONString(keys));
        }
        return keys;
    }

    @Override
    public Long batchDelete(List<String> keyList){
        JedisClusterPipeline jcp = JedisClusterPipeline.pipelined(jedisCluster);
        jcp.refreshCluster();
        int size = keyList.size();
        List<Object> batchResult = new ArrayList<>(size);
        LOGGER.info("batchDelete中size为:{}", size);
        Long sum = (long) 0;
        //Long result = (long) 0;
        String key = null;
        for (int indexNum = 0; indexNum <size ; indexNum++) {
            //result = deleteByKey(keyList.get(i));
            //sum = sum + result;
            key = keyList.get(indexNum);
            if(StringUtils.isBlank(key)){
                continue;
            }
            if(!key.contains(cachePrefix)){
                key = cachePrefix + key;
            }
            jcp.del(key);
            if(indexNum%batchSyncSize==0&&indexNum>0){
                batchResult.addAll(jcp.syncAndReturnAll());
            }
        }
        //jcp.sync();
        batchResult.addAll(jcp.syncAndReturnAll());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("batchDelete记录条数为:{}", JSON.toJSONString(batchResult.size()));
            LOGGER.debug("batchDelete记录结果为:{}", JSON.toJSONString(batchResult));
        }
        try {
            jcp.close();
        } catch (Exception e) {
            LOGGER.error("batchDelete.jcp.close()信息异常!", e);
        }
        // 计算batchResult合
        for (Object result : batchResult) {
            if(result!=null&&RegexUtils.isNumber(result.toString())){
                sum +=Integer.valueOf(result.toString());
            }else{
                continue;
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("batchDelete操作结果sum为:{}", sum);
        }
        return sum;
    }
    
    @Override
    public Long batchAddByKey(Map<String, Object> keyValue) {
        Long sum = (long) 0;
        String object2JsonString=null;
        Object value = null;
        String key=null;
        JedisClusterPipeline jcp = JedisClusterPipeline.pipelined(jedisCluster);
        jcp.refreshCluster();
        List<Object> batchResult = new ArrayList<>();
        int indexNum=0;
        for (Map.Entry<String, Object> entry : keyValue.entrySet()) {
            key=entry.getKey();
            if(StringUtils.isBlank(key)){
                continue;
            }
            if(!key.contains(cachePrefix)){
                key = cachePrefix + key;
            }
            value = entry.getValue();
            object2JsonString=null;
            if(value instanceof String){
                object2JsonString =value.toString();
            }else{
                object2JsonString = JSON.toJSONString(value);
            }
            //String result = jedisCluster.set(key, object2JsonString);
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("batchAddByKey信息为:[key={},object2JsonString={}]",key,object2JsonString);
            }
            jcp.set(key, object2JsonString);
            if(indexNum%batchSyncSize==0&&indexNum>0){
                batchResult.addAll(jcp.syncAndReturnAll());
            }
            indexNum++;
        }
        //jcp.sync();
        batchResult.addAll(jcp.syncAndReturnAll());
        // 计算batchResult合
        for (Object result : batchResult) {
            if(result!=null&&"OK".equalsIgnoreCase(result.toString())){
                sum +=1;
            }else{
                continue;
            }
        }
        LOGGER.info("batchSetByKey结果batchResult.size()为:{}", batchResult.size());
        try {
            jcp.close();
        } catch (Exception e) {
            LOGGER.error("batchSetByKey.jcp.close()信息异常!", e);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("batchSetByKey信息添加记录成功sum条数为:{}", sum);
        }
        return sum;
    }

    @Override
    public Map<String, String> batchGetByKey(Set<String> keys) {
        Map<String, String> resultMap = new HashMap<String,String>();
        HashSet<String> keysSet = new HashSet<String>(keys);
        JedisClusterPipeline jcp = null;
        List<Object> batchResult = new ArrayList<>(keys.size());
        int indexNum=0;
        try {
            jcp = JedisClusterPipeline.pipelined(jedisCluster);
            jcp.refreshCluster();
            for (String key : keysSet) {
                if(StringUtils.isBlank(key)){
                    continue;
                }
                if(!key.contains(cachePrefix)){
                    key = cachePrefix + key;
                }
                jcp.get(key);
                if(indexNum%batchSyncSize==0&&indexNum>0){
                    batchResult.addAll(jcp.syncAndReturnAll());
                }
                indexNum++;
            }
            batchResult.addAll(jcp.syncAndReturnAll());
            LOGGER.info("batchResult长度为:{}", JSON.toJSONString(batchResult.size()));
        } catch (Exception e){
            LOGGER.error("批量添加异常!",e);
        }
        finally {
            try {
                jcp.close();
            } catch (Exception e) {
                LOGGER.error("batchGetByKey.jcp.close()信息异常!!!", e);
            }
        }
        indexNum=0;
        for (String key : keysSet) {
            if(StringUtils.isBlank(key)){
                continue;
            }
            Object value = batchResult.get(indexNum);
            if(value!=null){
                resultMap.put(key, value.toString());
            }else{
                resultMap.put(key, "");
            }
            indexNum++;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("batchGetByKey查询resultMap长度为:{}" , resultMap.size());
            LOGGER.debug("batchGetByKey信息resultMap为:{}", JSON.toJSONString(resultMap));
        } 
        return resultMap;
    }

    @Override
    public Long pubMessage(String topic,String message) {
        Long result = jedisCluster.publish(topic, message);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("pubMessage信息:[topic={},result={}],message={}",topic,result,message);
        }
        return result;
    }
    
    @Override
    public Long pubMessage(String message) {
        return pubMessage(topic, message);
    }
    
    @Override
    public Long hset(final String namespace, final String fieldKey, final String fieldValue) {
        String redisKey=cachePrefix+namespace;
        String hashKey = HashShardUtils.getShardNamespace(redisKey);
        Long result = jedisCluster.hset(hashKey, fieldKey, fieldValue);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("hset添加操作信息为:redisKey={},hashKey={},fieldKey={},fieldValue={},result={}", redisKey,hashKey,fieldKey,fieldValue,result);
        }
        return result;
    }
    
    @Override
    public String hget(final String namespace, final String fieldKey) {
        String redisKey=cachePrefix+namespace;
        String hashKey = HashShardUtils.getShardNamespace(redisKey);
        String result = jedisCluster.hget(hashKey, fieldKey);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("hget查询操作结果为:redisKey={},hashKey={},fieldKey={},result={}", redisKey,hashKey,fieldKey,result);
        }
        return result;
    }
    
    @Override
    public Map<String, String> hgetAll(final String namespace) {
        String redisKey=cachePrefix+namespace;
        String hashKey = HashShardUtils.getShardNamespace(redisKey);
        Map<String, String> result = jedisCluster.hgetAll(hashKey);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("hgetAll查询操作结果为:redisKey={},hashKey={},result={}", redisKey,hashKey,JSON.toJSONString(result));
        }
        return result;
    }
    
    @Override
    public Long hdel(final String namespace, final String... fieldsKey) {
        String redisKey=cachePrefix+namespace;
        String hashKey = HashShardUtils.getShardNamespace(redisKey);
        Long result = jedisCluster.hdel(hashKey, fieldsKey);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("hdel删除操作结果为:redisKey={},hashKey={},fieldValue={},fieldsKey={},result={}", redisKey,hashKey,JSON.toJSONString(fieldsKey),result);
        }
        return result;
    }
    
}