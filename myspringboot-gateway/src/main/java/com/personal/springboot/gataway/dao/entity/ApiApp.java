package com.personal.springboot.gataway.dao.entity;

import com.personal.springboot.gataway.dao.base.BaseEntity;

public class ApiApp extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String appId;
	private String orgId;
	private String appName;
	private String appNameAbbreviation;
	private String appSecret;
	private String token;
	private String status;
	private String sysId;
    private String sandboxAppSecret;
    private String sandboxToken;
    //英文简写
    private String enAbbreviation;
	
	//获取redis的key值为appId
 	public String getRedisKey() {
 		return "app_id:"+appId;
 	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppNameAbbreviation() {
		return appNameAbbreviation;
	}

	public void setAppNameAbbreviation(String appNameAbbreviation) {
		this.appNameAbbreviation = appNameAbbreviation;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
    
    public String getSandboxAppSecret() {
		return sandboxAppSecret;
	}

	public void setSandboxAppSecret(String sandboxAppSecret) {
		this.sandboxAppSecret = sandboxAppSecret;
	}

	public String getSandboxToken() {
		return sandboxToken;
	}

	public void setSandboxToken(String sandboxToken) {
		this.sandboxToken = sandboxToken;
	}

	public String getEnAbbreviation() {
		return enAbbreviation;
	}

	public void setEnAbbreviation(String enAbbreviation) {
		this.enAbbreviation = enAbbreviation;
	}

	@Override
	public String toString() {
		return "ApiApp [appId=" + appId + ", orgId=" + orgId + ", appName="
				+ appName + ", appNameAbbreviation=" + appNameAbbreviation
				+ ", appSecret=" + appSecret + ", token=" + token + ", status="
				+ status + ", sysId=" + sysId + ", sandboxAppSecret="
				+ sandboxAppSecret + ", sandboxToken=" + sandboxToken
				+ ", toString()=" + super.toString() + "]";
	}

}