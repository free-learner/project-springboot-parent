package com.yh.loan.front.test.base;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.personal.springboot.Application;

/**
 * 基础测试类
 * @SpringApplicationConfiguration(classes = Application.class)
 * @RunWith(SpringJUnit4ClassRunner.class)
 * 替换为
 * @RunWith(SpringRunner.class)
 * @SpringBootTest(classes=Application.class)
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebAppConfiguration
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class)
@ActiveProfiles({"dev"})
//@ActiveProfiles("test")
public class BaseServicesTest {

    @Before
    public void testBefore() {
        System.out.println("測試开始执行。。。");
    }

    @After
    public void testAfter() {
        System.out.println("測試执行結束。。。");
    }
    
    @Test
    public void testEnv() throws Exception {
    }

}