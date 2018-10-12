package com.personal.springboot.gataway.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.conf.OpenApiMessageConstants;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.NetworkUtil;
import com.personal.springboot.gataway.utils.StringUtil;

public class ThirdPartParamInterceptor extends OpenApiValidateInterceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(ThirdPartParamInterceptor.class);

	private static final Logger msglog = LoggerFactory.getLogger("msglog");

	@Override
	public OpenApiHttpRequestBean iniOpenApiHttpRequestBean(
			HttpServletRequest request, OpenApiHttpRequestBean reqBean) {
		reqBean.setFlowType(CommConstants.FLOW_TYPE_THIRD_PART);
		return iniReqBean(request, reqBean);
	}
	

	public OpenApiHttpRequestBean iniReqBean(HttpServletRequest request,
			OpenApiHttpRequestBean bean) {

		ApiCommonParamDto apiCommonParamDto = new ApiCommonParamDto();
		String contentType = request.getContentType();
		String body = this.getHttpRequestBodyString(request);

		msglog.info("收到HTTP请求[FA --> OPEN_API]: Header:["
				+ getHeadersInfo(request) + "]  body:[" + body + "] ");

		String localAddr = request.getLocalAddr();
		int localPort = request.getLocalPort();

		bean.setReqHeader(getHeadersInfo(request));
		bean.setReqestMsg(body);

		bean.setOperationType(CommConstants.API_SERVICE_KEY);
		bean.setClientAddr(NetworkUtil.getClientIpAddr(request));
		bean.setLocalAddr(localAddr);
		bean.setLocalPort(localPort);
		bean.setQueryString(request.getQueryString());
		bean.setApiCommonParamDto(apiCommonParamDto);
		request.setAttribute(CommConstants.REQ_BEAN_KEY, bean);		

		if (null == contentType) {
			throw new OpenApiException(OpenApiErrorEnum.INVALID_CONTENTTYPE);
		}

		if (contentType.contains(ContentType.APPLICATION_JSON.getMimeType())) {
			apiCommonParamDto.setReqdate(body);
		} else {
			throw new OpenApiException(OpenApiErrorEnum.INVALID_CONTENTTYPE);
		}
		
		//验证QueryString 中  appid 
		String api_id = request.getParameter(OpenApiMessageConstants.API_ID);
		if(StringUtil.isEmpty(api_id)){
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_API_ID);
		}else{
			apiCommonParamDto.setApiID(api_id);
		}
		
		
		return bean;
	}

}
