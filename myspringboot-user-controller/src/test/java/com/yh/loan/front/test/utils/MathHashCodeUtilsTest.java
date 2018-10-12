package com.yh.loan.front.test.utils;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.DatetimeUtils;
import com.personal.springboot.common.utils.UUIDGenerator;

/**
 * Math测试类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月13日
 */
public class MathHashCodeUtilsTest {
    
    @Test
    public void testDatetimeUtil() throws Exception {
        int countDays = DatetimeUtils.countDays("2017-12-30");
        System.out.println("countDays:"+Math.abs(countDays));
        Timestamp dayPlus = DatetimeUtils.dayPlus(DatetimeUtils.currentTimestamp(),155);
        System.out.println("dayPlus:"+DatetimeUtils.formatTimestamp(dayPlus));
        int caculate2Days = DateTimeUtil.caculate2Days(DatetimeUtils.currentTimestamp(),dayPlus);
        System.out.println("caculate2Days:"+Math.abs(caculate2Days));
    }
    
    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            String key = UUIDGenerator.generate();
            System.out.println(String.format("key=%s,对应的hashcode结果为:%d", key,Math.abs(key.hashCode()%2)));
        }
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        String key = "41dfae0fa9054f57831811c6cf7899a0";//0
        System.out.println(String.format("key=%s,对应的hashcode结果为:%d", key,Math.abs(key.hashCode()%2)));
        key = "fe7abfc8375e40e499df1b2fd860cfaf";//0
        System.out.println(String.format("key=%s,对应的hashcode结果为:%d", key,Math.abs(key.hashCode()%2)));
        key = "8e1279d963fb499ab86f4592b16f6939";//1
        System.out.println(String.format("key=%s,对应的hashcode结果为:%d", key,Math.abs(key.hashCode()%2)));
        
    }
}
