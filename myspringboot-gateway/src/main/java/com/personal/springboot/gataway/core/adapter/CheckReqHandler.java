package com.personal.springboot.gataway.core.adapter;

import javax.inject.Inject;

import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.core.OpenApiHandler;
import com.personal.springboot.gataway.dao.entity.ApiApp;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.dao.entity.ApiInterface;
import com.personal.springboot.gataway.dao.entity.ApiSubInterface;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.dao.entity.RedisFastDto;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.service.RedisService;



public class CheckReqHandler extends OpenApiHandler{
	private static final Logger logger = LoggerFactory.getLogger(CheckReqHandler.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");

 	@Inject
	private RedisService redisService;
 	
 	// 	1-草稿 2-发布 3-阻塞 4-过时 5-冻结 6-退役
 	private String apiState="|2|4|5|";
 	
	
	private void validateParam(OpenApiHttpRequestBean requestBean){	
		
		ApiCommonParamDto apiCommonParam= requestBean.getApiCommonParamDto();
		String app_id=apiCommonParam.getAppSubId();
		String api_id=apiCommonParam.getApiID();
		String version=apiCommonParam.getApiVersion();
		
		msglog.debug("---------------------------------redis start");		
		
		
		RedisFastDto redisFastDto=redisService.findRedisFastDtoByKey(app_id, api_id, version);
		ApiApp app=null;
		ApiInterface apiInterface=null;
		ApiSubInterface	apiSubInterface=null;
		
		if(redisFastDto==null){
			 app=redisService.findApiAppByAppId(app_id);
			 apiInterface=redisService.findApiInterfaceByKey(api_id,version);
			 apiSubInterface=redisService.findApiSubInterfaceByKey(app_id,api_id,version);	
			 
			 if(app!=null &&apiInterface !=null && apiSubInterface!=null){
				 redisService.saveRedisFastDto(app,apiInterface,apiSubInterface);
			 }			
		}else{
			app=redisFastDto.getApiApp();
			apiInterface=redisFastDto.getApiInterface();
			apiSubInterface=redisFastDto.getApiSubInterface();	
		}
		
		 requestBean.setApiApp(app);
		 requestBean.setApiInterface(apiInterface);
		 requestBean.setApiSubInterface(apiSubInterface);
		 
		
		msglog.debug("---------------------------------redis end");		
		if(null == app){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_APP_SUB_ID_FAIL);
		}else{					
			if(!"0".equals(app.getStatus())){
				throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_APP_STATUS_FAIL);
			}		
			
		}
		
			
		//验证 Api_ID   Api_Version （不同地址）
		if(null == apiInterface){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_API_ID_FAIL);
		}else{			
			if(!apiState.contains("2")){
				throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_API_INTERFACE_STATUS_FAIL);
			}else{
				
				if(requestBean.getIsSandbox()){
					requestBean.setTarget_url(apiInterface.getSandboxTargetUrl());	
				}else{
					requestBean.setTarget_url(apiInterface.getTargetUrl());	
				}
				
				if(StringUtils.isEmpty(requestBean.getTarget_url())){
					throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_API_INTERFACE_URL_NULL);
				}
				
			}
		}		
		
		if(null == apiSubInterface && !apiInterface.getAppId().equals(app.getAppId())){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_API_R_APP_FAIL);
		}
				
	
	   //验证 App_Token		
       String  token	= requestBean.getIsSandbox()?app.getSandboxToken() : app.getToken();
       if(!requestBean.getApiCommonParamDto().getAppToken().equals(token)){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_TOKEN_FAIL);
		}
		
		String partnerID=requestBean.getApiCommonParamDto().getPartnerID();
		if(!partnerID.equals(app.getOrgId())){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_PARTNERID_FAIL);
		}
		 
		String sysID=requestBean.getApiCommonParamDto().getSysID();
		if(!sysID.equals(app.getSysId())){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_SYSID_FAIL);
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
