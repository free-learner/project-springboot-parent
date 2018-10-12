package com.personal.springboot.user.controller.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personal.springboot.common.cons.Constants;
import com.personal.springboot.common.cons.Constants.UserStatus;
import com.personal.springboot.common.utils.DESEncryptUtil;
import com.personal.springboot.common.utils.PasswordHashUtil;
import com.personal.springboot.controller.aop.Log;
import com.personal.springboot.controller.aop.LogOperation;
import com.personal.springboot.controller.base.BaseController;
import com.personal.springboot.controller.vo.ResultPageInfo;
import com.personal.springboot.user.controller.base.Employee;
import com.personal.springboot.user.controller.base.EmployeeService;
import com.personal.springboot.user.dao.entity.LoanUser;
import com.personal.springboot.user.service.LoanUserService;

/**
 * LoanUserController 控制层
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
@Controller
@RequestMapping("/loanUser")
public class LoanUserController extends BaseController<LoanUser,LoanUserController> {
    
    @Autowired
    private LoanUserService loanUserService;
    
    /**
     * 列表全部查询
     */
    @ResponseBody
    //@OnceTokenGenerator(false)
    @Log(module=LogOperation.FRONT,operation=LogOperation.CONTROLLER)
    @RequestMapping(value="/selectAll/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectAll(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Collection<LoanUser> data =loanUserService.selectAll();
        if(CollectionUtils.isEmpty(data)){
            data=CollectionUtils.emptyIfNull(data);
        }
        return getSuccessResultInfo(data);
    }
    
    /**
     * 列表查询-分页
     */
    @ResponseBody
    //@OnceTokenGenerator(true)
    @RequestMapping(value="/selectListByPage/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectListByPage(@RequestHeader Map<String, Object> headerMap,@RequestBody Map<String, Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws Exception {
        int pageIndex = Integer.valueOf(requestMap.get("pageIndex") != null ? requestMap.get("pageIndex").toString() : "0");
        int pageSize = Integer.valueOf(requestMap.get("pageSize") != null ? requestMap.get("pageSize").toString() : "10");
        LoanUser record=super.copyProperties(new LoanUser(),requestMap);
        //Integer totalCount=10;
        Integer totalCount=loanUserService.count(record);
        ResultPageInfo<Collection<LoanUser>> resultPageInfo = super.fillPageParam(requestMap, totalCount);
        Collection<LoanUser> data =loanUserService.selectListByPage(record, pageIndex, pageSize);
        if(CollectionUtils.isEmpty(data)){
            data=CollectionUtils.emptyIfNull(data);
        }
        resultPageInfo.setData(data);
        return resultPageInfo;
    }
    
    @ResponseBody
    @RequestMapping(value="/selectByEntity/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectByEntity(@RequestHeader Map<String, Object> headerMap,@RequestBody Map<String, Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws Exception {
        LoanUser record=super.copyProperties(new LoanUser(),requestMap);
        Collection<LoanUser> data =loanUserService.selectByEntity(record);
        if(CollectionUtils.isEmpty(data)){
            data=CollectionUtils.emptyIfNull(data);
        }
        return super.getSuccessResultInfo(data);
    }
    
    @ResponseBody
    @RequestMapping(value="/selectCount/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectCount(@RequestHeader Map<String, Object> headerMap,@RequestBody Map<String, Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws Exception {
        LoanUser record=super.copyProperties(new LoanUser(),requestMap);
        Integer totalCount=loanUserService.count(record);
        return super.getSuccessResultInfo(totalCount);
    }
    
    /**
     * 详情查询
     */
    @ResponseBody
    @RequestMapping(value="/getByCode/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object getByCode(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        //String code="3501442767204e9eba72b1c538596cb6";
        String code=paramMap.get("code").toString();
        LoanUser data = loanUserService.getByCode(code);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 详情查询
     */
    @ResponseBody
    @RequestMapping(value="/findById/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object findById(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        //String code="9362a7560c0a40e9864040aad322e93e";//9362a7560c0a40e9864040aad322e93f
        Long id=Long.valueOf(paramMap.get("id").toString());
        LoanUser data = loanUserService.selectByPrimaryKey(id);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 详情查询
     */
    @ResponseBody
    @RequestMapping(value="/findByCode/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object findByCode(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        //String code="9362a7560c0a40e9864040aad322e93e";//9362a7560c0a40e9864040aad322e93f
        String code=paramMap.get("code").toString();
        LoanUser data = loanUserService.findByCode(code);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 更新实体
     */
    @ResponseBody
    @RequestMapping(value="/updateByCode/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object updateByCode(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String code=paramMap.get("code").toString();
        LoanUser record=new LoanUser();
        record.setCode(code);
        record.setDelFlag(Boolean.TRUE);
        record.setUpdateBy("write-刘保");
        int data = loanUserService.updateByCodeSelective(record);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 删除实体
     */
    @ResponseBody
    @RequestMapping(value="/delByCode/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object delByCode(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String code="222";
        int data = loanUserService.deleteByCode(code);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 查询
     */
    @ResponseBody
    @RequestMapping(value="/queryDetail/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object queryDetail(@RequestBody Map<String,Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String idNumber = String.valueOf(requestMap.get("idNumber") != null ? requestMap.get("idNumber").toString() : "");
        EmployeeService employeeService = new EmployeeService();
        Employee data = employeeService.getEmpInfo(idNumber);
        return getSuccessResultInfo(data);
    }
    
    @ResponseBody
    @RequestMapping(value="/queryDetail/1", method = { RequestMethod.GET,RequestMethod.POST})
    public Object queryMDetail(@RequestBody Map<String,Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String queryString = String.valueOf(requestMap.get("queryString") != null ? requestMap.get("queryString").toString() : "");
        String result = DESEncryptUtil.decryptBasedDes(queryString);
        return getSuccessResultInfo(result);
    }
    
    /**
     * 插入实体
     */
    @ResponseBody
    @RequestMapping(value="/insertEntity/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object insertEntity(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        LoanUser record=super.copyProperties(new LoanUser(), paramMap);
        String password=record.getPassword();
        String salt=PasswordHashUtil.generateSalt();
        record.setSalt(salt);
        record.setPassword(PasswordHashUtil.getEncryptedPassword(password, salt));
        record.setStatus(UserStatus.ACTIVE.getKey());
        record.setUserType(Constants.USERTYPE_INNER);
        record.setChannelType(Constants.SOURCE_CHANNEL_TYPE_APP);
        record.setCreateBy("write");
        record.setUpdateBy("write");
        int data = loanUserService.insertSelective(record);
        return getSuccessResultInfo(data);
    }
    
}