package com.personal.springboot.scheduled.task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.excel.ExcelWriter;
import com.personal.springboot.common.utils.DatetimeUtils;
import com.personal.springboot.common.utils.FtpUtils;
import com.personal.springboot.scheduled.jdbc.JdbcCustomerBasicInfo;
import com.personal.springboot.scheduled.jdbc.JdbcLoanUser;
import com.personal.springboot.scheduled.jdbc.JdbcUserContacts;
import com.personal.springboot.scheduled.jdbc.JdbcUserCreditCard;
import com.personal.springboot.scheduled.jdbc.JdbcUserOperationHistory;


/**
 * 下午5:15前执行结束
 * 任务执行间隔为5分钟
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年7月4日
 */
@Component
public class Scheduler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
//    private static final String CRON_getLoanUser = "0 0 17 * * ?";
//    private static final String CRON_getUserContacts = "0 05 17 ? * *";
//    private static final String CRON_getCustomerBasicInfo = "0 10 17 ? * *";
//    private static final String CRON_getUserOperationHistory = "0 41 17 ? * *";
//    private static final String CRON_getUserCreditCard = "0 25 17 ? * *";
//    private static final String CRON_getFtpUpload = "0 30 17 ? * *";
    private static final String CRON_getLoanUser = "0 0 17 * * ?";
    private static final String CRON_getUserContacts = "0 02 17 ? * *";
    private static final String CRON_getCustomerBasicInfo = "0 04 17 ? * *";
    private static final String CRON_getUserOperationHistory = "0 08 17 ? * *";
    private static final String CRON_getUserCreditCard = "0 06 17 ? * *";
    private static final String CRON_getFtpUpload = "0 15 17 ? * *";

    /**
     * 获取客户信息表
     *  每天17:00执行一次
     */
    //@Scheduled(cron = CRON_getLoanUser) 
    public void getLoanUser() {
        LOGGER.info("获取客户信息表,[{}]执行一次。开始。。。",CRON_getLoanUser);
        try {
            JdbcLoanUser.getLoanUser(productFlag,localFilePath);
        } catch (Exception e) {
            LOGGER.error("获取客户信息表异常...",e);
        }
        LOGGER.info("获取客户信息表,[{}]执行一次。结束。。。",CRON_getLoanUser);
    }
    
    /**
     * 获取客户信息表-联系人
     *  每天17:02执行一次
     */
    //@Scheduled(cron = CRON_getUserContacts) 
    public void getUserContacts() {
        LOGGER.info("获取客户信息表-联系人,[{}]执行一次。开始。。。",CRON_getUserContacts);
        try {
            JdbcUserContacts.getUserContacts1(productFlag,localFilePath);
        } catch (Exception e) {
            LOGGER.error("获取客户信息表-联系人1异常...",e);
        }
        LOGGER.info("获取客户信息表-联系人1结束,2开始。。。",CRON_getUserContacts);
        try {
            JdbcUserContacts.getUserContacts2(productFlag,localFilePath);
        } catch (Exception e) {
            LOGGER.error("获取客户信息表-联系人2异常...",e);
        }
        LOGGER.info("获取客户信息表-联系人,[{}]执行一次。结束。。。",CRON_getUserContacts);
    }
    
    /**
     * 获取客户审批表信息列表
     *  每天17:10执行一次
     */
    //@Scheduled(cron = CRON_getCustomerBasicInfo) 
    public void getCustomerBasicInfo() {
        LOGGER.info("获取客户审批表信息列表,[{}]执行一次。开始。。。",CRON_getCustomerBasicInfo);
        try {
            JdbcCustomerBasicInfo.getCustomerBasicInfo(productFlag,localFilePath);
        } catch (Exception e) {
            LOGGER.error("获取客户审批表信息列表异常...",e);
        }
        LOGGER.info("获取客户审批表信息列表,[{}]执行一次。结束。。。",CRON_getCustomerBasicInfo);
    }
    
    /**
     * 获取设备授权信息表
     *  每天17:15执行一次
     */
    //@Scheduled(cron = CRON_getUserOperationHistory) 
    public void getUserOperationHistory() {
        LOGGER.info("获取设备授权信息表,[{}]执行一次。开始。。。",CRON_getUserOperationHistory);
        try {
            JdbcUserOperationHistory.getUserOperationHistory(productFlag,localFilePath);
        } catch (Exception e) {
            LOGGER.error("获取设备授权信息表异常...",e);
        }
        LOGGER.info("获取设备授权信息表,[{}]执行一次。结束。。。",CRON_getUserOperationHistory);
    }
    
    /**
     * 获取信用卡信息列表
     *  每天17:25执行一次
     */
    //@Scheduled(cron = CRON_getUserCreditCard) 
    public void getUserCreditCard() {
        LOGGER.info("获取信用卡信息列表,[{}]执行一次。开始。。。",CRON_getUserCreditCard);
        try {
            JdbcUserCreditCard.getUserCreditCard(productFlag,localFilePath);
        } catch (Exception e) {
            LOGGER.error("获取信用卡信息列表异常...",e);
        }
        LOGGER.info("获取信用卡信息列表,[{}]执行一次。结束。。。",CRON_getUserCreditCard);
    }
    
    @Autowired
    private FtpUtils ftpUtils;
    
    @Value("${ftp.conf.remoteFtpPath}")
    private String remoteFtpPath;
    
    @Value("${scheduler.product.flag}")
    private boolean productFlag;
    
    @Value("${local.excel.filePath}")
    private String localFilePath;
    
    @Autowired
    private Environment env;
    
    @Scheduled(fixedRate = 20000)
    public void testEnvironment() {
        LOGGER.info("测试获取Environment信息[{}],[{}]",env.getProperty("ftp.conf.remoteFtpPath"),env.getProperty("scheduler.product.flag"),boolean.class);
    }
    
    /**
     * 将对应的文件上传到对应的ftp服务器
     *  每天17:30执行一次
     */
    //@Scheduled(cron = "0 30 17 ? * *") 
    //@Scheduled(cron = "0 0/1 * * * ?") 
    //@Scheduled(fixedRate = 20000)
    //@Scheduled(cron = CRON_getFtpUpload) 
    public void getFtpUpload() {
        LOGGER.info("将对应的文件上传到对应的ftp服务器,[{}]执行一次。开始。。。",CRON_getFtpUpload);
        try {
            Assert.notNull(ftpUtils,"ftpUtils对象注入为空!");
            //构造对应文件名集合信息
            String[] fileNames = { "客户审批表-TIMESTAMP.xlsx", "客户信息表-TIMESTAMP.xlsx" , "客户信息表-联系人1-TIMESTAMP.xlsx" , "客户信息表-联系人2-TIMESTAMP.xlsx" ,"设备授权信息-TIMESTAMP.xlsx","信用卡信息表-TIMESTAMP.xlsx"};
            String nameReplace = DatetimeUtils.formatDate(DatetimeUtils.currentTimestamp(), DatetimeUtils.DATE_PATTERN_IDCARD);
            List<String> fileNameList=new ArrayList<>();
            for (String fileName : fileNames) {
                fileNameList.add(fileName.replaceAll("TIMESTAMP", nameReplace));
            }
            LOGGER.info("文件上传到对应的本地文件路径为:[{}]",ExcelWriter.filePath);
            LOGGER.info("对应的本地文件信息为:{}",JSON.toJSONString(fileNameList));
            ftpUtils.connectFTPServer();
            for (String fileName : fileNameList) {
                String fullFileName=ExcelWriter.filePath+fileName;
                if(ftpUtils.checkFileExist(fullFileName)){
                    //Boolean result = ftpUtils.uploadFile(fullFileName, new String(fileName.getBytes(), Charset.forName("ISO-8859-1")), remoteFtpPath);
                    //Boolean result = ftpUtils.uploadFile(fullFileName, new String(fileName.getBytes("utf-8"), Charset.forName("ISO-8859-1")), remoteFtpPath);
                    Boolean result = ftpUtils.uploadFile(fullFileName, fileName, remoteFtpPath);
                    LOGGER.info("ftp上传文件信息[{}]结果为:{}",fileName,result);
                }else{
                    LOGGER.info("ftp上传文件信息[{}]不存在!",fileName);
                }
            }
            if(ftpUtils!=null){
                ftpUtils.closeFTPClient();
            }
        } catch (Exception e) {
            LOGGER.error("将对应的文件上传到对应的ftp服务器异常...",e);
        }
        LOGGER.info("将对应的文件上传到对应的ftp服务器,[{}]执行一次。结束。。。",CRON_getFtpUpload);
    }

}