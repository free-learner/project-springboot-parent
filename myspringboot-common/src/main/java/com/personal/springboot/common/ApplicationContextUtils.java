package com.personal.springboot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Bean对象工具类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年6月2日
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextUtils.class);
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextUtils.applicationContext == null) {
            ApplicationContextUtils.applicationContext = applicationContext;
        }
        LOGGER.info("ApplicationContext配置成功,在普通类可以获取applicationContext对象");
    }

    /**
     *  获取applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过name获取 Bean.
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     *  通过class获取Bean.
     */
    public static <T> T getBean(Class<T> clazz) {
        LOGGER.info("ApplicationContext获取对象",clazz);
        return getApplicationContext().getBean(clazz);
    }

    /**
     *  通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}