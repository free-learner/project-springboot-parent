package com.personal.springboot.controller.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解类
 * 
 * @Author  LiuBao
 * @Version 2.0
 * @Date 2016年9月17日
 *
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    public LogOperation module() default LogOperation.FRONT;
	
    public String operation();

}