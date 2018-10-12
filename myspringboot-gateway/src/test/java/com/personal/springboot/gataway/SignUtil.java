package com.personal.springboot.gataway;



import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;

import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.utils.StringUtil;

public class SignUtil {
	public static final String MDF_CHARSET_UTF_8 = "UTF-8";

	public static String sign(ApiCommonParamDto commonParam, String key) {
		return sign(commonParam,key,MDF_CHARSET_UTF_8);
	}
	
	
	public static String sign(ApiCommonParamDto commonParam, String key,String input_charset) {
		String result=null;
		if (StringUtil.isEmpty(input_charset)) {
			input_charset = "UTF-8";
		}
		
		Map<String, String> paramMap =new TreeMap<String, String>();
		paramMap.put("App_Sub_ID", commonParam.getAppSubID());
		paramMap.put("App_Token", commonParam.getAppToken());
		paramMap.put("Api_ID", commonParam.getApiID());
		paramMap.put("Api_Version", commonParam.getApiVersion());
		paramMap.put("Time_Stamp", commonParam.getTimeStamp());
		paramMap.put("Sign_Method", commonParam.getSignMethod());
		paramMap.put("Format", commonParam.getFormat());
		paramMap.put("Partner_ID", commonParam.getPartnerID());
		paramMap.put("Sys_ID", commonParam.getSysID());
		paramMap.put("App_Pub_ID", commonParam.getAppPubID());		
		paramMap.put("REQUEST_DATA", commonParam.getBizdate());
		
		StringBuffer bufferSign = new StringBuffer();
		for (Map.Entry entry : paramMap.entrySet()) {
			bufferSign.append((String) entry.getKey()).append("=")
					.append((String) entry.getValue()).append("&");
		}
		bufferSign.append(key);		
		try {
			result=DigestUtils.md5Hex(bufferSign.toString().getBytes(input_charset)).toUpperCase();
		} catch (Exception e) {
			throw new OpenApiException(OpenApiErrorEnum.SIGN_CONVERSION_OTHER);
		}
		
		return result;
	}
	
	public static class ApiCommonParamDto implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String appSubID;		
		private String appToken;
		private String apiID;			
		private String apiVersion;		
		private String timeStamp;		
		private String signMethod;		
		private String format="json";		
		private String partnerID;
		private String sysID;		
		private String appPubID;		
		private String sign;		
		private String bizdate;

		

		public String getAppToken() {
			return appToken;
		}

		public void setAppToken(String appToken) {
			this.appToken = appToken;
		}

		public String getApiID() {
			return apiID;
		}

		public void setApiID(String apiID) {
			this.apiID = apiID;
		}

		public String getApiVersion() {
			return apiVersion;
		}

		public void setApiVersion(String apiVersion) {
			this.apiVersion = apiVersion;
		}

		public String getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(String timeStamp) {
			this.timeStamp = timeStamp;
		}

		public String getSignMethod() {
			return signMethod;
		}

		public void setSignMethod(String signMethod) {
			this.signMethod = signMethod;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		
		public String getPartnerID() {
			return partnerID;
		}

		public void setPartnerID(String partnerID) {
			this.partnerID = partnerID;
		}

		public String getAppPubID() {
			return appPubID;
		}

		public void setAppPubID(String appPubID) {
			this.appPubID = appPubID;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getBizdate() {
			return bizdate;
		}

		public void setBizdate(String bizdate) {
			this.bizdate = bizdate;
		}

		public String getAppSubID() {
			return appSubID;
		}

		public void setAppSubID(String appSubID) {
			this.appSubID = appSubID;
		}

		public String getSysID() {
			return sysID;
		}

		public void setSysID(String sysID) {
			this.sysID = sysID;
		}
		
		
		
	}


	public static void main(String[] args) {
		SignUtil.ApiCommonParamDto param = new SignUtil.ApiCommonParamDto();
		param.setAppSubID("Z000000100WZ");
		param.setAppToken("e16a2cca-39d2-4a10-9e6d-9af634df1a74");
		param.setApiID("hrt.cezql.ceshi.du008");
		param.setApiVersion("1.0");
		param.setTimeStamp("2016-08-25 16:56:13:234");
		param.setSignMethod("md5");
		param.setFormat("json");
		param.setPartnerID("Z0000000");
		param.setSysID("Z0000001");
		param.setAppPubID("S0000000");
		param.setBizdate("{\"customer\":{\"address\":\"shanghai\",\"id\":2,\"name\":\"liuliu\"}}");
		String sign = SignUtil.sign(param, "57BqcY884O3Sas20tWlxr4xpwj9434h18", "UTF-8");
		
		
		System.out.println(sign);
	}

}
