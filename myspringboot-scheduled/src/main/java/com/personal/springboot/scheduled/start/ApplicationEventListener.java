package com.personal.springboot.scheduled.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * 域對象監聽
 * 
 * @Author LiuBao
 * @Version 2.0 2017年7月21日
 */
@SuppressWarnings("rawtypes")
public class ApplicationEventListener implements ApplicationListener {
    private static Logger LOGGER = LoggerFactory.getLogger(ApplicationEventListener.class);

    /**
     * 在这里可以监听到Spring Boot的生命周期
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            // 初始化环境变量
            LOGGER.info("初始化环境变量执行了");
        } else if (event instanceof ApplicationPreparedEvent) {
            // 初始化完成
            LOGGER.info("初始化完成执行了");
        } else if (event instanceof ContextRefreshedEvent) {
            // 应用刷新
            LOGGER.info("应用刷新执行了");
        } else if (event instanceof ApplicationReadyEvent) {
            // 应用已启动完成
            LOGGER.info("应用已启动完成执行了");
        } else if (event instanceof ContextStartedEvent) {
            // 应用启动，需要在代码动态添加监听器才可捕获
            LOGGER.info("应用启动，需要在代码动态添加监听器才可捕获执行了");
        } else if (event instanceof ContextStoppedEvent) {
            // 应用停止
            LOGGER.info("应用停止执行了");
        } else if (event instanceof ContextClosedEvent) {
            // 应用关闭
            LOGGER.info("应用关闭执行了");
        }
    }
}