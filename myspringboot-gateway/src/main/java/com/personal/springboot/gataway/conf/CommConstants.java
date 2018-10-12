package com.personal.springboot.gataway.conf;

import java.util.HashSet;
import java.util.Set;

public class CommConstants {
	
	public static final String REQ_BEAN_KEY = "OPENAPI_REQUEST_BEAN";
	public static final String API_SERVICE_KEY = "service";
	
	public static final String HEADER_CONTENT_TYPE_KEY = "content-type";
	public static final String HEADER_COOKIE_KEY = "cookie";
	public static final String HEADER_HOST_KEY = "host";
	public static final String HEADER_X_REAL_IP_KEY = "X-Real-IP";
	
	
	
	
	public static  Set<String> respSet=new HashSet<String>();
	static {  
		respSet.add(HEADER_CONTENT_TYPE_KEY);
		respSet.add(HEADER_COOKIE_KEY);
		respSet.add(HEADER_HOST_KEY);		
	}  
	
	
	public static final String IN_KEY_SANDBOX = "sandbox";
	
	public final static String FLOW_TYPE_RS="FLOW_RS";
	public final static String FLOW_TYPE_WS="FLOW_WS";
	public final static String FLOW_TYPE_THIRD_PART="FLOW_THIRD_PART";
	
	public final static String FLOW_TYPE_RESP="FLOW_TYPE_RESP";

	
	
	
	
	
	public static final String CTRL_STR = "_";
	public static final String SYSTEM_ERROR_KEY="E";
	
	
	
	public static final String APIID_REDIS_KEY = ":fastapi:api_id";	//







	
}
