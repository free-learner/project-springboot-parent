package com.personal.springboot.cryption.core;


import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.common.exception.AbsErrorCodeConstant;
import com.personal.springboot.common.exception.BaseServiceException;
import com.personal.springboot.cryption.http.CommHttpConstants;

/**
 * 加解密工具类
 */
public final class RSAEncryptDecryptUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RSAEncryptDecryptUtil.class);
    
	public static final String CHARSET = CommHttpConstants.UTF_8;
    //密钥算法
    public static final String ALGORITHM_RSA = "RSA";
    //RSA 签名算法
    public static final String ALGORITHM_RSA_SIGN = "SHA256WithRSA";
    public static final int ALGORITHM_RSA_PRIVATE_KEY_LENGTH = 2048;
    
    private RSAEncryptDecryptUtil() {
    }
    
    /**
     * 初始化RSA算法密钥对
     *
     * @param keysize RSA1024已经不安全了,建议2048
     * @return 经过Base64编码后的公私钥Map, 键名分别为publicKey和privateKey
     */
    public Map<String, String> initRSAKey() {
        return initRSAKey(ALGORITHM_RSA_PRIVATE_KEY_LENGTH);
    }
    /**
     * 初始化RSA算法密钥对
     *
     * @param keysize RSA1024已经不安全了,建议2048
     * @return 经过Base64编码后的公私钥Map, 键名分别为publicKey和privateKey
     */
    protected Map<String, String> initRSAKey(int keysize) {
        if (keysize != ALGORITHM_RSA_PRIVATE_KEY_LENGTH) {
            throw new IllegalArgumentException("RSA1024已经不安全了,请使用" + ALGORITHM_RSA_PRIVATE_KEY_LENGTH + "初始化RSA密钥对");
        }
        //为RSA算法创建一个KeyPairGenerator对象
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("初始化加密字符串keysize=[" + keysize + "]时遇到异常,{}", e);
            throw new IllegalArgumentException(AbsErrorCodeConstant.ERROR_CODE_92000,e);
        }
        //初始化KeyPairGenerator对象,不要被initialize()源码表面上欺骗,其实这里声明的size是生效的
        kpg.initialize(ALGORITHM_RSA_PRIVATE_KEY_LENGTH);
        //生成密匙对
        KeyPair keyPair = kpg.generateKeyPair();
        //得到公钥
        Key publicKey = keyPair.getPublic();
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        //得到私钥
        Key privateKey = keyPair.getPrivate();
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        Map<String, String> keyPairMap = new HashMap<String, String>();
        keyPairMap.put("publicKey", publicKeyStr);
        keyPairMap.put("privateKey", privateKeyStr);
        return keyPairMap;
    }

    /**
     * RSA算法公钥加密数据
     *
     * @param data 待加密的明文字符串
     * @param publicKey  RSA公钥字符串
     * @return RSA公钥加密后的经过Base64编码的密文字符串
     */
    public static String encryptByPublicKey(String data, String publicKey) {
        try {
            //通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key publickey = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publickey);
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
        } catch (Exception e) {
            LOGGER.error("加密字符串[" + data + "]时遇到异常,{}", e);
            throw new BaseServiceException(AbsErrorCodeConstant.ERROR_CODE_92005, e);
        }
    }
    /**
     * RSA算法公钥解密数据
     *
     * @param data 待解密的经过Base64编码的密文字符串
     * @param publicKey  RSA公钥字符串
     * @return RSA公钥解密后的明文字符串
     */
    @Deprecated
    public static String decryptByPublicKey(String data, String publicKey) {
        try {
            //通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key publickey = keyFactory.generatePublic(x509KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, publickey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data)), CHARSET);
        } catch (Exception e) {
            LOGGER.error("解密字符串[" + data + "]时遇到异常,{}", e);
            throw new BaseServiceException(AbsErrorCodeConstant.ERROR_CODE_92004, e);
        }
    }
    /**
     * RSA算法私钥加密数据
     *
     * @param data 待加密的明文字符串
     * @param privateKey  RSA私钥字符串
     * @return RSA私钥加密后的经过Base64编码的密文字符串
     */
    @Deprecated
    public static String encryptByPrivateKey(String data, String privateKey) {
        try {
            //通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key privatekey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privatekey);
            return Base64.encodeBase64String(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET)));
        } catch (Exception e) {
            LOGGER.error("加密字符串[" + data + "]时遇到异常,{}", e);
            throw new BaseServiceException(AbsErrorCodeConstant.ERROR_CODE_92005, e);
        }
    }
    /**
     * RSA算法私钥解密数据
     * @param data 待解密的经过Base64编码的密文字符串
     * @param privateKey  RSA私钥字符串
     * @return RSA私钥解密后的明文字符串
     */
    public static String decryptByPrivateKey(String data, String privateKey) {
        try {
            //通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            Key privatekey = keyFactory.generatePrivate(pkcs8KeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privatekey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data)), CHARSET);
        } catch (Exception e) {
            LOGGER.error("解密字符串[" + data + "]时遇到异常,{}", e);
            throw new BaseServiceException(AbsErrorCodeConstant.ERROR_CODE_92004, e);
        }
    }
    /**
     * RSA算法使用私钥对数据生成数字签名
     *
     * @param data 待签名的明文字符串
     * @param privateKey  RSA私钥字符串
     * @return RSA私钥签名后的经过Base64编码的字符串
     */
    public static String signByPrivateKey(String data, String privateKey) {
        try {
            //通过PKCS#8编码的Key指令获得私钥对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privatekey = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
            signature.initSign(privatekey);
            signature.update(data.getBytes(CHARSET));
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            LOGGER.error("签名字符串[" + data + "]时遇到异常,{}", e);
            throw new BaseServiceException(AbsErrorCodeConstant.ERROR_CODE_92003, e);
        }
    }
    
    /**
     * RSA算法使用公钥校验数字签名
     *
     * @param data 参与签名的明文字符串
     * @param publicKey  RSA公钥字符串
     * @param sign RSA签名得到的经过Base64编码的字符串
     * @return true--验签通过,false--验签未通过
     */
    public static boolean verifyByPublicKey(String data, String publicKey, String sign) {
        try {
            //通过X509编码的Key指令获得公钥对象
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publickey = keyFactory.generatePublic(x509KeySpec);
            Signature signature = Signature.getInstance(ALGORITHM_RSA_SIGN);
            signature.initVerify(publickey);
            signature.update(data.getBytes(CHARSET));
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            LOGGER.error("验签字符串[" + data + "]时遇到异常,{}", e);
            throw new BaseServiceException(AbsErrorCodeConstant.ERROR_CODE_92002, e);
        }
    }

    private static final class InnerHolder {
        static RSAEncryptDecryptUtil rsaEncryptDecryptUtil = new RSAEncryptDecryptUtil();
    }
    
    public static RSAEncryptDecryptUtil getInstance() {
        return InnerHolder.rsaEncryptDecryptUtil;
    }
    
    /**
     * RSA算法分段加解密数据
     *
     * @param cipher 初始化了加解密工作模式后的javax.crypto.Cipher对象
     * @param opmode 加解密模式,值为javax.crypto.Cipher.ENCRYPT_MODE/DECRYPT_MODE
     * @return 加密或解密后得到的数据的字节数组
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8;
        } else {
            maxBlock = ALGORITHM_RSA_PRIVATE_KEY_LENGTH / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            LOGGER.error("加解密数据长度阀值为[" + maxBlock + "]的数据时发生异常,{}");
            throw new BaseServiceException(AbsErrorCodeConstant.ERROR_CODE_92001, e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }
}
