package com.yh.loan.front.test.idcard;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import com.personal.springboot.common.utils.idcard.IdcardUtils;

public class IdcardUtilsTest {

    @Test
    public void testName() throws Exception {
        String addressCode = "362421";
        List<String> resultList = IdcardUtils.generateIdCard(addressCode);
        System.out.println("数量为:" + resultList.size());

        String idCard = "H2167168";
        String[] validateIdCard10 = IdcardUtils.validateIdCard10(idCard);
        System.out.println("validateIdCard10结果为:"+ArrayUtils.toString(validateIdCard10));
        idCard = "Q226144638";
        validateIdCard10 = IdcardUtils.validateIdCard10(idCard);
        System.out.println("validateIdCard10结果为:"+ArrayUtils.toString(validateIdCard10));
        idCard = "360502198604162974";
        boolean result = IdcardUtils.validateCard(idCard);
        System.out.println("validateCard结果result为:"+result);
        idCard = "440253850213582";//true
        idCard = "440253850213583";//fasle
        result = IdcardUtils.validateIdCard15(idCard);
        System.out.println("validateIdCard15结果result为:"+result);
        idCard = "360502198604162974";
        result = IdcardUtils.validateIdCard18(idCard);
        System.out.println("validateIdCard18结果result为:"+result);
        idCard = "H2167168";
        result = IdcardUtils.validateHKCard(idCard);
        System.out.println("validateHKCard结果result为:"+result);
        idCard = "Q226144638";
        result = IdcardUtils.validateTWCard(idCard);
        System.out.println("validateTWCard结果result为:"+result);
    }

}
