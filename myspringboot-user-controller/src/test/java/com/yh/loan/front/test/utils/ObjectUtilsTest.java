package com.yh.loan.front.test.utils;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Test;

import com.personal.springboot.common.utils.ObjectUtils;
import com.personal.springboot.user.dao.entity.LoanUser;

/**
 * MD5测试类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月13日
 */
public class ObjectUtilsTest {

    @Test
    public void testValidPassword() throws Exception {
        Object obj = null;
        boolean result = ObjectUtils.isNullOrEmpty(obj);
        System.out.println("判断null结果为:" + result);
        obj = MapUtils.EMPTY_SORTED_MAP;
        result = ObjectUtils.isNullOrEmpty(obj);
        System.out.println("判断MAP结果为:" + result);
        obj = ListUtils.EMPTY_LIST;
        result = ObjectUtils.isNullOrEmpty(obj);
        System.out.println("判断LIST结果为:" + result);
        obj = new LoanUser();
        result = ObjectUtils.isNullOrEmpty(obj);
        System.out.println("判断LoanUser结果为:" + result);
    }

}
