package com.personal.springboot.user.controller.aop;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.cons.Constants;

/**
 * 日志拦截器:RequestBody请求参数信息打印
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月14日
 */
@Order(9)
@ControllerAdvice
public class RequestBodyLogInterceptor implements RequestBodyAdvice{
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestBodyLogInterceptor.class);

    @Override
    public boolean supports(MethodParameter paramMethodParameter, Type paramType,
            Class<? extends HttpMessageConverter<?>> paramClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage paramHttpInputMessage, MethodParameter paramMethodParameter,
            Type paramType, Class<? extends HttpMessageConverter<?>> paramClass) throws IOException {
        return paramHttpInputMessage;
    }

    @Override
    public Object handleEmptyBody(Object paramObject, HttpInputMessage paramHttpInputMessage,
            MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass) {
        if(paramObject instanceof Map){
            paramObject=MapUtils.EMPTY_MAP;
        }else if(paramObject instanceof List){
            paramObject=ListUtils.EMPTY_LIST;
        }else if(paramObject instanceof Array ){
            paramObject=ArrayUtils.EMPTY_OBJECT_ARRAY;
        }else{
            paramObject=ObjectUtils.NULL;
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("RequestBody请求封装空值结果为:{}", paramObject);
        }
        return paramObject;
    }
    
    @Override
    public Object afterBodyRead(Object paramObject, HttpInputMessage paramHttpInputMessage,
            MethodParameter paramMethodParameter, Type paramType, Class<? extends HttpMessageConverter<?>> paramClass) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();   
        String url = request.getRequestURI();
        String method = request.getMethod();
        String contentType = request.getContentType();
        String ip = request.getRemoteAddr();
//        HttpHeaders headers = paramHttpInputMessage.getHeaders();
//        String contentType = headers.getContentType().toString();
//        String authHeader = headers.getFirst(Constants.AUTH_HEADER);
        String authHeader = request.getHeader(Constants.AUTH_HEADER);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LOGGER.info("RequestBody[{}]请求url:{}" ,method, url);    
            LOGGER.info("RequestBody请求ip={},contentType为:{}",ip, contentType);
            LOGGER.info("RequestBody请求authHeader为:\n{}", authHeader);
            LOGGER.info("RequestBody请求参数信息为:\n{}", JSON.toJSONString(paramObject,true));
            LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }
        return paramObject;
    }

}