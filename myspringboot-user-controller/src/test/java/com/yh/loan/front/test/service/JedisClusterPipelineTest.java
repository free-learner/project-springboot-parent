package com.yh.loan.front.test.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.util.JedisClusterCRC16;

/**
 * RedisCacheService测试类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月19日
 */
public class JedisClusterPipelineTest {
    public final static Logger LOGGER = LoggerFactory.getLogger(JedisClusterPipelineTest.class);

    private static final String clusterNodes="10.0.68.213:7000,10.0.68.213:7001,10.0.68.213:7002,10.0.68.214:7000,10.0.68.214:7001,10.0.68.214:7002";
    private static final int maxAttempts=3;
    private static final int connectionTimeout=5000;
    private static final int maxIdle=30;
    private static final int maxWait=50;
    private static final long timeBetweenEvictionRunsMillis=30000;
    private static final long minEvictableIdleTimeMillis=30000;
    private static final boolean testOnBorrow=false;
    private static  final JedisCluster jedisCluster;
    private static  final TreeMap<Long, String> slotHostMap;
    private static  final Map<String, JedisPool> nodeMap;
    
    static{
        String[] serverArray = clusterNodes.split(",");
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
         for (String ipPort : serverArray) {
             String[] ipPortPair = ipPort.split(":");
             jedisClusterNodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
         }
         
         JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
         jedisPoolConfig.setMaxIdle(maxIdle);
         jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
         jedisPoolConfig.setMaxIdle(maxIdle);
         
         jedisCluster = new JedisCluster(jedisClusterNodes,connectionTimeout,maxAttempts,jedisPoolConfig);
        LOGGER.debug("获取JedisCluster执行结束。。。");
        
        nodeMap = jedisCluster.getClusterNodes();
        String anyHost = nodeMap.keySet().iterator().next();
        //getSlotHostMap方法在下面有
        slotHostMap = getSlotHostMap(anyHost); 
    }
    
    @SuppressWarnings("unchecked")
    private static TreeMap<Long, String> getSlotHostMap(String anyHostAndPortStr) {
        TreeMap<Long, String> tree = new TreeMap<Long, String>();
        String parts[] = anyHostAndPortStr.split(":");
        HostAndPort anyHostAndPort = new HostAndPort(parts[0], Integer.parseInt(parts[1]));
        try{
            Jedis jedis = new Jedis(anyHostAndPort.getHost(), anyHostAndPort.getPort());
            List<Object> list = jedis.clusterSlots();
            for (Object object : list) {
                List<Object> list1 = (List<Object>) object;
                List<Object> master = (List<Object>) list1.get(2);
                String hostAndPort = new String((byte[]) master.get(0)) + ":" + master.get(1);
                tree.put((Long) list1.get(0), hostAndPort);
                tree.put((Long) list1.get(1), hostAndPort);
            }
            jedis.close();
        }catch(Exception e){
            
        }
        return tree;
    }
    
    @Test
    public void testPipelined() throws IOException, ParseException {
        String key="*111101061977051203**";
        //获取槽号
        //获取到对应的Jedis对象
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            key=key + i;
            long slot = JedisClusterCRC16.getSlot(key); 
            Map.Entry<Long, String> entry = slotHostMap.lowerEntry(slot);
            Jedis jedis = nodeMap.get(entry.getValue()).getResource();
            Pipeline pipeline = jedis.pipelined();
            pipeline.set(key, "pvalue" + i);
            List<Object> results = pipeline.syncAndReturnAll(); 
            System.out.println("结果"+i+"为:"+JSON.toJSONString(results));
            jedis.disconnect();
        }
        System.out.println("Pipelined SET: " + ((System.currentTimeMillis() - start)/1000.0) + " S");
    }

}