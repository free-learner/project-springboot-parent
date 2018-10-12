package com.personal.springboot.gataway.core;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiLogBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.service.LogService;
import com.personal.springboot.gataway.utils.DateUtils;

@Service
public class OpenApiResponseUtils {
	private static final Logger logger = LoggerFactory.getLogger(OpenApiResponseUtils.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");
			
	@Inject
	private LogService logService;
		
	
	public  void writeRsp(HttpServletResponse response,OpenApiHttpRequestBean requestBean) {
		OpenApiLogBean openApiLogBean=requestBean.getLogBean();	
		setResponseHeader(response,requestBean.getReqHeader());
		try {
			PrintWriter writer = response.getWriter();
			writer.print(requestBean.getRespMsg());
			
			msglog.info("发送HTTP响应[OPEN_API --> FA]: key["+requestBean.getApiCommonParamDto().getSign()+"] body:[" + requestBean.getRespMsg() + "]");	
			openApiLogBean.getRsOpenApiLogDetail().setRespInfo(DateUtils.formatStr4Date(), requestBean.getRespMsg());
			logService.sendMsgLog(openApiLogBean);
			
			writer.flush();
			writer.close();
		} catch (Exception e) {
			throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,new String[]{e.getClass().getName(),e.getMessage()},e);
		}finally{			
			
		}
	}
	
	private  void setResponseHeader(HttpServletResponse response,Map<String, String> httpHeader){		
		if(httpHeader==null){
			response.addHeader(CommConstants.HEADER_CONTENT_TYPE_KEY, ContentType.APPLICATION_JSON.getMimeType());
			return;
		}
		
		Iterator<Entry<String, String>> entries = httpHeader.entrySet().iterator();  
		while (entries.hasNext()) { 
		    Entry<String, String> entry = entries.next();  	
		    //copy head
		    if(CommConstants.respSet.contains(entry.getKey().toLowerCase())){
		    	response.addHeader(entry.getKey(), entry.getValue());
		    }
		}  
	}

	
	
	
	
}
