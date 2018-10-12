package com.personal.springboot.gataway.dao.entity;

import com.personal.springboot.gataway.dao.base.BaseEntity;


public class ApiSubInterface extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String appSubId;

    private String appPubId;

    private String apiId;

    private String status;
    
    private String subInterfaceId;
     
    
    
    public ApiSubInterface() {
	}
    
    
    public ApiSubInterface(String appSubId, String apiId) {
		super();
		this.appSubId = appSubId;
		this.apiId = apiId;
	}

	//获取redis的key值为appSubId.apiId
   	public String getRedisKey() {
   		return appSubId+"."+apiId;
   	}
    
	public String getSubInterfaceId() {
		return subInterfaceId;
	}

	public void setSubInterfaceId(String subInterfaceId) {
		this.subInterfaceId = subInterfaceId;
	}

	public String getAppSubId() {
		return appSubId;
	}

	public void setAppSubId(String appSubId) {
		this.appSubId = appSubId;
	}

	public String getAppPubId() {
		return appPubId;
	}

	public void setAppPubId(String appPubId) {
		this.appPubId = appPubId;
	}

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ApiSubInterface [appSubId=" + appSubId + ", appPubId="
				+ appPubId + ", apiId=" + apiId + ", status=" + status
				+ ", subInterfaceId=" + subInterfaceId + ", toString()="
				+ super.toString() + "]";
	}

}