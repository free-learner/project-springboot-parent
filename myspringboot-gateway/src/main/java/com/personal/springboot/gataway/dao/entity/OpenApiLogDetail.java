package com.personal.springboot.gataway.dao.entity;

import java.io.Serializable;

public class OpenApiLogDetail implements Serializable {
    private static final long serialVersionUID = 3382762137392351013L;
    private String  reqTime;
	private String  respTime;
	private String  reqMsg;
	private String  respMsg;
	
	
	
	public void setReqInfo(String  reqTime,String  reqMsg){
		this.reqTime=reqTime;
		this.reqMsg=reqMsg;
	}
	
	public void setRespInfo(String  respTime,String  respMsg){
		this.respTime=respTime;
		this.respMsg=respMsg;
	}
	public String getReqTime() {
		return reqTime;
	}
	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}
	public String getRespTime() {
		return respTime;
	}
	public void setRespTime(String respTime) {
		this.respTime = respTime;
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
	
	
	
	
}
