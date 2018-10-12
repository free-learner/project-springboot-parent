package com.yh.loan.front.test.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.properties.RedisClusterProperties;
import com.personal.springboot.rediscluster.JedisClusterPipeline;
import com.personal.springboot.service.RedisCacheService;
import com.yh.loan.front.test.base.BaseServicesTest;

import redis.clients.jedis.JedisCluster;

/**
 * JedisClusterPipeline测试类2
 * 
 * @Author LiuBao
 * @Version 2.0 2017年6月22日
 */
public class JedisClusterPipelineTest2 extends BaseServicesTest {
    public final static Logger LOGGER = LoggerFactory.getLogger(JedisClusterPipelineTest2.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private JedisCluster jedisCluster;
    
    @Value("${redis.cache.prefix}")
    private String cachePrefix;

    /**
     * OK
     *  batch write
     *  10000=====>1.157 S
     *  100000=====>63.825 S
     */
    @Test
    public void testPipelinedBatchSet() throws IOException, ParseException {
        long start = System.currentTimeMillis();
        JedisClusterPipeline jcp = JedisClusterPipeline.pipelined(jedisCluster);
        jcp.refreshCluster();
        int tmpNum=0;
        try {
            for (int i = 0; i < 100000; i++) {
                jcp.set(cachePrefix+"batch-key****" + i, "batch-write*****v" + i);
                tmpNum++;
            }
            //jcp.sync();
            List<Object> batchResult = jcp.syncAndReturnAll();
            LOGGER.info("结果为:{}" , JSON.toJSONString(batchResult));
            LOGGER.info("结果tmpNum为:{}" , tmpNum);
        } finally {
            jcp.close();
        }
        LOGGER.info("Pipelined SET: " + (System.currentTimeMillis() - start) / 1000.0 + " S");
    }
    
    @Autowired
    private RedisClusterProperties redisClusterProperties;
    
    @Test
    public void testPipelinedBatchdel() throws IOException, ParseException {
        long start = System.currentTimeMillis();
        JedisClusterPipeline jcp = JedisClusterPipeline.pipelined(jedisCluster);
        jcp.refreshCluster();
        LOGGER.info("redisClusterProperties为:{}" , JSON.toJSONString(redisClusterProperties));
        int tmpNum=0;
        try {
            for (int i = 0; i < 100; i++) {
                //jcp.del(cachePrefix+"Json_key" + i);
                jcp.del("key" + i);
            }
            //jcp.sync();
            List<Object> batchResult = jcp.syncAndReturnAll();
            for (Object object : batchResult) {
                tmpNum+=Long.valueOf(object==null?"0":object.toString());
                
            }
            LOGGER.info("结果为:{}" , JSON.toJSONString(batchResult));
            LOGGER.info("结果tmpNum为:{},batchResult长度为:{}" , tmpNum,batchResult.size());
        } finally {
            jcp.close();
        }
        LOGGER.info("Pipelined DEL: " + (System.currentTimeMillis() - start) / 1000.0 + " S");
    }
    
    /**
     * OK
     *  batch read
     *  100000=====>17.353 S
     *  查询后速度为:4.988 S
     */
    @Test
    public void testPipelinedBatchGet() {
        long start = System.currentTimeMillis();
        JedisClusterPipeline jcp = JedisClusterPipeline.pipelined(jedisCluster);
        jcp.refreshCluster();
        try {
            for (int i = 0; i < 100000; i++) {
                jcp.get(cachePrefix+"batch-key*****" + i);
            }
            List<Object> batchResult = jcp.syncAndReturnAll();
            LOGGER.info("结果为:{}" , JSON.toJSONString(batchResult.size()));
        } finally {
            jcp.close();
        }
        LOGGER.info("Pipelined Get: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }

    /**
     * OK
     *  100000=====> 24.283 S
     *  110000=====>23.669 S
     *  110001=====>23.187 S
     *  199999=====>12.248 S
     *  
     *  200000=====>8.878 S
     */
    @Test
    public void testKeysPattern() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        long start = System.currentTimeMillis();
        // 匹配后缀为3个普通字符*的key
        // String pattern="*[/*][/*][/*]";
        String pattern = "*batch-key[/*][/*][/*][/*][/*]*";
        // String pattern = "*[/*][/*][/*]*";
        Set<String> keys = redisCacheService.keys(pattern);
        LOGGER.info("查询结果为:" + JSON.toJSONString(keys.size()));
        LOGGER.info("Pipelined Get: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }
    
    /**
      * OK
     * keys: 210000=====>170.253 S
     * keys: 209900=====>185.588 S
     * keys: 100000=====>111.132 S
     * 
     * del: 1000=====>1.829 S
     * del: 10000=====>1.715 S
     * del: 34994=====>5.658 S
     * del: 100000=====>17.058 S
     * del: 300000=====>77.093 S
     */
    @Test
    public void testBatchDelByKey3() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        Set<String> keyset=new HashSet<>();
        long start = System.currentTimeMillis();
        //String pattern = "*[/*][/*][/*][/*][/*]*";
        String pattern = "*6*[/*][/*]";//994789
        keyset = redisCacheService.keys(pattern);
        LOGGER.info("keys: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
        LOGGER.info("查询keyset长度为:" + JSON.toJSONString(keyset.size()));
        List<String> keyset2=new CopyOnWriteArrayList<>();
        int i=0;
        for (String key : keyset) {
            if(i<10000){
                keyset2.add(key);
                i++;
            }else{
                break;
            }
        }
        LOGGER.info("查询keyset2长度为:" + JSON.toJSONString(keyset2.size()));
        start = System.currentTimeMillis();
        Long result = redisCacheService.batchDelete(keyset2);
        LOGGER.info("查询result长度为:" +result);
        LOGGER.info("testBatchDelByKey: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }
    
    /**
     * 删除三要素认证信息
     */
    @Test
    public void testBatchDelByKey4() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        Set<String> keyset=new HashSet<>();
        long start = System.currentTimeMillis();
        String pattern = "*prd_TOKEN*";//
        keyset = redisCacheService.keys(pattern);
        LOGGER.info("keys: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
        LOGGER.info("查询keyset长度为:" + JSON.toJSONString(keyset.size()));
        List<String> keyset2=new CopyOnWriteArrayList<>(keyset);
        LOGGER.info("查询keyset2长度为:" + JSON.toJSONString(keyset2.size()));
        start = System.currentTimeMillis();
        Long result = redisCacheService.batchDelete(keyset2);
        LOGGER.info("查询result长度为:" +result);
        LOGGER.info("testBatchDelByKey: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }
    
    /**
     * OK
     * del: 1000=====>1.746  S
     * del: 10000=====> 18.273 SS
     * del: 100000=====> 18.008 SS
     */
    @Test
    public void testBatchDelByKey2() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        List<String> keyset=new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            keyset.add("batch-key****"+i);
        }
        LOGGER.info("查询keyset长度为:" + JSON.toJSONString(keyset));
        long start = System.currentTimeMillis();
        Long result = redisCacheService.batchDelete(keyset);
        LOGGER.info("查询result长度为:" +result);
        LOGGER.info("testBatchDelByKey: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }
    
    /**
     * OK
    * keys: 210000=====>170.253 S
    * keys: 209900=====>185.588 S
    * 
    * del: 1000=====>1.829 S
    */
    @Test
    public void testBatchDelByKey1() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        Set<String> keyset=new HashSet<>();
        long start = System.currentTimeMillis();
        String pattern = "*[/*][/*][/*][/*][/*]*";
        keyset = redisCacheService.keys(pattern);
        LOGGER.info("keys: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
        List<String> keyset2=new CopyOnWriteArrayList<>(keyset);
        LOGGER.info("查询keyset长度为:" + JSON.toJSONString(keyset.size()));
        int i=0;
        for (String key : keyset2) {
            i++;
            if(i>1000){
                keyset2.remove(key);
            }
        }
        keyset.clear();
        keyset.addAll(keyset2);
        
        start = System.currentTimeMillis();
        Long result = redisCacheService.batchDelete(new ArrayList<>(keyset));
        LOGGER.info("查询result长度为:" +result);
        LOGGER.info("testBatchDelByKey: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }
    
    
    /**
     * OK
     * keys:210000 =====>62.061 S
     * keys:210000 =====>48.402 S
     * 
     * Get: 10000=====>  4.161 SS
     * Get: 100000=====>  S
     */
    @Test
    public void testBatchGetByKey2() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        Set<String> keyset=new HashSet<>();
        long start = System.currentTimeMillis();
        String pattern = "*[/*][/*][/*][/*][/*]*";
        keyset = redisCacheService.keys(pattern);
        LOGGER.info("keys: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
        Iterator<String> iterator = keyset.iterator();
        Set<String> keyset2=new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            if(iterator.hasNext()){
                keyset2.add(iterator.next());
            }
        }
        //keyset2.clear();
        //keyset2.add("batch-key555*****");
        LOGGER.info("testBatchGetByKey查询长度为:" + JSON.toJSONString(keyset2.size()));
        start = System.currentTimeMillis();
        Map<String, String> resultMap = redisCacheService.batchGetByKey(keyset2);
        LOGGER.info("testBatchGetByKey查询resultMap长度为:" + JSON.toJSONString(resultMap.size()));
        LOGGER.info("Pipelined Get: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }
    
    /**
     *  OK
     *  10000=====>1.905 S
     */
    @Test
    public void testBatchGetByKey1() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        long start = System.currentTimeMillis();
        Set<String> keyset=new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            keyset.add("batch-key*****"+i);
        }
        keyset.add("batch-key558885*****");
        LOGGER.info("testBatchGetByKey查询长度为:" + JSON.toJSONString(keyset.size()));
        start = System.currentTimeMillis();
        Map<String, String> resultMap = redisCacheService.batchGetByKey(keyset);
        LOGGER.info("testBatchGetByKey查询resultMap长度为:" + JSON.toJSONString(resultMap.size()));
        LOGGER.info("Pipelined Get: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }
    
    /**
     *  OK
     *  1000=====>0.747 S
     *  10000=====>4.649 S==>1.053 S
     *  100000=====>51.265 S
     * @throws InterruptedException 
     */
    @Test
    public void testBatchSetByKey() throws IOException, ParseException, InterruptedException {
        Assert.assertNotNull(redisCacheService);
        long start = System.currentTimeMillis();
        Map<String, Object> keyValue=new ConcurrentHashMap<>();
        for (int i = 0; i < 10000; i++) {
            keyValue.put("batch-key****"+i,"value"+i);
        }
        Long result = redisCacheService.batchAddByKey(keyValue);
        LOGGER.info("查询result结果为:" +result);
        LOGGER.info("Pipelined Set: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
        Thread.sleep(1000);
        start = System.currentTimeMillis();
        result = redisCacheService.batchAddByKey(keyValue);
        LOGGER.info("查询result结果为:" +result);
        LOGGER.info("Pipelined Set: " + ((System.currentTimeMillis() - start) / 1000.0) + " S");
    }

}