package com.personal.springboot.gataway.dao.entity;

import java.io.Serializable;

public class RedisFastDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ApiApp apiApp;
	private ApiInterface apiInterface;
	private ApiSubInterface apiSubInterface;
	
	
	public RedisFastDto(){
		
	}
		
	
	public RedisFastDto(ApiApp apiApp, ApiInterface apiInterface,
			ApiSubInterface apiSubInterface) {
		super();
		this.apiApp = apiApp;
		this.apiInterface = apiInterface;
		this.apiSubInterface = apiSubInterface;
	}


	public ApiApp getApiApp() {
		return apiApp;
	}
	public void setApiApp(ApiApp apiApp) {
		this.apiApp = apiApp;
	}
	public ApiInterface getApiInterface() {
		return apiInterface;
	}
	public void setApiInterface(ApiInterface apiInterface) {
		this.apiInterface = apiInterface;
	}
	public ApiSubInterface getApiSubInterface() {
		return apiSubInterface;
	}
	public void setApiSubInterface(ApiSubInterface apiSubInterface) {
		this.apiSubInterface = apiSubInterface;
	}
	
	
}
