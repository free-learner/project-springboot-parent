package com.personal.springboot.cryption.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NoHttpResponseException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.exception.AbsErrorCodeConstant;
import com.personal.springboot.common.exception.HttpServiceException;
import com.personal.springboot.cryption.http.CommHttpConstants.Protocol;
import com.personal.springboot.cryption.http.CommHttpConstants.RequestMethod;

/**
 * 封装的HttpClient多场景工具类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年8月30日
 */
public class MutiHttpClientUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MutiHttpClientUtil.class);

    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String DEFAULT_CHARSET = CommHttpConstants.UTF_8;
    private static final int CONNNECT_TIMEOUT = 60 * 1000;
    private static final long CONNECTION_TIMEOUT_INTERVAL = 30 * 1000;
    private static IdleConnectionTimeoutThread idleConnectionTimeoutThread;
    private final static Object syncLock = new Object();
    private HttpClient client;
    private final static Map<Protocol,CloseableHttpClient> closeableHttpClientMap=new ConcurrentHashMap<>();
    
    /*
     * Available content Types
     * */
     public static final List<ContentType> CONTENT_TYPES = Arrays.asList(
             ContentType.TEXT_PLAIN, ContentType.TEXT_HTML,
             ContentType.TEXT_XML, ContentType.APPLICATION_XML,
             ContentType.APPLICATION_SVG_XML, ContentType.APPLICATION_XHTML_XML,
             ContentType.APPLICATION_ATOM_XML,
             ContentType.APPLICATION_JSON);

	private synchronized void addConnectionManager(HttpConnectionManager connectionManager) {
		if (idleConnectionTimeoutThread == null) {
			idleConnectionTimeoutThread = new IdleConnectionTimeoutThread();
			idleConnectionTimeoutThread.setTimeoutInterval(CONNECTION_TIMEOUT_INTERVAL);
			idleConnectionTimeoutThread.setConnectionTimeout(CONNNECT_TIMEOUT);			
			idleConnectionTimeoutThread.start();
		}
		idleConnectionTimeoutThread.addConnectionManager(connectionManager);
	}

	/**
	 * @since 3.1
	 */
	@SuppressWarnings("unused")
    private synchronized void removeConnectionManager(HttpConnectionManager connectionManager) {
		if (idleConnectionTimeoutThread == null) {
			return;
		}
		idleConnectionTimeoutThread.removeConnectionManager(connectionManager);
	}
	
