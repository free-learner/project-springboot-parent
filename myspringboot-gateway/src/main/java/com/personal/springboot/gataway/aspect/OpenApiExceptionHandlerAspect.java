package com.personal.springboot.gataway.aspect;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.core.OpenApiResponseUtils;
import com.personal.springboot.gataway.core.adapter.OpenApiRspHandler;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.exception.OpenApiException;

public class OpenApiExceptionHandlerAspect {
	public static final Logger logger = LoggerFactory
			.getLogger(OpenApiExceptionHandlerAspect.class);
	
	
	@Inject
	private OpenApiRspHandler rspHandler;
		
	@Inject
	private OpenApiResponseUtils openApiResponseUtils;
	
	
	public void doAfter(JoinPoint jp) {
		Object[] args = jp.getArgs();
		HttpServletRequest request = (HttpServletRequest) args[0];
		HttpServletResponse response = (HttpServletResponse) args[1];
		Exception ex = (Exception) args[3];
		
		logger.error("~~~~~~~~~~~~~OpenApiExceptionHandlerAspect doAfter~~~~~~~~~~~~~~~~~~~~~",ex);		
		OpenApiHttpRequestBean reqBean = (OpenApiHttpRequestBean) request.getAttribute(CommConstants.REQ_BEAN_KEY);
		
		
		if(ex instanceof OpenApiException){
			OpenApiException openApiException=(OpenApiException)ex;
			reqBean.setServiceBody(openApiException);
			reqBean.getLogBean().setReturn_code(openApiException.getErrorCode());
			reqBean.getLogBean().setReturn_desc(openApiException.getErrorMsg());
		}else{
			reqBean.setServiceBody(ex);
			reqBean.getLogBean().setReturn_code(OpenApiErrorEnum.SYSTEM_BUSY.getErrCode());
			String desc=String.format(OpenApiErrorEnum.SYSTEM_BUSY.getErrMsg(), new String[]{ex.getClass().getName(),ex.getMessage()});
			reqBean.getLogBean().setReturn_desc(desc);
		}
		String body = this.rspHandler.executePrint(reqBean);
		
		reqBean.setRespMsg(body);
		openApiResponseUtils.writeRsp(response,reqBean);		
	
	}
}
