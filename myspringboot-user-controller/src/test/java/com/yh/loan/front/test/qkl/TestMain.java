//package com.yh.loan.front.test.qkl;
//
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.lang3.StringUtils;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.alibaba.fastjson.JSON;
//import com.personal.springboot.common.utils.FastJsonUtil;
//import com.yonghui.supplychain.utils.FabricClient;
//
///**
// * @author JJCHANS
// */
//public class TestMain {
//    private static final Logger LOGGER = LoggerFactory.getLogger(TestMain.class);
//    
//    /**
//     * 插入和查询方法
//     */
//    @Test
//    public void testInsert() throws Exception {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put(ORAGESIGN, publicKey);// 公钥组织加密
//        String data = "{\"custcd\":\"1234567890\",\"custName\":\"供应商\",\"address\":\"北京\",\"corpScale\":\"02\",\"orgType\":\"1\",\"dom\":\"北京\",\"divisioncode\":\"北京\",\"bizType\":\"金融\"}";
//        String result2 = FabricClient.insert(data, map);
//        LOGGER.info("上报数据结果:{}" , result2);//{"payload":"MTVhMDQ4ZjgyMWE4ZjljZjRkMDJjZmI5YjQ5NjRjMWEzOTRmYzE1NGQ2NjI4ZGMxZTQyN2ZiYmYzZTQ5Nzk5MQ==","result":"success","txId":"15a048f821a8f9cf4d02cfb9b4964c1a394fc154d6628dc1e427fbbf3e497991"}
//        Map resultMap = FastJsonUtil.parseObject(result2, Map.class);
//        
//        map.clear();
//        if(StringUtils.equalsIgnoreCase("success", String.valueOf(resultMap.get("result")))){
//            map.put("txId", String.valueOf(resultMap.get("txId")));
//        }
//        map.put("org", ORAGESIGN);
//        map.put("privateKey",privateKey);
//        String queryResult = FabricClient.queryById(map);
//        LOGGER.info("queryById结果为:{}",JSON.toJSONString(queryResult));  
//    }
//    
//    @Test
//    public void testQueryByDate() throws Exception {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("org", ORAGESIGN);
//        map.put("privateKey",privateKey);
//        map.put("date","2018-10-25");
//        List<String> resultq = FabricClient.queryByDate(map);
//        LOGGER.info("queryByDate结果为:{}",JSON.toJSONString(resultq));
//        LOGGER.info("queryByDate结果为:{}",resultq);
//    }
//    
//    
//    static String ORAGESIGN = "yhjr_test";
//    String publicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAETjQ/bIF2La3PVur54jqSxLquD+X4uHBbk0cBQwDKglkl5s/7fQloOduVS3NHEftNEdllhdEOz8nRuk7CkBiJqw==";
//    String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgW0ZQzYllcWwYsQcOqI+mnZVfOGBwWQc0ylPpoUQ6QqOgCgYIKoZIzj0DAQehRANCAAROND9sgXYtrc9W6vniOpLEuq4P5fi4cFuTRwFDAMqCWSXmz/t9CWg525VLc0cR+00R2WWF0Q7PydG6TsKQGImr";
//    
//    public static void main(String[] args) throws Exception {
//        System.out.println("开始执行了===");
//        Map<String, String> map = new HashMap<String, String>();
//        Map<String, String> result = FabricClient.asymmetricGenerator(ORAGESIGN);// 永辉金融组织
//        String publicKey = result.get(ORAGESIGN + "Pub");// 永辉金融组织
//        String privateKey = result.get(ORAGESIGN + "Pri");// 永辉金融组织
//        LOGGER.info("值publicKey为:{}",publicKey);
//        LOGGER.info("值privateKey为:{}",privateKey);
//    }
//
//}
