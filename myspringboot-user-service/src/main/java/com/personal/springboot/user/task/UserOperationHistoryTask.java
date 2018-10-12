package com.personal.springboot.user.task;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.personal.springboot.common.cons.Constants;
import com.personal.springboot.user.dao.entity.UserDevice;
import com.personal.springboot.user.dao.entity.UserOperationHistory;
import com.personal.springboot.user.service.UserDeviceService;
import com.personal.springboot.user.service.UserOperationHistoryService;

/**
 * 用户操作历史异步线程Task
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月24日
 */
@Component
public class UserOperationHistoryTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserOperationHistoryTask.class);
    
    @Autowired
    private UserOperationHistoryService userOperationHistoryService;
    
    @Autowired
    private UserDeviceService userDeviceService;
    
    @Async
    public void executeSaveUserOperationHistory(UserOperationHistory userOperationHistory) {
        int insert = userOperationHistoryService.insert(userOperationHistory);
        LOGGER.info("保存用户操作历史信息结果为:insert={}",insert);
        if(Constants.USER_OPT_TYPE_LOGIN_PWD.equalsIgnoreCase(userOperationHistory.getOptType())
                ||Constants.USER_OPT_TYPE_LOGIN_SMS.equalsIgnoreCase(userOperationHistory.getOptType())
                ||Constants.USER_OPT_TYPE_REGISTER.equalsIgnoreCase(userOperationHistory.getOptType())){
            // 添加信息校验
            String userCode = userOperationHistory.getUserCode();
            UserDevice record=new UserDevice();
            record.setUserCode(userCode);
            List<UserDevice> userDevices = userDeviceService.selectByEntity(record);
            try {
                BeanUtils.copyProperties(record, userOperationHistory);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("用戶设备信息属性拷贝异常！",e);
            }
            if(CollectionUtils.isEmpty(userDevices)){
                int insert2 = userDeviceService.insert(record);
                LOGGER.info("保存用户设备信息结果为:insert2={}",insert2);
            }else{
                record.setDelFlag(null);
                record.setCreateDate(null);
                record.setCode(userDevices.get(0).getCode());
                int update = userDeviceService.updateByCodeSelective(record);
                LOGGER.info("更新用户设备信息结果为:update={}",update);
            }
            
        }
    }
	
}