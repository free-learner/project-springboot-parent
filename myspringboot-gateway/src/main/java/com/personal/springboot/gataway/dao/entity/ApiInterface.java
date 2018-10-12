package com.personal.springboot.gataway.dao.entity;

import com.personal.springboot.gataway.dao.base.BaseEntity;

public class ApiInterface  extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	public static final String SIGN_TYPE_NO_SIGN="0";
	public static final String SIGN_TYPE_MD5="1";
	public static final String SIGN_TYPE_RSA="2";
	
    private String apiId;
    private String apiName;
    private String apiDesc;
    private String testCase;
    private String targetUrl;
    private String sandboxTargetUrl;
    private String status;
    private String version;
    private String orgId;
    private String appId;
    private String sysId;
    private String columnCode;
    private String auth;
    private String authCodes;
    private Byte isFast;
    private String fileId;
    private String type;	//接口类型
    private String apiFunction;	//接口功能描述
    private String transMode;	//传输方式

    //获取redis的key值为String key = "api_id:"+api_id + "version:" + version;
 	public String getRedisKey() {
 		return "api_id:"+apiId + "version:" + version;
 	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiDesc() {
		return apiDesc;
	}

	public void setApiDesc(String apiDesc) {
		this.apiDesc = apiDesc;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getSandboxTargetUrl() {
		return sandboxTargetUrl;
	}

	public void setSandboxTargetUrl(String sandboxTargetUrl) {
		this.sandboxTargetUrl = sandboxTargetUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getColumnCode() {
		return columnCode;
	}

	public void setColumnCode(String columnCode) {
		this.columnCode = columnCode;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getAuthCodes() {
		return authCodes;
	}

	public void setAuthCodes(String authCodes) {
		this.authCodes = authCodes;
	}

	public Byte getIsFast() {
		return isFast;
	}

	public void setIsFast(Byte isFast) {
		this.isFast = isFast;
	}
	

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getApiFunction() {
		return apiFunction;
	}

	public void setApiFunction(String apiFunction) {
		this.apiFunction = apiFunction;
	}

	public String getTransMode() {
		return transMode;
	}

	public void setTransMode(String transMode) {
		this.transMode = transMode;
	}

	@Override
	public String toString() {
		return "ApiInterface [apiId=" + apiId + ", apiName=" + apiName
				+ ", apiDesc=" + apiDesc + ", testCase=" + testCase
				+ ", targetUrl=" + targetUrl + ", sandboxTargetUrl="
				+ sandboxTargetUrl + ", status=" + status + ", version="
				+ version + ", orgId=" + orgId + ", appId=" + appId
				+ ", sysId=" + sysId
				+ ", columnCode=" + columnCode + ", auth=" + auth
				+ ", authCodes=" + authCodes + ", isFast=" + isFast
				+ ",fileId="+fileId+ ",type="+type+",apiFunction="+apiFunction
				+",transMode="+transMode+", toString()=" + super.toString() + "]";
	}
}