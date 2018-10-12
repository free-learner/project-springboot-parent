package com.yh.loan.front.test.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.service.RedisCacheService;
import com.personal.springboot.user.dao.entity.LoanUser;
import com.personal.springboot.user.service.LoanUserService;
import com.yh.loan.front.test.base.BaseServicesTest;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * RedisCacheService测试类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月19日
 */
public class RedisServiceTest extends BaseServicesTest {

    public final static Logger logger = LoggerFactory.getLogger(RedisServiceTest.class);  
    
    @Autowired
    private RedisCacheService redisCacheService;
    
    @Autowired
    private JedisCluster jedisCluster;
    
    @Autowired
    private LoanUserService loanUserService;
    
    @Test
    public void testSelectAll() throws IOException, ParseException {
        Assert.assertNotNull(loanUserService);
        List<LoanUser> selectAll = loanUserService.selectAll();
        System.out.println("查询结果为:"+JSON.toJSONString(selectAll));
    }

    @Test
    public void testKeysPattern() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        //匹配后缀为3个普通字符*的key
//        String pattern="*[/*][/*][/*]";
//        String pattern="*[/*][/*]";
        //test_VO_TMerchantSecret_CUSTCD,test_TMerchantLoanRel_SITSY10001000_CUSTCD
        String pattern="*test_*TMerchant*";
        Set<String> keys=redisCacheService.keys(pattern);
        System.out.println("查询结果为:"+JSON.toJSONString(keys));
        
        List<String> keyList=new ArrayList<>(keys);
        redisCacheService.batchDelete(keyList);
    }
    
    @Test
    public void testKeys() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        String pattern="*dev_DynamicConf_*";
        Set<String> keys=redisCacheService.keys(pattern);
        System.out.println("查询结果为:"+JSON.toJSONString(keys));
    }
    
    @Test
    public void testHkeys() throws IOException, ParseException {
        Assert.assertNotNull(jedisCluster);
        String pattern="*TOKEN*";
        Set<String> hkeys = jedisCluster.hkeys(pattern);
        logger.info("查询结果为:"+JSON.toJSONString(hkeys));
        logger.debug("Start getting keys...");  
        TreeSet<String> keys = new TreeSet<>();  
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();  
        for(String k : clusterNodes.keySet()){  
            JedisPool jp = clusterNodes.get(k);  
            Jedis connection = jp.getResource();  
            try {  
                keys.addAll(connection.keys(pattern));  
            } catch(Exception e){  
                logger.error("Getting keys error: {}", e);  
            } finally{  
                logger.debug("Connection closed.");  
                connection.close();
            }  
        }  
        logger.info("查询结果为2:"+JSON.toJSONString(keys));
    }
    
    @Test
    public void testDesc() throws IOException, ParseException {
        Assert.assertNotNull(jedisCluster);
        String key="*TOKEN*";
        Long decr = jedisCluster.decr(key);
        System.out.println("查询结果为:"+JSON.toJSONString(decr));
    }
    
    @Test
    public void testExistsByKey() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        String key="username";
        Boolean existsByKey = redisCacheService.existsByKey(key);
        Assert.assertTrue(existsByKey);
    }
    
    @Test
    public void testAddExAtByKey() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        String key="username";
        String value="LIUBAO";
        long unixTime=DateTimeUtil.getNextDayEarlyUnixTime();
        String addExAtByKey = redisCacheService.addExAtByKey(key, unixTime, value);
        System.out.println("addExAtByKey:"+addExAtByKey);
    }
    
    @Test
    public void testIncrementBy() throws IOException, ParseException {
        Assert.assertNotNull(redisCacheService);
        String key="counter";
        Boolean existsByKey = redisCacheService.existsByKey(key);
        long incrementBy = redisCacheService.incrementBy(key);
        System.out.println("当前计数器值1:"+incrementBy);
        incrementBy = redisCacheService.incrementBy(key);
        System.out.println("当前计数器值1:"+incrementBy);
        incrementBy = redisCacheService.incrementBy(key);
        System.out.println("当前计数器值1:"+incrementBy);
        
        Assert.assertTrue(existsByKey);
        String string = redisCacheService.getString(key);
        System.out.println("当前计数器值2:"+string);
        
        String nextNDay = DateTimeUtil.getNextNDay(null, 1);
        System.out.println("nextNDay="+nextNDay);
        long unixTime=DateTimeUtil.getNextDayEarlyUnixTime();
        Long incrementExAtBy = redisCacheService.incrementExAtBy(key, unixTime);
        System.out.println("incrementExAtBy:"+incrementExAtBy);
    }

}