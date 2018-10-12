package com.personal.springboot.user.controller.user;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.List;
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

import com.personal.springboot.controller.aop.Log;
import com.personal.springboot.controller.aop.LogOperation;
import com.personal.springboot.controller.base.BaseController;
import com.personal.springboot.controller.vo.ResultPageInfo;
import com.personal.springboot.user.dao.entity.UserOperationHistory;
import com.personal.springboot.user.service.UserOperationHistoryService;

/**
 * UserOperationHistoryController 控制层
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
@Controller
@RequestMapping("/history")
public class UserOperationHistoryController extends BaseController<UserOperationHistory,UserOperationHistoryController> {
    
    @Autowired
    private UserOperationHistoryService userOperationHistoryService;
    
    /**
     * 列表信息<按月份时间>查询
     */
    @ResponseBody
    @Log(module=LogOperation.FRONT,operation=LogOperation.CONTROLLER)
    @RequestMapping(value="/selectByMonth/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectByMonth(@RequestBody Map<String, Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        int pageIndex = Integer.valueOf(requestMap.get("pageIndex") != null ? requestMap.get("pageIndex").toString() : "0");
        int pageSize = Integer.valueOf(requestMap.get("pageSize") != null ? requestMap.get("pageSize").toString() : "10");
        String createDate = String.valueOf(requestMap.get("createDate") != null ? requestMap.get("createDate").toString() : "");
        getLogger().info("請求信息加載完畢:{}",createDate);
        UserOperationHistory record=super.copyProperties(new UserOperationHistory(),requestMap);
        List<UserOperationHistory> data =userOperationHistoryService.selectListByMonth(record, pageIndex, pageSize);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 列表全部查询
     */
    @ResponseBody
    @Log(module=LogOperation.FRONT,operation=LogOperation.CONTROLLER)
    @RequestMapping(value="/selectAll/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectAll(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Collection<UserOperationHistory> data =userOperationHistoryService.selectAll();
        if(CollectionUtils.isEmpty(data)){
            data=CollectionUtils.emptyIfNull(data);
        }
        return getSuccessResultInfo(data);
    }
    
    /**
     * 列表查询-分页
     */
    @ResponseBody
    @RequestMapping(value="/selectListByPage/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectListByPage(@RequestHeader Map<String, Object> headerMap,@RequestBody Map<String, Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws Exception {
        int pageIndex = Integer.valueOf(requestMap.get("pageIndex") != null ? requestMap.get("pageIndex").toString() : "0");
        int pageSize = Integer.valueOf(requestMap.get("pageSize") != null ? requestMap.get("pageSize").toString() : "10");
        UserOperationHistory record=super.copyProperties(new UserOperationHistory(),requestMap);
        //Integer totalCount=10;
        Integer totalCount=userOperationHistoryService.selectCount(record);
        ResultPageInfo<Collection<UserOperationHistory>> resultPageInfo = super.fillPageParam(requestMap, totalCount);
        Collection<UserOperationHistory> data =userOperationHistoryService.selectListByPage(record, pageIndex, pageSize);
        if(CollectionUtils.isEmpty(data)){
            data=CollectionUtils.emptyIfNull(data);
        }
        resultPageInfo.setData(data);
        return resultPageInfo;
    }
    
    @ResponseBody
    @RequestMapping(value="/selectByEntity/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectByEntity(@RequestHeader Map<String, Object> headerMap,@RequestBody Map<String, Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws Exception {
        UserOperationHistory record=super.copyProperties(new UserOperationHistory(),requestMap);
        Collection<UserOperationHistory> data =userOperationHistoryService.selectByEntity(record);
        if(CollectionUtils.isEmpty(data)){
            data=CollectionUtils.emptyIfNull(data);
        }
        return super.getSuccessResultInfo(data);
    }
    
    @ResponseBody
    @RequestMapping(value="/selectCount/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object selectCount(@RequestHeader Map<String, Object> headerMap,@RequestBody Map<String, Object> requestMap,HttpServletRequest request,HttpServletResponse response) throws Exception {
        UserOperationHistory record=super.copyProperties(new UserOperationHistory(),requestMap);
        Integer totalCount=userOperationHistoryService.selectCount(record);
        return super.getSuccessResultInfo(totalCount);
    }
    
    /**
     * 详情查询
     */
    @ResponseBody
    @RequestMapping(value="/findById/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object findById(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        Long id=Long.valueOf(paramMap.get("id").toString());
        UserOperationHistory data = userOperationHistoryService.selectByPrimaryKey(id);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 更新实体
     */
    @ResponseBody
    @RequestMapping(value="/updateByCode/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object updateByCode(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String code=paramMap.get("code").toString();
        UserOperationHistory record=new UserOperationHistory();
        record.setCode(code);
        record.setDelFlag(Boolean.TRUE);
        record.setUpdateBy("write-刘保");
        int data = userOperationHistoryService.updateByCodeSelective(record);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 删除实体
     */
    @ResponseBody
    @RequestMapping(value="/delByCode/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object delByCode(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String code="222";
        int data = userOperationHistoryService.deleteByCode(code);
        return getSuccessResultInfo(data);
    }
    
    /**
     * 插入实体
     */
    @ResponseBody
    @RequestMapping(value="/insertEntity/0", method = { RequestMethod.GET,RequestMethod.POST})
    public Object insertEntity(@RequestBody Map<String,Object> paramMap,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        UserOperationHistory record=super.copyProperties(new UserOperationHistory(), paramMap);
        record.setCreateBy("liubao");
        record.setUpdateBy("刘保");
        int data = userOperationHistoryService.insertSelective(record);
        return getSuccessResultInfo(data);
    }
    
}