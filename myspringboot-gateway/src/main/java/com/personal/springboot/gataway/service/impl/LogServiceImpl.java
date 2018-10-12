package com.personal.springboot.gataway.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.personal.springboot.gataway.dao.entity.OpenApiLogBean;
import com.personal.springboot.gataway.service.LogService;
import com.personal.springboot.gataway.utils.FastJsonUtils;

@Service("logService")
public class LogServiceImpl implements LogService {

	public static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

	@Value("${mq.ignoreMqLog}")
	private String ignoreMqLog;

	@Value("${mq.queue.openapiLog.topic}")
	private String openapi_topic;

	@Value("${mq.queue.openapiLog.tag}")
	private String openapi_tag;

	@Override
	public void sendMsgLog(OpenApiLogBean openApiLogBean) {
		if (!"true".equals(ignoreMqLog)) {
			sendMsgLogInfo(openApiLogBean);
		}
	}

	private void sendMsgLogInfo(OpenApiLogBean openApiLogBean) {

		try {
			String all = FastJsonUtils.beanToText(openApiLogBean);
		} catch (Exception e) {
			logger.error("apiRocketMQ 消息解析错误", e);
		}
	}

}
