package com.personal.springboot.user.service;

import java.util.List;

import com.personal.springboot.service.BaseService;
import com.personal.springboot.user.dao.entity.UserOperationHistory;

public interface UserOperationHistoryService extends BaseService<UserOperationHistory> {
    
    List<UserOperationHistory> selectListByMonth(UserOperationHistory record, int pageIndex, int pageSize);
}
