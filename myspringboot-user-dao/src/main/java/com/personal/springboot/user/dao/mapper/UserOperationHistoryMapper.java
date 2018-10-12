package com.personal.springboot.user.dao.mapper;

import java.util.Date;
import java.util.List;

import com.personal.springboot.dao.BaseDAO;
import com.personal.springboot.user.dao.entity.UserOperationHistory;

public interface UserOperationHistoryMapper extends BaseDAO<UserOperationHistory>{

    List<UserOperationHistory> selectListByMonth(Date createDate);

}