package com.personal.springboot.controller.vo;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.entity.ToString;

/**
 * 统一返回数据实体类<非分页结果>
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年3月29日
 */
public class ResultInfo<T>  extends ToString{
	private static final long serialVersionUID = 2858244548725271096L;
	private String code;
	private String message=StringUtils.EMPTY;
	private T data;
	private String onceToken;

    public ResultInfo() {
		this.code = ErrorCodeConstant.SUCCESS;
	}

	public ResultInfo(T data) {
		this();
		this.data = data;
	}

	public ResultInfo(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResultInfo(String code, String message, T data) {
		this(code, message);
		this.data = data;
	}

	public ResultInfo<T> buildSuccess() {
		return this;
	}

	public ResultInfo<T> buildSuccess(T data) {
		this.setData(data);
		return this;
	}

	public ResultInfo<T> buildFailure() {
		this.setCode(ErrorCodeConstant.FAILURE);
		return this;
	}

	public ResultInfo<T> buildFailure(String code, String message) {
		if(StringUtils.isNotBlank(code)){
			this.setCode(code);
		}else{
			this.setCode(ErrorCodeConstant.FAILURE);
		}
		this.setMessage(message);
		return this;
	}

	public String getOnceToken() {
        return onceToken;
    }

    public void setOnceToken(String onceToken) {
        this.onceToken = onceToken;
    }
    
	@JSONField(name="code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JSONField(name="message")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@JSONField(name="data")
	@JsonProperty(value = "data")
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
