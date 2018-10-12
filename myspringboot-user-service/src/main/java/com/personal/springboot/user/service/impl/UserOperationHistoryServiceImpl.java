package com.personal.springboot.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.personal.springboot.service.impl.BaseServiceImpl;
import com.personal.springboot.user.dao.entity.UserOperationHistory;
import com.personal.springboot.user.dao.mapper.UserOperationHistoryMapper;
import com.personal.springboot.user.service.UserOperationHistoryService;

/**
 * 用户操作历史信息服务
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月28日
 */
@Service("userOperationHistoryService")
public class UserOperationHistoryServiceImpl extends BaseServiceImpl<UserOperationHistory>
		implements UserOperationHistoryService {
	
    private UserOperationHistoryMapper operationHistoryMapper;
	
	@Autowired
	public UserOperationHistoryServiceImpl(UserOperationHistoryMapper operationHistoryMapper) {
		super(operationHistoryMapper);
		this.operationHistoryMapper = operationHistoryMapper;
	}

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,readOnly = false)
    public List<UserOperationHistory> selectListByMonth(UserOperationHistory record, int pageIndex, int pageSize) {
        return  operationHistoryMapper.selectListByMonth(record.getCreateDate());
    }

}
