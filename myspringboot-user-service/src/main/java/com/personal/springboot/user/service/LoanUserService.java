package com.personal.springboot.user.service;

import com.personal.springboot.service.BaseService;
import com.personal.springboot.user.dao.entity.LoanUser;
import com.personal.springboot.user.dao.entity.UserOperationHistory;

/**
 * LoanUser接口定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月11日
 */
public interface LoanUserService extends BaseService<LoanUser>{

    /**
     * 测试数据源注解自动切换方式
     */
    public int count(LoanUser record);
    
    public abstract LoanUser getByCode(String code);

    public abstract LoanUser findByCode(String code);
    
    /**
     * 密码重置接口
     * @param loanUser
     * @param userOperationHistory
     * @return
     */
    int resetPwdByCode(LoanUser loanUser,UserOperationHistory userOperationHistory);

    /**
     * 注册用户信息
     * 
     * @param loanUser
     * @param userOperationHistory
     * @return
     */
    int insertUser(LoanUser loanUser, UserOperationHistory userOperationHistory);

}
