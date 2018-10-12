package com.personal.springboot.gataway.service;

import com.personal.springboot.gataway.dao.entity.OpenApiLogBean;


public interface LogService {
	
	public void sendMsgLog(OpenApiLogBean openApiLogBean);
	
}
