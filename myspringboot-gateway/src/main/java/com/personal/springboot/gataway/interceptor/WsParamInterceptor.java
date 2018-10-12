package com.personal.springboot.gataway.interceptor;

//import static com.crt.openapi.common.constants.CommConstants.API_SERVICE_KEY;
//import static com.crt.openapi.common.constants.CommConstants.REQ_BEAN_KEY;
//import static com.crt.openapi.common.constants.OpenApiErrorEnum.INVALID_CONTENTTYPE;
//import static com.crt.openapi.common.constants.OpenApiErrorEnum.IN_PARM_ERROR_REQUEST;
//import static com.crt.openapi.common.constants.OpenApiErrorEnum.IN_PARM_PARSE_ERROR;
//import static com.crt.openapi.common.constants.OpenApiMessageConstants.APP_TOKEN;
//import static com.crt.openapi.common.constants.OpenApiMessageConstants.BODY;
//import static com.crt.openapi.common.constants.OpenApiMessageConstants.HRT_ATTRS;
//import static com.crt.openapi.common.constants.OpenApiMessageConstants.REQUEST;
//import static com.crt.openapi.common.constants.OpenApiMessageConstants.REQUEST_DATA;
import static com.personal.springboot.gataway.conf.CommConstants.API_SERVICE_KEY;
import static com.personal.springboot.gataway.conf.CommConstants.REQ_BEAN_KEY;
import static com.personal.springboot.gataway.conf.OpenApiErrorEnum.INVALID_CONTENTTYPE;
import static com.personal.springboot.gataway.conf.OpenApiErrorEnum.IN_PARM_ERROR_REQUEST;
import static com.personal.springboot.gataway.conf.OpenApiErrorEnum.IN_PARM_PARSE_ERROR;
import static com.personal.springboot.gataway.conf.OpenApiMessageConstants.APP_TOKEN;
import static com.personal.springboot.gataway.conf.OpenApiMessageConstants.BODY;
import static com.personal.springboot.gataway.conf.OpenApiMessageConstants.HRT_ATTRS;
import static com.personal.springboot.gataway.conf.OpenApiMessageConstants.REQUEST;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.entity.ContentType;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.NetworkUtil;
import com.personal.springboot.gataway.utils.XmlUtils;

public class WsParamInterceptor extends OpenApiValidateInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(WsParamInterceptor.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");
	
	@Override
	public OpenApiHttpRequestBean iniOpenApiHttpRequestBean(HttpServletRequest request, OpenApiHttpRequestBean reqBean) {
		reqBean.setFlowType(CommConstants.FLOW_TYPE_WS);
		return iniReqBean(request,reqBean);
	}

	public OpenApiHttpRequestBean iniReqBean(HttpServletRequest request, OpenApiHttpRequestBean bean) {

		ApiCommonParamDto apiCommonParamDto = new ApiCommonParamDto();
		String body = this.getHttpRequestBodyString(request);
		
		bean.setReqHeader(getHeadersInfo(request));
		msglog.info("收到HTTP请求[FA --> OPEN_API]: Header:["+bean.getReqHeader()+"]  body:["+body+"] ");
		
				
		String localAddr = request.getLocalAddr();
		int localPort = request.getLocalPort();
		
		String contentType = bean.getReqHeader().get(CommConstants.HEADER_CONTENT_TYPE_KEY);
		bean.setReqestMsg(body);

		bean.setOperationType(API_SERVICE_KEY);
		bean.setClientAddr(NetworkUtil.getClientIpAddr(request));
		bean.setLocalAddr(localAddr);
		bean.setLocalPort(localPort);
		bean.setQueryString(request.getQueryString());
		bean.setApiCommonParamDto(apiCommonParamDto);		
		request.setAttribute(REQ_BEAN_KEY, bean);

		// 参数对象转换，静态参数校验
		if (contentType!=null&&(contentType.contains(ContentType.TEXT_XML.getMimeType())
				|| contentType.contains(ContentType.APPLICATION_XML.getMimeType()))) {
			initApiCommonParamDtoOfXml(body, apiCommonParamDto);
		} else {
			throw new OpenApiException(INVALID_CONTENTTYPE);
		}
		return bean;
	}
	String pattern = "<REQUEST_DATA>([\\d\\D]*)</REQUEST_DATA>";
	
//	String pattern = "<\\s*(.*:)?REQUEST_DATA(.*)>([\\d\\D]*)</\\1REQUEST_DATA>";
	Pattern r = Pattern.compile(pattern);
	
	private String getRequestData(String xml){		
		String result="";
		
		Matcher m = r.matcher(xml);		
		if(m.find()){
			result =m.group();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		String aa="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ais=\"http://AIS.CRC.com/gbs/ais\">"
				+ "   <soapenv:Header/>   <soapenv:Body>      <ais:REQUEST>         <HRT_ATTRS>            <App_Sub_ID>7000002401II</App_Sub_ID>"
				+ "      <App_Token>0affec11eefc4282beaeb5f3647ba1cd</App_Token>         <Api_ID>crt.mb.public.CheckMemberInfo</Api_ID>"
				+ "            <Api_Version>2.0.0</Api_Version>            <Time_Stamp>2017-11-09 16:03:13:234</Time_Stamp>"
				+ "            <Sign_Method>md5</Sign_Method>            <Sign>0AB500D4C1766BD009A362264425F619</Sign>"
				+ "            <Format>xml</Format>            <Partner_ID>70000000</Partner_ID>            <Sys_ID>70000024</Sys_ID>"
				+ "            <App_Pub_ID>T000000301TS</App_Pub_ID>         </HRT_ATTRS>"
				+ "         <REQUEST_DATA><channelId>IOS</channelId><mobile></mobile><mobile>18810000022</mobile></REQUEST_DATA>"
				+ "      </ais:REQUEST>   </soapenv:Body></soapenv:Envelope>";
		
		String pattern = "<\\s*(.*:)?REQUEST_DATA(.*)>([\\d\\D]*)</\\1REQUEST_DATA>";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(aa);	
		
		if(m.find()){
			System.out.println(m.group());
		}
		
		System.out.println(0);		
		
	}

	
	private void initApiCommonParamDtoOfXml(String xml, ApiCommonParamDto apiCommonParamDto) {
		
		try {
			Document document = DocumentHelper.parseText(xml.trim());
			Element root = document.getRootElement();
			Element body = root.element(BODY);
			Element request = body.element(REQUEST);
			if (request == null) {
				throw new OpenApiException(IN_PARM_ERROR_REQUEST);
			}

		
			ParamInterceptorHelp.paramParamOfxml(apiCommonParamDto, request);			
			XmlUtils.removeNamespace(request);
			request.element(HRT_ATTRS).remove(request.element(HRT_ATTRS).element(APP_TOKEN));
		
//			request.element(REQUEST_DATA).asXML()
			apiCommonParamDto.setReqdate(getRequestData(xml));

		} catch (OpenApiException e) {
			throw e;
		}catch (Exception e) {
			throw new OpenApiException(IN_PARM_PARSE_ERROR);
		}
	}
	

}
