package com.personal.springboot.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.personal.springboot.common.entity.ToString;

/**
 * redis cluster 属性配置信息
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月1日
 */
@Component
@ConfigurationProperties(prefix = "redis.cache")
public class RedisClusterProperties extends ToString{
    private static final long serialVersionUID = -4484563062044888074L;
    private String clusterNodes;
    private int connectionTimeout;
    private int soTimeout;
    private int maxAttempts;
    private String password;

    private int poolMaxWait;
    private int poolMaxIdle;
    private int poolMinIdle;
    private int poolMaxActive;
    private int poolMaxTotal;
    private boolean poolTestOnBorrow;
    private int poolMinEvictableIdleTimeMillis;
    private int poolTimeBetweenEvictionRunsMillis;

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoolMaxWait() {
        return poolMaxWait;
    }

    public void setPoolMaxWait(int poolMaxWait) {
        this.poolMaxWait = poolMaxWait;
    }

    public int getPoolMaxIdle() {
        return poolMaxIdle;
    }

    public void setPoolMaxIdle(int poolMaxIdle) {
        this.poolMaxIdle = poolMaxIdle;
    }

    public int getPoolMaxActive() {
        return poolMaxActive;
    }

    public void setPoolMaxActive(int poolMaxActive) {
        this.poolMaxActive = poolMaxActive;
    }

    public int getPoolMaxTotal() {
        return poolMaxTotal;
    }

    public void setPoolMaxTotal(int poolMaxTotal) {
        this.poolMaxTotal = poolMaxTotal;
    }

    public boolean isPoolTestOnBorrow() {
        return poolTestOnBorrow;
    }

    public void setPoolTestOnBorrow(boolean poolTestOnBorrow) {
        this.poolTestOnBorrow = poolTestOnBorrow;
    }

    public int getPoolMinEvictableIdleTimeMillis() {
        return poolMinEvictableIdleTimeMillis;
    }

    public void setPoolMinEvictableIdleTimeMillis(int poolMinEvictableIdleTimeMillis) {
        this.poolMinEvictableIdleTimeMillis = poolMinEvictableIdleTimeMillis;
    }

    public int getPoolTimeBetweenEvictionRunsMillis() {
        return poolTimeBetweenEvictionRunsMillis;
    }

    public void setPoolTimeBetweenEvictionRunsMillis(int poolTimeBetweenEvictionRunsMillis) {
        this.poolTimeBetweenEvictionRunsMillis = poolTimeBetweenEvictionRunsMillis;
    }

}
