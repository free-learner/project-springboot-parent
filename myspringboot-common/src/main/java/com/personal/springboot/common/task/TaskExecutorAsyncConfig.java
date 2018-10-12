package com.personal.springboot.common.task;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 异步线程执行初始化
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月24日
 */
@EnableAsync
@Configuration
public class TaskExecutorAsyncConfig implements AsyncConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskExecutorAsyncConfig.class);

    @Value("${spring.task.corePoolSize:10}")
    private int corePoolSize;
    @Value("${spring.task.maxPoolSize:100}")
    private int maxPoolSize;
    @Value("${spring.task.queueCapacity:10}")
    private int queueCapacity;
    @Value("${spring.task.keepAliveSeconds:200}")
    private int keepAliveSeconds;
    @Value("${spring.task.threadNamePrefix:MY-ThreadPoolTaskExecutor-}")
    private String threadNamePrefix;
    
    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        threadPoolTaskExecutor.setThreadNamePrefix(threadNamePrefix);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
        LOGGER.info("初始化完成了corePoolSize={},maxPoolSize={}",corePoolSize,maxPoolSize);
        return threadPoolTaskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }

}