package com.personal.springboot.cryption.core;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.gson.GsonBuilder;

import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

public class ECCCoderUtil
{
  private static KeyPairGenerator keyPairGenerator = null;

  public static Map<String, String> generator(String org)
  {
    Map<String, String> map = new HashMap<>();
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();
    ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate();
    String base64Pub = Base64.encodeBase64String(ecPublicKey.getEncoded());
    String base64Pri = Base64.encodeBase64String(ecPrivateKey.getEncoded());
    map.put(org + "Pub", base64Pub);
    map.put(org + "Pri", base64Pri);
    return map;
  }

  public static byte[] encrypt(byte[] content, String publicKey)
  {
    byte[] cipherText = null;
    try {
      Cipher cipher = Cipher.getInstance("ECIES", "BC");
      ECPublicKeyImpl ecPublicKey = new ECPublicKeyImpl(Base64.decodeBase64(publicKey));
      cipher.init(1, ecPublicKey);
      cipherText=cipher.doFinal(content);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }

  public static byte[] decrypt(String content, String privateKey)
  {
    byte[] plainText = null;
    try {
      Cipher cipher = Cipher.getInstance("ECIES", "BC");
      ECPrivateKeyImpl ecPrivateKey = new ECPrivateKeyImpl(Base64.decodeBase64(privateKey));
      cipher.init(2, ecPrivateKey);
      plainText= cipher.doFinal(Base64.decodeBase64(content));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return plainText;
  }

  public static void main(String[] args) {
    Map<String,String> map = generator("org1");
    System.out.println(new GsonBuilder().disableHtmlEscaping().create().toJson(map));
  }

  static
  {
    Security.addProvider(new BouncyCastleProvider());
    try {
      keyPairGenerator = KeyPairGenerator.getInstance("ECIES", "BC");
      keyPairGenerator.initialize(256, new SecureRandom());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}