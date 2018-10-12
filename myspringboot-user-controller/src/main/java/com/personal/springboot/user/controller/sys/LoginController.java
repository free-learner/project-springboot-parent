package com.personal.springboot.user.controller.sys;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.cons.Constants;
import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.PasswordHashUtil;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.controller.base.BaseController;
import com.personal.springboot.user.dao.entity.LoanUser;
import com.personal.springboot.user.dao.entity.UserOperationHistory;
import com.personal.springboot.user.service.LoanUserService;
import com.personal.springboot.user.task.UserOperationHistoryTask;

/**
 * 用户登录/登出相关请求
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月11日
 */
@RestController
@RequestMapping("/sys")
public class LoginController extends BaseController<LoanUser,LoginController> {

	@Autowired
	private LoanUserService loanUserService;
	
	@Autowired
	private UserOperationHistoryTask userOperationHistoryTask;
	
	/**
     * 注册接口
     * 默认值:
     * userType:0
     * channelType:1
     * status:1
     * 
     */
    @RequestMapping(value = "/register/0", method = { RequestMethod.POST })
    public Object sysRegister(@RequestBody Map<String, Object> requestMap,HttpServletRequest request, HttpServletResponse response) {
        String verifyCode = String.valueOf(requestMap.get("verifyCode") != null ? requestMap.get("verifyCode").toString() : "");
        if (StringUtils.isBlank(verifyCode)) {
            return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"verifyCode");
        }
        LoanUser loanUser=super.copyProperties(new LoanUser(), requestMap);
        String mobilePhone = loanUser.getMobilePhone();
        String password = loanUser.getPassword();
        if (StringUtils.isBlank(mobilePhone)) {
            return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"mobilePhone");
        }
        if (StringUtils.isBlank(password)) {
            return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"password");
        }
        
        //校验用户信息是否已经存在
        loanUser.setPassword(null);
        int count = loanUserService.selectCount(loanUser);
        if(count>0){
            getLogger().error("当前用户已注册,请直接登录!{}",JSON.toJSONString(loanUser));
            return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20005);
        }
        
        //添加用户信息
        loanUser.setChannelType(Constants.USERCHANNELTYPE_XJD_APP);
        loanUser.setUserType(Constants.USERTYPE_INNER);
        loanUser.setStatus(Constants.UserStatus.ACTIVE.getKey());
        try {
            // 加密密码
            String salt = PasswordHashUtil.generateSalt();
            loanUser.setSalt(salt);
            loanUser.setPassword(PasswordHashUtil.getEncryptedPassword(password, salt));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e ) {
            return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20007);
        }
        
        UserOperationHistory userOperationHistory=new UserOperationHistory();
        userOperationHistory.setOptType(Constants.USER_OPT_TYPE_REGISTER);
        //异步保存用户操作信息  
        //userOperationHistoryTask.executeSaveUserOperationHistory(userOperationHistory);
        
        boolean result = false ;
        loanUser.setCode(UUIDGenerator.generate());
        getLogger().info("进行插入操作之前对象信息为:{}",JSON.toJSONString(loanUser));       
        int insert=  loanUserService.insertUser(loanUser,userOperationHistory);
        result=(insert==1);
        if(!result){
            return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20006);
        }
        //返回对应的用户登录状态,Token及缓存有效时间等信息
        Map<String,Object> resultMap=new HashMap<>();