//	public HttpClient getClient() {
//		return client;
//	}

	public void setClient(HttpClient client) {
		if (client != null) {
			HttpConnectionManager connectionManager = client.getHttpConnectionManager();
			if (connectionManager != null) {
				addConnectionManager(connectionManager);
			}
			this.client = client;
		}
	}
	
    public synchronized void addCloseableHttpClient(Protocol key,CloseableHttpClient value) {
        switch (key) {
        case HTTP:
        case HTTPS:
            if(value!=null){
                closeableHttpClientMap.put(key, value);
            }
            break;
        default:
            break;
        }
    }
    
    public synchronized CloseableHttpClient getCloseableHttpClient(Protocol key) {
        if(key == null){
            key=Protocol.HTTP;
        }
        switch (key) {
        case HTTP:
        case HTTPS:
            return closeableHttpClientMap.get(key);
        default:
            break;
        }
        return closeableHttpClientMap.get(Protocol.HTTP);
    }
    
    /**
	 * 执行一个HTTP POST请求，返回请求响应的数据
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null,参数为form表单格式
	 * @return 返回请求响应的HTML
	 */
    public String doJsonBodyPost(String url, String decData) {
        String contentType=ContentType.APPLICATION_JSON.getMimeType();
        return doJsonBodyPost(url, contentType,null, decData);
    }
	
	/**
	 * 执行一个HTTP POST请求，返回请求响应的数据
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null,参数为form表单格式
	 * @return 返回请求响应的HTML
	 */
	public String doJsonBodyPost(String url,Map<String,Object> paramMap) {
	    String contentType=ContentType.APPLICATION_JSON.getMimeType();
	    return doJsonBodyPost(url, contentType,null,paramMap);
	}
	/**
	 * 执行一个HTTP POST请求，返回请求响应的数据
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null,参数为form表单格式
	 * @return 返回请求响应的HTML
	 */
	public String doJsonBodyPost(String url, String contentType,Map<String,Object> paramMap) {
	    return doJsonBodyPost(url, contentType,null,paramMap);
	}
	/**
	 * 执行一个HTTP POST请求，返回请求响应的数据
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null,参数为form表单格式
	 * @return 返回请求响应的HTML
	 */
	public String doJsonBodyPost(String url, String contentType,Map<String,String> requestHeaderMap,Map<String,Object> paramMap) {
	    return doJsonBodyPost(url, contentType,requestHeaderMap, JSON.toJSONString(paramMap));
	}
	/**
	 * 执行一个HTTP POST请求，返回请求响应的数据
	 * 
	 * @param url 请求的URL地址
	 * @param params 请求的查询参数,可以为null,参数为form表单格式
	 * @return 返回请求响应的HTML
	 */
	public String doJsonBodyPost(String url, String contentType,Map<String,String> requestHeaderMap,String reqData) {
	    if(LOGGER.isInfoEnabled()){
	        LOGGER.info("#####################################################");
	        LOGGER.info("doJsonBodyPost请求url:{}",url);
	        LOGGER.info("doJsonBodyPost请求参数contentType:{}",contentType);
	        LOGGER.info("doJsonBodyPost请求参数reqData:{}",reqData);
	        LOGGER.info("#####################################################");
	    }
		String response = null;
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,DEFAULT_CHARSET);
		if(StringUtils.isBlank(contentType)){
		    contentType="application/json;charset="+DEFAULT_CHARSET;
		}
		
		postMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, contentType);
		if(MapUtils.isNotEmpty(requestHeaderMap)){
		    Set<Entry<String,String>> entrySet = requestHeaderMap.entrySet();
		    for (Entry<String, String> entry : entrySet) {
		        String key = entry.getKey();
		        String value = entry.getValue();
		        if(!StringUtils.isAnyBlank(key,value)){
		            postMethod.setRequestHeader(key, value);
		        }
		    }
		}
		
		try {
            postMethod.setRequestEntity(new StringRequestEntity(reqData,contentType, DEFAULT_CHARSET));
			client.executeMethod(postMethod);
			int statusCode = postMethod.getStatusCode() ;
			LOGGER.warn("请求{}==>响应码statusCode:{}",url,statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				InputStream responseStream = postMethod.getResponseBodyAsStream();
				response = getStreamAsString(responseStream,contentType,DEFAULT_CHARSET);
				//response = method.getResponseBodyAsString();
				Header[] header=postMethod.getResponseHeaders(CommHttpConstants.HEADER_COOKIE_KEY);
				if(ArrayUtils.isNotEmpty(header)){
				    //header[0].getValue()
				}					
			} else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_500);
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_404);
			} else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_302);
			} else {
				throw new HttpServiceException(AbsErrorCodeConstant.FAILURE);
			}
		} catch (HttpServiceException e) {
            LOGGER.error("请求HttpServiceException异常:",e);
            throw e;
        } catch (NoHttpResponseException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91000, e);  
        } catch (ConnectException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91001, e);      
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91002, e);      
        } catch (UnknownHostException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91003, e);          
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            LOGGER.error("Http响应数据体信息异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004,e);
        }catch (SSLException e) {
            // Https协议解析失败
            LOGGER.error("Https协议解析失败！",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91008,e);
        }  catch (IOException e) {
            // 发生网络异常
            LOGGER.error("网络请求异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91005,e);
        } catch (Exception e) {
			LOGGER.error("doJsonBodyPost请求其他异常",e);			
			throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_DEFAULT,e.getClass().getName()+e.getMessage());
		} finally {
		    if(LOGGER.isInfoEnabled()){
		        LOGGER.info("#####################################################");
		        LOGGER.info("doJsonBodyPost请求url:{}",url);
		        LOGGER.info("doJsonBodyPost响应数据reqData:{}",response);
		        LOGGER.info("#####################################################");
		    }
	        if (postMethod != null) {
                postMethod.releaseConnection();
            } 
		}
		return response;
	}
	
	/**
	 * 发送post请求工具方法
	 * 格式为:form表单post提交格式
	 */
	@Deprecated
	public String doFormPostNew(String url, String contentType,Map<String,Object> paramMap,boolean needEncode) {
	    return doFormPostNew(url, contentType,null,paramMap,needEncode);
	}
	/**
	 * 发送post请求工具方法
     * 格式为:form表单post提交格式
	 */
	@Deprecated
	public String doFormPostNew(String url, String contentType,Map<String,String> requestHeaderMap,Map<String,Object> paramMap,boolean needEncode) {
	    return doFormPostNew(url, contentType,requestHeaderMap,buildQueryString(paramMap, needEncode, DEFAULT_CHARSET));
	}
	private String doFormPostNew(String url, String contentType,Map<String,String> requestHeaderMap,String reqData) {
	    if(LOGGER.isInfoEnabled()){
	        LOGGER.info("#####################################################");
	        LOGGER.info("doFormPostNew请求url:{}",url);
	        LOGGER.info("doFormPostNew请求参数contentType:{}",contentType);
	        LOGGER.info("doFormPostNew请求参数reqData:{}",reqData);
	        LOGGER.info("#####################################################");
	    }
	    String response = null;
	    PostMethod postMethod = new PostMethod(url);
	    postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,DEFAULT_CHARSET);
	    if(StringUtils.isBlank(contentType)){
	        contentType="application/x-www-form-urlencoded;charset="+DEFAULT_CHARSET;
	    }
	    postMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, contentType);
	    if(MapUtils.isNotEmpty(requestHeaderMap)){
            Set<Entry<String,String>> entrySet = requestHeaderMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(!StringUtils.isAnyBlank(key,value)){
                    postMethod.setRequestHeader(key, value);
                }
            }
        }
	    
	    try {
	        postMethod.setRequestEntity(new StringRequestEntity(reqData,contentType, DEFAULT_CHARSET));
	        client.executeMethod(postMethod);
	        int statusCode = postMethod.getStatusCode() ;
	        if (statusCode == HttpStatus.SC_OK) {
	            //InputStream responseStream = postMethod.getResponseBodyAsStream();
	            //response = getStreamAsString(responseStream,contentType,DEFAULT_CHARSET);
	            response = postMethod.getResponseBodyAsString();
	            Header[] header=postMethod.getResponseHeaders(CommHttpConstants.HEADER_COOKIE_KEY);
	            if(ArrayUtils.isNotEmpty(header)){
	                //header[0].getValue()
	            }					
	        } else if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
	            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_500);
	        } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
	            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_404);
	        } else if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_302);
	        } else {
	            throw new HttpServiceException(AbsErrorCodeConstant.FAILURE);
	        }
	    } catch (HttpServiceException e) {
            LOGGER.error("请求HttpServiceException异常:",e);
            throw e;
        } catch (NoHttpResponseException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91000, e);  
        } catch (ConnectException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91001, e);      
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91002, e);      
        } catch (UnknownHostException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91003, e);          
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            LOGGER.error("Http响应数据体信息异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004,e);
        } catch (IOException e) {
            // 发生网络异常
            LOGGER.error("网络请求异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91005,e);
        } catch (Exception e) {
	        LOGGER.error("doFormPostNew请求其他异常",e);			
	        throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_DEFAULT,e.getClass().getName()+e.getMessage());
	    } finally {
	        if(LOGGER.isInfoEnabled()){
	            LOGGER.info("#####################################################");
	            LOGGER.info("doFormPostNew请求url:{}",url);
	            LOGGER.info("doFormPostNew响应数据reqData:{}",response);
	            LOGGER.info("#####################################################");
	        }
	        if (postMethod != null) {
	            postMethod.releaseConnection();
	        } 
	    }
	    return response;
	}
	
	/**
	 * 发送post请求工具方法
	 * 格式为:form表单post提交格式
	 */
	public String doFormPost(String url, String contentType,Map<String,Object> paramMap,boolean needEncode) {
	    return doFormPost(url, contentType, null, paramMap, needEncode);
	}
	
	/**
     * 发送post请求工具方法
     * 格式为:form表单post提交格式
     */
    public String doFormPost(String url, String contentType,Map<String,String> requestHeaderMap,Map<String,Object> paramMap,boolean needEncode) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("#####################################################");
            LOGGER.info("doFormPost请求url:{}",url);
            LOGGER.info("doFormPost请求参数contentType:{}",contentType);
            LOGGER.info("doFormPost请求参数paramMap:{}",JSON.toJSONString(paramMap,true));
            LOGGER.info("#####################################################");
        }
        if(StringUtils.isBlank(contentType)){
            contentType="application/x-www-form-urlencoded;charset="+DEFAULT_CHARSET;
        }
        String charset = DEFAULT_CHARSET;
        String response = "";
        org.apache.commons.httpclient.NameValuePair[] pairs = null;
        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        if (MapUtils.isNotEmpty(paramMap)) {
            pairs = new org.apache.commons.httpclient.NameValuePair[paramMap.size()];
            Set<String> set = paramMap.keySet();
            Iterator<String> it = set.iterator();
            int i = 0;
            while (it.hasNext()) {
                Object obj = it.next();
                String key = obj==null?"":obj.toString();
                String value = paramMap.get(key)==null?"":paramMap.get(key).toString();
                if(needEncode&&StringUtils.isNotBlank(value)){
                    value=encode(value);
                }
                if (!StringUtils.isAnyBlank(key, value)) {
                    pairs[i] = new org.apache.commons.httpclient.NameValuePair(key, value);
                    i++;
                }
            }
            postMethod.setRequestBody(ArrayUtils.subarray(pairs, 0, i));
        }
        try {
            postMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, contentType);
            if(MapUtils.isNotEmpty(requestHeaderMap)){
                Set<Entry<String,String>> entrySet = requestHeaderMap.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if(!StringUtils.isAnyBlank(key,value)){
                        postMethod.setRequestHeader(key, value);
                    }
                }
            }
            
            int statusCode = client.executeMethod(postMethod);
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.error("响应码当前不为200: "+ postMethod.getStatusLine());
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004);
            } else {
                response = postMethod.getResponseBodyAsString();
            }
        } catch (HttpServiceException e) {
            LOGGER.error("请求HttpServiceException异常:",e);
            throw e;
        } catch (NoHttpResponseException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91000, e);  
        } catch (ConnectException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91001, e);      
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91002, e);      
        } catch (UnknownHostException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91003, e);          
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            LOGGER.error("Http响应数据体信息异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004,e);
        } catch (IOException e) {
            // 发生网络异常
            LOGGER.error("网络请求异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91005,e);
        } catch (Exception e) {
            LOGGER.error("doHttpsPostOrGet请求其他异常",e);         
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_DEFAULT,e.getClass().getName()+e.getMessage());
        } finally {
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("#####################################################");
                LOGGER.info("doFormPost请求url:{}",url);
                LOGGER.info("doFormPost响应数据:{}",response);
                LOGGER.info("#####################################################");
            }
            // 释放连接
            if (postMethod != null) {
                postMethod.releaseConnection();
            } 
        }
        return response;
    }
    
    /**
     * 发送HttpGet方法请求
     */
    public String doFormGet(String url,Map<String,Object> paramMap) {
        Map<String, String> resultMap = doFormGet(url, null,paramMap,Boolean.TRUE);
        return resultMap.get("response");
    }
    
    /**
     * 发送HttpGet方法请求
     */
    public Map<String, String> doFormGet(String url,Map<String,Object> paramMap,boolean needEncode) {
        return doFormGet(url, null,paramMap,needEncode);
    }
    
    /**
     * 发送HttpGet方法请求
     */
    public Map<String, String> doFormGet(String url,Map<String,String> requestHeaderMap,Map<String,Object> paramMap,boolean needEncode) {
        String queryString = buildQueryString(paramMap,needEncode,DEFAULT_CHARSET);
        return doFormGet(url, requestHeaderMap,queryString);
    }
    
    /**
     * 发送HttpGet方法请求
     */
    public Map<String, String> doFormGet(String url,Map<String,String> requestHeaderMap,String queryString) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("#####################################################");
            LOGGER.info("doFormGet请求url:{}",url);
            LOGGER.info("doFormGet请求参数queryString:{}",queryString);
            LOGGER.info("#####################################################");
        }
        // 设置编码格式
        String encoding = DEFAULT_CHARSET;
        if(StringUtils.isNotBlank(queryString)){
            if(queryString.indexOf("?")>0){
                if(url.indexOf("?")>0){
                    url = url + "&"+queryString.substring(1);
                }else {
                    url = url + queryString;
                }
            }else{
                if(url.indexOf("?")>0){
                    url = url + "&" + queryString;
                }else{
                    url = url + "?" + queryString;
                }
            }
        }
        LOGGER.info("doFormGet请求全路径url为:{} ", url);
        String response = "";
        GetMethod getMethod = null;
        int statusCode = -1;
        try {
            URI uri = new URI(url, false, encoding);
            getMethod = new GetMethod(uri.toString());
            String contentType="application/x-www-form-urlencoded;charset="+encoding;
            getMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
            getMethod.setRequestHeader(HttpHeaders.CONTENT_TYPE, contentType);
            if(MapUtils.isNotEmpty(requestHeaderMap)){
                Set<Entry<String,String>> entrySet = requestHeaderMap.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if(!StringUtils.isAnyBlank(key,value)){
                        getMethod.setRequestHeader(key, value);
                    }
                }
            }
            
            statusCode = client.executeMethod(getMethod);
            LOGGER.info("doFormGet响应码为:{} ", statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.error("响应码当前不为200: "+ getMethod.getStatusLine());
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004);
            } else {
                response = getMethod.getResponseBodyAsString();
            }
        } catch (HttpServiceException e) {
            LOGGER.error("请求HttpServiceException异常:",e);
            throw e;
        } catch (NoHttpResponseException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91000, e);  
        } catch (ConnectException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91001, e);      
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91002, e);      
        } catch (UnknownHostException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91003, e);          
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            LOGGER.error("Http响应数据体信息异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004,e);
        } catch (IOException e) {
            // 发生网络异常
            LOGGER.error("网络请求异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91005,e);
        } catch (Exception e) {
            LOGGER.error("doHttpsPostOrGet请求其他异常",e);         
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_DEFAULT,e.getClass().getName()+e.getMessage());
        } finally {         
            if (getMethod != null) {
                getMethod.releaseConnection();
            }           
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("statusCode", String.valueOf(statusCode));
        map.put("response", response);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("#####################################################");
            LOGGER.info("doFormGet请求url:{}",url);
            LOGGER.info("doFormGet响应数据:{}",JSON.toJSONString(map,true));
            LOGGER.info("#####################################################");
        }
        return map;
    }
    
    /**
     * HttpPost方式GET请求服务器(https协议)
     */
    public String doHttpsGet(String url,String contentType,Map<String,Object> paramMap,boolean needEncode) {
        String queryString = buildQueryString(paramMap,needEncode,DEFAULT_CHARSET);
        return doHttpsGet(url,contentType,queryString);
    }
    /**
     * HttpPost方式GET请求服务器(https协议)
     */
    public String doHttpsGet(String url,String contentType,String queryString) {
        return doHttpsPostOrGet(url,GET,contentType, null, queryString);
    }
    
    /**
     * HttpPost方式POST请求服务器(https协议)
     * Form表单格式请求数据体
     */
    @Deprecated
    public String doHttpsQueryBodyPost(String url,String contentType,Map<String,Object> paramMap) {
        String content = buildQueryString(paramMap,false,DEFAULT_CHARSET);
        return doHttpsQueryBodyPost(url,contentType, content);
    }
    /**
     * HttpPost方式POST请求服务器(https协议)
     * Form表单格式请求数据体
     */
    @Deprecated
    public String doHttpsQueryBodyPost(String url,String contentType,String content) {
        return doHttpsPostOrGet(url,POST,contentType, null, content);
    }
    
    /**
     * HttpPost方式POST请求服务器(https协议)
     * JSON格式请求数据体
     * 调用内部系统接口使用
     */
    public String doHttpsJosnBodyPost(String url,Map<String,Object> paramMap) {
        String contentType=ContentType.APPLICATION_JSON.getMimeType();
        return doHttpsJosnBodyPost(url,contentType,null,paramMap);
    }
    /**
     * HttpPost方式POST请求服务器(https协议)
     * JSON格式请求数据体
     */
    public String doHttpsJosnBodyPost(String url,String contentType,Map<String,String> requestHeaderMap,Map<String,Object> paramMap) {
        if(StringUtils.isBlank(contentType)){
            contentType=ContentType.APPLICATION_JSON.getMimeType();
        }
        return doHttpsJosnBodyPost(url,contentType,requestHeaderMap,JSON.toJSONString(paramMap));
    }
    /**
     * HttpPost方式POST请求服务器(https协议)
     * JSON格式请求数据体
     */
    public String doHttpsJosnBodyPost(String url,String contentType,Map<String,String> requestHeaderMap,String content) {
        return doHttpsPostOrGet(url,POST,contentType, requestHeaderMap, content);
    }
    
	/**
	 * HttpPost方式请求服务器(https协议)
	 */
    private String doHttpsPostOrGet(String url,String method,String contentType,Map<String,String> requestHeaderMap,String content) {
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("#####################################################");
            LOGGER.info("doHttpsPostOrGet请求url:{}",url);
            LOGGER.info("doHttpsPostOrGet请求参数contentType:{}",contentType);
            LOGGER.info("doHttpsPostOrGet请求参数content:{}",content);
            LOGGER.info("#####################################################");
        }
        if(StringUtils.isBlank(method)){
            method=POST;
        }
        String response = null;
        HttpURLConnection conn = null;
        try {
            conn=getHttpURLConnection(url, method, contentType,requestHeaderMap);
            conn.connect();
            OutputStream out = conn.getOutputStream();
            if(StringUtils.isNotBlank(content)){
                out.write(content.getBytes(DEFAULT_CHARSET));
            }
            out.flush();
            out.close();
            int respCode = conn.getResponseCode();
            if (respCode == HttpStatus.SC_OK) {
                InputStream inputStream = conn.getInputStream();
                response = getStreamAsString(inputStream,contentType,DEFAULT_CHARSET);
            }else if (respCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_500);
            } else if (respCode == HttpStatus.SC_NOT_FOUND) {
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_404);
            } else if (respCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_302);
            } else {
                throw new HttpServiceException(AbsErrorCodeConstant.FAILURE);
            }
        } catch (HttpServiceException e) {
            LOGGER.error("请求HttpServiceException异常:",e);
            throw e;
        } catch (NoHttpResponseException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91000, e);  
        } catch (ConnectException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91001, e);      
        } catch (ConnectTimeoutException | SocketTimeoutException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91002, e);      
        } catch (UnknownHostException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91003, e);          
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            LOGGER.error("Http响应数据体信息异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004,e);
        } catch (IOException e) {
            // 发生网络异常
            LOGGER.error("网络请求异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91005,e);
        } catch (Exception e) {
            LOGGER.error("doHttpsPostOrGet请求其他异常",e);         
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_DEFAULT,e.getClass().getName()+e.getMessage());
        } finally {
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("#####################################################");
                LOGGER.info("doHttpsPostOrGet请求url:{}",url);
                LOGGER.info("doHttpsPostOrGet响应数据:{}",response);
                LOGGER.info("#####################################################");
            }
            if (conn != null) {
                conn.disconnect();
            } 
        }
        return response;
    }
    
    /**
     * 支持HTTPS/HTTP-POST方式的form数据体表单数据请求
     */
    public String doAllGet(String urlStr,Map<String, Object> paramMap) {
        return sendAllRequest(urlStr, paramMap, HttpGet.METHOD_NAME,null);
    }
    /**
     * 支持HTTPS/HTTP-POST方式的form数据体表单数据请求
     */
    public String doAllPost(String urlStr,Map<String, Object> paramMap) {
        return sendAllRequest(urlStr, paramMap, HttpPost.METHOD_NAME,null);
    }
    /**
     * 支持HTTPS/HTTP-POST/GET方式的form/json数据体表单数据请求
     */
    private String sendAllRequest(String urlStr,Map<String, Object> paramMap,String method,ContentType contentType) {
        return sendAllRequest(urlStr, null, paramMap, method,contentType);
    }
    /**
     * FIXME 暂未优化
     * 支持HTTPS/HTTP-POST/GET方式的form/json数据体表单数据请求
     */
    @Deprecated
    private String sendAllRequest(String urlStr,Map<String,String> requestHeaderMap,Map<String, Object> paramMap,String method,ContentType contentType) {
        if(contentType==null){
            contentType=ContentType.APPLICATION_JSON;
        }
        final RequestBuilder builder;
        switch (method) {
        case HttpPost.METHOD_NAME:
            builder = RequestBuilder.post();
            if(ContentType.APPLICATION_JSON.equals(contentType)){
                String reqData=JSON.toJSONString(paramMap);
                builder.setEntity(new StringEntity(reqData, ContentType.APPLICATION_JSON));
            }else{
                if(MapUtils.isNotEmpty(paramMap)){
                    final Set<String> keySet = paramMap.keySet();
                    for (String key : keySet) {
                        Object object = paramMap.get(key);
                        builder.addParameter(key, object==null?"":object.toString());
                    }
                }
            }
            break;
        case HttpGet.METHOD_NAME:
        default:
            builder = RequestBuilder.get();
            if(MapUtils.isNotEmpty(paramMap)){
                final Set<String> keySet = paramMap.keySet();
                for (String key : keySet) {
                    Object object = paramMap.get(key);
                    builder.addParameter(key, object==null?"":object.toString());
                }
            }
            break;
        }
        //头信息
        if(MapUtils.isNotEmpty(requestHeaderMap)){
            Set<Entry<String,String>> entrySet = requestHeaderMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(!StringUtils.isAnyBlank(key,value)){
                    builder.setHeader(key, value);
                }
            }
        }
        
        HttpUriRequest request = builder.setUri(urlStr).build();
        request.setHeader(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
        
        CloseableHttpClient client = getCloseableHttpClient(urlStr);
        CloseableHttpResponse httpResponse=null;
        try {
            httpResponse = client.execute(request);
            if(httpResponse==null){
                LOGGER.error("请求返回结果为空!");
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91009);  
            }
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity, DEFAULT_CHARSET);
            return content;
        }catch (HttpServiceException e) {
            LOGGER.error("请求HttpServiceException异常:",e);
            throw e;
        } catch (NoHttpResponseException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91000, e);  
        } catch (ConnectException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91001, e);      
        } catch (ConnectTimeoutException |SocketTimeoutException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91002, e);      
        } catch (UnknownHostException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91003, e);          
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            LOGGER.error("Http响应数据体信息异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004,e);
        } catch (IOException e) {
            // 发生网络异常
            LOGGER.error("网络请求异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91005,e);
        } catch (Exception e) {
            LOGGER.error("sendAllRequest请求其他异常",e);         
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_DEFAULT,e.getClass().getName()+e.getMessage());
        }finally{
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                } 
            } catch (Exception e) {
                LOGGER.error("sendAllRequest请求关闭异常",e);         
            }
        }
    }
    
    /**
     * 当前推荐使用
     * 支持HTTPS/HTTP-GET方式的返回json数据体的请求
     */
    public String sendGet(String urlStr) {
        return sendAllRequest(urlStr,RequestMethod.GET, null, null,null);
    }
    
    /**
     * 当前推荐使用
     * 支持HTTPS/HTTP-GET方式的form数据体表单数据请求
     */
    public String sendGet(String url,Map<String, Object> paramMap) {
        String queryString = buildQueryString(paramMap, true);
        if(StringUtils.isNotBlank(queryString)){
            if(queryString.indexOf("?")>0){
                if(url.indexOf("?")>0){
                    url = url + "&"+queryString.substring(1);
                }else {
                    url = url + queryString;
                }
            }else{
                if(url.indexOf("?")>0){
                    url = url + "&" + queryString;
                }else{
                    url = url + "?" + queryString;
                }
            }
        }
        LOGGER.info("sendAllGet请求全路径url为:{} ", url);
        return sendGet(url);
    }
    
    /**
     * 当前推荐使用
     * 支持HTTPS/HTTP-POST方式的json数据体请求
     */
    public String sendPostJosnBody(String urlStr,Map<String, Object> paramMap) {
        return sendPostJosnBody(urlStr, JSON.toJSONString(paramMap));
    }
    
    /**
     * 当前推荐使用
     * 支持HTTPS/HTTP-POST方式的json数据体请求
     */
    public String sendPostJosnBody(String urlStr,String reqJsonData) {
        return sendAllRequest(urlStr,RequestMethod.POST, null, null,reqJsonData);
    }
    
    /**
     * 当前推荐使用
     * 支持HTTPS/HTTP-POST方式的json数据体请求
     */
    private String sendAllRequest(String urlStr,RequestMethod method,ContentType contentType,Map<String,String> requestHeaderMap,String reqJsonData) {
        HttpRequestBase httpRequest=null;
        if(contentType==null){
            contentType=ContentType.APPLICATION_JSON;
        }
        switch (method) {
        case POST:
            if(StringUtils.isBlank(reqJsonData)){
                reqJsonData=JSON.toJSONString(MapUtils.EMPTY_SORTED_MAP);
            }
            httpRequest = new HttpPost(urlStr);
            ((HttpPost)httpRequest).setEntity(new StringEntity(reqJsonData, contentType));
            break;
        case GET:
            httpRequest = new HttpGet(urlStr);
            break;
        default:
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_99005);
        }
        
        
        //头信息
        if(MapUtils.isNotEmpty(requestHeaderMap)){
            Set<Entry<String,String>> entrySet = requestHeaderMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(!StringUtils.isAnyBlank(key,value)){
                    httpRequest.setHeader(key, value);
                }
            }
        }
        httpRequest.setHeader(HttpHeaders.CONTENT_TYPE, contentType.getMimeType());
        //httpRequest.setHeader(HttpHeaders.CONTENT_LENGTH,String.valueOf(reqJsonData.length()));
        Protocol protocol;
        if (isHttps(urlStr)) {
            protocol=Protocol.HTTPS;
            LOGGER.info("執行HTTPS请求...");
        }else{
            protocol=Protocol.HTTP;
            LOGGER.info("執行HTTP请求...");
        }
        
        CloseableHttpResponse httpResponse=null;
        try {
            httpResponse = this.getCloseableHttpClient(protocol).execute(httpRequest);
            if(httpResponse==null){
                LOGGER.error("请求返回结果为空!");
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91009);  
            }
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity, DEFAULT_CHARSET);
            if(LOGGER.isDebugEnabled()){
                LOGGER.debug("執行HTTP/HTTPS[{}]请求[{}]结果为:{}",method,urlStr,content);
            }
            return content;
        }catch (HttpServiceException e) {
            LOGGER.error("请求HttpServiceException异常:",e);
            throw e;
        } catch (NoHttpResponseException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91000, e);  
        } catch (ConnectException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91001, e);      
        } catch (ConnectTimeoutException |SocketTimeoutException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91002, e);      
        } catch (UnknownHostException e) {
            LOGGER.error("请求异常:",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91003, e);          
        } catch (HttpException e) {
            // 发生致命的异常，可能是协议不对或者返回的内容有问题
            LOGGER.error("Http响应数据体信息异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91004,e);
        } catch (IOException e) {
            // 发生网络异常
            LOGGER.error("网络请求异常",e);
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91005,e);
        } catch (Exception e) {
            LOGGER.error("sendAllGet请求其他异常",e);         
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_DEFAULT,e.getClass().getName()+e.getMessage());
        }finally{
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                } 
            } catch (Exception e) {
                LOGGER.error("sendAllGet请求关闭异常",e);         
            }
        }
    }
    
    /**
     * CloseableHttpClient相关参数设置 
     */
    protected CloseableHttpClient getCloseableHttpClient(String urlStr) {
        CloseableHttpClient closeableHttpClient=null;
        if (isHttps(urlStr)) {
            closeableHttpClient = this.getCloseableHttpClient(Protocol.HTTPS);
        }else{
            closeableHttpClient = this.getCloseableHttpClient(Protocol.HTTP);
        }
        
        if (closeableHttpClient == null) {
            final RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(CONNNECT_TIMEOUT)
                    .setSocketTimeout(CONNNECT_TIMEOUT)
                    .setConnectTimeout(CONNNECT_TIMEOUT)
                    .build();
            
            if (isHttps(urlStr)) {
                SSLContext ctx = null;
                TrustManager[] allTrustCerts = new TrustManager[] { new TrustAnyTrustManager() };
                try {
                    ctx = SSLContext.getInstance("SSL");
                    // ctx.init(null, allTrustCerts, null);
                    // ctx = SSLContext.getInstance("TLS");
                    ctx.init(new KeyManager[0], allTrustCerts, new SecureRandom());
                } catch (Exception e) {
                    throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91006, e);
                }
                SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(ctx,
                        SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                closeableHttpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                        .setSSLSocketFactory(sslConnectionSocketFactory)
                        // .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                        .build();
            } else {
                closeableHttpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();
            }
            synchronized (syncLock) {
                if (isHttps(urlStr)) {
                    this.addCloseableHttpClient(Protocol.HTTPS,closeableHttpClient);
                }else{
                    this.addCloseableHttpClient(Protocol.HTTP,closeableHttpClient);
                }
            }
        }
        return closeableHttpClient;
    }
    
    @Deprecated
    protected static HttpURLConnection getHttpURLConnection(String urlStr, String method,String contentType,Map<String,String> requestHeaderMap) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = null;
        if (isHttps(urlStr)||Protocol.HTTPS.getKey().equalsIgnoreCase(url.getProtocol())) {
            SSLContext ctx = null;
            TrustManager[] allTrustCerts =new TrustManager[] { new TrustAnyTrustManager() };
            try {
                ctx = SSLContext.getInstance("SSL");
                //ctx.init(null, allTrustCerts, null);
                //ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], allTrustCerts,new SecureRandom());
            } catch (Exception e) {
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91006,e);
            }
            
            HttpsURLConnection connHttps = (HttpsURLConnection) url.openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn = connHttps;
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }
        
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod(method);
        conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType);
