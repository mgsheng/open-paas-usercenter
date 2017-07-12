package cn.com.open.user.app.tools;
import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import cn.com.open.user.app.sign.MD5;
import org.apache.commons.codec.binary.Base64;

public class DESUtil {
	 
 
    public static byte[] desEncrypt(byte[] plainText,String desKey) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey.getBytes();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        byte data[] = plainText;
        byte encryptedData[] = cipher.doFinal(data);
        return encryptedData;
    }
 
    public static byte[] desDecrypt(byte[] encryptText,String desKey) throws Exception {
        SecureRandom sr = new SecureRandom();
        byte rawKeyData[] = desKey.getBytes();
        DESKeySpec dks = new DESKeySpec(rawKeyData);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        byte encryptedData[] = encryptText;
        byte decryptedData[] = cipher.doFinal(encryptedData);
        return decryptedData;
    }
 
    public static String encrypt(String input,String desKey) throws Exception {
    	String encrypt = base64Encode(desEncrypt(input.getBytes(),desKey.substring(0,8)));
    	encrypt = encrypt.replaceAll(new String("\r"), "");
    	encrypt = encrypt.replaceAll(new String("\n"), "");
        return encrypt;
    }
 
    public static String decrypt(String input,String desKey) throws Exception {
        byte[] result = base64Decode(input);
        return new String(desDecrypt(result,desKey.substring(0,8)));
    }
 
    public static String base64Encode(byte[] s) {
        if (s == null)
            return null;
        Base64 b= new Base64();
        return b.encodeToString(s);
    }
    public static String getNewSecert(String password){
    	if(password.contains("+")){
    		password=password.replaceAll("\\+", "%2B");
    	}
    	return password;
    };
 
    public static byte[] base64Decode(String s) throws IOException {
        if (s == null)
            return null;
        Base64 decoder = new Base64();
        s = new String(s.getBytes("UTF-8"));
        byte[] b = decoder.decode(s);
        return b;
    } 

    public static void main(String[] args) throws Exception {
        String source = "zhj1234567890";
        System.out.println("原文: " + source);
        String key = "1d4d8c77108a4fd2a3c23feba0cfdccc";
        String encryptData = encrypt(source, key);
        System.out.println("加密后: " + encryptData);
        System.out.println("md5后: " + MD5.Md5(source));
        String decryptData = decrypt(encryptData,  key);
        System.out.println("解密后: " + decryptData);
         
    }
}