//        resultMap.put("experiedTime", getExpireSeconds());
        resultMap.put("timestamp", DateTimeUtil.formatDate2Str());
        return getSuccessResultInfo(resultMap);
    }
	
	/**
	 * :包含设备相关信息
	 * @param loanUser
	 * @param typeCode 
	 *         0:密码登录
	 *         1:快捷登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/*login/{typeCode}", method = { RequestMethod.POST })
	public Object sysLogin(@RequestBody Map<String, Object> requestMap,@PathVariable("typeCode") String typeCode,HttpServletRequest request, HttpServletResponse response) {
		String mobilePhone = String.valueOf(requestMap.get("mobilePhone") != null ? requestMap.get("mobilePhone").toString() : "");
		String password = String.valueOf(requestMap.get("password") != null ? requestMap.get("password").toString() : "");
		String verifyCode = String.valueOf(requestMap.get("verifyCode") != null ? requestMap.get("verifyCode").toString() : "");
		String cacheVerifyCode = String.valueOf(requestMap.get("cacheVerifyCode") != null ? requestMap.get("cacheVerifyCode").toString() : "");
		if (StringUtils.isBlank(mobilePhone)) {
		    return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"mobilePhone");
		}
		
		LoanUser loanUser=super.copyProperties(new LoanUser(), requestMap);
		loanUser.setPassword(null);
        //用户校验
		switch (typeCode) {
        case Constants.LOGIN_TYPE_PASSWD:
            if (StringUtils.isBlank(cacheVerifyCode)) {
                return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"cacheVerifyCode");
            }
            if (StringUtils.isBlank(verifyCode)) {
                return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_99996);
            }
            
            //密码校验
            if (StringUtils.isBlank(password)) {
                return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"password");
            }
            break;
        case Constants.LOGIN_TYPE_SMS:
            if (StringUtils.isBlank(verifyCode)) {
                return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"verifyCode");
            }
            break;
        default:
            //do none
        }
		//補全用戶loanUser信息及校验
		List<LoanUser> dataList = loanUserService.selectByEntity(loanUser);
		if(CollectionUtils.isEmpty(dataList)){
		    //用户信息不存在
		    return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20010,mobilePhone);
		}else{
		    loanUser=dataList.get(0);
		    //密码登录,非空,相等
		    if(StringUtils.isNoneBlank(password)){
                try {
                    String salt = loanUser.getSalt();
                    password = PasswordHashUtil.getEncryptedPassword(password, salt);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                    return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20007);
                }
		        if(!StringUtils.equalsIgnoreCase(loanUser.getPassword(), password)){
		            // 添加密码错误次数记录
		            return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20001);
		        }
		    }
		}
		
        //添加对应的设备相关信息记录及用户操作记录(pub/sub模式) <设备经纬度等相关信息>
        UserOperationHistory userOperationHistory=new UserOperationHistory();
        switch (typeCode) {
        case Constants.LOGIN_TYPE_PASSWD:
            userOperationHistory.setOptType(Constants.USER_OPT_TYPE_LOGIN_PWD);
            break;
        case Constants.LOGIN_TYPE_SMS:
            userOperationHistory.setOptType(Constants.USER_OPT_TYPE_LOGIN_SMS);
            break;
        }
        userOperationHistory.setUserCode(loanUser.getCode());
        //异步保存用户操作信息  
        userOperationHistoryTask.executeSaveUserOperationHistory(userOperationHistory);

        //返回对应的用户登录状态,Token及缓存有效时间等信息
        Map<String,Object> resultMap=new HashMap<>();
//        resultMap.put("experiedTime", getExpireSeconds());
        resultMap.put("timestamp", DateTimeUtil.formatDate2Str());
        return getSuccessResultInfo(resultMap);
	}
	
	/**
	 * 注销登录接口
	 */
	@RequestMapping(value = "/*logout/0", method = { RequestMethod.POST })
	public Object sysLogout(@RequestBody Map<String, Object> requestMap,HttpServletRequest request, HttpServletResponse response) {
	    //String mobilePhone = String.valueOf(requestMap.get("mobilePhone") != null ? requestMap.get("mobilePhone").toString() : "");
	    String mobilePhone = userMobilePhoneGetFromRequest(request);
        if (StringUtils.isBlank(mobilePhone)) {
            return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"mobilePhone");
        }
        LoanUser loanUser=super.copyProperties(new LoanUser(), requestMap);
        loanUser.setMobilePhone(mobilePhone);
        
        UserOperationHistory userOperationHistory=new UserOperationHistory();
        userOperationHistory.setOptType(Constants.USER_OPT_TYPE_LOGOUT);
        //异步保存用户操作信息  
        userOperationHistoryTask.executeSaveUserOperationHistory(userOperationHistory);
        return getSuccessResultInfo(true);
	}
	
	/**
	 * 忘记密码接口
	 */
	@RequestMapping(value = "/resetPwd/0", method = { RequestMethod.POST })
	public Object sysResetPwd(@RequestBody Map<String, Object> requestMap,HttpServletRequest request, HttpServletResponse response) {
	    String mobilePhone = String.valueOf(requestMap.get("mobilePhone") != null ? requestMap.get("mobilePhone").toString() : "");
	    String newPassword = String.valueOf(requestMap.get("newPassword") != null ? requestMap.get("newPassword").toString() : "");
	    String oldPassword = String.valueOf(requestMap.get("oldPassword") != null ? requestMap.get("oldPassword").toString() : "");
	    String verifyCode = String.valueOf(requestMap.get("verifyCode") != null ? requestMap.get("verifyCode").toString() : "");
	    if (StringUtils.isBlank(mobilePhone)) {
	        return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"mobilePhone");
	    }
	    if (StringUtils.isBlank(newPassword)) {
	        return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"newPassword");
	    }
	    if (StringUtils.isBlank(verifyCode)) {
	        return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"verifyCode");
	    }
	    
	    LoanUser loanUser=super.copyProperties(new LoanUser(), requestMap);
	    // 重置用户密码操作 
	    boolean result = false ;
	    loanUser.setDelFlag(Boolean.FALSE);
	    List<LoanUser> loanUsers = loanUserService.selectByEntity(loanUser);
	    if(CollectionUtils.isNotEmpty(loanUsers)){
	        String salt = loanUsers.get(0).getSalt();
	        try {
	            // 加密密码
	            if (StringUtils.isNoneBlank(oldPassword)) {
	                if(!StringUtils.equals(loanUsers.get(0).getPassword(), PasswordHashUtil.getEncryptedPassword(oldPassword, salt))){
	                    getLogger().error("该用户原始密码校验失败!");
	                    return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20001);
	                }
	            }
	            loanUser.setPassword(PasswordHashUtil.getEncryptedPassword(newPassword, salt));
            } catch (NoSuchAlgorithmException |InvalidKeySpecException e ) {
                return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20007);
            }
	        loanUser.setCode(loanUsers.get(0).getCode());
	        UserOperationHistory userOperationHistory=new UserOperationHistory();
	        int count= loanUserService.resetPwdByCode(loanUser,userOperationHistory);
	        result=(count==1);
	    }
	    if(!result){
	        return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_20003);
	    }
	    return getSuccessResultInfo(result);
	}
	
	/**
	 * 获取用户登录Token状态缓存信息接口
	 */
	@RequestMapping(value = "/cacheTtl/0", method = { RequestMethod.POST })
	public Object cacheTtl(@RequestBody Map<String, Object> requestMap,HttpServletRequest request, HttpServletResponse response) {
	    String mobilePhone = userMobilePhoneGetFromRequest(request);
	    if (StringUtils.isBlank(mobilePhone)) {
	        return getFailureResultInfo(ErrorCodeConstant.REQUIRED_PARAMETER_ISNOTEMPTY,"mobilePhone");
	    }
	    LoanUser loanUser=super.copyProperties(new LoanUser(), requestMap);
	    loanUser.setMobilePhone(mobilePhone);
	    Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("userToken",super.generateToken());
        return getSuccessResultInfo(resultMap);
	}

}
