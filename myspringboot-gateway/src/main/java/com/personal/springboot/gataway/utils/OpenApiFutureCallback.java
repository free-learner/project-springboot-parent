package com.personal.springboot.gataway.utils;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.http.Header;
import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.core.OpenApiResponseUtils;
import com.personal.springboot.gataway.core.adapter.OpenApiRspHandler;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpSessionBean;
import com.personal.springboot.gataway.exception.OpenApiException;
import com.personal.springboot.gataway.netty.OpenapiHttpServletResponse;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.stream.ChunkedStream;


public class OpenApiFutureCallback implements FutureCallback<org.apache.http.HttpResponse> {
	private static final Logger msglog = LoggerFactory.getLogger("msglog");
	private static final Logger log = LoggerFactory.getLogger(OpenApiFutureCallback.class);
		 
	
	private  OpenApiHttpSessionBean httpSessionBean;		
	private OpenApiRspHandler rspHandler;
		
	private OpenApiResponseUtils openApiResponseUtils;
		
	public OpenApiFutureCallback( ) {
		super();
	}
	
	

	public OpenApiFutureCallback(OpenApiHttpSessionBean httpSessionBean,
			OpenApiRspHandler rspHandler,
			OpenApiResponseUtils openApiResponseUtils) {
		super();
		this.httpSessionBean = httpSessionBean;
		this.rspHandler = rspHandler;
		this.openApiResponseUtils = openApiResponseUtils;
	}

	public  OpenApiHttpSessionBean getHttpSessionBean() {
		return httpSessionBean;
	}
	
	
	

	public void setHttpSessionBean(OpenApiHttpSessionBean httpSessionBean) {
		this.httpSessionBean = httpSessionBean;
	}
	@Override
	public void completed(org.apache.http.HttpResponse result) {
		
		String response =null;
		OpenApiHttpRequestBean reqbean =httpSessionBean.getRequest();		
		OpenapiHttpServletResponse servletResponse =(OpenapiHttpServletResponse)httpSessionBean.getResponse();
		
				
		try{
			response =opeanapiCompleted(result);
			msglog.info("收到HTTP响应[BA --> OPEN_API]: key["+reqbean.getApiCommonParamDto().getSign()+"]  response_completed:[" + response + "]");
			reqbean.getLogBean().getServiceOpenApiLogDetail().setRespInfo(DateUtils.formatStr4Date(), response);	
			
			
			servletResponse.getAcceptHandler().acceptRespose(httpSessionBean);
		}catch (Exception e){
			msglog.info("收到HTTP响应[BA --> OPEN_API]: key["+reqbean.getApiCommonParamDto().getSign()+"]  response_completed:[" + response + "]");
			log.error("",e);
			openApiwriteRsp(e);
		}		
		
		
		//netty 写响应
		futureWriteRsp(servletResponse);		
	}

	@Override
	public void failed(Exception ex) {
		OpenapiHttpServletResponse servletResponse =(OpenapiHttpServletResponse)httpSessionBean.getResponse();
		OpenApiHttpRequestBean reqbean =httpSessionBean.getRequest();
		msglog.info("收到HTTP响应[BA --> OPEN_API]: key["+reqbean.getApiCommonParamDto().getSign()+"]  response_failed:[" + ex.getMessage() + "]");
		reqbean.getLogBean().getServiceOpenApiLogDetail().setRespInfo(DateUtils.formatStr4Date(), "response_failed:[" + ex.getMessage() + "]");			
				
		try{
			openapiFailed(ex);
		}catch (Exception e){
			log.error("",e);
			openApiwriteRsp(e);
		}		
		
		//netty 写响应
		futureWriteRsp(servletResponse);
	}

	@Override
	public void cancelled() {
		OpenApiHttpRequestBean reqbean =httpSessionBean.getRequest();
		OpenapiHttpServletResponse servletResponse =(OpenapiHttpServletResponse)httpSessionBean.getResponse();
		
		msglog.info("收到HTTP响应[BA --> OPEN_API]: key["+reqbean.getApiCommonParamDto().getSign()+"]  response_cancelled:[cancelled]");
		reqbean.getLogBean().getServiceOpenApiLogDetail().setRespInfo(DateUtils.formatStr4Date(), "response_cancelled:[cancelled]");			
		
		//netty 写响应
		futureWriteRsp(servletResponse);
	}
	
