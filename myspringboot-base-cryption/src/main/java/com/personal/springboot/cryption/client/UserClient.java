package com.personal.springboot.cryption.client;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.personal.springboot.cryption.core.AESUtil;
import com.personal.springboot.cryption.core.ECCCoderUtil;
import com.personal.springboot.cryption.utils.PropertiesUtil;

/**
 * 对于数据上报等类型的接口,请求的业务参数部分,需要进行加密;
 * 对于数据查询类接口,查询参数直接使用明文
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2018年10月17日
 */
public class UserClient
{
  private static Logger LOGGER = LoggerFactory.getLogger(UserClient.class);

  public static Map<String, String> asymmetricGenerator(String org)
  {
    return ECCCoderUtil.generator(org);
  }

  private static byte[] asymmetricEncrypt(byte[] content, String publicKey)
  {
    return ECCCoderUtil.encrypt(content, publicKey);
  }
  @SuppressWarnings("unused")
private static String asymmetricEncryptStr(byte[] content, String publicKey)
  {
      return Base64.encodeBase64String(asymmetricEncrypt(content, publicKey));
  }

  private static byte[] asymmetricDecrypt(String content, String privateKye)
  {
    return ECCCoderUtil.decrypt(content, privateKye);
  }

  private static byte[] symmetricGenerator()
  {
    return AESUtil.getSecretKey();
  }

  private static String symmetricEncrypt(String content, byte[] secretKey)
  {
    return AESUtil.encrypt(content, secretKey);
  }

  private static String symmetricDecrypt(String content, byte[] secretKey)
  {
    return AESUtil.decrypt(content, secretKey);
  }

