package com.personal.springboot.cryption.test;

import java.util.Hashtable;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.cryption.core.RSAEncryptDecryptUtil;

/**
 * 
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年8月22日
 */
public class RSAEncryptDecryptUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAEncryptDecryptUtilTest.class);
    public static final String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkTJ/QF+/943bb55/CxO38OJxUIKTZtIDEKdHWW5P0vHoy1pSdgaLuCz4mrtyuMW4KTcGx2VMYGru4E327WSxJGKOby8FPL6jGW3StrFeo4i9tHoA8t7Y8CO8vT4LtObHeKuq/Q8B7UlfQIxME4NtB4Vc7VHb0yhGiNG4Sduj5m49xDv0msa6pKC7UloRgoQBQ5TJf2jB3xwDh1W6lS219REek5+UdwsaYAkI1DLxQBhrsDtZ0jEIBUwKwBYx+L6fchssg5glPCKfKE6hSEjY1hwy7sEVGFPT03FXoZRceIc45DeUi9/iDcHyTAOtTRrsF9yfFmwFpMG4nxkYeYeuxAgMBAAECggEAV4VV0IUf29AyvbvqF9fNqoEBjbMB/dKwkU5xsE2CSGmqcrBp5GltQH8OsY6OkeBU7Po0xyesoveCrfD8FjPPanTMmq50oFkT/+4x/l4DKDys5OTNjQqT8fEUcNBbP0FvpZiZCh/uRgM8MeytMfkR80s1q6432/hUYmKpi/555pR/5BYM/PU9DkB9JqdKPvPy3VRr2CgY7gVeGyckV8dbYtnn0M33mQ7SYwrpyFXp0B0Jh0Z1ji+FE0FValuWXTqb5hcxnxss44nP+3TRBcUYieUmbcAwd2zzYS6DtG8YhYmMDS5M8TMnHn4oMOachDvK7t5PeAgyXfJjH7wIg/mzkQKBgQD4B1SoFtxHQiqrK0RIqx+fo0wr+XlBFbQ2/NQo3YXMx9ZdJliKreZSVHkm+S8oJliHuc2/Vpb9+p/1jorQ8PIYXxy9PhdIPyV4YI3MaeTtydbQV+u0LC5VV65LjymEfWxV1qo2VUfh6vRwuJsjd/n0Q/R/hpOJdg+1rPZY8C+KNQKBgQCplGfsmqqWOhCTtG9JfF2hhW0Ez8/d922qLQFVvqbqjqDw5AyiKuJvP8OlfpUxvXAN5z7rxlB2p4W09Fwgg0wWC0iphJaZPS9mXty+UHNH4qrjR9jKGHXnMJRz9xU26t+m/LN215ntAX9ZM1FzY8VV/pfhQdrUf2L5ID/VIN0rDQKBgDQFxZnkAZvoou54CsB7NAzVwXpPv0EJfl0yNoXDwPasy6eKDXQ4+WA3W5nzFhMDx/nf3OnPDaiD0t08Yp1kjELUYEDJLzT2GbPf/J3tcBj4Bth4OHX39lfsJiCvDHQ/omN2vQjcZlFaaJ7j+ZY86e5z+mptHbDtVGo0tw78gdwpAoGBAKevhXhUlJyKfDviby1GWjvO+TeJ2u2NrpZZfHlyVhGYyhKKHuf46gHCFs++NP+DaJuG8BQq/QPnsXJ3M+Y+ju05aTefUV/kPcvHwBY87VHHFN47/GYS+/uJcTKewA6XAGNafIfmuGHBWlE1SrSEkuzvHPZ0rVP1R0Q7nIM+xRS9AoGAB49RGVlQo97ns8PdhQEHrl/MRB377WgFvsOFqu2w9Uoap0YvqA/kRq9mtqLEiRFlet+3H3dqFOnpjvCOSo2g4g4QSCOZI1Vequ3hsNc+3Zz7kdXmGA6bDOeJxe8aZx4v22xTi6+s/qNExFmut8fvHnBL8/LgWZr9w2GxLLmCsBs=";
    public static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApEyf0Bfv/eN22+efwsTt/DicVCCk2bSAxCnR1luT9Lx6MtaUnYGi7gs+Jq7crjFuCk3BsdlTGBq7uBN9u1ksSRijm8vBTy+oxlt0raxXqOIvbR6APLe2PAjvL0+C7Tmx3irqv0PAe1JX0CMTBODbQeFXO1R29MoRojRuEnbo+ZuPcQ79JrGuqSgu1JaEYKEAUOUyX9owd8cA4dVupUttfURHpOflHcLGmAJCNQy8UAYa7A7WdIxCAVMCsAWMfi+n3IbLIOYJTwinyhOoUhI2NYcMu7BFRhT09NxV6GUXHiHOOQ3lIvf4g3B8kwDrU0a7BfcnxZsBaTBuJ8ZGHmHrsQIDAQAB";
    public static final String privateKeyOutter = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCCk3eAUOTu6fBUIu/6f/JUNzI4i4z5CyMtQMvwbHZv35yaLPuz0p1frlx7JEv+hOW1IVmjRMS+qpEmFeVrl92Z4cExZvdj4uN5wt/91tClQeygJwmbWSPLvw39oZ6FOSp5Etxcb3yw0arJ941tdFapxj1EJMw2DOxhAxvUM5Mm2nE6sGf/aMts3tap5rHcFVANToaVmYAVwP2woFPJOpXa2F9/JcNP96DWZikU+8nFtjtjf47c+0apejvLSeHceUZ7kuVCDYcVjtUdzBnIWRbSG1B1i8xMJwcxmGJECrOvvz2NOq0CcqSB6hLjMfP3ER6syD27NFxLKAdBNJxPJPPrAgMBAAECggEAEqIIg38HrDAl41u48v4fDnVx7S+8xi0Yr/R3Ja/UltJJ/pY8tMRxQJ4ZBFmBnqhIPzirGelOSgb8/3pQarIZJURZ1TrkQpS18UIlxItK6solghUQAeMOgJya2n6dqFUoAm72kxo1qOXCgV+aBux7EW8AfyDhIzVk5dMTvYw+i2Sbzi7Vyk+NNKTOTAyQFSmUi4UizkLvZjFNlCkqoXZdKbtzvFNAGyyk8AYu2uHXexXyL1NgfCq2/Vhskk05GAkQXbcpLX7riKKKWTARClKllNUMeZNN55wiVyonFuhu3IrF0G83hLv4Qfwg+Q57zQguBqigo+diLhBLrIc0dJzPwQKBgQDUhAZD9QbNEp0qgRyeGikXJm2TEaaxxVBvuKGJ85L2S2ulLH6qbrzxbncIDPvqiAvoGUQNvm/3M/MHE1tYyvS5+hq7fglqsgMabhe/t/G6CL9o3Lh6kjWxlp3VHhR/JpdJPWxoPcmK/StoBiYEpl7jxFX0KfQr666MK6EBsiMU5QKBgQCdS0l1CSRAX3UOBlRduqaJWc2BxCyu97qwa3AKJVcShxYTrVezh8BEMRnc5WFCFvl0praVD1ijHKJO7DiuJyOGaExlNCL129MJ29/etPvXN3B0OgIyjMZb3aXZjAZAqb2t7Y9ra9+17w3cbrCzp065hC+DseEzEhpkLV4ksR6ojwKBgQCuskE8SAZpOi38rNpLlZHFuA9HYLE5Xcx7qrkQYCylTuaUh3kwvYuQpJAxDAtihqfTCkIJUk1UV14mqODkoyJ+Z3zmN/uhu+gVJ/9Z2OpbVamTmC4GhH6jF/9zHCEZTS38RQQCxRgSq3+tKbDDDcjfT5yg9IQGPt3j2FGoxTlViQKBgQCS6U2btUUZps6yobDMtldzc9/g8vR1G0ZQHpjPsR6JXIh/Kmj+cq1eQ6oPSmKzkre9fzSLNoEoH8dtBW7PIhkpIWMxJyf8ECwlSirnXIzX75zlwYRp6FggV33fGcyBOsjBmV9aCrAwwi1lFDGQKpT56v9h9pMK2z6xEDqc9J1Y+wKBgQC3YMk4GTcKRsuhB5GRT4jhAhIJmRwqSA9I/SG2McfNdIBFiZERR39D54D8cp4a8AQjrOXBaKjHudDW5y9HT86DPXyzxEgEURpi9tLqhdwVTXmkVKR9bjbnYcavTLfyHeRIxeFBWy5Yw5TbxwelRztiWigbrkcEAOn9bkhle51NPA==";
    public static final String publicKeyOutter  = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgpN3gFDk7unwVCLv+n/yVDcyOIuM+QsjLUDL8Gx2b9+cmiz7s9KdX65ceyRL/oTltSFZo0TEvqqRJhXla5fdmeHBMWb3Y+LjecLf/dbQpUHsoCcJm1kjy78N/aGehTkqeRLcXG98sNGqyfeNbXRWqcY9RCTMNgzsYQMb1DOTJtpxOrBn/2jLbN7Wqeax3BVQDU6GlZmAFcD9sKBTyTqV2thffyXDT/eg1mYpFPvJxbY7Y3+O3PtGqXo7y0nh3HlGe5LlQg2HFY7VHcwZyFkW0htQdYvMTCcHMZhiRAqzr789jTqtAnKkgeoS4zHz9xEerMg9uzRcSygHQTScTyTz6wIDAQAB";
