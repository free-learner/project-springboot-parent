package com.personal.springboot.common.exception;

/**
 * NoAuth异常统一定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月6日
 */
public class NoAuthException extends BaseException {
	private static final long serialVersionUID = 3449623024482478847L;

	public NoAuthException(String erroeCode, String message) {
        super(erroeCode, message);
    }
    
    public NoAuthException(String erroeCode) {
        super(erroeCode);
    }

    public NoAuthException(String erroeCode, Throwable throwable) {
        super(null, throwable);
        this.setErroeCode(erroeCode);
    }
	
}