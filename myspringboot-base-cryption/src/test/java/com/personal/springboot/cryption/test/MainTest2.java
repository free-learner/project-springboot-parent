package com.personal.springboot.cryption.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.personal.springboot.common.utils.DateTimeUtil;
import com.yonghui.supplychain.handler.HandlerCallback;
import com.yonghui.supplychain.model.Contract;
import com.yonghui.supplychain.model.Credit;
import com.yonghui.supplychain.model.Expenditure;
import com.yonghui.supplychain.model.Payback;
import com.yonghui.supplychain.model.Register;
import com.yonghui.supplychain.utils.FabricClient;

/**
 * 
 * @Author  LiuBao
 * @Version 2.0
 * @Date 2018年11月2日
 */
public class MainTest2 implements HandlerCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainTest2.class);
    
    private static final String custcd="G0003010";
    private static final String queryDate = "2018-11-06";
    
    /**
     * falg 参数 1对应着注册查询 2-授信 3-合同 4-支用 5-回款
     */
    public static void main(String[] args) throws Exception {
        MainTest2 test = new MainTest2();
     
        Register register =new Register();
        register.setCustcd(custcd);
        register.setTaxpayerNo("9135010011115156XY");
        register.setCustName("福建超创**有限公司");
        register.setAddress("福州市台江区**庆里7号3层D172室");
        register.setCorpScale("1");
        register.setOrgtype("1");
        register.setDom("福州市台江区**庆里7号3层D172室");
        register.setDivisioncode("350100");
        register.setBizType("f5124");
        register.setFlag("1");
        
        Credit credit=new Credit();
        credit.setCustcd(custcd);
        credit.setAuditAmt(new BigDecimal("45000.00"));
        credit.setBalAmt(BigDecimal.ZERO);
        credit.setBrcode("1002");
        credit.setBreedCreditNo("100218SX0335");
        credit.setBreedSmallClass("PD0007");
        credit.setCreditType("1");
        credit.setAuditDate(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        credit.setDueDate(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        credit.setUseType("1");
        credit.setFlag("2");
        
        Contract contract=new Contract();
        contract.setCustcd(custcd);
        contract.setContractno("G100D0002017091155");
        contract.setIsdate(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        contract.setTedate(DateTimeUtil.parseDatetime("2019-10-19",DateTimeUtil.DATE_PATTON_1));
        contract.setTerm("0010000000");
        contract.setShortLoanDays(5);
        contract.setTotamt(new BigDecimal(20000000.00));
        contract.setFlag("3");
        
        Expenditure expenditure=new Expenditure();
        expenditure.setCustno(custcd);
        expenditure.setLncino("QYD.201806031605222190");
        expenditure.setClrClass("1");
        expenditure.setGperidays(3);
        expenditure.setLnamt(new BigDecimal(20000000.00));
        expenditure.setLnbal(new BigDecimal(20000000.00));
        expenditure.setLnco("G100D0002017091155");
        expenditure.setLnid("PD0007");
        expenditure.setLnrtnmod("1");
        expenditure.setLnstat("01");
        expenditure.setRtndt("21");
        expenditure.setSidt(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        expenditure.setEidt(DateTimeUtil.parseDatetime("2019-10-19",DateTimeUtil.DATE_PATTON_1));
        expenditure.setUsdintrate(new BigDecimal(10.00));
        expenditure.setUsdpintrate(new BigDecimal(15.00));
        expenditure.setFlag("4");
        
        Payback payback=new Payback();
        payback.setCustcd(custcd);
        payback.setLncino("ANO.201806031605222190");
        payback.setRtnamt(new BigDecimal("45000.00"));
        //payback.setCreateDate(DateTimeUtil.parseDatetime("2018-10-19",DateTimeUtil.DATE_PATTON_1));
        payback.setCreateDate(DateTimeUtil.currentDate());
        payback.setRtnint(BigDecimal.ZERO);
        payback.setRtnpint(BigDecimal.ZERO);
        payback.setFeeAmt(BigDecimal.ZERO);
        payback.setLoanAmt(payback.getRtnamt().add(payback.getRtnint()).add(payback.getRtnpint()));
        payback.setTransDate(DateTimeUtil.formatDate2Str());
        payback.setFlag("5");
        
        Gson gson = new GsonBuilder().setDateFormat(DateTimeUtil.DATE_TIME_PATTON_1).create();
        //test.testInsert(JSON.toJSONString(payback));
        System.out.println(gson.toJson(contract));
//        test.testInsert(gson.toJson(register));
//        test.testInsert(gson.toJson(credit));
//        test.testInsert(gson.toJson(contract));
        test.testInsert(gson.toJson(expenditure));
//        test.testInsert(gson.toJson(payback));
//        test.testQueryById();
//        test.testQueryByDate();
    }
    
    /**
     * 插入和查询方法
     */
    public void testInsert(String data) throws Exception {
        if(StringUtils.isBlank(data)){
            LOGGER.error("当前参数信息[data]为空!");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put(ORAGESIGN, publicKey);// 公钥组织加密
        FabricClient.insert(data, map,new MainTest2());
    }
    
    @Test
    public void testInsert() throws Exception {
        String data = "{\"custcd\":\"1234567890\",\"custName\":\"供应商\",\"address\":\"北京\",\"corpScale\":\"02\",\"orgType\":\"1\",\"dom\":\"北京\",\"divisioncode\":\"北京\",\"bizType\":\"金融\"}";
        testInsert(data);
    }
    
    public void testQueryByDate(String date) throws Exception {
        if(StringUtils.isBlank(date)){
            LOGGER.error("当前参数信息[date]为空!");
            return;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("org", ORAGESIGN);
        map.put("privateKey",privateKey);
        map.put("date",date);
        FabricClient.queryByDate(map,new MainTest2());
    }
    
    @Test
    public void testQueryByDate() throws Exception {
        testQueryByDate(queryDate);
    }
    
    @Test
    public void testQueryById() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("org", ORAGESIGN);
        map.put("privateKey",privateKey);
        map.put("custcd",custcd); //
        map.put("flag","5");
        FabricClient.queryById(map,new MainTest2());
    }
    
    @Override
    public void back(String s) {
        System.out.println("成功返回时的结果"+s);
        LOGGER.info("区块链数据操作结果:{}" , s);
    }
    
    @Test
    public void testAsymmetricGenerator() throws Exception {
        Map<String, String> result = FabricClient.asymmetricGenerator(ORAGESIGN);// 永辉金融组织
        String publicKey = result.get(ORAGESIGN + "Pub");// 永辉金融组织
        String privateKey = result.get(ORAGESIGN + "Pri");// 永辉金融组织
        LOGGER.info("值publicKey为:{}",publicKey);
        LOGGER.info("值privateKey为:{}",privateKey);
    }
    
    static String ORAGESIGN = "yhjr_test";
    String publicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAETjQ/bIF2La3PVur54jqSxLquD+X4uHBbk0cBQwDKglkl5s/7fQloOduVS3NHEftNEdllhdEOz8nRuk7CkBiJqw==";
    String privateKey = "MIGTAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBHkwdwIBAQQgW0ZQzYllcWwYsQcOqI+mnZVfOGBwWQc0ylPpoUQ6QqOgCgYIKoZIzj0DAQehRANCAAROND9sgXYtrc9W6vniOpLEuq4P5fi4cFuTRwFDAMqCWSXmz/t9CWg525VLc0cR+00R2WWF0Q7PydG6TsKQGImr";
   

}
