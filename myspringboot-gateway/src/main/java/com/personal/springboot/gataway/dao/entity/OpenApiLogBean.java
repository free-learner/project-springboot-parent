package com.personal.springboot.gataway.dao.entity;

import java.io.Serializable;
import java.util.Map;

public class OpenApiLogBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private String clientAddr;
	private String localAddr;
	private int localPort;
	private String queryString;
	private String logType;

	private String return_code;
	private String return_desc;
	private String return_data;
	private String return_stamp;

	private String rsaReqTime;
	private String rsaRespTime;
	private String rsaInfoStamp;

	private Map<String, String> reqHeader;
	private ApiCommonParamDto apiCommonParamDto;

	private OpenApiLogDetail rsOpenApiLogDetail;
	private OpenApiLogDetail serviceOpenApiLogDetail;

	private int reqMegLen;

	private int respMsgLen;

	public OpenApiLogBean() {

	}

	public OpenApiLogBean(OpenApiHttpRequestBean reqBean) {
		this.clientAddr = reqBean.getClientAddr();
		this.localAddr = reqBean.getLocalAddr();
		this.localPort = reqBean.getLocalPort();
		this.reqHeader = reqBean.getReqHeader();
		this.queryString = reqBean.getQueryString();
		this.apiCommonParamDto = reqBean.getApiCommonParamDto();
	}

	public String getReturn_data() {
		return return_data;
	}

	public void setReturn_data(String return_data) {
		this.return_data = return_data;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_desc() {
		return return_desc;
	}

	public void setReturn_desc(String return_desc) {
		this.return_desc = return_desc;
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

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

	public Map<String, String> getReqHeader() {
		return reqHeader;
	}

	public void setReqHeader(Map<String, String> reqHeader) {
		this.reqHeader = reqHeader;
	}

	public ApiCommonParamDto getApiCommonParamDto() {
		return apiCommonParamDto;
	}

	public void setApiCommonParamDto(ApiCommonParamDto apiCommonParamDto) {
		this.apiCommonParamDto = apiCommonParamDto;
	}

	public OpenApiLogDetail getRsOpenApiLogDetail() {
		return rsOpenApiLogDetail;
	}

	public void setRsOpenApiLogDetail(OpenApiLogDetail openApiLogDetail) {
		this.rsOpenApiLogDetail = openApiLogDetail;
	}

	public OpenApiLogDetail getServiceOpenApiLogDetail() {
		return serviceOpenApiLogDetail;
	}

	public void setServiceOpenApiLogDetail(OpenApiLogDetail openApiLogDetail) {
		this.serviceOpenApiLogDetail = openApiLogDetail;
	}

	public int getReqMegLen() {
		return reqMegLen;
	}

	public void setReqMegLen(int reqMegLen) {
		this.reqMegLen = reqMegLen;
	}

	public int getRespMsgLen() {
		return respMsgLen;
	}

	public void setRespMsgLen(int respMsgLen) {
		this.respMsgLen = respMsgLen;
	}

	public String getReturn_stamp() {
		return return_stamp;
	}

	public void setReturn_stamp(String return_stamp) {
		this.return_stamp = return_stamp;
	}

	public String getRsaReqTime() {
		return rsaReqTime;
	}

	public void setRsaReqTime(String rsaReqTime) {
		this.rsaReqTime = rsaReqTime;
	}

	public String getRsaRespTime() {
		return rsaRespTime;
	}

	public void setRsaRespTime(String rsaRespTime) {
		this.rsaRespTime = rsaRespTime;
	}

	public String getRsaInfoStamp() {
		return rsaInfoStamp;
	}

	public void setRsaInfoStamp(String rsaInfoStamp) {
		this.rsaInfoStamp = rsaInfoStamp;
	}

}
