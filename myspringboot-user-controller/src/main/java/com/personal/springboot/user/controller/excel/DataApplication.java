//package com.personal.springboot.user.controller.excel;
//
//import org.apache.commons.lang3.ArrayUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//
///**
// * main启动类
// */
//public class DataApplication {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DataApplication.class);
//
//    public static void main(String[] args) throws Exception {
//        LOGGER.info("请求参数信息为:{}",JSON.toJSONString(args));
//        if(ArrayUtils.isEmpty(args)){
//            LOGGER.error("请求参数信息为空!");
//            return;
//        }
//        switch (args[0]) {
//        case "1":
//            LOGGER.info("请求参数为1..");
//            break;
//        case "2":
//            LOGGER.info("请求参数为2..");
//            break;
//        default:
//            break;
//        }
//        JdbcBaoBiaoUtiles.setTotalCount(100);
//        //JdbcBaoBiaoUtiles.getUserContaxts();
//        LOGGER.info("DataApplication finished start...");
//    }
//
//}
