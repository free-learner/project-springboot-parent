package com.personal.springboot.common.vo;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.personal.springboot.common.entity.ToString;
import com.personal.springboot.common.exception.AbsErrorCodeConstant;
import com.personal.springboot.common.utils.DateTimeUtil;

/**
 * 统一返回公共数据结构体
 * 
 * @Author LiuBao
 * @Version 2.0 2017年8月30日
 */
public class ResultBody<T> extends ToString {
    private static final long serialVersionUID = 2858244548725271096L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultBody.class);
    
    private String optCode;
    private String optMsg = StringUtils.EMPTY;
    private String optTimeStamp;
    private String optTransactionId;
    private String optSign;
    private T optData;
    
    public ResultBody() {
        this.optCode = AbsErrorCodeConstant.SUCCESS;
        this.optTimeStamp = DateTimeUtil.formatDate2Str();
    }

    public ResultBody(T optData) {
        this();
        this.optData = optData;
    }

    public ResultBody(String optCode, String optMsg) {
        this();
        this.optCode = optCode;
        this.optMsg = optMsg;
    }

    public ResultBody(String optCode, String optMsg, T optData) {
        this(optCode, optMsg);
        this.optData = optData;
    }

    public ResultBody<T> buildSuccess() {
        this.setOptCode(AbsErrorCodeConstant.SUCCESS);
        return this;
    }

    public ResultBody<T> buildSuccess(T optData) {
        this.setOptCode(AbsErrorCodeConstant.SUCCESS);
        this.setOptData(optData);
        return this;
    }

    public ResultBody<T> buildFailure() {
        this.setOptCode(AbsErrorCodeConstant.FAILURE);
        return this;
    }

    public ResultBody<T> buildFailure(String optCode, String optMsg) {
        if(StringUtils.isNotBlank(optCode)){
            this.setOptCode(optCode);
        }else{
            this.setOptCode(AbsErrorCodeConstant.FAILURE);
        }
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("返回封装后的错误信息为:optCode={},optMsg={}",this.getOptCode(),optMsg);
        }
        this.setOptMsg(optMsg);
        return this;
    }

    @JSONField(name = "optCode")
    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    @JSONField(name = "optTimeStamp")
    public String getOptTimeStamp() {
        return optTimeStamp;
    }

    public void setOptTimeStamp(String optTimeStamp) {
        this.optTimeStamp = optTimeStamp;
    }

    @JSONField(name = "optTransactionId")
    public String getOptTransactionId() {
        return optTransactionId;
    }

    public void setOptTransactionId(String optTransactionId) {
        this.optTransactionId = optTransactionId;
    }

    @JSONField(name = "optMsg")
    public String getOptMsg() {
        return optMsg;
    }

    public void setOptMsg(String optMsg) {
        this.optMsg = optMsg;
    }

    @JSONField(name = "optSign")
    public String getOptSign() {
        return optSign;
    }

    public void setOptSign(String optSign) {
        this.optSign = optSign;
    }

    @JSONField(name = "optData")
    @JsonProperty(value = "optData")
    public T getOptData() {
        return optData;
    }
    
    public void setOptData(T optData) {
        this.optData = optData;
    }

}
