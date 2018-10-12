package com.personal.springboot.common.exception;

/**
 * service层异常统一定义类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年3月29日
 */
public class BaseServiceException extends BaseException {
	private static final long serialVersionUID = 3449623024482478847L;

	public BaseServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BaseServiceException(String message) {
		super(message);
	}
	
}