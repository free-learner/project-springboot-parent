package com.personal.springboot.controller.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Token校验注解类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月15日
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OnceTokenGenerator {
    public boolean value() default true;
}