  public static String insert(String data, Map<String, String> map)
  {
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    Map<String,String> param = new HashMap<>();
    Map<String,Object> insertParams = new ConcurrentHashMap<>();
    if ((map == null) || (map.size() < 1)) {
      throw new RuntimeException("请传入公钥信息");
    }
    if (StringUtils.isBlank(data)) {
      throw new RuntimeException("必须输入原始数据");
    }
    // 获取aes密钥s后,将数据data进行aes加密,结果为s1
    byte[] s = symmetricGenerator();
    String s1 = symmetricEncrypt(data, s);
    //1.将原始数据data,通过aes密钥进行加密
    param.put("data", s1);
    String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    param.put("date", date);
    for (Map.Entry<String,String> entry : map.entrySet()) {
      String vlaue = (String)entry.getValue();
      //2.通过ECC公钥vlaue,分别加密将aes密钥后,设置为参数值secretPub,并Base64编码;key不变,仍未org1命名.
      byte[] secretPub = asymmetricEncrypt(s, vlaue);
      param.put(entry.getKey(), Base64.encodeBase64String(secretPub));
    }
    String arg = gson.toJson(param);
    String channelID = PropertiesUtil.getValue("channelID");
    String insertUrl = PropertiesUtil.getValue("insertUrl");
    String name = PropertiesUtil.getValue("name");
    String targetPeer = PropertiesUtil.getValue("targetPeer");
    String netId = PropertiesUtil.getValue("netId");

    int[] targetPeers = { Integer.parseInt(targetPeer) };
    String[] args = { arg };
    insertParams.put("args", args);
    insertParams.put("channelID", channelID);
    insertParams.put("fcn", "insert");
    insertParams.put("name", name);
    insertParams.put("targetPeer", targetPeers);
    insertParams.put("netId", Integer.valueOf(Integer.parseInt(netId)));
    String insertJson = gson.toJson(insertParams);
    LOGGER.info("Info of insert >>>>>>>>" + insertJson);
    try {
     // String str = HttpUtil.doPost(insertUrl, insertJson);
      //LOGGER.info(str);
      //return str;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return insertJson;
  }

  public static String queryById(Map<String, String> map)
  {
    Map<String, Object> queryParams = new ConcurrentHashMap<>();
    Gson gson = new Gson();
    String res = null;
    if ((map == null) || (map.size() != 3)) {
      throw new RuntimeException("输入参数个数有误");
    }
    if (map.get("org") == null) {
      throw new RuntimeException("必须传入组织名称");
    }
    if (map.get("privateKey") == null) {
      throw new RuntimeException("必须传入相应组织的私钥");
    }
    String org = (String)map.get("org");
    String privateKey = (String)map.get("privateKey");
    String channelID = PropertiesUtil.getValue("channelID");
    String name = PropertiesUtil.getValue("name");
    String targetPeer = PropertiesUtil.getValue("targetPeer");
    String netId = PropertiesUtil.getValue("netId");
    String queryUrl = PropertiesUtil.getValue("queryUrl");
    int[] targetPeers = { Integer.parseInt(targetPeer) };
    queryParams.put("channelID", channelID);
    queryParams.put("name", name);
    queryParams.put("targetPeer", targetPeers);
    queryParams.put("netId", Integer.valueOf(Integer.parseInt(netId)));
    queryParams.put("queryUrl", queryUrl);
    if (map.get("txId") != null) {
      String[] args = { (String)map.get("txId") };
      queryParams.put("fcn", "queryById");
      queryParams.put("args", args);
      String queryJson = gson.toJson(queryParams);
      res=queryJson;
//      try {
//        res = HttpUtil.doPost(queryUrl, queryJson);
//        logger.info("return data >>>>>>>>>>" + res);
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
    } else {
      throw new RuntimeException("传入的查询参数不正确");
    }
    
    // TODO 测试待删除
    Map<String, String> tmMap =new HashMap<>();
    Map<String, String> paramMap =new HashMap<>();
    paramMap.put("data", "TQFChf2Bwww2aQ3C4DOIAQ8LFbdZmpmZSpguTEVfE2QLKoWb5IwjS183RAzBLzxsETkW/9+fOyLKZ+ERuD+M79cxxpnegn7e2GFzinQF7uU/gLrCAdLT/le9Gbf8XErNyKKMtitOhxmVMBfcGGUPRTwf9VNmd9cDvimWkb64L64tjtqgBEQAvrPa0TWg8Q5yVHqfXP98ihNz0R7TAw4Agg==");
    paramMap.put("org1", "BB13o7VJNtmywwgq1qi+TsUTtOKNmrwkQyJCgJ9aK7Qcp+e0z5UhOGWOFhBBUivzQXBovk05pIBoEEp4vJ+Rm9x3dCGYAnC1aFVG3oZQNsH895QrAEZfHpm5/zcpQM1i1W8GUSOymVw+QStL/uFNV3hbYxD2");
    paramMap.put("org2", "BMknan0BdiQz86QBgebKq08SqtvpqPQu/F9RSmgi5/wx3HjkqBNmmXs5kBMECIt8U9UXuZTOlAObdcZMVrv4W7/Yhwk1+w0ts2U0Scd08Fw02nA/2Ef0oEEPl+5woSH1EtOYtWlRC63QZxUdE2o1aVJFheVy");
    tmMap.put("result", gson.toJson(paramMap));
    res=gson.toJson(tmMap);
    
    if (res != null) {
      Map<?, ?> resMap = gson.fromJson(res, Map.class);
      String dataJson = (String)resMap.get("result");
      Map<?, ?> dataMap = gson.fromJson(dataJson, Map.class);
      String dataSecret = (String)dataMap.get("data");//通过aes密钥加密后的业务参数值.
      String orgPubSecret = (String)dataMap.get(org);//aes密钥通过ECC公钥加密后,再进行Base64编码后的值.
      byte[] aesSecret = asymmetricDecrypt(orgPubSecret, privateKey);//Base64解码后,再ECC私钥解密,获取对应的aes密钥值.
      String result = symmetricDecrypt(dataSecret, aesSecret);
      LOGGER.info("parse data >>>>>>>>>>>>>>>" + result);
      return result;
    }
    return null;
  }

  public static List<String> queryByDate(Map<String, String> map) {
    Map<String, Object> queryParams = new ConcurrentHashMap<>();
    List<String> resList = new ArrayList<>();
    Gson gson = new Gson();
    String res = null;
    if ((map == null) || (map.size() != 3)) {
      throw new RuntimeException("输入参数个数有误");
    }
    if (map.get("org") == null) {
      throw new RuntimeException("必须传入组织名称");
    }
    if (map.get("privateKey") == null) {
      throw new RuntimeException("必须传入相应组织的私钥");
    }
    String org = (String)map.get("org");
    String privateKey = (String)map.get("privateKey");
    String channelID = PropertiesUtil.getValue("channelID");
    String name = PropertiesUtil.getValue("name");
    String targetPeer = PropertiesUtil.getValue("targetPeer");
    String netId = PropertiesUtil.getValue("netId");
    String queryUrl = PropertiesUtil.getValue("queryUrl");
    int[] targetPeers = { Integer.parseInt(targetPeer) };
    queryParams.put("channelID", channelID);
    queryParams.put("name", name);
    queryParams.put("targetPeer", targetPeers);
    queryParams.put("netId", Integer.valueOf(Integer.parseInt(netId)));
    queryParams.put("queryUrl", queryUrl);
    if (map.get("date") != null) {
      queryParams.put("fcn", "queryByDate");
      String[] args = { (String)map.get("date") };
      queryParams.put("args", args);
      String queryJson = gson.toJson(queryParams);
      //return Collections.singletonList(queryJson);
      LOGGER.info("参数信息为:{}",queryJson);
//      try {
//        res = HttpUtil.doPost(queryUrl, queryJson);
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
    } else {
      throw new RuntimeException("传入的查询参数不正确");
    }
    
    
    // TODO 测试待删除
    Map<String, String> tmMap =new HashMap<>();
    Map<String, String> paramMap =new HashMap<>();
    paramMap.put("data", "TQFChf2Bwww2aQ3C4DOIAQ8LFbdZmpmZSpguTEVfE2QLKoWb5IwjS183RAzBLzxsETkW/9+fOyLKZ+ERuD+M79cxxpnegn7e2GFzinQF7uU/gLrCAdLT/le9Gbf8XErNyKKMtitOhxmVMBfcGGUPRTwf9VNmd9cDvimWkb64L64tjtqgBEQAvrPa0TWg8Q5yVHqfXP98ihNz0R7TAw4Agg==");
    paramMap.put("org1", "BB13o7VJNtmywwgq1qi+TsUTtOKNmrwkQyJCgJ9aK7Qcp+e0z5UhOGWOFhBBUivzQXBovk05pIBoEEp4vJ+Rm9x3dCGYAnC1aFVG3oZQNsH895QrAEZfHpm5/zcpQM1i1W8GUSOymVw+QStL/uFNV3hbYxD2");
    paramMap.put("org2", "BMknan0BdiQz86QBgebKq08SqtvpqPQu/F9RSmgi5/wx3HjkqBNmmXs5kBMECIt8U9UXuZTOlAObdcZMVrv4W7/Yhwk1+w0ts2U0Scd08Fw02nA/2Ef0oEEPl+5woSH1EtOYtWlRC63QZxUdE2o1aVJFheVy");
    tmMap.put("result", gson.toJson(Collections.singletonList(paramMap)));
    res=gson.toJson(tmMap);
    
    
    if (StringUtils.isNoneBlank(res)) {
      Map<?, ?> resMap = gson.fromJson(res, Map.class);
      String dataJson = (String)resMap.get("result");
      if (StringUtils.isNoneBlank(dataJson)) {
         List list = JSON.parseArray(dataJson);
        //List list = (List)gson.fromJson(dataJson, new TypeToken() {  } .getType());
        if ((list != null) && (list.size() > 0)) {
          for (int i = 0; i < list.size(); i++) {
            Map dataMap = (Map)list.get(i);
            String dataSecret = (String)dataMap.get("data");
            String orgPubSecret = (String)dataMap.get(org);
            byte[] aesSecret = asymmetricDecrypt(orgPubSecret, privateKey);
            String result = symmetricDecrypt(dataSecret, aesSecret);
            resList.add(result);
          }
          LOGGER.info("parse data >>>>>>>>>>>" + resList);
          return resList;
        }
      }
    }
    return null;
  }

}