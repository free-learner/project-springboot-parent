package com.personal.springboot.common.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义注解,用于参数校验等
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年9月3日
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthParam {
    String value() default "";
    boolean required() default true;
}