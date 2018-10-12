package com.personal.springboot.gataway.core.adapter;

import javax.inject.Inject;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.core.OpenApiHandler;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.dao.entity.OpenApiLogBean;
import com.personal.springboot.gataway.dao.entity.OpenApiLogDetail;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.ApiMutiHttpClientUtil;
import com.personal.springboot.gataway.utils.ContentTypeUtil;
import com.personal.springboot.gataway.utils.DateUtils;
import com.personal.springboot.gataway.utils.FastJsonUtils;
import com.personal.springboot.gataway.utils.JsonUtil;

public class OpenApiReqHandler extends OpenApiHandler {

	private static final Logger logger = LoggerFactory.getLogger(OpenApiReqHandler.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");

	@Inject
	private ApiMutiHttpClientUtil mutiHttpClientUtil;

	private String initServiceMsg(ApiCommonParamDto apiCommonParamDto,String contentType) {
		String result="";
	
		String hrt_attrs=FastJsonUtils.beanToText(apiCommonParamDto);
		
		String reqdate=	 apiCommonParamDto.getReqdate();	
		if(contentType.toLowerCase().contains(ContentTypeUtil.XML)){			
//			 reqdate=XmlToJsonStaxon.xml2json(apiCommonParamDto.getReqdate(), OpenApiMessageConstants.REQUEST_DATA);
			 reqdate=JsonUtil.xml2JSON(apiCommonParamDto.getReqdate());
		}
		
		result=	OpenApiHandler.REQ_JSON_TEMPLATE.replace("#hrt_attrs#", hrt_attrs)
				.replace("#request_data#", reqdate);

		return result ;

	}
	
	
	private String serviceRoute(OpenApiHttpRequestBean reqbean) {
		String serviceRspData = null;
		String contentType = reqbean.getReqHeader().get(CommConstants.HEADER_CONTENT_TYPE_KEY);
				
		if(!reqbean.getFlowType().equals(CommConstants.FLOW_TYPE_THIRD_PART)){
			reqbean.setReqMsg(initServiceMsg(reqbean.getApiCommonParamDto(),contentType));
		}else{
			reqbean.setReqMsg(reqbean.getReqestMsg());
		}
		
		
		OpenApiLogBean logBean = reqbean.getLogBean();				
		OpenApiLogDetail openApiLogDetail=new OpenApiLogDetail();
		openApiLogDetail.setReqInfo(DateUtils.formatStr4Date(), reqbean.getReqMsg());
		
		try {
			
			String req_contentType=ContentTypeUtil.APPLICATION_JSON;
			msglog.info("发送HTTP请求[OPEN_API --> BA]: url:["+reqbean.getTarget_url()+"]  contentType:["+req_contentType+"] body:["+reqbean.getReqMsg()+"] ");
			if (reqbean.getTarget_url().startsWith("https://")) {
				serviceRspData = mutiHttpClientUtil.doHttpsPost(reqbean.getTarget_url(),reqbean.getReqMsg(), req_contentType);
			} else {
				serviceRspData = mutiHttpClientUtil.doPost(reqbean.getTarget_url(),reqbean.getReqMsg(), req_contentType,reqbean);	
			}
					
			openApiLogDetail.setRespInfo(DateUtils.formatStr4Date(), serviceRspData);
			
		} catch (OpenApiException e) {
			throw e;
		}catch (Exception e) {
			openApiLogDetail.setRespInfo(DateUtils.formatStr4Date(), "异常："+ e.getMessage());
			logger.error("",e);
			throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,new String[]{e.getClass().getName(),e.getMessage()},e);
		}finally{
			logBean.setServiceOpenApiLogDetail(openApiLogDetail);
		}
		
		return serviceRspData;
	}

	@Override
	public boolean execute(Context context) {
		return doExcuteBiz(context);
	}

	@Override
	public boolean doExcuteBiz(Context context) {
		msglog.debug("~~~~~ req in ");
		OpenApiContext blCtx = (OpenApiContext) context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
	
		String rspData = serviceRoute(request);
		request.setServiceBody(rspData);
		request.setRespMsg(rspData);
		msglog.debug("~~~~~ req out  ");
		return false;
	}
}
