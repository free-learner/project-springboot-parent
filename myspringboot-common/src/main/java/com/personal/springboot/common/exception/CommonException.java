package com.personal.springboot.common.exception;

public class CommonException extends BaseException {

	private static final long serialVersionUID = -7246451667124308209L;

	public CommonException(String erroeCode, String message) {
        super(erroeCode, message);
    }
    
    public CommonException(String erroeCode) {
        super(erroeCode);
    }

    public CommonException(String erroeCode, Throwable throwable) {
        super(null, throwable);
        this.setErroeCode(erroeCode);
    }
    
}
