package com.personal.springboot.gataway.core.adapter;

import javax.inject.Inject;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.core.OpenApiHandler;
import com.personal.springboot.gataway.core.OpenApiResponseUtils;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.dao.entity.OpenApiLogBean;
import com.personal.springboot.gataway.dao.entity.OpenApiLogDetail;
import com.personal.springboot.gataway.utils.AsyncHttpClientUtil;
import com.personal.springboot.gataway.utils.ContentTypeUtil;
import com.personal.springboot.gataway.utils.DateUtils;
import com.personal.springboot.gataway.utils.FastJsonUtils;
import com.personal.springboot.gataway.utils.JsonUtil;
import com.personal.springboot.gataway.utils.OpenApiFutureCallback;


public class OpenApiReqAsyncHandler extends OpenApiHandler {

	private static final Logger logger = LoggerFactory.getLogger(OpenApiReqAsyncHandler.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");
	
	@Value("${http.client.soTimeout}")
	private  int http_client_timeout ;
	
	@Inject
	private OpenApiRspHandler rspHandler;
		
	@Inject
	private OpenApiResponseUtils openApiResponseUtils;
	
	private static AsyncHttpClientUtil asyncHttpClientUtil=AsyncHttpClientUtil.getInterfaces() ;

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
	
	
	private void serviceRoute(OpenApiHttpRequestBean reqbean,OpenApiHttpSessionBean httpSessionBean) {
		
		String contentType = reqbean.getReqHeader().get(CommConstants.HEADER_CONTENT_TYPE_KEY);
				
		if(!reqbean.getFlowType().equals(CommConstants.FLOW_TYPE_THIRD_PART)){
			reqbean.setReqMsg(initServiceMsg(reqbean.getApiCommonParamDto(),contentType));
		}else{
			reqbean.setReqMsg(reqbean.getReqestMsg());
		}
		
		OpenApiLogBean logBean = reqbean.getLogBean();		
		OpenApiLogDetail openApiLogDetail=new OpenApiLogDetail();
		openApiLogDetail.setReqInfo(DateUtils.formatStr4Date(), reqbean.getReqMsg());
		
				
		String req_contentType=ContentTypeUtil.APPLICATION_JSON;
		msglog.info("发送HTTP请求[OPEN_API --> BA]: url:["+reqbean.getTarget_url()+"]  contentType:["+req_contentType+"] body:["+reqbean.getReqMsg()+"] ");
		
		asyncHttpClientUtil.doPost(reqbean.getTarget_url(), reqbean.getReqMsg(), req_contentType, reqbean,http_client_timeout,
				new OpenApiFutureCallback(httpSessionBean,rspHandler,openApiResponseUtils));
		
		logBean.setServiceOpenApiLogDetail(openApiLogDetail);
		
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
	
		serviceRoute(request,httpSessionBean);		
		
		httpSessionBean.getResponse().setStatus(99);
		
		
		msglog.debug("~~~~~ req out  ");
		return false;
	}


	
	
}
