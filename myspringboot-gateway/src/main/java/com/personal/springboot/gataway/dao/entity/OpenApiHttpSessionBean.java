package com.personal.springboot.gataway.dao.entity;

import javax.servlet.http.HttpServletResponse;



public class OpenApiHttpSessionBean extends AbstractOpenApiSessionBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5996435535509848485L;
	private OpenApiHttpRequestBean request;
	private HttpServletResponse response;
	
	public OpenApiHttpRequestBean getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setRequest(OpenApiHttpRequestBean request) {
		this.request = request;
	}

	public OpenApiHttpSessionBean(OpenApiHttpRequestBean request,HttpServletResponse response) {
		super();
		this.request = request;
		this.response=response;
	}
	
	public OpenApiHttpSessionBean(){
		
	}
	
}