//    public static final String rawData="{\"merchantId\":\"SYDEV10001000\",\"signRequestMethod\":\"RSA\",\"apiVersion\":\"1.0.2\",\"timeStamp\":\"2017-09-07 23:00:00\",\"data\":{\"idCardNo\":\"130634111100000000\",\"mobile\":\"18611478781\",\"username\":\"刘保\"}}";
//    public static final String rawData="{\"merchantId\":\"SYDEV10001000\",\"signRequestMethod\":\"RSA\",\"apiVersion\":\"1.0.2\",\"timeStamp\":\"2017-09-07 23:00:00\",\"idCardNo\":\"130634111100000000\",\"mobile\":\"18611478781\",\"username\":\"刘保\"}";
    public static final String rawData="{\"merchantId\":\"SITSY10001000\",\"signRequestMethod\":\"RSA\",\"apiVersion\":\"1.0.2\",\"timeStamp\":\"2017-09-07 23:00:00\",\"idCardNo\":\"130634111100000000\",\"mobile\":\"18611478781\",\"username\":\"刘保\"}";

    @Test
    public void testEncryptByPublicKey() throws Exception {
        String publicKeyOutter1="";
        String rawData="{\"ret\":1,\"start\":-1,\"end\":-1,\"country\":\"中国\",\"province\":\"江苏\",\"city\":\"苏州\",\"district\":\"\",\"isp\":\"\",\"type\":\"\",\"desc\":\"\"}";
        String encrypt = RSAEncryptDecryptUtil.encryptByPublicKey(rawData, publicKeyOutter1);
        LOGGER.info("encrypt:{}",encrypt.length());
        LOGGER.info("encrypt的结果为:{}",encrypt);
    }
    
    /**
     */
    @Test
    public void encrypt() throws Exception {
        String encrypt = RSAEncryptDecryptUtil.encryptByPublicKey(rawData, publicKey);
        LOGGER.info("encrypt的结果为:{}",encrypt);
        Map<String, String> initRSAKey = RSAEncryptDecryptUtil.getInstance().initRSAKey();
        LOGGER.info("initRSAKey的结果为:{}",JSON.toJSONString(initRSAKey,true));
    }
    
    /**
     * 空指针异常
     */
    @Test
    public void testName() throws Exception {
        Hashtable< String, String> hashtable=new Hashtable<>();
        hashtable.put("key", null);
        System.out.println(hashtable);
    }
    
    /**
     */
    @Test
    public void decrypt() throws Exception {
        String encData="mYMNw66elakmoQJ3WbAVmxhdWz3vNSKGonEVWFsrFvO8P/d1ZmGh2c2cwpxiElhHiHz4b9O2ihmutFo98SB7ZZJjO2LPP/wFerFsnPvGs8rdS7AddvrfCdciea/+IYM573/giPaSCDVnNqVINZ9fWNtG8+wlTcxc0VDAKkjlnD7mdSx//ORAN6FFOq4M/GZP5NlY5NBNC4FR/fZemG//t7s59gL7TqCTBN1MmfWgzMXctzHifjrsUMR0kvfCGwplbIkrGKt+3xV9GZjX1lb06F5Z1GqZQyS3Lezn08vp9u+rdryuEN5f5v57tK47uYmYH8KRsxlSY/5ztoFVQOLLXw==";
        String decrypt = RSAEncryptDecryptUtil.decryptByPrivateKey(encData, privateKey);
        LOGGER.info("{}",encData.length());
        LOGGER.info("decrypt的结果为:{}",decrypt);
    }
    
    @Test
    public void testAllRequest() throws Exception {
        LOGGER.info("=======================start===========================");
        //TODO 修改公钥信息publicKey  会报错  校验不通过
        //String publicKey=this.publicKey.substring(0,this.publicKey.length()-1)+"C";
        
        //请求：原始报文加密- 内部公钥
        String encrypt = RSAEncryptDecryptUtil.encryptByPublicKey(rawData, publicKey);
        LOGGER.info("encrypt:{}",encrypt.length());
        LOGGER.info("encrypt的结果为:{}",encrypt);
        
        LOGGER.info("==================================================");
        //请求：原始报文加签- 外部商户私钥
        String sign = RSAEncryptDecryptUtil.signByPrivateKey(rawData, privateKey);//测试行
        //String sign = RSAEncryptDecryptUtil.signByPrivateKey(rawData, privateKeyOutter);
        //String sign = RSAEncryptDecryptUtil.signByPrivateKey(encrypt, privateKeyOutter);
        LOGGER.info("sign:{}",sign.length());
        LOGGER.info("sign的结果为:{}",sign);
        
        LOGGER.info("==================================================");
        String encData="bjU4fvZ8Zgx+ax2F/g/zcjpGlFUnmfOt2i5f+FGrALo7vYP4AsGpSllpyivmhRQ5FoPLFtuAy53ZLlwBUx8I3Qho7HI+zQnNQTGLoeHVkJXc5rx6yTqRQXAJEORem1r6zI7smLVfA4n01YkLheSjlQEf1gnaRMQMxtzAE2R+Lxr63XNUX0gbE6ozM7qoMWlunlRTk8s9jyAYJ/TjtJoY9zZ8czI2W7fMzhED11Hn4dlK/gs177pVDZhHtcOSvsg0U2EDTm0RNYgnI/Qf15i5GrKhbc6Gc4gbwGRyXb5ZUwE4l4004qpHWgv/1IAhICQvpGuS6FnlZtqspGYaqyG9LA==";
        encData=encrypt;
        //请求：原始报文解密- 内部私钥
        String decrypt = RSAEncryptDecryptUtil.decryptByPrivateKey(encData, privateKey);
        LOGGER.info("decrypt:{}",decrypt.length());
        LOGGER.info("decrypt的结果为:{}",decrypt);
        
        //请求：原始报文验签- 外部公钥
        //sign="";
        LOGGER.info("==================================================");
        //TODO 修改公钥信息publicKey  会报错  校验不通过
        String publicKey=this.publicKey.substring(0,this.publicKey.length()-1)+"D";
        
        boolean verify = RSAEncryptDecryptUtil.verifyByPublicKey(rawData, publicKey, sign);//测试行
        //boolean verify = RSAEncryptDecryptUtil.verifyByPublicKey(rawData, publicKeyOutter, sign);
        //boolean verify = RSAEncryptDecryptUtil.verifyByPublicKey(encData, publicKeyOutter, sign);
        LOGGER.info("verify的结果为:{}",verify);
        LOGGER.info("==================================================");
        LOGGER.info("{}",publicKey.length());
        LOGGER.info("{}",privateKey.length());
        LOGGER.info("{}",publicKeyOutter.length());
        LOGGER.info("{}",privateKeyOutter.length());
        LOGGER.info("======================end============================");
    }
    
    @Test
    public void testAllResponse() throws Exception {
        String rawData="{\"ret\":1,\"start\":-1,\"end\":-1,\"country\":\"中国\",\"province\":\"江苏\",\"city\":\"苏州\",\"district\":\"\",\"isp\":\"\",\"type\":\"\",\"desc\":\"\"}";
//        String rawData="{\"ret\":1,\"start\":-1,\"end\":-1,\"country\":\"\u4e2d\u56fd\",\"province\":\"\u6c5f\u82cf\",\"city\":\"\u82cf\u5dde\",\"district\":\"\",\"isp\":\"\",\"type\":\"\",\"desc\":\"\"}";
        //响应：原始报文加密- 外部商户公钥
        String encrypt = RSAEncryptDecryptUtil.encryptByPublicKey(rawData, publicKeyOutter);
        LOGGER.info("encrypt:{}",encrypt.length());
        LOGGER.info("encrypt的结果为:{}",encrypt);
        
        //响应：原始报文加签- 内部私钥
        String sign = RSAEncryptDecryptUtil.signByPrivateKey(encrypt, privateKey);
        LOGGER.info("sign:{}",sign.length());
        LOGGER.info("sign的结果为:{}",sign);
        
        //响应：原始报文解密-  外部商户私钥
        //String encData=encrypt;
        //String encData="U5ox4J0hjSDdkky2Wm62SmjnnmiqsiOsYTiHSI/uzfsQYhmdKjLF8MmGHDzKY6i32EXrQzNFISu9mtEdFv4/hmOP3WTXkqNDqS4Vd9/33QcDsH5dbH19KLRYNWFHBnFncCdBUQtLhCCNowcxg3eEfceX2l10O9Qc6FqA9Ad6oMZcYF95E+a9fNHIV/F33Ty8TR8GhkQS/gYQ1pAlKimqSKrwqMNzDZcVcCfgnp1ojBTB6tM7u8UNHF40r8smqrXDpdKTmAoHwujj1EDlZvG3vR+NhMtvCmLLb/TNx/aqEW92kxSihfPofHRIMWvXvcmXl/Nu1ZDU0YahZIgf3Y9yPg==";
        //String encData="Ckpi3c2ExNLqstAhtKpBgvNtFkat6vsfCM4SS1jK1+nZDiGxLCV28AczwI3Epp0ycQ8v3UQHiJLUEqVg4wpB7lskF36OxTZzUTh6Wc8X+FiHtuZfmqGNXTGnU4rg9WVqHow6/5A0sN5VTSsqFlbIkAy9IeKMEXzwlgdtLjk1zFyFNUG7JFsIPzVtFGZXTNBzk6MRAZmz+d42R1aGol/AO57hc3UzQYc0UmrgMMTSJtq5KcKH/i8LgP/gTvgylwC4VQ6XtV8XiKMhFk9MPLQy6C5pNGzCUxGpqnThnKcg9MK8Rp5sc05W4JHVTBQspR7uuBCZhXxsQer8DBFqkQhdsA==";
        String encData="aNW2DUB0LTQhF+ocbhukRneI5Sc8p8bdivnvKrNahNGW9Y0oDnp9HR0TraZtErYZESc/uisyM3T7h7LaTDjmBm/VoUs+YfPPhBlw8zKSMGcVeki8g8KtinXtXXCmPRnVzrFlqCJQGVzvics2yTk83XhrAPc3cMycZQIFnoSJtTnyi74P7uvkG+jbxNtdDa8f2K1JagDRi0czrcSm9NdsbpZ7m5Nf41Lv3g5KwL3K0DeYWVDGfT38U9KBsMbBdmvU8cH7xOHLLrWyDVuzZH7kEdUaOz4T9TwU68PVns/P4kFB5MukZ813zMz64Bu3g3/uTUsa2xXa1BV1VqBF47uS8Q==";
        String decrypt = RSAEncryptDecryptUtil.decryptByPrivateKey(encData, privateKeyOutter);
        LOGGER.info("decrypt:{}",decrypt.length());
        LOGGER.info("decrypt的结果为:{}",decrypt);
        
        //响应：原始报文验签- 内部公钥
        rawData=decrypt;
        Assert.assertEquals(rawData, decrypt);
        rawData="{\"code\":\"0\",\"data\":{\"username\":\"%E5%88%98%E4%BF%9D\",\"custcd\":\"100118\"},\"message\":\"\"}";
        sign="Xnc+Pz+iTj1coBDK9ug2KOusmjrj9tp/O9eHXvMb0stX9c87Oiz7bScj6MM2GX92TX2rMQ+lSpX+6o3WSvRfM1K0O42qJhFEk49WsxFZZcUH3tWaCL4aKpbV8Z32mM/PR6kiwFOwnvbI0sekUCXKop4xqodSeYgD77evL7H9RfDQXOe5hapJVLGpcGU8PU1zTaTbwO2SmYL5K0A2abkYEp4IR79qeLVmsYezCvpfDa22TCTCW6Kn10Q4oCxhvC6li+01ehXuLAzmKyMVQFH87qTR4iP9iiQCDX/W8IDDASlf/2oBUg/fy6d0fcGcLuKvpKYrMfBzfn4SapCE/uKTJw==";
        boolean verify = RSAEncryptDecryptUtil.verifyByPublicKey(rawData, publicKey, sign);
        LOGGER.info("{}",publicKey.length());
        LOGGER.info("{}",privateKey.length());
        LOGGER.info("publicKeyOutter:{}",publicKeyOutter.length());
        LOGGER.info("privateKeyOutter:{}",privateKeyOutter.length());
        LOGGER.info("verify的结果为:{}",verify);
    }
    
    /**
     */
    @Test
    public void sign() throws Exception {
        String sign = RSAEncryptDecryptUtil.signByPrivateKey(rawData,  privateKeyOutter);
        LOGGER.info("sign的结果为:{}",sign);
    }
    
    @Test
    public void verify() throws Exception {
        String sign="EorIYLgZdtK2gIYezUn3PlwWFzkBd0h+C9pzWC++0+/DRVW1kJ5N441z2Xw2pY8oCcTiwFwt7sH9iG8vyJ/noNG4vA3wd0/dEmRlcMOrIE3hsjlwu8P/tZMu0VpelL6EelyuS356DFbZYwg3cpIVPK/JImwaxTE0oHqe3n8tGYNt8liMJkOLRicEhEG7Hj3DEPx9dloI2rwTZ1NI00smvx0n9Blm4htLOzpW1f2PYK+8kqJBl72ccZ2fg/0hWLSTbbMKpoMBnbqM/r7PqrXrR3Z2zAFrGTwVkrvbjufiyYJHuOPen3H276LubrOPjD9tUgE07WW1+gE1d1Qy8naftA==";
        boolean verify = RSAEncryptDecryptUtil.verifyByPublicKey(rawData, publicKeyOutter, sign);
        LOGGER.info("{}",publicKey.length());
        LOGGER.info("{}",privateKey.length());
        LOGGER.info("{}",sign.length());
        LOGGER.info("verify的结果为:{}",verify);
    }

}
