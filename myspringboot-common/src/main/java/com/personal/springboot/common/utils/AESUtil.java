package com.personal.springboot.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES加解密工具类
 */
public class AESUtil extends CoderUtil {

    /**AES算法*/
    private static final String AES_ALGORITHM         = "AES";

    /**AES算法以及所采用的Mode和padding方式*/
    private static final String AES_ALGORITHM_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 利用AES算法对数据进行加密
     * 
     * @param data 要加密的数据
     * @param aesBase64Key base64编码的AES Key
     * @param charset 字符集
     * @return 加密后的数据(Base64编码)
     */
    public static String encrypt(String data, String aesBase64Key, String charset) throws Exception {
        // AES加密  
        Cipher cipher = initCipher(aesBase64Key, Cipher.ENCRYPT_MODE);

        byte[] bytOut = cipher.doFinal(data.getBytes(charset));
        return encryptBASE64(bytOut);
    }

    /**
     * 加密文件，从sourceFile读取文件内容，将加密结果放入targetFile
     * 
     * @param sourceFile 待加密的文件
     * @param targetFile 加密后存储的文件
     * @param aesBase64Key aes秘钥
     * @param charset 编码格式
     */
    public static void encryptFile(File sourceFile, File targetFile, String aesBase64Key,
                                   String charset) throws Exception {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(targetFile);
            Cipher cipher = initCipher(aesBase64Key, Cipher.ENCRYPT_MODE);
            CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = cipherInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
                outputStream.flush();
            }
            cipherInputStream.close();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    /**
     * 利用AES算法对数据进行解密
     * 
     * @param encryptedData AES加密的数据
     * @param aesBase64Key  base64编码的AES Key
     * @param charset 字符集
     * @return 解密后的数据
     * @throws Exception 解密异常
     */
    public static String decrypt(String encryptedData, String aesBase64Key, String charset)
                                                                                           throws Exception {
        // AES加密  
        Cipher cipher = initCipher(aesBase64Key, Cipher.DECRYPT_MODE);

        byte[] bytOut = cipher.doFinal(decryptBASE64(encryptedData));
        String result = new String(bytOut, charset);

        //ios加密的数据解密后会吗会出现ascii码等于0的填充值,需要去掉
        int indexOfNil = result.indexOf('\u0000');
        if (indexOfNil >= 0) {
            result = result.substring(0, indexOfNil);
        }
        return result;
    }

    /**
     * 将加密后的内容解密后存入文件中
     * 
     * @param file 要保存解密后的内容的文件
     * @param encryptedData 加密后的内容（没有base64编码）
     * @param aesBase64Key aes key
     * @throws Exception 解密异常
     */
    public static void decryptToFile(File file, InputStream encryptedData, String aesBase64Key)
                                                                                               throws Exception {
        CipherOutputStream cipherOutputStream = null;
        try {
            OutputStream os = new FileOutputStream(file);
            Cipher cipher = initCipher(aesBase64Key, Cipher.DECRYPT_MODE);
            cipherOutputStream = new CipherOutputStream(os, cipher);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = encryptedData.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, length);
            }
        } finally {

            if (cipherOutputStream != null) {
                cipherOutputStream.close();
            }

            if (encryptedData != null) {
                encryptedData.close();
            }
        }

    }

    /**
     * 
     * 
     * @param aesBase64Key
     * @param mode 指定是加密还是解密模式
     * @return
     * @throws Exception
     */
    public static Cipher initCipher(String aesBase64Key, int mode) throws Exception {
        SecretKeySpec skeySpec = getSecretKeySpec(aesBase64Key);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM_PADDING);
        cipher.init(mode, skeySpec);
        return cipher;
    }

    /**
     * 根据base64编码的key获取SecretKeySpec对象
     * 
     * @param aesBase64Key base64编码的key
     * @return SecretKeySpec对象
     * @throws Exception 获取SecretKeySpec对象异常
     */
    public static SecretKeySpec getSecretKeySpec(String aesBase64Key) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(decryptBASE64(aesBase64Key),
            AES_ALGORITHM);
        return skeySpec;
    }

    /**
     * 初始化AES Key
     * 
     * @return base64编码的AES Key
     * @throws Exception 异常
     */
    public static String initKey() throws Exception {
        //实例化  
        KeyGenerator kgen = KeyGenerator.getInstance(AES_ALGORITHM);
        //设置密钥长度  
        SecureRandom random = new SecureRandom();
        kgen.init(128, random);
        //kgen.init(128);
        //生成密钥
        SecretKey skey = kgen.generateKey();

        //返回密钥的二进制编码  
        return encryptBASE64(skey.getEncoded());
    }
}