//        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.setRequestProperty("User-Agent", "aop-sdk-java");
        conn.setRequestProperty("Content-Type", contentType);
        if(MapUtils.isNotEmpty(requestHeaderMap)){
            Set<Entry<String,String>> entrySet = requestHeaderMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = entry.getKey();
                String value = entry.getValue();
                if(!StringUtils.isAnyBlank(key,value)){
                    conn.setRequestProperty(key, value);
                }
            }
        }
        return conn;
    }

	/**
	 * 
	 * 类描述...实现X509TrustManager，管理证书
	 */
    public static class TrustAnyTrustManager implements TrustManager,X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            return;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        
    }

	/**
	 * false:进行证书校验。
	 * true:主机名是可接受的,不校验
	 */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier, X509HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("当前获取到的hostname为:{}",hostname);
            }
            return true;
        }

        @Override
        public void verify(String host, SSLSocket ssl) throws IOException {
            return;
        }

        @Override
        public void verify(String host, X509Certificate cert) throws SSLException {
            return;
        }

        @Override
        public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            return;
        }
    }
    
    public static boolean isHttps(String url) {
        return url.toLowerCase().startsWith(Protocol.HTTPS.getKey());
    }
	
	    /**
	     * 将输入流转换为String 
	     * @param defaultCharset *
	     */
	    private static String getStreamAsString(InputStream stream, String contentType, String charset) throws IOException {
	        if(StringUtils.isBlank(charset)){
	            charset=getResponseCharset(contentType);
	        }
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
	            StringWriter writer = new StringWriter();
	            char[] chars = new char[1024];
	            int count = 0;
	            while ((count = reader.read(chars)) > 0) {
	                writer.write(chars, 0, count);
	            }
	            return writer.toString();
	        } finally {
	            if (stream != null) {
	                stream.close();
	            }
	        }
	    }

	    private static String getResponseCharset(String contentType) {
	        String charset = DEFAULT_CHARSET;
	        if (!StringUtils.isBlank(contentType)) {
	            String[] params = contentType.split(";");
	            for (String param : params) {
	                param = param.trim();
	                if (param.startsWith("charset")) {
	                    String[] pair = param.split("=", 2);
	                    if (pair.length == 2) {
	                        if (!StringUtils.isBlank(pair[1])) {
	                            charset = pair[1].trim();
	                        }
	                    }
	                    break;
	                }
	            }
	        }
	        if(LOGGER.isInfoEnabled()){
	            LOGGER.info("getResponseCharset响应数据:{}",charset);
	        }
	        return charset;
	    }
	
	/**
	 * 把数组所有元素ascii排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 * @param params 需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String buildQueryString(Map<String, Object> params, boolean needEncode) {
	    return buildQueryString(params, needEncode, DEFAULT_CHARSET);
	}
	    
	    /**
	     * 把数组所有元素ascii排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	     * @param params 需要排序并参与字符拼接的参数组
	     * @return 拼接后字符串
	     */
    public static String buildQueryString(Map<String, Object> params, boolean needEncode, String charset) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("buildQueryString请求数据:\n{}", JSON.toJSONString(params, true));
        }
        if (MapUtils.isEmpty(params)) {
            return "";
        }
        if (StringUtils.isBlank(charset)) {
            charset = DEFAULT_CHARSET;
        }
        List<String> keys = new ArrayList<String>(params.keySet());
        // 排序
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key) == null ? "" : params.get(key).toString();
            if (needEncode && StringUtils.isNotBlank(value)) {
                value = encode(value);
            }
            if (!StringUtils.isAnyBlank(key, value)) {
                if (i == keys.size() - 1) {
                    // 拼接时，不包括最后一个&字符
                    sb.append(key).append("=").append(value);
                } else {
                    sb.append(key).append("=").append(value).append("&");
                }
            }
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("buildQueryString结果数据:{}", sb.toString());
        }
        return sb.toString();
    }
	
	 /**
     * 使用默认的UTF-8字符集反编码请求参数值。
     * 
     * @param value 参数值
     * @return 反编码后的参数值
     */
    public static String decode(String value) {
        return decode(value, DEFAULT_CHARSET);
    }

    /**
     * 使用默认的UTF-8字符集编码请求参数值。
     * 
     * @param value 参数值
     * @return 编码后的参数值
     */
    public static String encode(String value) {
        return encode(value, DEFAULT_CHARSET);
    } 
    /**
     * 使用指定的字符集反编码请求参数值。
     * 
     * @param value 参数值
     * @param charset 字符集
     * @return 反编码后的参数值
     */
    public static String decode(String value, String charset) {
        if(StringUtils.isBlank(charset)){
            charset=DEFAULT_CHARSET;
        }
        String result = null;
        if (StringUtils.isNotBlank(value)) {
            try {
                result = URLDecoder.decode(value, charset);
            } catch (IOException e) {
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91007,e);
            }
        }
        return result;
    }

    /**
     * 使用指定的字符集编码请求参数值。
     * 
     * @param value 参数值
     * @param charset 字符集
     * @return 编码后的参数值
     */
    public static String encode(String value, String charset) {
        if(StringUtils.isBlank(charset)){
            charset=DEFAULT_CHARSET;
        }
        String result = null;
        if (StringUtils.isNotBlank(value)) {
            try {
                result = URLEncoder.encode(value, charset);
            } catch (IOException e) {
                throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91007,e);
            }
        }
        return result;
    }
	
	public static Map<String, String> getQueryStringFromUrl(String paramUrl) {
        LOGGER.info("getQueryStringFromUrl结果数据:{}",paramUrl);
        Map<String, String> resultMap = null;
        if (StringUtils.isNotBlank(paramUrl) && paramUrl.indexOf('?') != -1) {
            resultMap = splitUrlQuery(paramUrl.substring(paramUrl.indexOf('?') + 1));
        }
        if (resultMap == null) {
            resultMap = new HashMap<String, String>();
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("getQueryStringFromUrl请求数据:{}",JSON.toJSONString(resultMap,true));
        }
        return resultMap;
    }
    
    /**
     * 从URL中提取所有的参数。
     */
    private static Map<String, String> splitUrlQuery(String queryStr) {
        if (StringUtils.isNotBlank(queryStr)) {
            return null;
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        String[] pairs = queryStr.split("&");
        if (ArrayUtils.isNotEmpty(pairs)) {
            for (String pair : pairs) {
                String[] param = pair.split("=", 2);
                if (ArrayUtils.isNotEmpty(param) && param.length == 2) {
                    resultMap.put(param[0], param[1]);
                }
            }
        }
        return resultMap;
    }
    
    public static class IdleConnectionEvictor extends Thread {
        private final HttpClientConnectionManager connMgr;
        private volatile boolean shutdown;

        public IdleConnectionEvictor(HttpClientConnectionManager connMgr) {
            this.connMgr = connMgr;
            // 启动线程
            LOGGER.info("IdleConnectionEvictor构造方法执行了");
            this.start();
        }

        @Override
        public void run() {
            try {
                while (!shutdown) {
                    synchronized (this) {
                        wait(5000*3);
                        if(LOGGER.isDebugEnabled()){
                            LOGGER.debug("connMgr连接[{}],当前信息:{}",closeableHttpClientMap.size(),JSON.toJSONString(connMgr));
                            for ( Entry<Protocol, CloseableHttpClient> entrySet : closeableHttpClientMap.entrySet()) {
                                LOGGER.debug("当前信息:{}===>>{}",entrySet.getKey(),entrySet.getValue().toString());
                            }
                        }
                        connMgr.closeExpiredConnections();
                    }
                }
            } catch (InterruptedException ex) {
                // 结束
                LOGGER.info("run:刷新失效的连接异常!",ex);
            }
        }

        public void shutdown() {
            shutdown = true;
            LOGGER.info("shutdown:关闭失效的连接方法执行了");
            synchronized (this) {
                notifyAll();
            }
        }
    }

}

