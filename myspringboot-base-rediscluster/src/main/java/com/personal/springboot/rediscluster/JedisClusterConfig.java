package com.personal.springboot.rediscluster;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.personal.springboot.common.properties.RedisClusterProperties;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisCluster配置文件
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月1日
 */
@Configuration
public class JedisClusterConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterConfig.class);
    
    @Autowired(required=false)
    private RedisClusterProperties redisClusterProperties;
    
    @Bean
    public JedisCluster getJedisCluster() {
        String[] serverArray = redisClusterProperties.getClusterNodes().split(",");
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
         for (String ipPort : serverArray) {
             String[] ipPortPair = ipPort.split(":");
             jedisClusterNodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
         }
        JedisCluster jedisCluster = new JedisCluster(jedisClusterNodes,redisClusterProperties.getConnectionTimeout(),
                redisClusterProperties.getMaxAttempts(),initJedisPoolConfig());
        //带密码认证的配置方式
//        jedisCluster = new JedisCluster(jedisClusterNodes, redisClusterProperties.getConnectionTimeout(), 
//                redisClusterProperties.getSoTimeout(), redisClusterProperties.getMaxAttempts(), redisClusterProperties.getPassword(), initJedisPoolConfig());
        LOGGER.debug("获取JedisCluster执行结束。。。");
         return jedisCluster;
        //return new JedisCluster(jedisClusterNodes, redisProperties.getCommandTimeout());
    }
    
    @Bean(name="jedisPoolConfig")
    public JedisPoolConfig initJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisClusterProperties.getPoolMaxIdle());
        jedisPoolConfig.setMaxWaitMillis(redisClusterProperties.getPoolMaxWait());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisClusterProperties.getPoolTimeBetweenEvictionRunsMillis());
        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisClusterProperties.getPoolMinEvictableIdleTimeMillis());
        jedisPoolConfig.setTestOnBorrow(redisClusterProperties.isPoolTestOnBorrow());
        jedisPoolConfig.setMaxIdle(redisClusterProperties.getPoolMaxIdle());
        jedisPoolConfig.setMinIdle(redisClusterProperties.getPoolMinIdle());
        return jedisPoolConfig;
    }

}