package com.personal.springboot.user.controller.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.entity.BaseEntity;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.controller.aop.OnceTokenGenerator;
import com.personal.springboot.controller.base.BaseController;
import com.personal.springboot.controller.vo.ResultInfo;
import com.personal.springboot.service.RedisCacheService;

/**
 * 日志拦截器:ResponseBody响应参数信息打印
 * 添加包扫描路径信息配置
 * 
 * ResponseBodyAdvice的子类:com.alibaba.fastjson.support.spring.FastJsonpResponseBodyAdvice
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月14日
 */
@Order(10)
@ControllerAdvice(basePackages={/*"com.personal.springboot.user.controller.user",*/"com.personal.springboot.user.controller.sys","com.personal.springboot.controller.base"})
public class ResponseBodyAdviceLogInterceptor extends BaseController<BaseEntity,ResponseBodyAdviceLogInterceptor> implements ResponseBodyAdvice<ResultInfo<Object>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ResponseBodyAdviceLogInterceptor.class);

	@Autowired
	private RedisCacheService redisCacheService;
	
    @Override
    public boolean supports(MethodParameter paramMethodParameter, Class<? extends HttpMessageConverter<?>> paramClass) {
        return true;
    }
    
    @SuppressWarnings("unused")
    @Override
    public ResultInfo<Object> beforeBodyWrite(ResultInfo<Object> resultObject, MethodParameter paramMethodParameter,
            MediaType mediaType, Class<? extends HttpMessageConverter<?>> paramClass,
            ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();    
        HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();    
        String url = request.getRequestURI();
        String method = request.getMethod();
        String contentType = request.getContentType();
        String ip = request.getRemoteAddr();
        String type = mediaType.toString();
        if(MediaType.APPLICATION_JSON_UTF8_VALUE.equals(type)
                ||MediaType.APPLICATION_JSON_VALUE.equals(type)){
        }
        //添加Token判断  
        OnceTokenGenerator onceToken =paramMethodParameter.getMethod().getAnnotation(OnceTokenGenerator.class);
        if (onceToken!=null&&onceToken.value()) {
            Assert.notNull(redisCacheService,"缓存数据源redisCacheService为空!");
            resultObject.setOnceToken(UUIDGenerator.generate());
        }
        
        //校验接口 (在校验接口位置添加,需要在请求开始的时候校验) TODO
        if (onceToken!=null&&!onceToken.value()) {
            Assert.notNull(redisCacheService,"缓存数据源redisCacheService为空!");
            String onceTokenStr="onceTokenStr";
            try {
                if(redisCacheService.existsByKey(onceTokenStr)){
                    redisCacheService.deleteByKey(onceTokenStr);
                }else{
                    //重定向或抛出指定重复提交异常即可 
//                    request.getRequestDispatcher("/error/expired").forward(request,response);
//                    return null;
                    LOGGER.error("ResponseBody中OnceToken信息[onceToken={}]重复提交." ,onceTokenStr);    
                    return super.getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_NOAUTH);
                }
            } catch (Exception e) {
                LOGGER.error("ResponseBody中OnceToken信息[onceToken={}]校验异常:" ,onceTokenStr, e);    
                return super.getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_DEFAULT);
            }
        }
        
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LOGGER.info("ResponseBody[{}]请求url:{}" ,method, url);    
            LOGGER.info("ResponseBody请求ip={},contentType为:{}",ip, contentType);
            LOGGER.info("ResponseBody响应参数信息为:\n{}", JSON.toJSONString(resultObject,true));
            LOGGER.info("##########################################################");
        }
        return resultObject;
    }

}