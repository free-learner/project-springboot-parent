package com.personal.springboot.controller.vo;

import com.personal.springboot.common.entity.ToString;

/**
 * 請求頭消息實體
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月20日
 */
public class AppAuthHeader extends ToString {
    private static final long serialVersionUID = 5083452090493798759L;

    private String userToken;
    private String mobilePhone;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

}