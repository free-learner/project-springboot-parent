package com.personal.springboot.gataway.utils;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;
import com.personal.springboot.gataway.exception.OpenApiException;

public class SignUtil {
	public static final String MDF_CHARSET_UTF_8 = "UTF-8";
	private static final Logger msglog = LoggerFactory.getLogger("msglog");

	public static String sign(ApiCommonParamDto commonParam, String key) {
		return sign(commonParam, key, MDF_CHARSET_UTF_8);
	}

	public static String sign(ApiCommonParamDto commonParam, String key,
			String input_charset) {
		String result = null;
		if (StringUtil.isEmpty(input_charset)) {
			input_charset = "UTF-8";
		}

		Map<String, String> paramMap = new TreeMap<String, String>();
		paramMap.put("App_Sub_ID", commonParam.getAppSubId());
		paramMap.put("App_Token", commonParam.getAppToken());
		paramMap.put("Api_ID", commonParam.getApiID());
		paramMap.put("Api_Version", commonParam.getApiVersion());
		paramMap.put("Time_Stamp", commonParam.getTimeStamp());
		paramMap.put("Sign_Method", commonParam.getSignMethod());
		paramMap.put("Format", commonParam.getFormat());
		paramMap.put("Partner_ID", commonParam.getPartnerID());
		paramMap.put("Sys_ID", commonParam.getSysID());
		paramMap.put("App_Pub_ID", commonParam.getAppPubID());
		paramMap.put("REQUEST_DATA", commonParam.getReqdate());

		StringBuffer bufferSign = new StringBuffer();
		for (Map.Entry entry : paramMap.entrySet()) {
			bufferSign.append((String) entry.getKey()).append("=")
					.append((String) entry.getValue()).append("&");
		}
		bufferSign.append(key);	
		
		msglog.debug("---------------------------------sign String：    "+bufferSign);
		try {
			result = DigestUtils.md5Hex(bufferSign.toString().getBytes(input_charset))
					.toUpperCase();
		} catch (Exception e) {
			throw new OpenApiException(OpenApiErrorEnum.SIGN_CONVERSION_OTHER);
		}

		return result;
	}

	
	public static void main(String[] args) {

//		String json = "{\"REQUEST\": {\"HRT_ATTRS\": {\"App_Sub_ID\":\"T000000101OB\",\"App_Token\":\"bccf7023e52b4056a7e40215dda293b3\",\"Api_ID\":\"crt.ec.public.getGoodsCategories\",\"Api_Version\":\"2.0.0\",\"Time_Stamp\":\"2017-11-03 11:20:00:00\",\"Sign_Method\":\"md5\",\"Sign\":\"9CBC6F0CE0B180BCAEE6E78EB51D9C45\",\"Format\":\"json\",\"Partner_ID\":\"T0000000\",\"App_Pub_ID\":\"T000000201EN\",\"Sys_ID\":\"T0000001\"},\"REQUEST_DATA\":{\"cardNo\":\"\",\"cardSign\":\"\",\"certificateNo\":\"\",\"certificateType\":\"\",\"channelId\":\"IOS\",\"innerCardNo\":\"\",\"mobile\":\"15084902078\",\"sysId\":\"20000000\"}}}";

		
		String json = "{\"REQUEST\": {\"REQUEST_DATA\": {\"mobile\": \"18824635426\", \"channelId\": \"APP\", \"appVersion\": \"0.6.0\", \"sysId\": \"20000T18\", \"transactionUuid\": \"123\", \"loginPwd\": \"e10adc3949ba59abbe56e057f20f883e\"}, \"HRT_ATTRS\": {\"App_Sub_ID\": \"T000000304KH\", \"Sys_ID\": \"T0000003\", \"App_Pub_ID\": \"T000000304KH\", \"Api_ID\": \"crt.mb.app.LoginWithPWD\", \"Sign_Method\": \"md5\", \"Format\": \"json\", \"Time_Stamp\": \"2017-11-18 21:06:33\", \"App_Token\": \"610647e2149047a0a012a8f5f5b05791\", \"Partner_ID\": \"T0000000\", \"Api_Version\": \"0.2.0\"}}}";

		

//	    System.out.println(json);
		
		String app_Sub_ID = null;
		String app_Token = null;
		String api_ID = null;
		String app_Version = null;
		String time_Stamp = null;
		String sign_Method = null;
		String format = null;
		String partner_id = null;
		String sysId = null;
		String app_Pub_ID = null;
		String data = null;

		
		
		
		JSONObject jasonObject = FastJsonUtils.parse(json, true);
		if (jasonObject.containsKey("REQUEST")) {
			
			JSONObject jasonObject2 = jasonObject.getJSONObject("REQUEST");
			Map mapJson2 = (Map) jasonObject2;
			if (mapJson2.containsKey("REQUEST_DATA")) {
				data = String.valueOf(mapJson2.get("REQUEST_DATA"));
			}
			
			jasonObject = jasonObject.getJSONObject("REQUEST").getJSONObject("HRT_ATTRS");
			Map mapJson = (Map) jasonObject;

			if (mapJson.containsKey("App_Sub_ID")) {
				app_Sub_ID = String.valueOf(mapJson.get("App_Sub_ID"));
			}
			if (mapJson.containsKey("App_Token")) {
				app_Token = String.valueOf(mapJson.get("App_Token"));
			}
			if (mapJson.containsKey("Api_ID")) {
				api_ID = String.valueOf(mapJson.get("Api_ID"));
			}
			if (mapJson.containsKey("Api_Version")) {
				app_Version = String.valueOf(mapJson.get("Api_Version"));
			}
			if (mapJson.containsKey("Time_Stamp")) {
				time_Stamp = String.valueOf(mapJson.get("Time_Stamp"));
			}
			if (mapJson.containsKey("Sign_Method")) {
				sign_Method = String.valueOf(mapJson.get("Sign_Method"));
			}
			
			if (mapJson.containsKey("Format")) {
				format = String.valueOf(mapJson.get("Format"));
			}
			if (mapJson.containsKey("Partner_ID")) {
				partner_id = String.valueOf(mapJson.get("Partner_ID"));
			}
			if (mapJson.containsKey("App_Pub_ID")) {
				app_Pub_ID = String.valueOf(mapJson.get("App_Pub_ID"));
			}
			if (mapJson.containsKey("Sys_ID")) {
				sysId = String.valueOf(mapJson.get("Sys_ID"));
			}
		
		}

		ApiCommonParamDto param = new ApiCommonParamDto();
		param.setAppSubId(app_Sub_ID);
		param.setAppToken(app_Token);
		param.setApiID(api_ID);
		param.setApiVersion(app_Version);
		param.setTimeStamp(time_Stamp);
		param.setSignMethod(sign_Method);
		param.setFormat(format);
		param.setPartnerID(partner_id);
		param.setSysID(sysId);
		param.setAppPubID(app_Pub_ID);
		param.setReqdate(data);

		String dosign = sign(param, "711b661dea8f4ce5b5d6f4663d0e39e1");

		System.out.println(dosign);
	}

}
