package com.personal.springboot.user.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.personal.springboot.common.aop.DataSourceType;
import com.personal.springboot.common.aop.TargetDataSource;
import com.personal.springboot.common.cons.Constants;
import com.personal.springboot.service.impl.BaseServiceImpl;
import com.personal.springboot.user.dao.entity.LoanUser;
import com.personal.springboot.user.dao.entity.UserOperationHistory;
import com.personal.springboot.user.dao.mapper.LoanUserMapper;
import com.personal.springboot.user.dao.mapper.UserOperationHistoryMapper;
import com.personal.springboot.user.service.LoanUserService;
import com.personal.springboot.user.task.UserOperationHistoryTask;

/**
 * 基础LoanUser接口定义實現
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月11日
 */
@Service("loanUserService")
public class LoanUserServiceImpl extends BaseServiceImpl<LoanUser> implements LoanUserService {
	private static final Logger logger = LoggerFactory.getLogger(LoanUserServiceImpl.class);
	
	private LoanUserMapper loanUserMapper;
    
    @Autowired
    private UserOperationHistoryMapper userOperationHistoryMapper;
    
    @Autowired
    private UserOperationHistoryTask userOperationHistoryTask;
    
    @Autowired
    public LoanUserServiceImpl(LoanUserMapper loanUserMapper) {
        super(loanUserMapper);
        this.loanUserMapper = loanUserMapper;
        logger.debug("LoanUserServiceImpl构造方法执行了...");
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int resetPwdByCode(LoanUser loanUser,UserOperationHistory userOperationHistory) {
        int count= loanUserMapper.updateByCodeSelective(loanUser);
        if(count==1&&userOperationHistory!=null){
            userOperationHistory.setId(null);
            userOperationHistory.setCode(null);
            userOperationHistory.setCreateDate(null);
            userOperationHistory.setUpdateDate(null);
            userOperationHistory.setUserCode(loanUser.getCode());
            userOperationHistory.setOptType(Constants.USER_OPT_TYPE_RESETPWD);
            int insert = userOperationHistoryMapper.insert(fillInsertBaseEntity(userOperationHistory));
            logger.debug("resetPwdByCode记录历史操作信息结果:{}",insert);
        }
        return count;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public int insertUser(LoanUser loanUser, UserOperationHistory userOperationHistory) {
        int count= loanUserMapper.insert(fillInsertBaseEntity(loanUser));
        if(count==1&&userOperationHistory!=null){
            userOperationHistory.setId(null);
            userOperationHistory.setCode(null);
            userOperationHistory.setCreateDate(null);
            userOperationHistory.setUpdateDate(null);
            userOperationHistory.setUserCode(loanUser.getCode());
            userOperationHistory.setOptType(Constants.USER_OPT_TYPE_REGISTER);
            userOperationHistoryTask.executeSaveUserOperationHistory(userOperationHistory);
        }
        return count;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = true)
    public LoanUser getByCode(String code) {
        return loanUserMapper.selectByCode(code);
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = true)
    public LoanUser findByCode(String code) {
        return loanUserMapper.selectByCode(code);
    }

    @Override
    @TargetDataSource(DataSourceType.READ)
    public int count(LoanUser record) {
        return loanUserMapper.count(record);
    }

}
