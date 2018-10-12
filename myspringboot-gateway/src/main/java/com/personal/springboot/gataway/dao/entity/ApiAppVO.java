package com.personal.springboot.gataway.dao.entity;

/**
 * ApiAppVO上层返回vo信息
 * 
 * @Author  LiuBao
 * @Version 2.0
 * @Date 2016年10月9日
 *
 */
public class ApiAppVO extends ApiApp {

	private static final long serialVersionUID = -1368740417682206163L;
	private String orgZhName;
	private String orgZhAbbreviation;
	private String sysZhName;
	private String sysZhAbbreviation;
	private String sysCnName;
	private String sysCnAbbreviation;
	private Integer pubInterfaceCount=0;
	private Integer subInterfaceCount=0;
	private String createrName;
	private String updaterName;
	
	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public Integer getPubInterfaceCount() {
		return pubInterfaceCount;
	}

	public void setPubInterfaceCount(Integer pubInterfaceCount) {
		this.pubInterfaceCount = pubInterfaceCount;
	}

	public Integer getSubInterfaceCount() {
		return subInterfaceCount;
	}

	public void setSubInterfaceCount(Integer subInterfaceCount) {
		this.subInterfaceCount = subInterfaceCount;
	}

	public String getSysZhName() {
		return sysZhName;
	}

	public void setSysZhName(String sysZhName) {
		this.sysZhName = sysZhName;
	}

	public String getSysZhAbbreviation() {
		return sysZhAbbreviation;
	}

	public void setSysZhAbbreviation(String sysZhAbbreviation) {
		this.sysZhAbbreviation = sysZhAbbreviation;
	}

	public String getSysCnName() {
		return sysCnName;
	}

	public void setSysCnName(String sysCnName) {
		this.sysCnName = sysCnName;
	}

	public String getSysCnAbbreviation() {
		return sysCnAbbreviation;
	}

	public void setSysCnAbbreviation(String sysCnAbbreviation) {
		this.sysCnAbbreviation = sysCnAbbreviation;
	}

	public String getOrgZhName() {
		return orgZhName;
	}

	public void setOrgZhName(String orgZhName) {
		this.orgZhName = orgZhName;
	}

	public String getOrgZhAbbreviation() {
		return orgZhAbbreviation;
	}

	public void setOrgZhAbbreviation(String orgZhAbbreviation) {
		this.orgZhAbbreviation = orgZhAbbreviation;
	}

}