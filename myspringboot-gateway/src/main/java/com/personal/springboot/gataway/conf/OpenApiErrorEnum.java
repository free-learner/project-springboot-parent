package com.personal.springboot.gataway.conf;





public enum OpenApiErrorEnum {
	
	SYSTEM_SUCCESS("S0A00000", "success"),
	SYSTEM_BUSY("E0S00099", "server is other error,errorType[%s] ,errorInfo[%s]"),	
	
	
	S_01("E0S00001", "time out"),
	S_02("E0S00002", "Connection refused"),
	S_05("E0S00005", "500 Server Error"),
	S_04("E0S00004", "404 Not Found"),
	S_03("E0S00003", "Moved Permanently"),
	S_06("E0S00006", "UnknownHostException "),
	S_07("E0S00007", "%s "),
	S_22("E0S00022", "HttpStatus other"),
	
	
	
	
	
	
	S_RSA_01("E0SR0001", "RSA time out"),
	S_RSA_02("E0SR0002", "RSA Connection refused"),
	S_RSA_05("E0SR0005", "RSA 500 Server Error"),
	S_RSA_04("E0SR0004", "RSA 404 Not Found"),
	S_RSA_03("E0SR0003", "RSA Moved Permanently"),
	S_RSA_06("E0SR0006", "RSA UnknownHostException "),
	S_RSA_22("E0SR0022", "RSA HttpStatus other"),
	M_RSA_RESP_FAIL("E0MRO001", "RSA The server  failed to respond"),
	M_RSA_RESP_PARSE_ERROR("E0MRO001", "RSA resp  msg  parse error "),
	M_RSA_ERROR_RESPONSE("E0MRO001", "RSA resp msg parm [RESPONSE] is required"),
	M_RSA_ERROR_RETURN_DATA("E0MRO001", "RSA resp msg parm [RETURN_DATA] is required"),
	M_RSA_ERROR_RETURN_CODE("E0MRO001", "RSA resp msg parm [RETURN_CODE] is required"),
	M_RSA_ERROR_RETURN_STAMP("E0RO0001", "RSA resp msg parm [RETURN_STAMP] is required"),
	
	
	CONTENTTYPE("E0MI0001", "httprequest header content-type is required"),
	INVALID_CONTENTTYPE("E0MI0002", "invalid content-Type,just application/xml or application/json"),	
	
	IN_PARM_PARSE_ERROR("E0MI0001", "input parm parse error "),
	IN_PARM_ERROR_REQUEST("E0MI0001", "input parm [REQUEST] is required"),
	IN_PARM_ERROR_HRT_ATTRS("E0MI0001", "input parm [HRT_ATTRS] is required"),
	IN_PARM_ERROR_APP_SUB_ID("E0MI0001", "input parm [App_Sub_ID] is required"),	
	IN_PARM_ERROR_APP_TOKEN("E0MI0001", "input parm [App_Token] is required"),	
	IN_PARM_ERROR_API_ID("E0MI0001", "input parm [Api_ID] is required"),	
	IN_PARM_ERROR_API_VERSION("E0MI0001", "input parm [Api_Version] is required"),	
	IN_PARM_ERROR_TIME_STAMP("E0MI0001", "input parm [Time_Stamp] is required"),
	
	
	IN_PARM_ERROR_SIGN_METHOD("E0MI0001", "input parm [Sign_Method] is required"),
	IN_PARM_ERROR_SIGN("E0MI0001", "input parm [Sign] is required"),
	IN_PARM_ERROR_FORMAT("E0MI0001", "input parm [Format] is required"),
	IN_PARM_ERROR_REQUEST_DATA("E0MI0001", "input parm [REQUEST_DATA] is required"),
	IN_PARM_ERROR_PARTNER_ID("E0MI0001", "input parm [Partner_ID] is required"),
	IN_PARM_ERROR_SYS_ID("E0MI0001", "input parm [Sys_ID] is required"),
		
	IN_PARM_ERROR_APP_SUB_ID_FAIL("E0MI0002", " App_Sub_ID  is  fail "),
	IN_PARM_ERROR_APP_STATUS_FAIL("E0MI0002", " app  status  is  fail "), 
	IN_PARM_ERROR_API_ID_FAIL("E0MI0002", " Api_ID/Api_Version  is  fail "),
	IN_PARM_ERROR_API_INTERFACE_STATUS_FAIL("E0MI0002", " apiInterface  status  is  fail "),	
	IN_PARM_ERROR_API_R_APP_FAIL("E0MI0002", " API_R_APP  auth  is  fail "),
	IN_PARM_PARTNERID_FAIL("E0MI0002", " partnerID  is  fail "),	
	IN_PARM_SYSID_FAIL("E0MI0002", " SysID  is  fail "),
	IN_PARM_ERROR_TIME_STAMP_FAIL("E0MI0002", "input parm [Time_Stamp] format fail"),
	IN_PARM_ERROR_TIME_STAMP_TIMEOUT("E0MI0002", "input parm [Time_Stamp] timeout"),
	IN_PARM_ERROR_SIGN_METHOD_NOT_SUPPORT("E0MI0002", "input parm [Sign_Method] not support"),
	
	IN_PARM_ERROR_API_INTERFACE_URL_NULL("E0MI0002", " api target_url is null"),
	
	IN_PARM_TOKEN_FAIL("E0MI0002", " App_Token  is  fail "),
	IN_PARM_SIGN_FAIL("E0MI0002", " Sign  is  fail "),	
	
	OUT_PARM_PARSE_ERROR("E0MO0001", "resp  msg  parse error "),
	OUT_PARM_ERROR_RESPONSE("E0MO0001", "resp msg parm [RESPONSE] is required"),
	OUT_PARM_ERROR_RETURN_DATA("E0MO0001", "resp msg parm [RETURN_DATA] is required"),
	OUT_PARM_ERROR_RETURN_CODE("E0MO0001", "resp msg parm [RETURN_CODE] is required"),
	OUT_PARM_ERROR_RETURN_STAMP("E0MO0001", "resp msg parm [RETURN_STAMP] is required"),
	
	OUT_PARM_ERROR_RETURN_DESC("E0MO0001", "resp msg parm [RETURN_DESC] is required"),
	OUT_PARM_ERROR_SIGN("E0MO0001", "resp msg parm [SIGN] is required"),
	

	SIGN_CONVERSION_OTHER("E0SZ0001", "sign conversion error "),	
	INVALID_TOKEN("E0SZ0002", "invalid token error "),
	
	B_RESP_FAIL("E0SC0001", "The server  failed to respond")
	
	
	;
	// 成员变量
    private String errCode;
	private String errMsg;
    
    // 构造方法
    private OpenApiErrorEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
    // 普通方法
    public static String getErrMsg(String errCode) {
        for (OpenApiErrorEnum c : OpenApiErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c.getErrMsg();
            }
        }
        return null;
    }
    
    public static OpenApiErrorEnum getErr(String errCode) {
        for (OpenApiErrorEnum c : OpenApiErrorEnum.values()) {
            if (c.getErrCode().equals(errCode)) {
                return c;
            }
        }
        return null;
    }
    
    public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
