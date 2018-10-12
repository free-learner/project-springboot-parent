package com.personal.springboot.gataway.conf;

import java.util.HashSet;
import java.util.Set;


/* {"REQUEST":{"HRT_ATTRS":{"App_Sub_ID":"2002","App_Token":"5b8fe283-9f43-4baf-a099-
15409e37be3c","Api_ID":"hrt.order.getOrder.testliuliu10jsonnew","Api_Version":"1.0","Time_Stamp":"20160825165613","Si
gn_Method":"md5","Sign":"5d3ff2c191942614b08ac558c17460a0","Format":"json","Partner_id":"S001","App_Pub_ID":"MOA"},"REQUEST_DATA":{"customer":{"address":"shanghai","id":2,"name":"liuliu"}}}}
*/
public class OpenApiMessageConstants {
	public static final String REQUEST="REQUEST";	
	public static final String BODY = "Body";
	public static final String HRT_ATTRS="HRT_ATTRS";	
	public static final String REQUEST_DATA="REQUEST_DATA";
	
	public static final String APP_SUB_ID="App_Sub_ID";
	public static final String APP_TOKEN="App_Token";
	public static final String API_ID="Api_ID";
	public static final String API_VERSION="Api_Version";
	public static final String TIME_STAMP="Time_Stamp";
	public static final String SIGN_METHOD="Sign_Method";	
	public static final String SIGN="Sign";	
	public static final String FORMAT="Format";	
	public static final String PARTNER_ID="Partner_ID";	
	public static final String SYS_ID="Sys_ID";	
	public static final String APP_PUB_ID="App_Pub_ID";	
	
	
	public static final String RESPONSE="RESPONSE";	
	public static final String RETURN_DATA="RETURN_DATA";	
	public static final String RETURN_STAMP="RETURN_STAMP";	
	public static final String RETURN_CODE="RETURN_CODE";	
	public static final String RETURN_DESC="RETURN_DESC";	
	
	
	
	public static Set<String > signMethodSet=new HashSet<String>();
	static{
		signMethodSet.add("md5");
		signMethodSet.add("rsa");
	}
	
	
	
}
