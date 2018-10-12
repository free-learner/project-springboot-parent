package com.personal.springboot.user.controller.base;

import java.io.Serializable;

/**
 * @author SongWei
 * 
 * 员工
 *
 */
public class Employee implements Serializable{
	/**
	 * 序列化编码
	 */
	private static final long serialVersionUID = -3652438973573018537L;
	/** 返回码*/
	private String regCode;	
	/** 返回MSG*/
	private String regMsg;
	/** 是否在职*/	
	private String isWorking;
	/** 返回码*/		
	private String bankAccount;
	/** 授权码*/
	private String authCode;
	/** 集团*/
	private String manDt;
	/** 工号*/
	private String person;
	/** 身份证号*/
	private String zhrc0040;
	/** 姓名*/
	private String tperson;
	/** 雇佣状态*/
	private String templStatus;
	/** 手机号*/
	private String tel;
	/** 工资卡帐号*/
	private String zhrc0074;
	/** 工资卡开户行*/
	private String zhrc0075;	
	/** 工资卡开户支行信息*/
	private String zhrc0076;
	/** 工资卡开户行编号*/
	private String zhrc0077;
	
	private String cqts;
	/** 当前薪资*/
	private String amount;
	/** 所在部门代码*/
	private String orgunit;
	/** 所在部门*/
	private String torgunit;
	/** 用户所属代码*/	
	private String compCode;
	/** 用户所属公司*/
	private String tcompCode;
	/** 当前职位代码*/
	private String job;
	/** 当前职位*/
	private String tjob;
	/** 入职时间*/
	private String zhrc0019;
	/** 合同类型代码*/
	private String cntrctType;
    /** 合同类型*/
	private String tcntrctType;
	/** 所在区域*/
	private String qy;
	/** 公司邮箱*/
	private String yx;
	/** 性别*/
	private String tgender;
	/** 出生年月*/
	private String dateBirth;
	/** 籍贯*/
	private String zhrc0073;
	/** 民族*/
	private String mz;
	/** 婚育状况*/
	private String tmeMarSta;
	/** 最高学历*/
	private String tzhrc0006;
	/** 政治面貌*/
	private String tzhrc0066;
	/** 户籍地址*/
	private String zhrc0067;
	/** 户口性质*/
	private String tzhrc0068;
	/** 目前居住地*/
	private String zhrc0069;
	/** 紧急联系人*/
	private String zhrc0070;
	/** 紧急联系人与本人关系*/
	private String tzhrc0071;
	/** 紧急联系人联系电话*/
	private String zhrc0072;
	/** 员工组*/
	private String emplGroup;
	public String getRegCode() {
		return regCode;
	}

	public void setRegCode(String regCode) {
		this.regCode = regCode;
	}

	public String getRegMsg() {
		return regMsg;
	}

	public void setRegMsg(String regMsg) {
		this.regMsg = regMsg;
	}

