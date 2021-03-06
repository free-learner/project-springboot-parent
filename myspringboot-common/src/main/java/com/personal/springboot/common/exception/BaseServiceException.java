package com.personal.springboot.common.exception;

/**
 * service层异常统一定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年8月27日
 */
public class BaseServiceException extends BaseException {
    private static final long serialVersionUID = 3449623024482478847L;

    public BaseServiceException(String erroeCode, String message) {
        super(erroeCode, message);
    }
    
    public BaseServiceException(String erroeCode) {
        super(erroeCode);
    }
    
    public BaseServiceException(String erroeCode, Throwable throwable) {
        super(null, throwable);
        this.setErroeCode(erroeCode);
    }

}