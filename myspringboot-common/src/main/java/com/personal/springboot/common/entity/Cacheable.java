package com.personal.springboot.common.entity;

/**
 * 基础的缓存实体接口定义
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
public interface Cacheable {
    
    //获取redis的key值
    public abstract String getRedisKey();
    //是否缓存标识
    public abstract boolean isCachIgnore();

}
