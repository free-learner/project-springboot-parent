package com.personal.springboot.common.exception;

/**
 * Http层异常统一定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年8月27日
 */
public class HttpServiceException extends BaseException {
    private static final long serialVersionUID = 3449623024482478847L;

    public HttpServiceException(String erroeCode, String message) {
        super(erroeCode, message);
    }
    
    public HttpServiceException(String erroeCode) {
        super(erroeCode);
    }

    public HttpServiceException(String erroeCode, Throwable throwable) {
        super(null, throwable);
        this.setErroeCode(erroeCode);
    }
    
    

}