	private String openapiFailed(Exception ex){
		
		if(ex instanceof NoHttpResponseException){
			throw new OpenApiException(OpenApiErrorEnum.B_RESP_FAIL, ex);	
		}else if(ex instanceof ConnectException){
			throw new OpenApiException(OpenApiErrorEnum.S_02, ex);		
		}else if(ex instanceof ConnectTimeoutException){
			throw new OpenApiException(OpenApiErrorEnum.S_01, ex);			
		}else if(ex instanceof SocketTimeoutException){
			throw new OpenApiException(OpenApiErrorEnum.S_01, ex);				
		} else if(ex instanceof UnknownHostException){ 
			throw new OpenApiException(OpenApiErrorEnum.S_06, ex);			
		}else if(ex instanceof java.net.SocketException){ 		
			throw new OpenApiException(OpenApiErrorEnum.S_07,new String[]{ex.getMessage()} , ex);
		}else {
			log.error("HttpClientUtil 其他异常",ex);
			throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,new String[]{ex.getClass().getName(),ex.getMessage()}, ex);
//			throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY, ex);
		}
		
		
		
	}
	
	
	private  String  opeanapiCompleted(org.apache.http.HttpResponse result){
		String response =null;
		OpenApiHttpRequestBean reqbean =httpSessionBean.getRequest();
		OpenapiHttpServletResponse servletResponse =(OpenapiHttpServletResponse)httpSessionBean.getResponse();
		
		
		int statusCode=result.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			InputStream responseStream=null;
			try {
				responseStream = result.getEntity().getContent();
			} catch (Exception e) {
				throw new OpenApiException(OpenApiErrorEnum.SYSTEM_BUSY,new String[]{e.getClass().getName(),e.getMessage()}, e);
			}
			response = StringUtil.convertStreamToString(responseStream);
			if(reqbean!=null){
				Header[] header=result.getHeaders(CommConstants.HEADER_COOKIE_KEY);
				if(header!=null && header.length>1){
					reqbean.getReqHeader().put(CommConstants.HEADER_COOKIE_KEY, header[0].getValue());
				}					
		 	}
			
			reqbean.setServiceBody(response);
			reqbean.setRespMsg(response);
			
			
		} else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
			throw new OpenApiException(OpenApiErrorEnum.S_05);
		} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
			throw new OpenApiException(OpenApiErrorEnum.S_04);
		} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			throw new OpenApiException(OpenApiErrorEnum.S_03);
		} else {
			throw new OpenApiException(
					OpenApiErrorEnum.S_22.getErrCode(),
					OpenApiErrorEnum.S_22.getErrMsg()+ statusCode);
		}
		
	
		return response;
	}
	
	
	private void openApiwriteRsp(Object ex){		
		OpenApiHttpRequestBean reqBean =httpSessionBean.getRequest();
		OpenapiHttpServletResponse servletResponse =(OpenapiHttpServletResponse)httpSessionBean.getResponse();
				
		if(ex instanceof OpenApiException){
			OpenApiException openApiException=(OpenApiException)ex;
			reqBean.setServiceBody(openApiException);
			reqBean.getLogBean().setReturn_code(openApiException.getErrorCode());
			reqBean.getLogBean().setReturn_desc(openApiException.getErrorMsg());
		}else{
			Exception e=(Exception)ex;
			reqBean.setServiceBody(ex);
			reqBean.getLogBean().setReturn_code(OpenApiErrorEnum.SYSTEM_BUSY.getErrCode());
			String desc=String.format(OpenApiErrorEnum.SYSTEM_BUSY.getErrMsg(), new String[]{e.getClass().getName(),e.getMessage()});
			reqBean.getLogBean().setReturn_desc(desc);
			
		}
		String body = this.rspHandler.executePrint(reqBean);
		
		reqBean.setRespMsg(body);
		openApiResponseUtils.writeRsp(servletResponse,reqBean);	
	}
	
		
	private void futureWriteRsp(OpenapiHttpServletResponse servletResponse ){
		
		io.netty.handler.codec.http.HttpResponse nettyResponse = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);

		for (String name : servletResponse.getHeaderNames()) {
			for (Object value : servletResponse.getHeaders(name)) {
				nettyResponse.addHeader(name, value);
			}
		}
		
		servletResponse.getCtx().write(nettyResponse);
        InputStream contentStream = new ByteArrayInputStream(servletResponse.getContentAsByteArray());	
        ChannelFuture writeFuture = servletResponse.getCtx().write(new ChunkedStream(contentStream));
        
       writeFuture.addListener(ChannelFutureListener.CLOSE);
       
	}
	
	

}
