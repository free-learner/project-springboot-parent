package com.personal.springboot.gataway.core;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.task.OpenApiHttpReqTask;

@Named
public class OpenApiAcceptHandler {
	
	
	private static final Logger logger = LoggerFactory.getLogger(OpenApiAcceptHandler.class);
	
	
	@Resource(name="serviceHandlerExecuteTemplate") 
	private OpenApiHandlerExecuteTemplate serviceHandlerExecuteTemplate;
	
	@Resource(name="thirdServiceHandlerExecuteTemplate") 
	private OpenApiHandlerExecuteTemplate thirdPartserviceHandlerExecuteTemplate;	
	
	@Resource(name="responseHandlerExecuteTemplate") 
	private OpenApiHandlerExecuteTemplate responseHandlerExecuteTemplate;
	
	
	public void acceptRequest(HttpServletRequest request,HttpServletResponse response){
		OpenApiHttpRequestBean reqBean = (OpenApiHttpRequestBean) request.getAttribute(CommConstants.REQ_BEAN_KEY);
		OpenApiHttpSessionBean sessionBean = new OpenApiHttpSessionBean(reqBean,response);		
		addTask2Pool(sessionBean,reqBean.getFlowType());
	}
	
	public void acceptRespose(OpenApiHttpSessionBean sessionBean){		
		addTask2Pool(sessionBean,CommConstants.FLOW_TYPE_RESP);
	}
	
	
	private void addTask2Pool(OpenApiHttpSessionBean sessionBean,String type){
		
		OpenApiHandlerExecuteTemplate executeTemlate=serviceHandlerExecuteTemplate;
		switch (type) {
		case CommConstants.FLOW_TYPE_WS:
			executeTemlate=serviceHandlerExecuteTemplate;
			break;
		case CommConstants.FLOW_TYPE_RS:
			executeTemlate=serviceHandlerExecuteTemplate;
			break;
		case CommConstants.FLOW_TYPE_THIRD_PART:
			executeTemlate=thirdPartserviceHandlerExecuteTemplate;
			break;
			
		case CommConstants.FLOW_TYPE_RESP:
			executeTemlate=responseHandlerExecuteTemplate;
			break;
			
			
		default:
			break;
		}		
				
		
		try {
			new OpenApiHttpReqTask(sessionBean,executeTemlate).doBussiness();
		}catch (OpenApiException e) {
			throw e;
		} catch (Exception e) {
			logger.error("doBussiness异常",e);
			throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,new String[]{e.getClass().getName(),e.getMessage()}, e);
		}
		
    	
	}
}
