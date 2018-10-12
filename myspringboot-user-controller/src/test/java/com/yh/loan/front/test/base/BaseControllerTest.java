package com.yh.loan.front.test.base;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.springboot.Application;
import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.controller.vo.AppAuthHeader;
import com.personal.springboot.controller.vo.ResultInfo;

/**
 * 基础Controller测试类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
public class BaseControllerTest {

    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationConnect;

    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
    }

    /**
     * GET可带请求参数/POST无请求参数信息/請求測試
     */
    @Test
    public void testSelectAll() throws Exception {
        String uri = "/loanUser/selectAll/0";
        AppAuthHeader header=new AppAuthHeader();
        header.setMobilePhone("18611478781");
        header.setUserToken("TOKEN_2ad54d9019bd4cd1b7f09b4ac1593cd2");
        
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Auth-Header", JSON.toJSONString(header))
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        /*String code = "1ea39089948348349bc676a1872a4619";
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri).header("Auth-Header", JSON.toJSONString(header))
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("code", code))
                .andReturn();*/
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull("返回值信息为空!",content);
        Assert.assertTrue("错误，正确的返回值为200", status == 200);
    }
    
    /**
     * POST带参数信息请求测试
     */
    @Test
    public void testFindByCode() throws Exception {
        String uri = "/loanUser/findByCode/0";
        AppAuthHeader header=new AppAuthHeader();
        header.setMobilePhone("18611478781");
        header.setUserToken("TOKEN_2ad54d9019bd4cd1b7f09b4ac1593cd2");
        String code="1ea39089948348349bc676a1872a4619";
        Map<String,String> paramMap=new HashMap<String,String>();
        paramMap.put("code", code);
        ResultMatcher matcher=new ResultMatcher() {
            @Override
            public void match(MvcResult mvcResult) throws Exception {
                String content = mvcResult.getResponse().getContentAsString();
                ResultInfo<?> result = JSON.parseObject(content, ResultInfo.class);
                String resultCode = ErrorCodeConstant.SUCCESS;
                Assert.assertTrue("数据不一致", resultCode.equals(result.getCode()));
            }
        };
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Auth-Header", Obj2Json(header))
                        .characterEncoding("utf-8")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        //.content(JSON.toJSONString(paramMap))
                        .content(Obj2Json(paramMap))
                        .accept(MediaType.APPLICATION_JSON)
                        .param("code", code))
                .andExpect(matcher)
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull("返回值信息为空!",content);
        Assert.assertTrue("错误，正确的返回值为200", status == 200);
    }
    
    protected String Obj2Json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

}