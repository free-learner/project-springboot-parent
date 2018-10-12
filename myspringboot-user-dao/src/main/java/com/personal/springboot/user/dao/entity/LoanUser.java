package com.personal.springboot.user.dao.entity;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.personal.springboot.common.entity.BaseEntity;

@Alias("loanUser")
public class LoanUser extends BaseEntity {
    private static final long serialVersionUID = 3165332875532284333L;

    private String mobilePhone;
    private String password;
    private String salt;
    private String userType;
    private String channelType;
    private Integer status;
    private List<UserOperationHistory> optList;
    
    public List<UserOperationHistory> getOptList() {
        return optList;
    }

    public void setOptList(List<UserOperationHistory> optList) {
        this.optList = optList;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone == null ? null : mobilePhone.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType == null ? null : userType.trim();
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType == null ? null : channelType.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}