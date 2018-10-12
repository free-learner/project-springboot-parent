package com.personal.springboot.gataway.core.adapter;

import javax.inject.Inject;

import org.apache.commons.chain.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.conf.OpenApiMessageConstants;
import com.personal.springboot.gataway.core.OpenApiHandler;
import com.personal.springboot.gataway.core.OpenApiResponseUtils;
import com.personal.springboot.gataway.dao.entity.OpenApiContext;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.DateUtils;
import com.personal.springboot.gataway.utils.FastJsonUtils;
import com.personal.springboot.gataway.utils.JsonUtil;
import com.personal.springboot.gataway.utils.StringUtil;
import com.personal.springboot.gataway.utils.XmlUtils;

public class OpenApiRspHandler extends OpenApiHandler {
	private static final Logger logger = LoggerFactory.getLogger(OpenApiRspHandler.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");
	
	
	@Inject
	private OpenApiResponseUtils openApiResponseUtils;
	
	
	public String executePrint(OpenApiHttpRequestBean request) {
		try {
			return this.getResponseBody(request);
		} catch (Exception e) {
			OpenApiException ex = null;
			if (e instanceof OpenApiException) {
				ex = (OpenApiException) e;
			} else {
				ex = new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,new String[]{e.getClass().getName(),e.getMessage()},
						e.getCause());
			}

			logger.error("executePrint error", e);
			request.getLogBean().setReturn_code(ex.getErrorCode());
			request.getLogBean().setReturn_desc(ex.getErrorMsg());
			return XmlUtils.bean2xml((ex.getShortMsg(DateUtils.formatStr4Date())));
		}
	}

	private String getResponseBody(OpenApiHttpRequestBean bean) {
		String result = "";
		Object body = (Object) bean.getServiceBody();

		String format ="json";
//		String format = bean.getApiCommonParamDto().getFormat();
//		if (StringUtil.isEmpty(format)) {
//			String content_type = bean.getReqHeader().get(CommConstants.HEADER_CONTENT_TYPE_KEY);
//			if (content_type!=null && content_type
//					.contains(ContentType.APPLICATION_XML.getMimeType())) {
//				format = "xml";
//			} else {
//				format = "json";
//			}
//		}

		if (body instanceof String) {
			result = body.toString();
		} else if (body instanceof OpenApiException) {
			OpenApiException ex = (OpenApiException) body;

			if ("xml".equalsIgnoreCase(format)) {
				result = XmlUtils.bean2xml((ex.getShortMsg(DateUtils.formatStr4Date())));
			} else {
				result = FastJsonUtils.beanToText(ex.getShortMsg(DateUtils.formatStr4Date()), OpenApiMessageConstants.RESPONSE);
			}

		} else if (body instanceof Exception) {
			
			Exception e=(Exception)body;
			OpenApiException ex = new OpenApiException(
					OpenApiErrorEnum.SYSTEM_BUSY,new String[]{e.getClass().getName(),e.getMessage()} ,e);
			
			if ("xml".equalsIgnoreCase(format)) {
				result = XmlUtils.bean2xml((ex.getShortMsg(DateUtils.formatStr4Date())));
			} else {
				result = FastJsonUtils.beanToText(ex.getShortMsg(DateUtils.formatStr4Date()), OpenApiMessageConstants.RESPONSE);
			}

		}

		if (CommConstants.FLOW_TYPE_WS.equals(bean.getFlowType())) {			
			result= JsonUtil.json2XML(result, OpenApiMessageConstants.RESPONSE);
			result = result.replaceAll("RESPONSE>", "ais:RESPONSE>");
			result = OpenApiHandler.RESP_XML_TEMPLATE.replace("#resp#", result);
		}

		return StringUtil.isEmpty(result) ? body.toString() : result;
	}

	@Override
	public boolean execute(Context context) {
		return doExcuteBiz(context);
	}

	@Override
	public boolean doExcuteBiz(Context context) {
		msglog.debug("~~~~~ resp in");
		OpenApiContext blCtx = (OpenApiContext) context;
		OpenApiHttpSessionBean httpSessionBean = (OpenApiHttpSessionBean) blCtx.getOpenApiHttpSessionBean();
		OpenApiHttpRequestBean request = httpSessionBean.getRequest();
		
		String respMsg = this.executePrint(request);
		request.setRespMsg(respMsg);
		
		openApiResponseUtils.writeRsp(httpSessionBean.getResponse(),request);		
		
		msglog.debug("~~~~~ resp out");
		return false;
	}

}
