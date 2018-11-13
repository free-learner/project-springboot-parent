package com.personal.springboot.cryption.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.cryption.client.UserClient;

public class UserClientTest {
    private static Logger LOGGER = LoggerFactory.getLogger(UserClientTest.class);
    
    @Test
    public void testAsymmetricGenerator() {
        Map<String, String> generateResult = UserClient.asymmetricGenerator(org1);
        LOGGER.info("asymmetricGenerator结果为:{}",JSON.toJSONString(generateResult,true));
        LOGGER.info("org1公钥为:{}",generateResult.get(org1+"Pub")  );
        LOGGER.info("org1私钥为:{}",generateResult.get(org1+"Pri"));
        generateResult = UserClient.asymmetricGenerator(org2);
        LOGGER.info("asymmetricGenerator结果为:{}",JSON.toJSONString(generateResult,true));
        LOGGER.info("org2公钥为:{}",generateResult.get(org2+"Pub")  );
        LOGGER.info("org2私钥为:{}",generateResult.get(org2+"Pri"));
    }
    
    private static final String org1="org1";
    private static final String org2="org2";
    
    private static final String publicKey1="MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAErxh7VtWE2jxKDXLr+2ovOfWtGlzOP7W/ToLYKIian2l68unGUTgND7ckptwcCSmp6BaoVv/RQBtSv8yjfoqxkQ==";
    private static final String privateKey1="MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgS9HMZqLYcodNTiCXfIisqrFsX/u/DyTNFqb+W6mz8iigCgYIKoZIzj0DAQehRANCAASvGHtW1YTaPEoNcuv7ai859a0aXM4/tb9OgtgoiJqfaXry6cZROA0PtySm3BwJKanoFqhW/9FAG1K/zKN+irGR";
    
    private static final String publicKey2="MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEQgKzmXPMFjzucRzifW+OHUIML9DraoQXu6URpKfFqueFPsqvCD/SapoYeuuvhovAWyoU85o5Rbb40CUMl5O24A==";
    @SuppressWarnings("unused")
    private static final String privateKey2="MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgXLDrE5ZRkbNz3YG4QGd61sDeKa+hv71WzvjB8vV8LdWgCgYIKoZIzj0DAQehRANCAARCArOZc8wWPO5xHOJ9b44dQgwv0OtqhBe7pRGkp8Wq54U+yq8IP9Jqmhh666+Gi8BbKhTzmjlFtvjQJQyXk7bg";
    
    /**
     * 插入操作,相当于是加密数据,准备发送
     */
    @Test
    public void testInsert() {
        String data = "{\"custcd\":\"123456\",\"custName\":\"供应商\",\"address\":\"北京\",\"corpScale\":\"02\",\"orgType\":\"1\",\"dom\":\"北京\",\"divisioncode\":\"北京\",\"bizType\":\"金融\"}";
        Map<String,String> basicMap = new HashMap<String,String>();
        basicMap.put(org1,publicKey1);
        basicMap.put(org2,publicKey2);
        String result = UserClient.insert(data,basicMap);
        //{"targetPeer":[1],"netId":2,"fcn":"insert","name":"supplychain","args":["{\"data\":\"TQFChf2Bwww2aQ3C4DOIAQ8LFbdZmpmZSpguTEVfE2QLKoWb5IwjS183RAzBLzxsETkW/9+fOyLKZ+ERuD+M79cxxpnegn7e2GFzinQF7uU/gLrCAdLT/le9Gbf8XErNyKKMtitOhxmVMBfcGGUPRTwf9VNmd9cDvimWkb64L64tjtqgBEQAvrPa0TWg8Q5yVHqfXP98ihNz0R7TAw4Agg==\",\"org1\":\"BB13o7VJNtmywwgq1qi+TsUTtOKNmrwkQyJCgJ9aK7Qcp+e0z5UhOGWOFhBBUivzQXBovk05pIBoEEp4vJ+Rm9x3dCGYAnC1aFVG3oZQNsH895QrAEZfHpm5/zcpQM1i1W8GUSOymVw+QStL/uFNV3hbYxD2\",\"org2\":\"BMknan0BdiQz86QBgebKq08SqtvpqPQu/F9RSmgi5/wx3HjkqBNmmXs5kBMECIt8U9UXuZTOlAObdcZMVrv4W7/Yhwk1+w0ts2U0Scd08Fw02nA/2Ef0oEEPl+5woSH1EtOYtWlRC63QZxUdE2o1aVJFheVy\",\"date\":\"2018-10-17\"}"],"channelID":"testchannel"}
        LOGGER.info("insert结果为:{}",result);
    }
    
    /**
     * 查询参数格式为: 明文
     * 查询数据,相当于获取数据后,进行解密查看结果
     * 
     */
    @Test
    public void testQueryById() {
        Map<String,String> map = new HashMap<>();
        String   privateKey1="BLeEQjZpVjl3qZx3BARKlmuQCFhCuVT7qowrwHVToCcYJgu19RMvaXqXmLrlUJoTxUur/+fGsUGgr4TGr+j++kg8iAWslA300MS6yuIga5fDQucdfznUbfpyT0IEFr6fxtwli+9+WTL6zTSThV5OcmqmXvL3";
        String org1="org1";
        map.put("org", org1);
        map.put("privateKey",privateKey1);
        map.put("txId", "bd1586726d63c24c4658006039ceaf8baaa9eaa49a5aa12af2324ac8e6e01ae6");
        String queryResult = UserClient.queryById(map);
        LOGGER.info("queryById结果为:{}",JSON.toJSONString(queryResult));
    }
    
    @Test
    public void testQueryByDate() {
        Map<String,String> map = new HashMap<>();
        map.put("org", org1);
        map.put("privateKey",privateKey1);
        map.put("date", "2018-10-15");
        List<String> queryResult = UserClient.queryByDate(map);
        LOGGER.info("queryByDate结果为:{}",JSON.toJSONString(queryResult));
    }
    
}