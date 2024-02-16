package com.gaming.utils;

import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordUtil {

    private static final String cryptKey = "gamingencryptkey";

    public static String Encrypt(String password){
        try{
            SecretKeySpec key = new SecretKeySpec(cryptKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes()));
        } catch (Exception ex){
            System.out.println("encrypt error: " + ex.toString());
            return null;
        }
    }

    public static String Decrypt(String encryptedPassword){
        try{
            SecretKeySpec key = new SecretKeySpec(cryptKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedPassword)));
        } catch (Exception ex){
            System.out.println("encrypt error: " + ex.toString());
            return null;
        }
    }

    public static Boolean ComparePasswords(String pass1, String pass2) {
        String encryptedPass1 = Encrypt(pass1);
        assert encryptedPass1 != null;
        return encryptedPass1.equals(pass2);
    }
}
