package com.personal.springboot.user.controller.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.entity.BaseEntity;
import com.personal.springboot.controller.aop.Log;
import com.personal.springboot.controller.base.BaseController;
import com.personal.springboot.controller.vo.ResultInfo;

/**
 * 日志拦截器:Controller层注解实现
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月13日
 */
@Order(4)
@ControllerAdvice
public class LogInterceptor extends BaseController<BaseEntity,LogInterceptor>implements HandlerInterceptor,AfterReturningAdvice {

	private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(logger.isDebugEnabled()){
			logger.debug("当前请求uri信息:" + request.getRequestURI());
		}
		if (!HandlerMethod.class.isAssignableFrom(handler.getClass())) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		MethodParameter[] params = handlerMethod.getMethodParameters();
		boolean result = true;
		for (MethodParameter param : params) {
			RequestParam requestParam = param.getParameterAnnotation(RequestParam.class);
			if (requestParam != null) {
				if (requestParam.required()) {
					String paramValue = request.getParameter(requestParam.value());
					if (param.getParameterType() == String.class && StringUtils.isBlank(paramValue)) {
						OutputStream os = response.getOutputStream();
						ResultInfo<Object> resultInfo = super.getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_DEFAULT,
						        super.getMessage(ErrorCodeConstant.ERROR_CODE_DEFAULT));
						os.write(JSON.toJSONString(resultInfo).getBytes("UTF-8"));
						//os.write("validate failed".getBytes("UTF-8"));
						os.flush();
						os.close();
						return false;
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	@SuppressWarnings("rawtypes")
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	    if(logger.isDebugEnabled()){
            logger.debug("当前请求方法{}对应uri信息:{}" ,request.getMethod(), request.getRequestURI());
        }
		if (handler instanceof HandlerMethod) {
			HandlerMethod methodHandler = (HandlerMethod) handler;
			Log log = methodHandler.getMethod().getAnnotation(Log.class);
			if (log != null) {
				Map parameters=getMapFromRequest(request);
				logger.info("请求参数信息为:" + (parameters == null ? "null" :  JSON.toJSONString(parameters,true)));
			}
		}
	}

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		logger.info("响应参数信息 [{}] method [{}] args[{}]", target.getClass(), method.getName(), formatArgs(args));
	}

	
	private String formatArgs(Object[] args) {
		StringBuffer argsStr = new StringBuffer();
		for (Object arg : args) {
			if (arg != null) {
				if (HttpServletRequest.class.isAssignableFrom(arg.getClass())
						|| HttpServletResponse.class.isAssignableFrom(arg.getClass())) {
					continue;
				} else {
					argsStr.append(JSON.toJSONString(arg)).append(",");
				}
			} else {
				argsStr.append("null,");
			}
		}
		if (argsStr.length() > 0) {
			argsStr.setLength(argsStr.length() - 1);
		}
		return argsStr.toString();
	}
	
	/**
     * 解析request对象请求参数信息
     */
    @SuppressWarnings("unchecked")
    private Map<String ,Object> getMapFromRequest(HttpServletRequest request) throws IOException{
    	Map<String ,Object> resultMap = new HashMap<String ,Object>();
    	String method = request.getMethod();
    	if(HttpMethod.GET.matches(method)){
    		Map<String, String[]> requestMap = request.getParameterMap();
    		if (requestMap != null) {
    			for (String key : requestMap.keySet()) {
    				String[] values = requestMap.get(key);
    				resultMap.put(key, values.length == 1 ? values[0].trim() : values);
    			}
    		}
			/*if(logger.isDebugEnabled()){
				logger.debug("GET请求参数为:{}", ( JSON.toJSONString(resultMap)));
			}*/
		} else if (HttpMethod.POST.matches(method)) {
			ServletInputStream sis = request.getInputStream();
			DataInputStream dataInStream = new DataInputStream(sis);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				byte[] buf = new byte[1024];
				int n = dataInStream.read(buf);
				while (n != -1) {
					os.write(buf, 0, n);
					n = dataInStream.read(buf);
				}
				byte[] res = os.toByteArray();
				if(res.length>1000){
					res=Arrays.copyOf(res, 1000);
				}
				String paramAsString=null;
				if(res.length>0){
				    paramAsString =new String(res,"utf-8");
				    resultMap=JSON.parseObject(paramAsString,Map.class);
				}
				/*if (logger.isDebugEnabled()) {
					logger.debug("POST请求参数为:{}" ,paramAsString);
				}*/
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(os!=null){
					os.close();
				}
				if(dataInStream!=null){
					dataInStream.close();
				}
			}
		}
        return resultMap;
    }

}