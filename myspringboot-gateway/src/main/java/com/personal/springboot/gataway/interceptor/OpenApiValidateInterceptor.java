package com.personal.springboot.gataway.interceptor;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiLogBean;
import com.personal.springboot.gataway.dao.entity.OpenApiLogDetail;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.DateUtils;

public abstract class OpenApiValidateInterceptor implements HandlerInterceptor {
	public static final Logger logger = LoggerFactory
			.getLogger(OpenApiValidateInterceptor.class);
	
		
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		return;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		return;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object obj) throws OpenApiException {
		
		
		OpenApiHttpRequestBean reqBean=new OpenApiHttpRequestBean();
		try {
			reqBean = iniOpenApiHttpRequestBean(request,reqBean);					
			
		} catch (OpenApiException e) {
			throw e;
		}finally{						
			OpenApiLogDetail o=new OpenApiLogDetail();
			o.setReqInfo(DateUtils.formatStr4Date(), reqBean.getReqestMsg());
			
			OpenApiLogBean logBean= new OpenApiLogBean(reqBean);
			logBean.setRsOpenApiLogDetail(o);
			reqBean.setLogBean(logBean);
			
			request.setAttribute(CommConstants.REQ_BEAN_KEY, reqBean);				
		}
		
		return true;
	}

	public Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key.toLowerCase(), value);
		}
		return map;
	}

	public String getHttpRequestBodyString(HttpServletRequest request) {
		if ("POST".equalsIgnoreCase(request.getMethod())) {
			Scanner s = null;
			try {
				s = new Scanner(request.getInputStream(), "UTF-8")
						.useDelimiter("\\A");
			} catch (IOException e) {
				logger.error(e.getMessage());
			}			
			String body =s.hasNext() ? s.next() : "";					
			String[] tem=body.split("\r\n\r\n",2);
			body =tem.length>1?tem[1].trim():tem[0].trim();			
			
			return body;
		}
		return "";
	}

	public abstract OpenApiHttpRequestBean iniOpenApiHttpRequestBean(
			HttpServletRequest request,OpenApiHttpRequestBean reqBean);

}
