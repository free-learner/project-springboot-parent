package com.personal.springboot.gataway.interceptor;

import static com.personal.springboot.gataway.conf.OpenApiErrorEnum.IN_PARM_ERROR_REQUEST;
import static com.personal.springboot.gataway.conf.OpenApiErrorEnum.IN_PARM_PARSE_ERROR;
import static com.personal.springboot.gataway.conf.OpenApiMessageConstants.REQUEST;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.entity.ContentType;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.conf.OpenApiMessageConstants;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.DateUtils;
import com.personal.springboot.gataway.utils.FastJsonUtils;
import com.personal.springboot.gataway.utils.NetworkUtil;
import com.personal.springboot.gataway.utils.StringUtil;

public class RsParamInterceptor extends OpenApiValidateInterceptor {

	private static final Logger logger = LoggerFactory
			.getLogger(RsParamInterceptor.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");

	@Override
	public OpenApiHttpRequestBean iniOpenApiHttpRequestBean(
			HttpServletRequest request, OpenApiHttpRequestBean reqBean) {
		reqBean.setFlowType(CommConstants.FLOW_TYPE_RS);
		return iniReqBean(request, reqBean);
	}

	
	public static void main(String[] args) throws UnknownHostException {
		
		
		System.out.println(InetAddress.getLocalHost().getHostAddress());
		
	}
	
	public OpenApiHttpRequestBean iniReqBean(HttpServletRequest request,
			OpenApiHttpRequestBean bean) {

		ApiCommonParamDto apiCommonParamDto = new ApiCommonParamDto();
		String contentType = request.getContentType();
		String body = this.getHttpRequestBodyString(request);
		bean.setReqHeader(getHeadersInfo(request));

		msglog.info("收到HTTP请求[FA --> OPEN_API]: Header:[" + bean.getReqHeader()
				+ "]  body:[" + body + "] ");

		String localAddr = request.getLocalAddr();
		try {
			localAddr=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			msglog.error("",e);
		}
		int localPort = request.getLocalPort();
		
		bean.setReqestMsg(body);
		String sandbox = request.getParameter(CommConstants.IN_KEY_SANDBOX);
		if (!StringUtil.isEmpty(sandbox)) {
			bean.setIsSandbox(true);
		}

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

		// 参数对象转换，静态参数校验
		if (contentType.contains(ContentType.APPLICATION_XML.getMimeType())) {
			initApiCommonParamDtoOfXml(body, apiCommonParamDto);
		} else if (contentType.contains(ContentType.APPLICATION_JSON
				.getMimeType())) {
			initApiCommonParamDtoOfJson(body, apiCommonParamDto);
		} else {
			throw new OpenApiException(OpenApiErrorEnum.INVALID_CONTENTTYPE);
		}

		return bean;
	}

	// {"REQUEST":{"HRT_ATTRS":{"App_Sub_ID":"2002","App_Token":"5b8fe283-9f43-4baf-a099-
	// 15409e37be3c","Api_ID":"hrt.order.getOrder.testliuliu10jsonnew","Api_Version":"1.0","Time_Stamp":"20160825165613","Sign_Method":"md5","Sign":"5d3ff2c191942614b08ac558c17460a0","Format":"json","Partner_id":"S001","Ap
	// p_Pub_ID":"MOA"},"REQUEST_DATA":{"customer":{"address":"shanghai","id":2,"name":"liuliu"}}}}
	// {"REQUEST":{"HRT_ATTRS":{"App_Sub_ID":"2002","App_Token":"708da043-3ad1-48fd-b200-
	// ac575533eee1","Api_ID":"hrt.order.getOrder.tianqiyubao","Api_Version":"1.0","Time_Stamp":"20160825151758","Sign_Method":"md5","Sign":"054d005ab9aec0c3fba730d1c58ad894","Format":"json","Partner_id":"S001","App_Pub_I
	// D":"MOA"},REQUEST_DATA:{}}}
	private void initApiCommonParamDtoOfJson(String body,
			ApiCommonParamDto apiCommonParamDto) {

		try {

			JSONObject jsonObj = FastJsonUtils.parse(body, true);	
			if (!jsonObj.containsKey(OpenApiMessageConstants.REQUEST)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_REQUEST);
			}

			JSONObject request_obj = (JSONObject) jsonObj
					.get(OpenApiMessageConstants.REQUEST);
			if (!request_obj.containsKey(OpenApiMessageConstants.HRT_ATTRS)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_HRT_ATTRS);
			}

			JSONObject hrt_attrs_obj = (JSONObject) request_obj
					.get(OpenApiMessageConstants.HRT_ATTRS);

			// API_ID校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.API_ID)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_API_ID);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.API_ID);
				if (StringUtil.isEmpty(tem)) {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_API_ID);
				} else {
					apiCommonParamDto.setApiID(tem);
				}
			}

			// APP_SUB_ID校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.APP_SUB_ID)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_APP_SUB_ID);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.APP_SUB_ID);
				if (StringUtil.isEmpty(tem)) {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_APP_SUB_ID);
				} else {
					apiCommonParamDto.setAppSubId(tem);
				}
			}

			// APP_TOKEN校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.APP_TOKEN)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_APP_TOKEN);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.APP_TOKEN);
				if (StringUtil.isEmpty(tem)) {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_APP_TOKEN);
				} else {
					apiCommonParamDto.setAppToken(tem);
				}
			}

			// SIGN_METHOD校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.SIGN_METHOD)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_SIGN_METHOD);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.SIGN_METHOD);
				if (StringUtil.isEmpty(tem)) {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_SIGN_METHOD);
				} else {
					
					if(!OpenApiMessageConstants.signMethodSet.contains(tem.toLowerCase())){
						throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_SIGN_METHOD_NOT_SUPPORT);
					}					
					apiCommonParamDto.setSignMethod(tem);
				}
			}

			// SIGN校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.SIGN)) {
				throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_SIGN);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.SIGN);
				if (StringUtil.isEmpty(tem)) {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_SIGN);
				} else {
					apiCommonParamDto.setSign(tem);
				}
			}

			// API_VERSION校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.API_VERSION)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_API_VERSION);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.API_VERSION);
				if (StringUtil.isEmpty(tem)) {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_API_VERSION);
				} else {
					apiCommonParamDto.setApiVersion(tem);
				}
			}

			// FORMAT校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.FORMAT)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_FORMAT);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.FORMAT);
				if (StringUtil.isEmpty(tem)) {
					// throw new
					// OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_FORMAT);
				} else {
					apiCommonParamDto.setFormat(tem);
				}
			}

			// TIME_STAMP校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.TIME_STAMP)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_TIME_STAMP);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.TIME_STAMP);
				if (StringUtil.isEmpty(tem)) {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_TIME_STAMP);
				} else {
					// 判断时间是否相差10分钟。
					try {
						System.out.println(tem);
						long inTime = DateUtils.string2TimeStamp(tem);
						if (System.currentTimeMillis() - inTime > 1000 * 60 * 10) {
							throw new OpenApiException(
									OpenApiErrorEnum.IN_PARM_ERROR_TIME_STAMP_TIMEOUT);
						}
					} catch (ParseException e) {
						throw new OpenApiException(
								OpenApiErrorEnum.IN_PARM_ERROR_TIME_STAMP_FAIL);
					}

					apiCommonParamDto.setTimeStamp(tem);
				}
			}

			// REQUEST_DATA校验
			if (!request_obj.containsKey(OpenApiMessageConstants.REQUEST_DATA)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_REQUEST_DATA);
			} else {
				String tem = request_obj
						.getString(OpenApiMessageConstants.REQUEST_DATA);
				apiCommonParamDto.setReqdate(tem);
			}

			// APP_PUB_ID校验
			if (hrt_attrs_obj.containsKey(OpenApiMessageConstants.APP_PUB_ID)) {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.APP_PUB_ID);
				if (StringUtil.isNotEmpty(tem)) {
					apiCommonParamDto.setAppPubID(tem);
				} else {
					apiCommonParamDto.setAppPubID("");
				}
			}

			// PARTNER_ID校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.PARTNER_ID)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_PARTNER_ID);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.PARTNER_ID);
				if (StringUtil.isNotEmpty(tem)) {
					apiCommonParamDto.setPartnerID(tem);
				} else {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_PARTNER_ID);
				}
			}

			// SYS_ID校验
			if (!hrt_attrs_obj.containsKey(OpenApiMessageConstants.SYS_ID)) {
				throw new OpenApiException(
						OpenApiErrorEnum.IN_PARM_ERROR_SYS_ID);
			} else {
				String tem = hrt_attrs_obj
						.getString(OpenApiMessageConstants.SYS_ID);
				if (StringUtil.isNotEmpty(tem)) {
					apiCommonParamDto.setSysID(tem);
				} else {
					throw new OpenApiException(
							OpenApiErrorEnum.IN_PARM_ERROR_SYS_ID);
				}
			}
		} catch (OpenApiException e) {
			throw e;
		} catch (Exception e) {
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_PARSE_ERROR, e);
		}
	}

	// private void addBizDate(ApiCommonParamDto apiCommonParamDto,String
	// key,Object obj){
	// if(obj instanceof JSONObject){
	// JSONObject request_data_obj=(JSONObject)obj;
	// for (Object iterable_element : request_data_obj.keySet()) {
	// String tem_key =iterable_element.toString();
	// Object tem_obj=request_data_obj.getString(tem_key);
	// addBizDate(apiCommonParamDto, tem_key, tem_obj);
	// }
	// }else{
	// apiCommonParamDto.addBizDate(key, obj.toString());
	// }
	// }

	private void initApiCommonParamDtoOfXml(String bodyString,ApiCommonParamDto apiCommonParamDto) {
	
		try {
			Document document = DocumentHelper.parseText(bodyString.trim());
			Element root = document.getRootElement();
			
			
			if(!REQUEST.equals(root.getName())){
				throw new OpenApiException(IN_PARM_ERROR_REQUEST);
			}
						
			ParamInterceptorHelp.paramParamOfxml(apiCommonParamDto, root);
		} catch(OpenApiException e1){
			throw e1;
		}catch (Exception e) {
			throw new OpenApiException(IN_PARM_PARSE_ERROR);
		}


		
	}
}
