package com.yh.loan.front.test.idcard;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.utils.DatetimeUtils;
import com.personal.springboot.common.utils.idcard.AddressCode;
import com.personal.springboot.common.utils.idcard.BirthDate;
import com.personal.springboot.common.utils.idcard.ICNumber;
import com.personal.springboot.common.utils.idcard.SeqCode;
import com.personal.springboot.common.utils.idcard.Sex;

public class TestICNumber {

    public static void main(String[] args) {
        System.out.println("测试开始了...");
        Long begin = new Date().getTime();
        List<String> resultList=new ArrayList<>();
        ICNumber ic = null;
        AddressCode ac = new AddressCode("360502");
        for (int j = 0; j < 12; j++) {
            //Timestamp monthPlus = DatetimeUtils.monthPlus(DatetimeUtils.currentTimestamp(), j);
            Timestamp monthPlus = DatetimeUtils.monthPlus(DatetimeUtils.parseTimestamp("1986-04-16 10:00:00"), j);
            String formatDate = DatetimeUtils.formatDate(monthPlus, DatetimeUtils.DATE_PATTERN_IDCARD);
            BirthDate bd = new BirthDate(formatDate);
            Sex[] sexs={Sex.male,Sex.female};
            for (Sex sex : sexs) {
                for (int i = 100; i < 1000; i++) {
                    SeqCode sc = new SeqCode(i+"", sex);
                    if (ac.isValid() && bd.isValid() && sc.isValid()) {
                        ic = new ICNumber(ac, bd, sc);
                        // System.out.println("我身份证号码为：" + ic.toString());
                        // System.out.println("我的性别为：" + ic.getSex());
                        resultList.add(ic.toString());
                    }
                }
            }
        }
        
        //5400条
        System.out.println(JSON.toJSONString(resultList));

        // 默认身份证号码
        // ic = new ICNumber();
        // System.out.println("新身份证号码为：" + ic.toString());
        // System.out.println("ta的性别为：" + ic.getSex());
        Long end = new Date().getTime();
        System.out.println("cast : " + (end - begin) / 1000 + " ms");
        System.out.println("测试结束了...");
    }
    
    
    @Test
    public void testName() throws Exception {
        
    }

}
