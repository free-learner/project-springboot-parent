package com.personal.springboot.gataway.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;

public class SandboxInterceptor   implements HandlerInterceptor{

	
	
	private boolean sandbox=false;
	
	
	


	public boolean isSandbox() {
		return sandbox;
	}

	public void setSandbox(boolean sandbox) {
		this.sandbox = sandbox;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		OpenApiHttpRequestBean reqBean= (OpenApiHttpRequestBean)request.getAttribute(CommConstants.REQ_BEAN_KEY);
		reqBean.setIsSandbox(sandbox);
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
