package com.personal.springboot.user.controller.base;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;


@Service
public class EmployeeService {
	
	private static Logger log = Logger.getLogger(EmployeeService.class);

	/**
	 * 根据身份证号查询
	 * @param idNo
	 * @return
	 */
	public Employee getEmpInfo(String idNo){
		return getEmpInfo(idNo, "");
	}
	
	/**
	 * 根据工号查询
	 * @param idNo
	 * @return
	 */
	public Employee getEmpInfoByEmpNo(String empNo){
		return getEmpInfo("", empNo);
	}
	
	public Employee getEmpInfo(String idNo, String empNo){
		Employee emp = null;
		JSONObject jsonParam33 = new JSONObject();
		jsonParam33.put(InterfaceConstants.IDNUMBER, idNo);
		jsonParam33.put("EMP_NUMBER", empNo);
		try {
			JSONObject res33 = null;
			res33 = HttpClientUtil.httpPostYhloanJson(InterfaceConstants.YHFIN0033, jsonParam33.toString());
			if(null != res33){
				String res33regCode = res33.getString(InterfaceConstants.REGCODE);
				String res33isWorking = res33.getString("IS_WORKING");
				if(InterfaceConstants.SUCCESS.equals(res33regCode) && InterfaceConstants.INSERVICE.equals(res33isWorking)){
					JSONObject jsonParam34 = new JSONObject();
					String authCode =  getAuthCode(idNo + empNo);
					jsonParam34.put("ZHRC0040", idNo);
					jsonParam34.put("AUTH_CODE", authCode);
					jsonParam34.put("EMP_NUMBER", empNo);
					JSONObject res34 = null;
					res34 = HttpClientUtil.httpPostYhloanJson(InterfaceConstants.YHFIN0034, jsonParam34.toString());
					if(null == res34){
						res34 = HttpClientUtil.httpPostYhloanJson(InterfaceConstants.YHFIN0034, jsonParam34.toString());
					}
					if(null == res34){
						log.error("调用"+InterfaceConstants.YHFIN0034+"异常");
						throw new Exception("调用"+InterfaceConstants.YHFIN0034+"异常");
					}
					String res34RegCode = res34.getString(InterfaceConstants.REGCODE);
					if(InterfaceConstants.SUCCESS.equals(res34RegCode)){
						JSONObject jsonParam35 = new JSONObject();
						jsonParam35.put(InterfaceConstants.IDNUMBER, idNo);
						jsonParam35.put("AUTH_CODE", authCode);
						jsonParam35.put("EMP_NUMBER", empNo);
						JSONObject res35 = null;
						res35 = HttpClientUtil.httpPostYhloanJson(InterfaceConstants.YHFIN0035, jsonParam35.toString());
						if(null == res35){
							log.error("调用"+InterfaceConstants.YHFIN0035+"异常");
							throw new Exception("调用"+InterfaceConstants.YHFIN0035+"异常");
						}
						String res35RegCode = res35.getString(InterfaceConstants.REGCODE);
						if(InterfaceConstants.SUCCESS.equals(res35RegCode)){
							emp = getEmployee(res35);
							log.info("查询员工信息成功");
							return emp;
						}else{
							emp = new Employee();
							emp.setRegCode(res35RegCode);
							emp.setRegMsg(res35.getString(InterfaceConstants.REGMSG));
							log.error("调用"+InterfaceConstants.YHFIN0035+"返回失败");
							return emp;
						}
					}else{
						emp = new Employee();
						emp.setRegCode(res34RegCode);
						emp.setRegMsg(res34.getString(InterfaceConstants.REGMSG));
						log.error("调用"+InterfaceConstants.YHFIN0034+"返回失败");
						return emp;
					}
				}else{
					emp = new Employee();
					emp.setRegCode(res33regCode);
					emp.setRegMsg(res33.getString(InterfaceConstants.REGMSG));
					emp.setIsWorking(res33isWorking);
					log.error("调用"+InterfaceConstants.YHFIN0033+"返回失败");
					return emp;
				}
			}else{
				log.error("调用"+InterfaceConstants.YHFIN0033+"异常");
				throw new Exception("调用"+InterfaceConstants.YHFIN0033+"异常");
			}
		} catch (Exception e) {
			emp = new Employee();
			emp.setRegCode(InterfaceConstants.ERROR);
			emp.setRegMsg("失败");
			log.error("调用接口异常，异常信息："+e);
			return emp;
		}
	}
	
