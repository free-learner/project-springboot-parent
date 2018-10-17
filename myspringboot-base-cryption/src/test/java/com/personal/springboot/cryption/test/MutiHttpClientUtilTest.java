package com.personal.springboot.cryption.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.cryption.http.CommHttpConstants;
import com.personal.springboot.cryption.http.MutiHttpClientUtil;

/**
 * HttpClient测试工具类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年8月24日
 */
public class MutiHttpClientUtilTest  {
    private static final Logger LOGGER = LoggerFactory.getLogger(MutiHttpClientUtilTest.class);

    private static final MutiHttpClientUtil mutiHttpClientUtil=new MutiHttpClientUtil();
    
    @Test
    public void testMutiHttpClientUtilFormGet() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("userToken", "TOKEN_5a4b7505cf7148ff850194acbf4f4f3e");
        param.put("mobilePhone", "15056968620");
        String authHeader = JSON.toJSONString(param);
        
       Map<String,String> requestHeaderMap=new HashMap<>();
       requestHeaderMap.put("method","getRepaymentPlanTrial");
       requestHeaderMap.put(CommHttpConstants.AUTH_HEADER, authHeader);
       requestHeaderMap.put(CommHttpConstants.HEADER_COOKIE_KEY,"");
       requestHeaderMap.put(CommHttpConstants.HEADER_X_REAL_IP_KEY,"");
       