	public String getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(String isWorking) {
		this.isWorking = isWorking;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getManDt() {
		return manDt;
	}

	public void setManDt(String manDt) {
		this.manDt = manDt;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getZhrc0040() {
		return zhrc0040;
	}

	public void setZhrc0040(String zhrc0040) {
		this.zhrc0040 = zhrc0040;
	}

	public String getTperson() {
		return tperson;
	}

	public void setTperson(String tperson) {
		this.tperson = tperson;
	}

	public String getTemplStatus() {
		return templStatus;
	}

	public void setTemplStatus(String templStatus) {
		this.templStatus = templStatus;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getZhrc0074() {
		return zhrc0074;
	}

	public void setZhrc0074(String zhrc0074) {
		this.zhrc0074 = zhrc0074;
	}

	public String getZhrc0075() {
		return zhrc0075;
	}

	public void setZhrc0075(String zhrc0075) {
		this.zhrc0075 = zhrc0075;
	}

	public String getZhrc0076() {
		return zhrc0076;
	}

	public void setZhrc0076(String zhrc0076) {
		this.zhrc0076 = zhrc0076;
	}

	public String getZhrc0077() {
		return zhrc0077;
	}

	public void setZhrc0077(String zhrc0077) {
		this.zhrc0077 = zhrc0077;
	}

	public String getCqts() {
		return cqts;
	}

	public void setCqts(String cqts) {
		this.cqts = cqts;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrgunit() {
		return orgunit;
	}

	public void setOrgunit(String orgunit) {
		this.orgunit = orgunit;
	}

	public String getTorgunit() {
		return torgunit;
	}

	public void setTorgunit(String torgunit) {
		this.torgunit = torgunit;
	}

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getTcompCode() {
		return tcompCode;
	}

	public void setTcompCode(String tcompCode) {
		this.tcompCode = tcompCode;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getTjob() {
		return tjob;
	}

	public void setTjob(String tjob) {
		this.tjob = tjob;
	}

	public String getZhrc0019() {
		return zhrc0019;
	}

	public void setZhrc0019(String zhrc0019) {
		this.zhrc0019 = zhrc0019;
	}

	public String getCntrctType() {
		return cntrctType;
	}

	public void setCntrctType(String cntrctType) {
		this.cntrctType = cntrctType;
	}

	public String getTcntrctType() {
		return tcntrctType;
	}

	public void setTcntrctType(String tcntrctType) {
		this.tcntrctType = tcntrctType;
	}

	public String getQy() {
		return qy;
	}

	public void setQy(String qy) {
		this.qy = qy;
	}

	public String getYx() {
		return yx;
	}

	public void setYx(String yx) {
		this.yx = yx;
	}

	public String getTgender() {
		return tgender;
	}

	public void setTgender(String tgender) {
		this.tgender = tgender;
	}

	public String getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(String dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getZhrc0073() {
		return zhrc0073;
	}

	public void setZhrc0073(String zhrc0073) {
		this.zhrc0073 = zhrc0073;
	}

	public String getMz() {
		return mz;
	}

	public void setMz(String mz) {
		this.mz = mz;
	}

	public String getTmeMarSta() {
		return tmeMarSta;
	}

	public void setTmeMarSta(String tmeMarSta) {
		this.tmeMarSta = tmeMarSta;
	}

	public String getTzhrc0006() {
		return tzhrc0006;
	}

	public void setTzhrc0006(String tzhrc0006) {
		this.tzhrc0006 = tzhrc0006;
	}

	public String getTzhrc0066() {
		return tzhrc0066;
	}

	public void setTzhrc0066(String tzhrc0066) {
		this.tzhrc0066 = tzhrc0066;
	}

	public String getZhrc0067() {
		return zhrc0067;
	}

	public void setZhrc0067(String zhrc0067) {
		this.zhrc0067 = zhrc0067;
	}

	public String getTzhrc0068() {
		return tzhrc0068;
	}

	public void setTzhrc0068(String tzhrc0068) {
		this.tzhrc0068 = tzhrc0068;
	}

	public String getZhrc0069() {
		return zhrc0069;
	}

	public void setZhrc0069(String zhrc0069) {
		this.zhrc0069 = zhrc0069;
	}

	public String getZhrc0070() {
		return zhrc0070;
	}

	public void setZhrc0070(String zhrc0070) {
		this.zhrc0070 = zhrc0070;
	}

	public String getTzhrc0071() {
		return tzhrc0071;
	}

	public void setTzhrc0071(String tzhrc0071) {
		this.tzhrc0071 = tzhrc0071;
	}

	public String getZhrc0072() {
		return zhrc0072;
	}

	public void setZhrc0072(String zhrc0072) {
		this.zhrc0072 = zhrc0072;
	}

	public String getEmplGroup() {
		return emplGroup;
	}

	public void setEmplGroup(String emplGroup) {
		this.emplGroup = emplGroup;
	}

}
