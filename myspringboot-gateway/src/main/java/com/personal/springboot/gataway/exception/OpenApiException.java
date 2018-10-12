package com.personal.springboot.gataway.exception;

import com.personal.springboot.gataway.conf.OpenApiErrorEnum;
import com.personal.springboot.gataway.dao.entity.OpenApiRespDto;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("error")
public  class OpenApiException extends RuntimeException {
    private static final long serialVersionUID = 8710396445793589764L;
    
    
    
    @XStreamAlias("errorCode")
    private String errorCode = null;
    
    @XStreamAlias("errorMsg")
    private String errorMsg = null;
    public String getErrorCode() {
		return errorCode;
	}
    
    
    
    

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

    
    public OpenApiException() {

    }
    
   
    public OpenApiException(OpenApiErrorEnum error) {
    	super(error.getErrMsg());
    	this.errorCode = error.getErrCode();
        this.errorMsg = error.getErrMsg();
    }
    
    public OpenApiException(OpenApiErrorEnum error,Throwable cause) {
    	super(error.getErrMsg(),cause);
    	this.errorCode = error.getErrCode();
        this.errorMsg = error.getErrMsg();
    }
    public OpenApiException(OpenApiErrorEnum error,String[] msg,Throwable cause) {
    	super(error.getErrMsg(),cause);
    	this.errorCode = error.getErrCode();
        this.errorMsg = String.format(error.getErrMsg(), msg);
    }

    public OpenApiException(String message) {
        super(message);
    }

    public OpenApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenApiException(String code, String message) {
        super(message);
        this.errorCode = code;
        this.errorMsg = message;
    }
    
  

    public OpenApiException(Throwable cause) {
        super(cause);
    }

    public OpenApiRespDto getShortMsg() { 
        return new OpenApiRespDto(this.errorCode,this.errorMsg); 
    } 
    
    
    public OpenApiRespDto getShortMsg(String return_stamp) { 
        return new OpenApiRespDto(this.errorCode,this.errorMsg,return_stamp); 
    } 
    
 }
