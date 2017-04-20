package cn.com.open.openpaas.userservice.app.tools;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
 
import org.apache.commons.codec.binary.Base64;
public class DES {
 
    public final static String DES_KEY_STRING = "ABSujsuu";
     
    public static String encrypt(String message, String key) throws Exception {
    	key=key.substring(0,8);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
 
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
 
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        String encrypt=base64Encode(cipher.doFinal(message.getBytes("UTF-8")));
        encrypt = encrypt.replaceAll(new String("\r"), "");
    	encrypt = encrypt.replaceAll(new String("\n"), "");
 
        return encrypt;
    }
 
    public static String decrypt(String message, String key) throws Exception {
    	key=key.substring(0,8);
        byte[] bytesrc = base64Decode(message);//convertHexString(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
 
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
 
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte, "utf-8");
    }
 
    public static byte[] convertHexString(String ss) {
        byte digest[] = new byte[ss.length() / 2];
        for (int i = 0; i < digest.length; i++) {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte) byteValue;
        }
 
        return digest;
    }
 
    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }
 
        return hexString.toString();
    }
     
/*    public static String encodeBase64(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
     
    public static byte[] decodeBase64(String base64String) {
        return Base64.decode(base64String, Base64.DEFAULT);
    }*/
    public static String base64Encode(byte[] s) {
        if (s == null)
            return null;
        Base64 b= new Base64();
        return b.encodeToString(s);
    }
 
    public static byte[] base64Decode(String s) throws IOException {
        if (s == null)
            return null;
        Base64 decoder = new Base64();
        s = new String(s.getBytes("UTF-8"));
        byte[] b = decoder.decode(s);
        return b;
    }
    public static String getNewSecert(String secert){
    	if(secert.contains("+")){
    		secert=secert.replaceAll("\\+", "%2B");
    	}
    	return secert;
    };
    public static void main(String[] args) throws Exception {
        String source = "2206880#20170224164235#91d921e029d0470b9eb41e39d895a0e0#874714#http://xjtudlc.imooc.com/learn/8089#oes_101011010998#测试用户";
        ///EZ/8Fo4VJ7LgubAk4hmyHNEzUeuIBRK0b9Leza4My1AJ4dROaLxIIVd8Z0RaqBt5ZtljVXjSHWh6+gC8B2jJT8LbcujAciF1T4onjEz6y8Zwvm5tgW/VxxZ2U+zHTPBFMixwO0aPRyrgb3A/qS+5KzdlxkJ1yCkLIBzik2CzGB/XGxmGjYfpg==
        System.out.println("原文: " + source);
        String key = "1e167b2f28a6f4daf5a4812baa958242";
        String encryptData = encrypt(source, key);
        System.out.println("加密后: " + encryptData);
        String decryptData = decrypt(encryptData,  key);
        System.out.println("解密后: " + decryptData);
    }
}