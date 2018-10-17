package com.personal.springboot.cryption.core;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * AES,高级加密标准（英语：Advanced Encryption
 * Standard，缩写：AES），在密码学中又称Rijndael加密法，是美国联邦政府采用的一种区块加密标准。这个标准用来替代原先的DES，
 * 已经被多方分析且广为全世界所使用。严格地说，AES和Rijndael加密法并不完全一样（虽然在实际应用中二者可以互换），
 * 因为Rijndael加密法可以支持更大范围的区块和密钥长度：AES的区块长度固定为128
 * 比特，密钥长度则可以是128，192或256比特；而Rijndael使用的密钥和区块长度可以是32位的整数倍，以128位为下限，256比特为上限。
 * 包括AES-ECB,AES-CBC,AES-CTR,AES-OFB,AES-CFB
 * 
 * @Author LiuBao
 * @Version 2.0 2018年10月17日
 */
public class AESUtil
{
  @SuppressWarnings("unused")
private static final String KEY_ALGORITHM = "AES";
  @SuppressWarnings("unused")
  private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
  private static KeyGenerator kg = null;

  public static String encrypt(String content, byte[] secretKey)
  {
    try
    {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      byte[] byteContent = content.getBytes("utf-8");
      System.out.println(Arrays.toString(byteContent));
      cipher.init(1, new SecretKeySpec(secretKey, "AES"));
      byte[] result = cipher.doFinal(byteContent);
      System.out.println("加密后的数据" + Arrays.toString(result) + "长度" + result.length);
      return Base64.encodeBase64String(result);
    } catch (Exception ex) {
      Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }

  public static String decrypt(String content, byte[] secretKey)
  {
    try
    {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

      cipher.init(2, new SecretKeySpec(secretKey, "AES"));

      byte[] result = cipher.doFinal(Base64.decodeBase64(content));
      return new String(result, "utf-8");
    } catch (Exception ex) {
      Logger.getLogger(AESUtil.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }

  public static byte[] getSecretKey()
  {
    SecretKey secretKey = kg.generateKey();
    return secretKey.getEncoded();
  }

  public static void main(String[] args) {
    byte[] str = getSecretKey();
    System.out.println(Arrays.toString(str) + "---" + str.length);
    String s1 = encrypt("12624", str);
    System.out.println(s1 + "======" + s1.length());
    String s2 = decrypt(s1, str);
    System.out.println(s2);
  }

  static
  {
    try
    {
      kg = KeyGenerator.getInstance("AES");
      // 256位长度,需要升级jdk自带的security包下的两个jar包
      kg.init(256, new SecureRandom(String.valueOf(System.currentTimeMillis()).getBytes()));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
}