package com.personal.springboot.gataway.core.adapter;

import javax.inject.Inject;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.fastjson.JSONObject;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.conf.OpenApiMessageConstants;
import com.personal.springboot.gataway.core.OpenApiHandler;
import com.personal.springboot.gataway.dao.entity.ApiInterface;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.ApiMutiHttpClientUtil;
import com.personal.springboot.gataway.utils.DateUtils;
import com.personal.springboot.gataway.utils.SignUtil;

public class SignHandler extends OpenApiHandler {
	
	
	private static final Logger logger = LoggerFactory.getLogger(OpenApiReqHandler.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");

	@Inject
	private ApiMutiHttpClientUtil mutiHttpClientUtil;
	
	@Value("${signUrl}")
	private String signUrl;
	
	
	private void serviceRoute(OpenApiHttpRequestBean reqbean) {
				
		switch (reqbean.getApiInterface().getType()) {		
		case ApiInterface.SIGN_TYPE_MD5: //MD5
			String sign=SignUtil.sign(reqbean.getApiCommonParamDto(),reqbean.getIsSandbox()?reqbean.getApiApp().getSandboxAppSecret() : reqbean.getApiApp().getAppSecret());
			msglog.debug("---------------------------------md5 sign end    "+sign);
			if(!reqbean.getApiCommonParamDto().getSign().equals(sign)){
					throw new OpenApiException(OpenApiErrorEnum.IN_PARM_SIGN_FAIL);
			 }			
			
			break;
		case ApiInterface.SIGN_TYPE_RSA:	//RSA
			
			reqbean.getLogBean().setRsaReqTime(DateUtils.formatStr4Date());	
			String date = mutiHttpClientUtil.RSAPost(signUrl,reqbean);						
			reqbean.getLogBean().setRsaRespTime(DateUtils.formatStr4Date());
								
			//解析date 			
			validateParamJson(date,reqbean);
			
			
		case ApiInterface.SIGN_TYPE_NO_SIGN: //不验签			
			break;
		default:			
			break;
		}
		
	}
	
	
	
	private void validateParamJson(String respMsg,OpenApiHttpRequestBean reqbean){
		
		JSONObject jsonObj = JSONObject.parseObject(respMsg);
		String return_code=null;
		String return_desc=null;
		
		
		if(!jsonObj.containsKey(OpenApiMessageConstants.RESPONSE)){
			throw new OpenApiException(OpenApiErrorEnum.M_RSA_ERROR_RESPONSE);
		}
		
		JSONObject response_obj=(JSONObject)jsonObj.get(OpenApiMessageConstants.RESPONSE);
	
		if(!response_obj.containsKey(OpenApiMessageConstants.RETURN_CODE)){
			throw new OpenApiException(OpenApiErrorEnum.M_RSA_ERROR_RETURN_CODE);
		}else{
			return_code= response_obj.getString(OpenApiMessageConstants.RETURN_CODE);			
		}
		
		if(!response_obj.containsKey(OpenApiMessageConstants.RETURN_DESC)){
			throw new OpenApiException(OpenApiErrorEnum.M_RSA_ERROR_RETURN_DATA);
		}else{
			return_desc= response_obj.getString(OpenApiMessageConstants.RETURN_DESC);			
		}
		
		if(!response_obj.containsKey(OpenApiMessageConstants.RETURN_STAMP)){
			throw new OpenApiException(OpenApiErrorEnum.M_RSA_ERROR_RETURN_STAMP);
		}else{
			String tem= response_obj.getString(OpenApiMessageConstants.RETURN_STAMP);
			reqbean.getLogBean().setRsaRespTime(tem);
		}		
		
		if(!return_code.equals(OpenApiErrorEnum.SYSTEM_SUCCESS.getErrCode())){
			throw new OpenApiException(return_code,return_desc);
		}		
	}
	
	
	
	
	
	
	
	
	@Override
	public boolean execute(Context context) throws Exception {
		return doExcuteBiz(context);
	}
	
	
	

	@Override
	public boolean doExcuteBiz(Context context) {
		msglog.debug("~~~~~ sigHandler in ");
		OpenApiContext blCtx = (OpenApiContext) context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
		
		serviceRoute(request);
		
		msglog.debug("~~~~~ sigHandler out  ");
		return false;
	}

}
