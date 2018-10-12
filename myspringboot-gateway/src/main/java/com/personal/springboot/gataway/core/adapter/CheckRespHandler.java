package com.personal.springboot.gataway.core.adapter;



import org.apache.commons.chain.Context;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.conf.OpenApiMessageConstants;
import com.personal.springboot.gataway.core.OpenApiHandler;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.exception.OpenApiException;



public class  CheckRespHandler  extends OpenApiHandler{
	private static final Logger logger = LoggerFactory.getLogger(CheckRespHandler.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");

	
 		
	@Override
	public boolean execute(Context context) {
		
		return doExcuteBiz(context);
	}
	
		
	
	@Override
	public boolean doExcuteBiz(Context context) {
		msglog.debug("~~~~~ check resp in  ");
		OpenApiContext blCtx = (OpenApiContext)context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
		
		
		String format= "json";//request.getApiCommonParamDto().getFormat();
		if("json".equalsIgnoreCase(format)){			
			validateParamJson(request);	
		}else if("xml".equalsIgnoreCase(format)){
			validateParamXml(request);
		}
		msglog.debug("~~~~~ check resp out ");
		return false;
	}
	
	private void validateParamJson(OpenApiHttpRequestBean request){
		String respMsg=request.getRespMsg();
		
		JSONObject jsonObj = JSONObject.parseObject(respMsg);
		
		if(!jsonObj.containsKey(OpenApiMessageConstants.RESPONSE)){
			throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_PARSE_ERROR);
		}
		
		JSONObject response_obj=(JSONObject)jsonObj.get(OpenApiMessageConstants.RESPONSE);				
		
	
		if(!response_obj.containsKey(OpenApiMessageConstants.RETURN_CODE)){
			throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_CODE);
		}else{
			String return_code= response_obj.getString(OpenApiMessageConstants.RETURN_CODE);
			request.getLogBean().setReturn_code(return_code);
		}
		
		if(!response_obj.containsKey(OpenApiMessageConstants.RETURN_DESC)){
			throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_DATA);
		}else{
			String return_desc= response_obj.getString(OpenApiMessageConstants.RETURN_DESC);
			request.getLogBean().setReturn_desc(return_desc);
		}
		
		if(!response_obj.containsKey(OpenApiMessageConstants.RETURN_STAMP)){
			throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_STAMP);
		}else{
			String tem= response_obj.getString(OpenApiMessageConstants.RETURN_STAMP);
			request.getLogBean().setReturn_stamp(tem);
		}
		
		
		if(!response_obj.containsKey(OpenApiMessageConstants.RETURN_DATA)){
			throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_DATA);
		}else{
			String return_data= response_obj.getString(OpenApiMessageConstants.RETURN_DATA);
			request.getLogBean().setReturn_data(return_data);
		}
		
		
	}
	
	
	private void validateParamXml(OpenApiHttpRequestBean request){
		String respMsg=request.getRespMsg();
		
		try {
			Document document = DocumentHelper.parseText(respMsg);
			Element response = document.getRootElement();
			
			if(!OpenApiMessageConstants.RESPONSE.equals(response.getName())){
				throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_PARSE_ERROR);
			}
			
			Element return_code = response.element(OpenApiMessageConstants.RETURN_CODE);
			if (return_code == null) {
				throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_CODE);
			} else {
				request.getLogBean().setReturn_code(return_code.getStringValue());
			}

			Element return_desc = response.element(OpenApiMessageConstants.RETURN_DESC);
			if (return_desc == null) {
				throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_DESC);
			} else {
				request.getLogBean().setReturn_desc(return_desc.getStringValue());
			}
			
			
			Element return_data = response.element(OpenApiMessageConstants.RETURN_DATA);
			if (return_data == null) {
				throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_DATA);
			} else {
				request.getLogBean().setReturn_data(return_data.asXML());
			}
			
			Element return_stamp = response.element(OpenApiMessageConstants.RETURN_STAMP);
			if (return_stamp == null) {
				throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_RETURN_DATA);
			} else {
				request.getLogBean().setReturn_stamp(return_stamp.asXML());
			}
			
			
//			Element sign = response.element(OpenApiMessageConstants.SIGN);
//			if (sign == null) {
//				throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_ERROR_SIGN);
//			}else{
//				//是不是要校验签名
//				
//				
//				
//				//处理签名字段
//				response.remove(sign);		
//				request.setRespMsg(response.asXML());
//			}
			
			
			
		
		} catch (Exception e) {
			throw new OpenApiException(OpenApiErrorEnum.OUT_PARM_PARSE_ERROR,e);
		}
		
	}
	
}
