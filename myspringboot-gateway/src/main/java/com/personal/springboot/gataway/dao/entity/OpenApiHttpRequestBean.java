package com.personal.springboot.gataway.dao.entity;

import java.util.Map;


public class OpenApiHttpRequestBean extends AbstractOpenApiSessionBean {

	private static final long serialVersionUID = 674793422113507519L;

	private Map<String, String> reqHeader;
	private ApiCommonParamDto apiCommonParamDto;
	private OpenApiLogBean logBean;	

	private String clientAddr;
	private String localAddr;
	private int localPort;
	private String queryString;

	private String reqestMsg;
	private String operationType;
	private Object serviceBody;
	private String target_url;
	
		
	private String reqMsg;
	private String respMsg;
	
	private String  flowType;
	private boolean isSandbox;
	
	private ApiApp apiApp;
	private ApiInterface apiInterface;
	private ApiSubInterface apiSubInterface;
	
	

	public String getFlowType() {
		return flowType;
	}


	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	

	public boolean getIsSandbox() {
		return isSandbox;
	}


	public void setIsSandbox(Boolean isSandbox) {
		this.isSandbox = isSandbox;
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


	public String  getWsLogBeanKey(){
		return apiCommonParamDto.getTimeStamp()+"_"+apiCommonParamDto.getSign();
	}
	
	public String getTarget_url() {
		return target_url;
	}

	public void setTarget_url(String target_url) {
		this.target_url = target_url;
	}



	public String getReqMsg() {
		return reqMsg;
	}


	public void setReqMsg(String reqMsg) {
		this.reqMsg = reqMsg;
	}


	public String getRespMsg() {
		return respMsg;
	}


	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}


	public ApiCommonParamDto getApiCommonParamDto() {
		return apiCommonParamDto;
	}

	public void setApiCommonParamDto(ApiCommonParamDto apiCommonParamDto) {
		this.apiCommonParamDto = apiCommonParamDto;
	}

	

	
	public Object getServiceBody() {
		return serviceBody;
	}

	public void setServiceBody(Object serviceBody) {
		this.serviceBody = serviceBody;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Map<String, String> getReqHeader() {
		return reqHeader;
	}

	public void setReqHeader(Map<String, String> reqHeader) {
		this.reqHeader = reqHeader;
	}
	
	

	public String getReqestMsg() {
		return reqestMsg;
	}

	public void setReqestMsg(String reqestMsg) {
		this.reqestMsg = reqestMsg;
	}

	public String getClientAddr() {
		return clientAddr;
	}

	public void setClientAddr(String clientAddr) {
		this.clientAddr = clientAddr;
	}

	public String getLocalAddr() {
		return localAddr;
	}

	public void setLocalAddr(String localAddr) {
		this.localAddr = localAddr;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}


	public OpenApiLogBean getLogBean() {
		return logBean;
	}

	public void setLogBean(OpenApiLogBean logBean) {
		this.logBean = logBean;
	}

	
	

}
