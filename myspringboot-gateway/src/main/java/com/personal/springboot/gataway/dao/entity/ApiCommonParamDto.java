package com.personal.springboot.gataway.dao.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("HRT_ATTRS")
public class ApiCommonParamDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JSONField(name="App_Sub_ID")
	@XStreamAlias("App_Sub_ID")
	private String appSubId;
	
	@JSONField(serialize=false,deserialize=false)
	private String appToken;
	
	@JSONField(name="Api_ID")
	@XStreamAlias("Api_ID")
	private String apiID;
	
	
	@JSONField(name="Api_Version")
	@XStreamAlias("Api_Version")
	private String apiVersion;
	
	@JSONField(name="Time_Stamp")
	@XStreamAlias("Time_Stamp")
	private String timeStamp;
	
	@JSONField(name="Sign_Method")
	@XStreamAlias("Sign_Method")
	private String signMethod;
	
	@JSONField(name="Format")
	@XStreamAlias("Format")
	private String format;
	
	@JSONField(name="Partner_ID")
	@XStreamAlias("Partner_ID")
	private String partnerID;
	
	@JSONField(name="Sys_ID")
	@XStreamAlias("Sys_ID")
	private String sysID;
	
	@JSONField(name="App_Pub_ID")
	@XStreamAlias("App_Pub_ID")
	private String appPubID;
	
	@JSONField(name="Sign")
	@XStreamAlias("Sign")
	private String sign;
	
	@JSONField(serialize=false,deserialize=false)
	@XStreamOmitField 
	private String reqdate;
		

	public String getReqdate() {
		return reqdate;
	}

	public void setReqdate(String reqdate) {
		this.reqdate = reqdate;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppSubId() {
		return this.appSubId;
	}

	public void setAppSubId(String appSubId) {
		this.appSubId = appSubId;
	}

	public String getAppToken() {
		return this.appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public String getApiID() {
		return this.apiID;
	}

	public void setApiID(String apiID) {
		this.apiID = apiID;
	}

	public String getApiVersion() {
		return this.apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSignMethod() {
		return this.signMethod;
	}

	public void setSignMethod(String signMethod) {
		this.signMethod = signMethod;
	}

	public String getFormat() {
		return this.format;
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

	public String getSysID() {
		return sysID;
	}

	public void setSysID(String sysID) {
		this.sysID = sysID;
	}

	public String getAppPubID() {
		return this.appPubID;
	}

	public void setAppPubID(String appPubID) {
		this.appPubID = appPubID;
	}

}
