package com.personal.springboot.common.exception;

/**
 * 基础自定义异常类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年3月29日
 */
public abstract class BaseException extends RuntimeException {

    private static final long serialVersionUID = 6775179545328979398L;
    private String erroeCode;

    public BaseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    protected BaseException(String erroeCode, String message) {
        super(message);
        this.erroeCode = erroeCode;
    }

    protected BaseException(String erroeCode) {
        this.erroeCode = erroeCode;
    }

    protected BaseException(String erroeCode, String message, Throwable throwable) {
        super(message, throwable);
        this.erroeCode = erroeCode;
    }

    public String getErroeCode() {
        return erroeCode;
    }

    public void setErroeCode(String erroeCode) {
        this.erroeCode = erroeCode;
    }

}