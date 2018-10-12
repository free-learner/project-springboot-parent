package com.personal.springboot.gataway.core;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.exception.OpenApiException;

public abstract class OpenApiHandler implements Command {

	
	public static final String REQ_XML_TEMPLATE="<REQUEST>#hrt_attrs# #request_data#</REQUEST>";
	public static final String RESP_XML_TEMPLATE="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ais=\"http://AIS.CRC.com/gbs/ais\"><soapenv:Body>#resp#</soapenv:Body></soapenv:Envelope>";
		
	public static final String REQ_JSON_TEMPLATE="{\"REQUEST\":{\"HRT_ATTRS\":#hrt_attrs#,\"REQUEST_DATA\":#request_data#}}";

	
	
	public final String HEADER_HOST_KEY = "host";
	public final String HEADER_SERVER_KEY = "server";
	
	
	public Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}

		return map;
	}

	

	public void validateRequestHeader(OpenApiHttpRequestBean routeBean) {
		String contentType = routeBean.getReqHeader().get(CommConstants.HEADER_CONTENT_TYPE_KEY);
		if (StringUtils.isBlank(contentType)) {
			throw new OpenApiException(OpenApiErrorEnum.CONTENTTYPE);
		}
		
		if (!contentType.contains(ContentType.APPLICATION_JSON.getMimeType()) 
				&& !contentType.contains(ContentType.APPLICATION_XML.getMimeType())) {
			throw new OpenApiException(OpenApiErrorEnum.INVALID_CONTENTTYPE);
		}
	}

	public abstract boolean doExcuteBiz(Context context);
	
	

}
