package com.personal.springboot.user.controller.task;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.service.RedisCacheService;

/**
 * 异步线程Task
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年6月20日
 */
@Component
public class RedisCacheAsyncTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheAsyncTask.class);
    
    @Lazy
    @Autowired
    private RedisCacheService redisCacheService;
    
    @Async
    public void executeBatchSaveCache(int pageIndex,Map<String, Object> paramMap) {
        LOGGER.info("第{}次异步线程Task保存缓存信息参数为:{}",pageIndex,JSON.toJSONString(paramMap.size()));
        /*for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            redisCacheService.addByKey(entry.getKey(), entry.getValue());
        }*/
        redisCacheService.batchAddByKey(paramMap);
        LOGGER.info("第{}次异步线程Task保存缓存信息参数结束!",pageIndex);
    }
	
}