        //String url="http://10.0.68.220/yhloan/LoanBusinessServlet?method=rtnAmount&channel=01&custcd=100000526&cino=20171122000010";
        String url="http://10.0.68.220/yhloan/LoanBusinessServlet";
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("method", "rtnAmount");
        paramMap.put("channel", "01");
        paramMap.put("custcd", "100000526");
        paramMap.put("cino", "20171122000010");
        Map<String, String> responseMap= mutiHttpClientUtil.doFormGet(url,requestHeaderMap,paramMap,true);
        LOGGER.info("http请求結果1為:{}",JSON.toJSONString(responseMap));
        paramMap.put("typeCode", "register");
        url="http://10.0.12.26/LoanFrontService/verifyCode/img";
        responseMap = mutiHttpClientUtil.doFormGet(url,requestHeaderMap,paramMap,false);
        LOGGER.info("http请求結果2為:\n{}",JSON.toJSONString(responseMap,true));
        String queryString="?method=rtnAmount&channel=01&custcd=100000526&cino=20171122000010";
        responseMap= mutiHttpClientUtil.doFormGet(url,requestHeaderMap,queryString);
        LOGGER.info("http请求結果3為:{}",JSON.toJSONString(responseMap));
        queryString="method=rtnAmount&channel=01&custcd=100000526&cino=20171122000010";
        responseMap= mutiHttpClientUtil.doFormGet(url,requestHeaderMap,queryString);
        LOGGER.info("http请求結果4為:{}",JSON.toJSONString(responseMap));
    }
    
    @Test
    public void testMutiHttpClientUtilFormGet2() {
        //String url="http://10.0.68.220/yhloan/LoanBusinessServlet?method=goApplyLoan&channel=01&CI_LINKMAN_CELL1=13636363636&CI_LINKMAN_NAME2=%E4%BD%B3%E4%BD%B3&CI_LINKMAN_NAME1=%E4%BF%8A%E4%BF%8A&CI_LINKMAN_CELLS=[\"13636363636\",\"15056968658\"]&lnid=101&CI_ADDRESSBOOKCOUNT=12&CI_LINKMAN_RELA1=%E9%85%8D%E5%81%B6&custcd=100000526&CI_ISYHMEMBER=1&CI_LINKMAN_CELL2=15056968658&userType=101";
        String url="http://10.0.68.220/yhloan/LoanBusinessServlet?";
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("method", "goApplyLoan");
        paramMap.put("channel", "01");
        paramMap.put("CI_LINKMAN_CELL1", "13636363636");
        paramMap.put("CI_LINKMAN_NAME2", "%E4%BD%B3%E4%BD%B3");
        paramMap.put("CI_LINKMAN_NAME1", "%E4%BF%8A%E4%BF%8A");
        paramMap.put("CI_LINKMAN_CELLS", "[\"13636363636\",\"15056968658\"]");
        paramMap.put("CI_ADDRESSBOOKCOUNT", "12");
        paramMap.put("lnid", "101");
        paramMap.put("CI_LINKMAN_RELA1", "%E9%85%8D%E5%81%B6");
        paramMap.put("custcd", "100000528");
        paramMap.put("CI_ISYHMEMBER", "1");
        paramMap.put("CI_LINKMAN_CELL2", "15056968658");
        paramMap.put("userType", "101");
        Map<String, String> responseMap= mutiHttpClientUtil.doFormGet(url,null,paramMap,false);
        LOGGER.info("http请求結果為:{}",JSON.toJSONString(responseMap));
    }
    
    @Test
    public void testMutiHttpClientUtilFormGet22() {
        String url="https://10.0.12.26/LoanFrontService/userCenter/verifyCodeSetInfo/0";
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("typeCode", "register");
        String responseMap= mutiHttpClientUtil.doHttpsJosnBodyPost(url, paramMap);
        LOGGER.info("http请求結果為:{}",JSON.toJSONString(responseMap));
    }
    
    @Test
    public void testSendHttpsJosnBodyPost() throws InterruptedException {
        String url="http://10.0.12.26/LoanFrontService/userCenter/verifyCodeSetInfo/0";
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("typeCode", "register");
        String responseString= mutiHttpClientUtil.sendPostJosnBody(url,paramMap);
        int i=0;
        while(i++<5){
            LOGGER.info("http请求結果1為:{}",JSON.toJSONString(responseString));
            url = "https://10.0.12.26/LoanFrontService/odsAuth/listAllBankInfo/0";
            responseString= mutiHttpClientUtil.sendPostJosnBody(url,paramMap);
            LOGGER.info("https请求[{}]結果2為:{}",i,JSON.toJSONString(responseString));
        }
        
        LOGGER.info("**********************************************************************************");
        url = "http://10.0.68.220/yhloan/LoanBusinessServlet?method=viewLoanProduct&channel=01&custcd=100000553";
        responseString= mutiHttpClientUtil.sendGet(url,paramMap);
        LOGGER.info("http请求結果3為:{}",JSON.toJSONString(responseString));
        paramMap.clear();
        responseString= mutiHttpClientUtil.sendGet(url,paramMap);
        LOGGER.info("http请求結果4為:{}",JSON.toJSONString(responseString));
        responseString= mutiHttpClientUtil.sendGet(url);
        LOGGER.info("http请求結果5為:{}",JSON.toJSONString(responseString));
    }
    
    @Test
    public void testMutiHttpClientUtilFormGet3() {
        //String url="http://10.0.68.220/yhloan/LoanBusinessServlet?method=goApplyLoan&channel=01&CI_LINKMAN_CELL1=13636363636&CI_LINKMAN_NAME2=%E4%BD%B3%E4%BD%B3&CI_LINKMAN_NAME1=%E4%BF%8A%E4%BF%8A&CI_LINKMAN_CELLS=[\"13636363636\",\"15056968658\"]&lnid=101&CI_ADDRESSBOOKCOUNT=12&CI_LINKMAN_RELA1=%E9%85%8D%E5%81%B6&custcd=100000526&CI_ISYHMEMBER=1&CI_LINKMAN_CELL2=15056968658&userType=101";
        String url="http://10.0.68.220/yhloan/LoanBusinessServlet?";
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("method", "goApplyLoan");
        paramMap.put("channel", "01");
        paramMap.put("CI_LINKMAN_CELL1", "13636363636");
        paramMap.put("CI_LINKMAN_NAME2", "名字2");
        paramMap.put("CI_LINKMAN_NAME1", "名字1");
        paramMap.put("CI_LINKMAN_CELLS", "[\"13636363636\",\"15056968658\"]");
        paramMap.put("CI_ADDRESSBOOKCOUNT", "12");
        paramMap.put("lnid", "101");
        paramMap.put("CI_LINKMAN_RELA1", "非常关系");
        paramMap.put("custcd", "100000528");
        paramMap.put("CI_ISYHMEMBER", "1");
        paramMap.put("CI_LINKMAN_CELL2", "15056968658");
        paramMap.put("userType", "101");
        Map<String, String> responseMap= mutiHttpClientUtil.doFormGet(url,null, paramMap,true);
        LOGGER.info("http请求結果為:{}",JSON.toJSONString(responseMap));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testMutiHttpClientUtilFormPost() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("userToken", "TOKEN_5a4b7505cf7148ff850194acbf4f4f3e");
        param.put("mobilePhone", "15056968620");
        String authHeader = JSON.toJSONString(param);
        
       Map<String,String> requestHeaderMap=new HashMap<>();
       requestHeaderMap.put("method","getRepaymentPlanTrial");
       requestHeaderMap.put(CommHttpConstants.AUTH_HEADER, authHeader);
       requestHeaderMap.put(CommHttpConstants.HEADER_COOKIE_KEY,"");
       requestHeaderMap.put(CommHttpConstants.HEADER_X_REAL_IP_KEY,"");
       
        String contentType = ContentType.APPLICATION_JSON.getMimeType();
        String url="http://10.0.12.26/LoanFrontService/banner/listAll/0";
        Map<String,Object> paramMap = new HashMap<String,Object>();
         String responseData = mutiHttpClientUtil.doFormPost(url, contentType,requestHeaderMap,paramMap,true);
         LOGGER.info("http请求結果1為:{}",responseData);
         
         url="http://10.0.68.220/yhloan/LoanBusinessServlet";
         paramMap = new HashMap<String,Object>();
         paramMap.put("method", "loanListDetail");
         paramMap.put("channel", "01");
         paramMap.put("loanStatus", "3");
         paramMap.put("loanType", "01");
         paramMap.put("custcd", "100000529");
         paramMap.put("cino", "20171222000015");
         responseData = mutiHttpClientUtil.doFormPostNew(url, contentType,requestHeaderMap,paramMap,true);
         LOGGER.info("http请求結果2為:{}",responseData);
    }
    
    @Test
    public void testMutiHttpClientUtilJsonBodyPost() {
        String contentType = ContentType.APPLICATION_JSON.getMimeType();
        Map<String, String> param = new HashMap<String, String>();
        param.put("userToken", "TOKEN_dae7c0512e3c46c3adffae7e8c0f26a9");
        param.put("mobilePhone", "15056968620");
        String authHeader = JSON.toJSONString(param);
        
       Map<String,String> requestHeaderMap=new HashMap<>();
       requestHeaderMap.put(CommHttpConstants.AUTH_HEADER, authHeader);
       requestHeaderMap.put(CommHttpConstants.HEADER_COOKIE_KEY,"");
       requestHeaderMap.put(CommHttpConstants.HEADER_X_REAL_IP_KEY,"");
       
        //LIST测试
        String url="http://10.0.12.26/LoanFrontService/userCenter/phoneContactsUp/0";
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> contactsMap = new HashMap<String,String>();
        contactsMap.put("contactTel", "18888888881");
        contactsMap.put("contactName", "LIU刘保1");
        list.add(contactsMap);
        contactsMap = new HashMap<String,String>();
        contactsMap.put("contactTel", "18888888882");
        contactsMap.put("contactName", "LIU刘保2");
        list.add(contactsMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("contacts", list);
        paramMap.put("totalCount", "10");
        String responseData = mutiHttpClientUtil.doJsonBodyPost(url, contentType,requestHeaderMap,JSON.toJSONString(paramMap));
        LOGGER.info("http请求結果1為:{}",JSON.toJSONString(responseData));
        responseData = mutiHttpClientUtil.doJsonBodyPost(url, contentType,requestHeaderMap,paramMap);
        LOGGER.info("http请求結果2為:{}",JSON.toJSONString(responseData));
        url = "http://10.0.12.26/LoanFrontService/userCenter/verifyCodeSetInfo/0";
        String content = "{\"typeCode\":\"register\"}";
        responseData = mutiHttpClientUtil.doJsonBodyPost(url, contentType,requestHeaderMap,content);
        LOGGER.info("http请求結果3為:{}",responseData);
    }
    
    @Test
    public void testMutiHttpClientUtilJsonBodyPost2() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("userToken", "TOKEN_5a4b7505cf7148ff850194acbf4f4f3e");
        param.put("mobilePhone", "15056968620");
        String authHeader = JSON.toJSONString(param);
        
       Map<String,String> requestHeaderMap=new HashMap<>();
       requestHeaderMap.put("method","getRepaymentPlanTrial");
       requestHeaderMap.put(CommHttpConstants.AUTH_HEADER, authHeader);
       requestHeaderMap.put(CommHttpConstants.HEADER_COOKIE_KEY,"");
       requestHeaderMap.put(CommHttpConstants.HEADER_X_REAL_IP_KEY,"");
       
        String contentType = ContentType.APPLICATION_JSON.getMimeType();
        String url="http://10.0.68.220:8080/yhloan/InterfaceServlet";
        Map<String,String> appBody = new HashMap<String,String>();
        appBody.put("custcd",  "100000526");
        appBody.put("lnid",  "101");
        appBody.put("amt",  "1100000");
        appBody.put("rtntype",  "10");
        appBody.put("loandays",  "90");
        appBody.put("rate",  "6.5");
        Map<String,String> appHead = new HashMap<String,String>();
        appHead.put("content-type",  "application-json;charset=utf-8");
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("APP_BODY",appBody );
        paramMap.put("APP_HEAD",appHead);
        //paramMap.put("method",  "getRepaymentPlanTrial");
        String responseData = mutiHttpClientUtil.doJsonBodyPost(url, contentType,requestHeaderMap,paramMap);
        LOGGER.info("http请求結果為:{}",JSON.toJSONString(responseData));
    }
    
    @Test
    public void testMutiHttpClientUtilHttpsPost() {
        String contentType = ContentType.APPLICATION_JSON.getMimeType();
        Map<String, String> param = new HashMap<String, String>();
        param.put("userToken", "TOKEN_fe06f48280364c64a74294f02fce68cd");
        param.put("mobilePhone", "15056968620");
        String authHeader = JSON.toJSONString(param);
        
       Map<String,String> requestHeaderMap=new HashMap<>();
       requestHeaderMap.put(CommHttpConstants.AUTH_HEADER, authHeader);
       requestHeaderMap.put(CommHttpConstants.HEADER_COOKIE_KEY,"");
       requestHeaderMap.put(CommHttpConstants.HEADER_X_REAL_IP_KEY,"");
       
        //LIST测试
        String url="https://10.0.12.26/LoanFrontService/userCenter/phoneContactsUp/0";
        List<Map<String,String>> list=new ArrayList<>();
        Map<String,String> contactsMap = new HashMap<String,String>();
        contactsMap.put("contactTel", "18888888881");
        contactsMap.put("contactName", "LIU刘保1");
        list.add(contactsMap);
        contactsMap = new HashMap<String,String>();
        contactsMap.put("contactTel", "18888888882");
        contactsMap.put("contactName", "LIU刘保2");
        list.add(contactsMap);
        
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("contacts", list);
        paramMap.put("totalCount", "10");
        String responseData = mutiHttpClientUtil.doHttpsJosnBodyPost(url, contentType, requestHeaderMap,paramMap);
        LOGGER.info("https请求結果1為:{}",JSON.toJSONString(responseData));
        
        url="https://10.0.12.26/LoanFrontService/verifyCode/img";
        paramMap = new HashMap<String,Object>();
        responseData = mutiHttpClientUtil.doHttpsJosnBodyPost(url, contentType,null,JSON.toJSONString(paramMap));
        LOGGER.info("https请求結果2為:{}",JSON.toJSONString(responseData));
        
        paramMap=new HashMap<>();
        paramMap.put("typeCode", "register");
        url = "https://10.0.12.26/LoanFrontService/userCenter/verifyCodeSetInfo/0";
        responseData = mutiHttpClientUtil.doHttpsJosnBodyPost(url, contentType,null,paramMap);
        LOGGER.info("https请求結果3為:{}",responseData);
        
    }
    
    @Test
    public void testMutiHttpClientUtilHttpsGet() {
        String contentType = ContentType.APPLICATION_JSON.getMimeType();
        Map<String, String> param = new HashMap<String, String>();
        param.put("userToken", "TOKEN_dae7c0512e3c46c3adffae7e8c0f26a9");
        param.put("mobilePhone", "15056968620");
        String authHeader = JSON.toJSONString(param);
        
       Map<String,String> requestHeaderMap=new HashMap<>();
       requestHeaderMap.put(CommHttpConstants.AUTH_HEADER, authHeader);
       requestHeaderMap.put(CommHttpConstants.HEADER_COOKIE_KEY,"");
       requestHeaderMap.put(CommHttpConstants.HEADER_X_REAL_IP_KEY,"");
       
       String url="https://10.0.12.26/LoanFrontService/verifyCode/img";
        String responseData = mutiHttpClientUtil.doHttpsGet(url, contentType, null);
        LOGGER.info("https请求結果1為:{}",JSON.toJSONString(responseData));
        
        Map<String, Object> paramMap=new HashMap<>();
        paramMap.put("typeCode", "register");
        responseData = mutiHttpClientUtil.doHttpsGet(url, contentType, paramMap, true);
        LOGGER.info("https请求結果2為:{}",JSON.toJSONString(responseData));
    }
    
    @Test
    public void testMutiHttpClientUtilHttpsFormRequest() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("userToken", "TOKEN_dae7c0512e3c46c3adffae7e8c0f26a9");
        param.put("mobilePhone", "15056968620");
        String authHeader = JSON.toJSONString(param);
        
        Map<String,String> requestHeaderMap=new HashMap<>();
        requestHeaderMap.put(CommHttpConstants.AUTH_HEADER, authHeader);
        requestHeaderMap.put(CommHttpConstants.HEADER_COOKIE_KEY,"");
        requestHeaderMap.put(CommHttpConstants.HEADER_X_REAL_IP_KEY,"");
        
        String url="https://10.0.12.26/LoanFrontService/verifyCode/img";
        String responseData = mutiHttpClientUtil.doAllGet(url, null);
        LOGGER.info("https请求結果1為:{}",JSON.toJSONString(responseData));
        
        url = "https://10.0.12.26/LoanFrontService/userCenter/verifyCodeSetInfo/0";
        Map<String, Object> paramMap=new HashMap<>();
        paramMap.put("typeCode", "register");
        //url = "https://10.0.12.26/LoanFrontService/odsAuth/listAllBankInfo/0";
        //paramMap.clear();
        responseData = mutiHttpClientUtil.doAllPost(url, paramMap);
        LOGGER.info("https请求結果2為:{}",JSON.toJSONString(responseData));
    }
    

}
