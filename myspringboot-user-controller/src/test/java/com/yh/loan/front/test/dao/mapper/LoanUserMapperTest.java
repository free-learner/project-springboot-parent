package com.yh.loan.front.test.dao.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.user.dao.entity.LoanUser;
import com.personal.springboot.user.dao.mapper.LoanUserMapper;
import com.yh.loan.front.test.base.BaseServicesTest;

/**
 * 添加事物測試
 * @Rollback(false)
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月10日
 */
public class LoanUserMapperTest extends BaseServicesTest {

    @Autowired
    private LoanUserMapper loanUserMapper;

    @Test
    @Rollback(false)
    public void testSelectByPrimaryKey() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser loanUser = loanUserMapper.selectByPrimaryKey(1L);
        if(loanUser != null) {
        	System.out.println("返回："+loanUser.toString());        	
        } else {
        	System.out.println("返回："+"暂无数据");        	
        }
        System.out.println("testSelectByPrimaryKey測試执行完成了。。。");
    }
    
    @Test
    @Rollback(false)
    public void testSelectAll() {
        Assert.assertNotNull(loanUserMapper);
        List<LoanUser> list = loanUserMapper.selectAll();
        System.out.println("返回："+ list.toString());
        System.out.println("testSelectAll測試执行完成了。。。");
    }
    
    @Test
    public void testSelectByEntity() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
//        user.setCode("0");
        user.setDelFlag(false);//0-false;1-true
        user.setMobilePhone("181");
        user.setUserType("");
        List<LoanUser> list = loanUserMapper.selectByEntity(user);
        System.out.println("返回："+ list.toString());
        System.out.println("testSelectByEntity測試执行完成了。。。");
    }
    
    @Test
    public void testSelectByCode() {
        Assert.assertNotNull(loanUserMapper);
        String code = "1";
        LoanUser loanUser = loanUserMapper.selectByCode(code);
        if(loanUser != null) {
        	System.out.println("返回："+loanUser.toString());        	
        } else {
        	System.out.println("返回："+"暂无数据");        	
        }
        System.out.println("testSelectByCode測試执行完成了。。。");
    }
    
    @Test
    public void testSelectCountByPage() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
//        user.setCode("0");
        user.setDelFlag(false);//0-false;1-true
//        user.setMobilePhone("181");
        user.setUserType("");
        int count = loanUserMapper.selectCount(user);
        System.out.println("返回："+ count);
        System.out.println("testSelectCountByPage測試执行完成了。。。");
    }
    
    @Test
    public void testInsert() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
        user.setCode(UUIDGenerator.generate());
        user.setDelFlag(false);//0-false;1-true
        user.setMobilePhone("181");
        user.setUserType("2");
        user.setChannelType("1");
        user.setStatus(0);
        user.setCreateDate(DateTimeUtil.currentTimestamp());
        user.setUpdateDate(DateTimeUtil.currentTimestamp());
        int count = loanUserMapper.insert(user);
        System.out.println("返回："+ count);
        System.out.println("testInsert測試执行完成了。。。");
    }
    
    @Test
    @Rollback(false)
    public void testInsertSelective() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
        user.setCode(UUIDGenerator.generate());
        user.setDelFlag(false);//0-false;1-true
        user.setMobilePhone("18912341234");
        user.setUserType("2");
        user.setChannelType("1");
        user.setStatus(0);
        user.setCreateDate(DateTimeUtil.currentTimestamp());
        user.setUpdateDate(DateTimeUtil.currentTimestamp());
        int count = loanUserMapper.insertSelective(user);
        System.out.println("返回："+ count);
        System.out.println("testInsertSelective測試执行完成了。。。");
    }
    
    @Test
    public void testUpdateByPrimaryKey() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
        user.setId(6L);
        user.setMobilePhone("123");
        user.setDelFlag(false);//0-false;1-true
        user.setUserType("");
        user.setChannelType("1");
        user.setStatus(1);
        user.setUpdateBy("更新人");
        user.setUpdateDate(DateTimeUtil.currentTimestamp());
        int count = loanUserMapper.updateByPrimaryKey(user);
        System.out.println("返回："+ count);
        System.out.println("testUpdateByPrimaryKey測試执行完成了。。。");
    }
    
    @Test
    public void testUpdateByPrimaryKeySelective() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
        user.setId(6L);
        user.setMobilePhone("11111");
        user.setDelFlag(false);//0-false;1-true
        user.setUserType("");
        user.setChannelType("1");
        user.setStatus(1);
        user.setUpdateBy("更新人");
        user.setUpdateDate(DateTimeUtil.currentTimestamp());
        int count = loanUserMapper.updateByPrimaryKeySelective(user);
        System.out.println("返回："+ count);
        System.out.println("testUpdateByPrimaryKeySelective測試执行完成了。。。");
    }
    
    @Test
    public void testUpdateByCode() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
        user.setId(1L);
        user.setCode("0e9c08f118124a0c929106d19b3340f5");
        user.setMobilePhone("123");
        user.setDelFlag(false);//0-false;1-true
        user.setUserType("");
        user.setChannelType("1");
        user.setStatus(1);
        user.setUpdateBy("更新人");
        user.setUpdateDate(DateTimeUtil.currentTimestamp());
        int count = loanUserMapper.updateByCode(user);
        System.out.println("返回："+ count);
        System.out.println("testUpdateByCode測試执行完成了。。。");
    }
    
    @Test
    public void testUpdateByCodeSelective() {
        Assert.assertNotNull(loanUserMapper);
        LoanUser user = new LoanUser();
        user.setCode("0e9c08f118124a0c929106d19b3340f5");
        user.setMobilePhone("11111");
        user.setDelFlag(false);//0-false;1-true
        user.setUserType("");
        user.setChannelType("1");
        user.setStatus(1);
        user.setUpdateBy("更新人");
        user.setUpdateDate(DateTimeUtil.currentTimestamp());
        int count = loanUserMapper.updateByCodeSelective(user);
        System.out.println("返回："+ count);
        System.out.println("testUpdateByCodeSelective測試执行完成了。。。");
    }
    
    @Test
    public void testDeleteByPrimaryKey() {
        Assert.assertNotNull(loanUserMapper);
        Long id = 1L;
        int count = loanUserMapper.deleteByPrimaryKey(id);
        System.out.println("返回："+count);
        System.out.println("testDeleteByPrimaryKey測試执行完成了。。。");
    }
    
    @Test
    public void testDeleteByCode() {
        Assert.assertNotNull(loanUserMapper);
        String code = "0e9c08f118124a0c929106d19b3340f5";
        int count = loanUserMapper.deleteByCode(code);
        System.out.println("返回："+count);
        System.out.println("testDeleteByCode測試执行完成了。。。");
    }
}