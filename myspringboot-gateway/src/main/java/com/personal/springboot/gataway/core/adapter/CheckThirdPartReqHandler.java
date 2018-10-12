package com.personal.springboot.gataway.core.adapter;

import javax.inject.Inject;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.core.OpenApiHandler;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.dao.entity.ApiInterface;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.service.RedisService;



public class CheckThirdPartReqHandler extends OpenApiHandler{
	private static final Logger logger = LoggerFactory.getLogger(CheckThirdPartReqHandler.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");

 	@Inject
	private RedisService redisService;
 	
 	// 	1-草稿 2-发布 3-阻塞 4-过时 5-冻结 6-退役
 	private String apiState="|2|4|5|";
 	
	
	private void validateParam(OpenApiHttpRequestBean requestBean){	
		
		ApiCommonParamDto apiCommonParam= requestBean.getApiCommonParamDto();
		String api_id=apiCommonParam.getApiID();
		
		ApiInterface apiInterface=redisService.findApiInterfaceByKey(api_id,null);
		if(null == apiInterface){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_API_ID_FAIL);
		}else{			
			if(!apiState.contains(apiInterface.getStatus())){
				throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_API_INTERFACE_STATUS_FAIL);
			}else{
				requestBean.setTarget_url(apiInterface.getTargetUrl());				
			}
		}	
	}
	
		
	
	@Override
	public boolean execute(Context context) {
		return doExcuteBiz(context);
	}
	
	@Override
	public boolean doExcuteBiz(Context context) {
		msglog.debug("~~~~~ check in  ");
		OpenApiContext blCtx = (OpenApiContext)context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
		
		//校验参数
		validateParam(request);	
		msglog.debug("~~~~~ check out  ");
		return false;
	}
}
