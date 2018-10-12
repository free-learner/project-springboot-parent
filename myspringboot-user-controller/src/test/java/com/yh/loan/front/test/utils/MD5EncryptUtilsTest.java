package com.yh.loan.front.test.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.junit.Assert;
import org.junit.Test;

import com.personal.springboot.common.utils.MD5EncryptUtils;
import com.personal.springboot.common.utils.PasswordHashUtil;

/**
 * MD5测试类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月13日
 */
public class MD5EncryptUtilsTest {
    
    @Test
    public void testValidPassword() throws Exception {
        String password="123456";
        String mobilePhone="18611478781";
        String passwordInDb="FE2F447B8F1C36FC0818467FA3D64C3F";
        Assert.assertEquals(passwordInDb, MD5EncryptUtils.getEncryptedPwd(password, mobilePhone));
        Assert.assertTrue(MD5EncryptUtils.validPassword(password, mobilePhone, passwordInDb));
    }
    
    
    @Test
    public void  testPasswordHashUtil() {
        String password = "123456";
        String salt;
        String ciphertext;
        try {
//          boolean result = validatePassword("test", "56bf5fac6728987fb0e875efc7a7958ebf9b22f0364843ecbbf0c888ea7e0bf7", "b6f26aa1e2d551eb324c44f629167cbdb199074ee2c66bec3a46b49b26112721");
            salt = PasswordHashUtil.generateSalt();
            ciphertext = PasswordHashUtil.getEncryptedPassword(password, salt);
            boolean result = PasswordHashUtil.validatePassword(password, ciphertext, salt);

            System.out.println("Password:\t\t\t\t"+password + "  " + password.length());
            System.out.println("Salt：\t\t\t\t"+ salt + "  " + salt.length());
            System.out.println("Encrypted Password：\t"+ciphertext + "  " + ciphertext.length());
            if (result) {
                System.out.println("succeed");
            } else {
                System.out.println("failed");
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException");
        } catch (InvalidKeySpecException e) {
            System.out.println("InvalidKeySpecException");
        }
    }

}
