package com.personal.springboot.gataway.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.conf.CommConstants;
import com.personal.springboot.gataway.dao.entity.OpenApiHttpRequestBean;


public class AsyncHttpClientUtil {

	private static final Logger msglog = LoggerFactory.getLogger("msglog");
	private static Logger log = LoggerFactory.getLogger(AsyncHttpClientUtil.class);
	
	private static final int CONN_TIMEOUT = 30 * 1000;

	private static final int http_client_maxTotalConnections = 50;
	private static final int http_client_defaultMaxConnectionsPerHost = 50;
	
	
	private static final int http_client_connectionTimeout = 3000;
	private static AsyncHttpClientUtil httpClientUtil = null;

	private AsyncHttpClientUtil() {
	}

	private static CloseableHttpAsyncClient httpclient = null;

	public static AsyncHttpClientUtil getInterfaces() {

		if (httpClientUtil == null) {

			try {
				IOReactorConfig ioReactorConfig = IOReactorConfig
						.custom()
						.setIoThreadCount(Runtime.getRuntime().availableProcessors())
						.setConnectTimeout(CONN_TIMEOUT)
						.setSoTimeout(CONN_TIMEOUT).build();
				
				ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
				PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);				
				connManager.setDefaultConnectionConfig(ConnectionConfig.DEFAULT);				
				connManager.setMaxTotal(http_client_maxTotalConnections);
				connManager.setDefaultMaxPerRoute(http_client_defaultMaxConnectionsPerHost);
				
				
				httpclient = HttpAsyncClients.custom().setConnectionManager(connManager).build();
				httpClientUtil=new AsyncHttpClientUtil();

			} catch (IOReactorException e) {
				log.error("AsyncHttpClientUtil 初始化异常",e);
			}

		}

		return httpClientUtil;
	}

	public void doPost(String url, String reqData, String contentType,
			OpenApiHttpRequestBean reqbean,
			FutureCallback<HttpResponse> futureCallback) {
		this.doPost(url, reqData, contentType, reqbean,http_client_connectionTimeout, futureCallback);
	}
	
	
	public void doPost(String url, String reqData, String contentType,
			OpenApiHttpRequestBean reqbean,int timeout,
			FutureCallback<HttpResponse> futureCallback) {

		try {
			HttpPost postRequest = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig
					.copy(RequestConfig.DEFAULT)
					.setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout)
					.setSocketTimeout(timeout).build();

			postRequest.setConfig(requestConfig);
		
			postRequest.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			
			if (reqbean != null) {
				postRequest.setHeader(CommConstants.HEADER_COOKIE_KEY, reqbean
						.getReqHeader().get(CommConstants.HEADER_COOKIE_KEY));
			}
			postRequest.setEntity(new StringEntity(reqData, contentType,"UTF-8"));

			
			
			httpclient.start();
			httpclient.execute(postRequest,futureCallback);

		} catch (UnsupportedEncodingException e) {
			log.error("",e);
		} 

	}

	
	
	

	public final static void main(String[] args) throws Exception {

		
		AsyncHttpClientUtil.getInterfaces().doPost("http://10.0.62.132:6080/rest_test_NDNDKDK", "aaaa", "application/json", null, new FutureCallback<HttpResponse>() {

			@Override
			public void completed(HttpResponse result) {
				System.out.println("111111111111");
				
			}

			@Override
			public void failed(Exception ex) {
				System.out.println("2222");
				
			}

			@Override
			public void cancelled() {
				System.out.println("33333");
				
			}
		});
		
		
		
		Thread.sleep(1000*60*60);
		
		
		
	}


}
