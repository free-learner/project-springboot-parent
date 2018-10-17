package com.personal.springboot.cryption.core;

import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.sun.crypto.provider.SunJCE;

public class DESUtil
{
  public static void main(String[] args)
    throws Exception
  {
    Security.addProvider(new SunJCE());

    KeyGenerator keygen = KeyGenerator.getInstance("DESede");

    SecretKey deskey = keygen.generateKey();
    System.out.println("生成的密钥长度" + deskey.getEncoded().length);

    Cipher c = Cipher.getInstance("DESede");

    String msg = "12343432";

    System.out.println("明文是：" + msg);

    c.init(1, deskey);

    byte[] src = msg.getBytes();

    byte[] enc = c.doFinal(src);

    System.out.println("密文是：" + new String(enc) + "密文长度" + enc.length);

    c.init(2, deskey);

    byte[] dec = c.doFinal(enc);

    System.out.println("解密后的结果是：" + new String(dec));
  }
}