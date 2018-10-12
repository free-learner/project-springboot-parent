package com.personal.springboot.gataway.interceptor;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import static com.personal.springboot.gataway.conf.OpenApiErrorEnum.*;
import static com.personal.springboot.gataway.conf.OpenApiMessageConstants.*;

import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.conf.OpenApiMessageConstants;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.DateUtils;
import com.personal.springboot.gataway.utils.StringUtil;

public class ParamInterceptorHelp {
	
	

	public static void paramParamOfxml(ApiCommonParamDto apiCommonParamDto,
			Element request) {
		Element hrtAttrs = request.element(HRT_ATTRS);

		if (hrtAttrs == null) {
			throw new OpenApiException(IN_PARM_ERROR_HRT_ATTRS);
		}
		// API_ID校验
		if (hrtAttrs.element(API_ID) == null) {
			throw new OpenApiException(IN_PARM_ERROR_API_ID);
		} else {
			Element element = hrtAttrs.element(API_ID);
			String tem = element.getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_API_ID);
			} else {
				apiCommonParamDto.setApiID(tem);
			}
		}

		// APP_SUB_ID校验
		if (hrtAttrs.element(APP_SUB_ID) == null) {
			throw new OpenApiException(IN_PARM_ERROR_APP_SUB_ID);
		} else {
			String tem = hrtAttrs.element(APP_SUB_ID).getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_APP_SUB_ID);
			} else {
				apiCommonParamDto.setAppSubId(tem);
			}
		}
		// APP_TOKEN校验
		if (hrtAttrs.element(APP_TOKEN) == null) {
			throw new OpenApiException(IN_PARM_ERROR_APP_TOKEN);
		} else {
			String tem = hrtAttrs.element(APP_TOKEN).getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_APP_TOKEN);
			} else {
				apiCommonParamDto.setAppToken(tem);
			}
		}
		// SIGN_METHOD校验
		if (hrtAttrs.element(SIGN_METHOD) == null) {
			throw new OpenApiException(IN_PARM_ERROR_SIGN_METHOD);
		} else {			
			String tem = hrtAttrs.element(SIGN_METHOD).getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_SIGN_METHOD);
			} else {				
				if(!OpenApiMessageConstants.signMethodSet.contains(tem.toLowerCase())){
					throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_SIGN_METHOD_NOT_SUPPORT);
				}
				
				apiCommonParamDto.setSignMethod(tem);
			}
		}
		// SIGN校验
		if (hrtAttrs.element(OpenApiMessageConstants.SIGN) == null) {
			throw new OpenApiException(IN_PARM_ERROR_SIGN);
		} else {
			String tem = hrtAttrs.element(OpenApiMessageConstants.SIGN)
					.getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_SIGN);
			} else {
				apiCommonParamDto.setSign(tem);
			}
		}

		// API_VERSION校验
		if (hrtAttrs.element(API_VERSION) == null) {
			throw new OpenApiException(IN_PARM_ERROR_API_VERSION);
		} else {
			String tem = hrtAttrs.element(API_VERSION).getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_API_VERSION);
			} else {
				apiCommonParamDto.setApiVersion(tem);
			}
		}
		// FORMAT校验
		if (hrtAttrs.element(FORMAT) == null) {
			throw new OpenApiException(IN_PARM_ERROR_FORMAT);
		} else {
			String tem = hrtAttrs.element(FORMAT).getStringValue();
			if (StringUtil.isEmpty(tem)
					|| (!tem.equalsIgnoreCase("xml") && !tem
							.equalsIgnoreCase("json"))) {
				throw new OpenApiException(IN_PARM_ERROR_FORMAT);
			} else {
				apiCommonParamDto.setFormat(tem);
			}
		}

		// TIME_STAMP校验
		if (hrtAttrs.element(TIME_STAMP) == null) {
			throw new OpenApiException(IN_PARM_ERROR_TIME_STAMP);
		} else {
			String tem = hrtAttrs.element(TIME_STAMP).getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_TIME_STAMP);
			} else {				
				// 判断时间是否相差10分钟。
				try {
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

		// APP_PUB_ID校验
		if (hrtAttrs.element(APP_PUB_ID) != null) {
			String tem = hrtAttrs.element(APP_PUB_ID).getStringValue();
			if (StringUtil.isNotEmpty(tem)) {
				apiCommonParamDto.setAppPubID(tem);
			} else {
				apiCommonParamDto.setAppPubID("");
			}
		}

		// PARTNER_ID校验
		if (hrtAttrs.element(PARTNER_ID) == null) {
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_PARTNER_ID);
		}else{
			String tem = hrtAttrs.element(PARTNER_ID).getStringValue();
			if (StringUtil.isNotEmpty(tem)) {
				apiCommonParamDto.setPartnerID(tem);
			}
		}

		// Sys_ID校验
		if (hrtAttrs.element(OpenApiMessageConstants.SYS_ID) == null) {
			throw new OpenApiException(OpenApiErrorEnum.IN_PARM_ERROR_SYS_ID);
		}else{
			String tem = hrtAttrs.element(OpenApiMessageConstants.SYS_ID).getStringValue();
			if (StringUtil.isNotEmpty(tem)) {
				apiCommonParamDto.setSysID(tem); 
			}
		}

		// REQUEST_DATA校验
		if (request.element(REQUEST_DATA) == null) {
			throw new OpenApiException(IN_PARM_ERROR_REQUEST_DATA);
		} else {
			String tem = request.element(REQUEST_DATA).getStringValue();
			if (StringUtil.isEmpty(tem)) {
				throw new OpenApiException(IN_PARM_ERROR_REQUEST_DATA);
			} else {
				apiCommonParamDto.setReqdate(request.element(REQUEST_DATA).asXML());
				// generateBizDataMMap(apiCommonParamDto.getBizDateMap(),request.element(REQUEST_DATA));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void generateBizDataMMap(Map<String, String> map,
			Element element) {

		List<Element> elements = element.elements();
		if (elements == null || elements.size() == 0) {
			if (!StringUtils.isEmpty(element.getStringValue())) {
				map.put(element.getName(), element.getStringValue());
			}
		} else {
			for (Element e : elements) {
				generateBizDataMMap(map, e);
			}
		}
	}

}
