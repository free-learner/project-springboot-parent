package com.personal.springboot.scheduled.start;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.ApplicationContextUtils;
import com.personal.springboot.scheduled.jdbc.JdbcCustomerBasicInfo;
import com.personal.springboot.scheduled.jdbc.JdbcLoanUser;
import com.personal.springboot.scheduled.jdbc.JdbcUserContacts;
import com.personal.springboot.scheduled.jdbc.JdbcUserCreditCard;
import com.personal.springboot.scheduled.jdbc.JdbcUserOperationHistory;
import com.personal.springboot.scheduled.task.Scheduler;

@EnableScheduling
@SpringBootApplication
@ComponentScan("com.personal.springboot")
@EnableAutoConfiguration(exclude={
        DataSourceAutoConfiguration.class,  
            HibernateJpaAutoConfiguration.class, 
            DataSourceTransactionManagerAutoConfiguration.class,  
            }) 
public class MySpringBootApplication {
    private static Logger LOGGER = LoggerFactory.getLogger(MySpringBootApplication.class);

    public static void main(String[] args) {
        LOGGER.info("请求参数信息为:{}",JSON.toJSONString(args));
        args=new String[]{"1","false"};
        if (ArrayUtils.isEmpty(args)) {
            LOGGER.error("请求参数信息为空!");
            System.exit(0);
            return;
        }
        boolean productFlag=false;
        if(args.length>=2){
            //"true"
            productFlag = Boolean.valueOf(args[1]);
        }
        LOGGER.info("MySpringBootApplication Prd Environment:[{}]",productFlag);
        LOGGER.info("MySpringBootApplication Begin Started");
        ConfigurableApplicationContext context = SpringApplication.run(MySpringBootApplication.class, args);
        LOGGER.info("MySpringBootApplication Finished Start...");
        LOGGER.info("MySpringBootApplication Mian Continue Started");
        try {
            Scheduler scheduler = ApplicationContextUtils.getBean(Scheduler.class);
            Assert.notNull(scheduler,"scheduler对象注入为空!");
            Environment env= ApplicationContextUtils.getBean(Environment.class);
            Assert.notNull(env,"Environment对象注入为空!");
           String localFilePath= env.getProperty("local.excel.filePath");
           LOGGER.warn("Main方法执行[localFilePath]值为:{}",localFilePath);
            switch (args[0]) {
            case "0":
                LOGGER.warn("请求[FTP上传]参数为{}..",args[0]);
                scheduler.getFtpUpload();
                LOGGER.warn("Main方法执行请求[FTP上传]参数为{}..结束",args[0]);
                break;
            case "1":
                LOGGER.info("请求[客户信息表]参数为{}..",args[0]);
                JdbcLoanUser.getLoanUser(productFlag,localFilePath);
                LOGGER.info("Main方法执行请求[客户信息表]参数为{}..结束",args[0]);
                break;
            case "2":
                LOGGER.info("请求[获取客户信息表-联系人]参数为{}..",args[0]);
                JdbcUserContacts.getUserContacts1(productFlag,localFilePath);
                Thread.sleep(6000);
                JdbcUserContacts.getUserContacts2(productFlag,localFilePath);
                LOGGER.info("Main方法执行请求[获取客户信息表-联系人]参数为{}..结束",args[0]);
                break;
            case "3":
                LOGGER.info("请求[客户审批表信息]参数为{}..",args[0]);
                JdbcCustomerBasicInfo.getCustomerBasicInfo(productFlag,localFilePath);
                LOGGER.info("Main方法执行请求[客户审批表信息]参数为{}..结束",args[0]);
                break;
            case "4":
                LOGGER.info("请求[设备授权信息表]参数为{}..",args[0]);
                JdbcUserOperationHistory.getUserOperationHistory(productFlag,localFilePath);
                LOGGER.info("Main方法执行请求[设备授权信息表]参数为{}..结束",args[0]);
                break;
            case "5":
                LOGGER.info("请求[信用卡信息列表]参数为{}..",args[0]);
                JdbcUserCreditCard.getUserCreditCard(productFlag,localFilePath);
                LOGGER.info("Main方法执行请求[信用卡信息列表]参数为{}..结束",args[0]);
                break;
            default:
                LOGGER.info("Main方法执行请求[default],参数为{}..开始",args[0]);
                scheduler.testEnvironment();
                LOGGER.warn("Main方法执行请求[default]参数为{}..结束",args[0]);
                break;
            }
            Thread.sleep(6000);
            SpringApplication.exit(context, new MyExitException());
            LOGGER.info("MySpringBootApplication Mian Shutdown");
            System.exit(0);
        } catch (Exception e) {
            LOGGER.error("MySpringBootApplication Mian Exception Exit",e);
        }
        LOGGER.info("MySpringBootApplication Mian Continue Finished Started");
    }
}