package com.personal.springboot.user.controller.base;


import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.personal.springboot.common.cons.Constants;


public class HttpClientUtil {
	private static Logger log = Logger.getLogger(HttpClientUtil.class);

	/**
	 * 处理post请求
	 * @param path 请求的地址
	 * @param data 请求的数据
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpPostYhloanJson(String path,String data) throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();  
		String urll = Constants.bundle.getString(path);
		String IS_ESB = Constants.bundle.getString("IS_ESB");
		if (!"1".equals(IS_ESB)) {
			urll = Constants.bundle.getString("TOPSCFURL");
		}
		log.info("请求URL："+urll+"请求报文："+data);
	    HttpPost htPost = new HttpPost(urll);  
	    JSONObject jsonObject = null;  
	    try {  
	        htPost.setHeader("content-type", "application/json");  
	        String username = Constants.bundle.getString("uid");//PO  用户
			String password = Constants.bundle.getString("password");//PO 密码
			String encode = username + ":" + password;
			byte[] p = Base64.encodeBase64(encode.getBytes());
			String encodeHead = new String(p);
			System.out.println("encodeHead:"+encodeHead);
			htPost.setHeader("Authorization", "Basic " + encodeHead);
	        StringEntity reqEntity = new StringEntity(data,"UTF-8"); 
	        reqEntity.setContentType("application/json");
	        htPost.setEntity(reqEntity);  
	        HttpResponse httpResponse = client.execute(htPost);  
	        HttpEntity entity = httpResponse.getEntity();  
	        String backMes = EntityUtils.toString(entity, "UTF-8").toString();  
	        log.info("响应报文："+backMes);
	        try {
	        	jsonObject = JSONObject.parseObject(backMes);
			}catch (Exception ex){
				log.error("---------------------响应报文解析异常-----------------------");
				return null;
			}
	    } catch (ClientProtocolException e) {  
	    	log.error("---------------------接口请求失败：ClientProtocolException-----------------------");
	        e.printStackTrace();  
	        return null;
	    } catch (IOException e) {  
	    	log.error("---------------------接口请求失败：IOException-----------------------");
	        e.printStackTrace();  
	        return null;
	    } finally {  
	        try {  
	            client.close();  
	            client = null;  
	        } catch (IOException e) {  
	        	log.error("---------------------关闭连接失败-----------------------");
	            e.printStackTrace();  
	        }  
	    }  
	    return jsonObject;
	}
	
	/**
	 * 处理get请求
	 * @param url 请求的地址
	 * @return
	 * @throws Exception
	 */
	public static JSONObject httpGet(String url) throws Exception{

		JSONObject jsonObject = new JSONObject();
		org.apache.commons.httpclient.HttpClient httpClient = new HttpClient();	
		httpClient.getParams().setContentCharset("UTF-8");
		GetMethod getMethod = new GetMethod(url);
		int statusCode = httpClient.executeMethod(getMethod);
		String result = getMethod.getResponseBodyAsString();
//		System.out.println(result);
		if(statusCode==200){
			jsonObject = (JSONObject)JSON.parse(result);
		}else{
			throw new Exception("接口请求失败");
		}
		
		return jsonObject;
	}

	
	/**
	 * 处理post请求微信
	 * @param path 请求的地址
	 * @param data 请求的数据
	 * @return
	 * @throws Exception
	 */
	public static JSONObject WxHttpPostYhloanJson(String path,String data) throws Exception{
		CloseableHttpClient client = HttpClients.createDefault();  
		
		log.info("请求URL："+path+"请求报文："+data);
	    HttpPost htPost = new HttpPost(path);  
	    JSONObject jsonObject = null;  
	    try {  
	        htPost.setHeader("content-type", "application/json"); 
	        StringEntity reqEntity = new StringEntity(data,"utf-8");  
	        htPost.setEntity(reqEntity);  
	        HttpResponse httpResponse = client.execute(htPost);  
	        HttpEntity entity = httpResponse.getEntity();  
	        String backMes = EntityUtils.toString(entity, "UTF-8").toString();  
	        log.info("响应报文："+backMes);
	        try {
	        	jsonObject = JSONObject.parseObject(backMes);
			}catch (Exception ex){
				log.error("---------------------响应报文解析异常-----------------------");
				return null;
			}
	    } catch (ClientProtocolException e) {  
	    	log.error("---------------------接口请求失败：ClientProtocolException-----------------------");
	        e.printStackTrace();  
	        return null;
	    } catch (IOException e) {  
	    	log.error("---------------------接口请求失败：IOException-----------------------");
	        e.printStackTrace();  
	        return null;
	    } finally {  
	        try {  
	            client.close();  
	            client = null;  
	        } catch (IOException e) {  
	        	log.error("---------------------关闭连接失败-----------------------");
	            e.printStackTrace();  
	        }  
	    }  
	    return jsonObject;
	}
}
