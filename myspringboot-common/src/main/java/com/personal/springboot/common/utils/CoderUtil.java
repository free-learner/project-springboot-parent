package com.personal.springboot.common.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64加密工具类
 */
public class CoderUtil {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";
    public static final String  defaule_charset   = "UTF-8";
    public static final Charset UTF8 = Charset.forName(defaule_charset);

    /**
     * BASE64解密
     */
    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    /**
     * BASE64加密
     */
    protected static String encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeBase64String(key);
    }
    
    /**
     * BASE64加密
     */
    public static String encryptBASE64(String key) throws Exception {
        return encryptBASE64(key.getBytes(UTF8));
    }

    /**
     * MD5加密
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * SHA加密
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }
    
}
