package com.personal.springboot.user.dao.entity;

import org.apache.ibatis.type.Alias;

import com.personal.springboot.common.entity.BaseEntity;

@Alias("userDevice")
public class UserDevice extends BaseEntity {
    private static final long serialVersionUID = -9221337693154738592L;

    private String userCode;
    private String deviceId;
    private String clientId;
    private String deviceInfo;
    private String deviceVersion;
    private String plateform;
    private String imei;
    private String imsi;
    private String wifiMac;
    private String installedList;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getWifiMac() {
        return wifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
    }

    public String getInstalledList() {
        return installedList;
    }

    public void setInstalledList(String installedList) {
        this.installedList = installedList;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode == null ? null : userCode.trim();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo == null ? null : deviceInfo.trim();
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion == null ? null : deviceVersion.trim();
    }

    public String getPlateform() {
        return plateform;
    }

    public void setPlateform(String plateform) {
        this.plateform = plateform == null ? null : plateform.trim();
    }
}