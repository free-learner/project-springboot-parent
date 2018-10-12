package com.yh.loan.front.test.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.personal.springboot.user.dao.entity.LoanUser;
import com.personal.springboot.user.service.LoanUserService;
import com.yh.loan.front.test.base.BaseControllerTest;

/**
 * 基础Controller测试类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
public class LoanUserControllerTest extends BaseControllerTest{

    @Autowired
    private LoanUserService loanUserService;

    private String expectedJson;

    @Test
    public void testShow() throws Exception {
        String expectedResult = "hello world!";
        String uri = "/show";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertTrue("错误，正确的返回值为200", status == 200);
        Assert.assertFalse("错误，正确的返回值为200", status != 200);
        Assert.assertTrue("数据一致", expectedResult.equals(content));
        Assert.assertFalse("数据不一致", !expectedResult.equals(content));
    }

    @Test
    public void testShowDaoInt() throws Exception {
        LoanUser testPOJOList = loanUserService.selectByPrimaryKey(1L);
        expectedJson = Obj2Json(testPOJOList);

        String uri = "/showDao?age=10";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        Assert.assertTrue("错误，正确的返回值为200", status == 200);
        Assert.assertFalse("错误，正确的返回值为200", status != 200);
        Assert.assertTrue("数据一致", expectedJson.equals(content));
        Assert.assertFalse("数据不一致", !expectedJson.equals(content));
    }

}