package com.personal.springboot.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5加密工具类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年3月30日
 */
public class MD5EncryptUtils {
    
    private static final Logger log = LoggerFactory.getLogger(MD5EncryptUtils.class);
    
    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String salt="liubao";
        String password="123456";
        String encryptedPwd = getEncryptedPwd(password, salt);
        //0751F12F6EAC259BC02A11E4D49CA717
        log.debug("加密结果:"+encryptedPwd);
        String passwordInDb="0751F12F6EAC259BC02A11E4D49CA717";
        log.debug("校验结果:"+validPassword(password, salt, passwordInDb));
    }

    /**
     * 验证16进制形式口令是否合法
     * 
     * @param password
     * @param passwordInDb
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static boolean validPassword(String password, String salt, String passwordInDb)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String encryptedPwd = getEncryptedPwd(password, salt);
        log.debug("加密校验HEX:"+encryptedPwd);
        if (StringUtils.equals(encryptedPwd, passwordInDb)) {
            // 口令正确返回口令匹配消息
            return true;
        } else {
            // 口令不正确返回口令不匹配消息
            return false;
        }
        
        /*// 将16进制字符串格式口令转换成字节数组
        byte[] pwdInDb = hexStringToByte(passwordInDb);
        String pwdMD5 = new String(pwdInDb,"utf-8");
        // 创建消息摘要对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 将口令的数据传给消息摘要对象
        md.update(addSalt(password, salt).getBytes("UTF-8"));
        // 生成输入口令的消息摘要
        byte[] digest = md.digest();
        // 比较根据输入口令生成的消息摘要和数据库中消息摘要是否相同
        if (Arrays.equals(digest, pwdInDb)) {
            // 口令正确返回口令匹配消息
            return true;
        } else {
            // 口令不正确返回口令不匹配消息
            return false;
        }*/
    }

    /**
     * 获得加密后的16进制形式口令
     * 
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String getEncryptedPwd(String password, String salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 声明消息摘要对象
        MessageDigest md = null;
        // 创建消息摘要
        md = MessageDigest.getInstance("MD5");
        // 将口令+盐数据传入消息摘要对象
        md.update(addSalt(password, salt).getBytes("UTF-8"));
        // 获得消息摘要的字节数组
        byte[] digest = md.digest();
        return byteToHexString(digest);
    }

    /**
     * 将16进制形式字符串转换成字节数组
     * 
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) ((hexChars[pos]) << 4);
        }
        return result;
    }

    /**
     * 将指定byte数组转换成16进制形式字符串
     * 
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 在加密对象后加盐
     */
    private static String addSalt(String password, String salt) {
        StringBuilder sb=new StringBuilder();
        if (StringUtils.isBlank(password)) {
            password = "";
        }
        if (StringUtils.isBlank(salt)) {
            sb.append(password);
        } else {
            // TODO  salt可以进行转换HEX16操作
            sb.append(password).append(":").append(salt);
        }
        log.debug("加盐结果:"+sb.toString());
        return sb.toString();
    }

}