	/**
	 * 验证用户是否在职
	 * @param idNo
	 * @return
	 */
	public static boolean isWorking(String idNo){
		if(StringUtils.isBlank(idNo)){
			return false;
		}
		JSONObject jsonParam33 = new JSONObject();
		jsonParam33.put(InterfaceConstants.IDNUMBER, idNo);
		jsonParam33.put("EMP_NUMBER", "");
		try {
			JSONObject res33 = null;
			res33 = HttpClientUtil.httpPostYhloanJson(InterfaceConstants.YHFIN0033, jsonParam33.toString());
			if(null != res33){
				log.info(res33);
				String res33regCode = res33.getString(InterfaceConstants.REGCODE);
				String res33isWorking = res33.getString("IS_WORKING");
				if(InterfaceConstants.SUCCESS.equals(res33regCode) && InterfaceConstants.INSERVICE.equals(res33isWorking)){
					return true;
				} else {
					return false;
				}
				
			}
		}catch (Exception e) {
			log.error("调用接口异常，异常信息："+e);
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 获取在职状态
	 * @param idNo
	 * @return
	 */
	public static String getWorkingStatus(String idNo){
		if(StringUtils.isBlank(idNo)){
			return null;
		}
		JSONObject jsonParam33 = new JSONObject();
		jsonParam33.put(InterfaceConstants.IDNUMBER, idNo);
		jsonParam33.put("EMP_NUMBER", "");
		try {
			JSONObject res33 = null;
			res33 = HttpClientUtil.httpPostYhloanJson(InterfaceConstants.YHFIN0033, jsonParam33.toString());
			if(null != res33){
				log.info(res33);
				String res33regCode = res33.getString(InterfaceConstants.REGCODE);
				String res33isWorking = res33.getString("IS_WORKING");
				if(InterfaceConstants.SUCCESS.equals(res33regCode)){
					return res33isWorking;
				} else if(InterfaceConstants.ERROR.equals(res33regCode)) {
					return InterfaceConstants.LEAVEOFFICE;
				} else {
					return null;
				}
			}
			return null;
		}catch (Exception e) {
			log.error("调用接口异常，异常信息："+e);
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 获取授权码
	 * 
	 * @param idNo
	 * @return
	 */
	public static String getAuthCode(String idNo){
		long date = System.currentTimeMillis();
		int hashCode = (date+idNo).hashCode();
		return date+idNo+String.valueOf(hashCode);
	}
	
	/**
	 * 返回报文转为对象
	 * 
	 * @param jsonParam
	 * @return
	 */
	public static Employee getEmployee(JSONObject jsonParam){
		Employee emp = new Employee();
		emp.setRegCode(jsonParam.getString("REG_CODE")==null? "" : jsonParam.getString("REG_CODE"));
		emp.setRegMsg(jsonParam.getString("REG_MSG")==null? "" : jsonParam.getString("REG_MSG"));
		emp.setManDt(jsonParam.getString("MANDT")==null? "" : jsonParam.getString("MANDT"));
		emp.setPerson(jsonParam.getString("PERSON")==null? "" : jsonParam.getString("PERSON"));
		emp.setZhrc0040(jsonParam.getString("ZHRC0040")==null? "" : jsonParam.getString("ZHRC0040"));
		emp.setTperson(jsonParam.getString("TPERSON")==null? "" : jsonParam.getString("TPERSON"));
		emp.setTemplStatus(jsonParam.getString("TEMPLSTATUS")==null? "" : jsonParam.getString("TEMPLSTATUS"));
		emp.setTel(jsonParam.getString("TEL")==null? "" : jsonParam.getString("TEL"));
		emp.setZhrc0074(jsonParam.getString("ZHRC0074")==null? "" : jsonParam.getString("ZHRC0074"));
		emp.setZhrc0075(jsonParam.getString("ZHRC0075")==null? "" : jsonParam.getString("ZHRC0075"));
		emp.setZhrc0076(jsonParam.getString("ZHRC0076")==null? "" : jsonParam.getString("ZHRC0076"));
		emp.setZhrc0077(jsonParam.getString("ZHRC0077")==null? "" : jsonParam.getString("ZHRC0077"));
		emp.setCqts(jsonParam.getString("CQTS")==null? "" : jsonParam.getString("CQTS"));
		emp.setAmount(jsonParam.getString("AMOUNT")==null? "" : jsonParam.getString("AMOUNT"));
		emp.setOrgunit(jsonParam.getString("ORGUNIT")==null? "" : jsonParam.getString("ORGUNIT"));
		emp.setTorgunit(jsonParam.getString("TORGUNIT")==null? "" : jsonParam.getString("TORGUNIT"));
		emp.setCompCode(jsonParam.getString("COMP_CODE")==null? "" : jsonParam.getString("COMP_CODE"));
		emp.setTcompCode(jsonParam.getString("TCOMP_CODE")==null? "" : jsonParam.getString("TCOMP_CODE"));
		emp.setJob(jsonParam.getString("JOB")==null? "" : jsonParam.getString("JOB"));
		emp.setTjob(jsonParam.getString("TJOB")==null? "" : jsonParam.getString("TJOB"));
		emp.setZhrc0019(jsonParam.getString("ZHRC0019")==null? "" : jsonParam.getString("ZHRC0019"));
		emp.setCntrctType(jsonParam.getString("CNTRCTTYPE")==null? "" : jsonParam.getString("CNTRCTTYPE"));
		emp.setTcntrctType(jsonParam.getString("TCNTRCTTYPE")==null? "" : jsonParam.getString("TCNTRCTTYPE"));
		emp.setQy(jsonParam.getString("QY")==null? "" : jsonParam.getString("QY"));
		emp.setYx(jsonParam.getString("YX")==null? "" : jsonParam.getString("YX"));
		emp.setTgender(jsonParam.getString("TGENDER")==null? "" : jsonParam.getString("TGENDER"));
		emp.setDateBirth(jsonParam.getString("DATEBIRTH")==null? "" : jsonParam.getString("DATEBIRTH"));
		emp.setZhrc0073(jsonParam.getString("ZHRC0073")==null? "" : jsonParam.getString("ZHRC0073"));
		emp.setMz(jsonParam.getString("MZ")==null? "" : jsonParam.getString("MZ"));
		emp.setTmeMarSta(jsonParam.getString("TME_MAR_STA")==null? "" : jsonParam.getString("TME_MAR_STA"));
		emp.setTzhrc0006(jsonParam.getString("TZHRC0006")==null? "" : jsonParam.getString("TZHRC0006"));
		emp.setTzhrc0066(jsonParam.getString("TZHRC0066")==null? "" : jsonParam.getString("TZHRC0066"));
		emp.setZhrc0067(jsonParam.getString("ZHRC0067")==null? "" : jsonParam.getString("ZHRC0067"));
		emp.setTzhrc0068(jsonParam.getString("TZHRC0068")==null? "" : jsonParam.getString("TZHRC0068"));
		emp.setZhrc0069(jsonParam.getString("ZHRC0069")==null? "" : jsonParam.getString("ZHRC0069"));
		emp.setZhrc0070(jsonParam.getString("ZHRC0070")==null? "" : jsonParam.getString("ZHRC0070"));
		emp.setTzhrc0071(jsonParam.getString("TZHRC0071")==null? "" : jsonParam.getString("TZHRC0071"));
		emp.setZhrc0072(jsonParam.getString("ZHRC0072")==null? "" : jsonParam.getString("ZHRC0072"));
		emp.setEmplGroup(jsonParam.getString("EMPLGROUP")==null? "" : jsonParam.getString("EMPLGROUP"));
		return emp;
	}

}
