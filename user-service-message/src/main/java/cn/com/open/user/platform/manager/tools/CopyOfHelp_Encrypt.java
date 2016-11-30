package cn.com.open.user.platform.manager.tools;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.lang.StringUtils;


public class CopyOfHelp_Encrypt {
	private static String IV_KEY ="OPEN2012";   //OPEN2012
	private static String PASSWORD_CRYPT_KEY ="OPEN2012";   //EDUP9999
	
	//解密数据     
	public static String decrypt(String message,String key) throws Exception {     
        byte[] bytesrc =convertHexString(message);        
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");         
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");        
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);        
        IvParameterSpec iv = new IvParameterSpec(IV_KEY().getBytes("UTF-8"));     
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);           
        byte[] retByte = cipher.doFinal(bytesrc);          
        return new String(retByte);      
	}    
	
	public static byte[] encrypt(String message, String key) throws Exception {     
	    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");    
	    DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));    
	    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");     
	    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);     
	    IvParameterSpec iv = new IvParameterSpec(IV_KEY().getBytes("UTF-8"));     
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
	    return cipher.doFinal(message.getBytes("UTF-8"));      
	}    
	
	public static String encrypt(String value){  
		String result="";  
		try{  
			value=java.net.URLEncoder.encode(value, "utf-8");   
			result=toHexString(encrypt(value, PASSWORD_CRYPT_KEY())).toUpperCase();  
		}catch(Exception ex){  
		   ex.printStackTrace();  
		   return "";  
		}  
		return result;   
	}
	
	public static String decrypt(String value){  
		String result="";  
		try{  
			result = decrypt(value, PASSWORD_CRYPT_KEY());
		}catch(Exception ex){  
		   ex.printStackTrace();  
		   return "";  
		}  
		return result;   
	}
	
	public static byte[] convertHexString(String ss) {      
		byte digest[] = new byte[ss.length() / 2];      
		for(int i = 0; i < digest.length; i++) {      
			String byteString = ss.substring(2 * i, 2 * i + 2);      
			int byteValue = Integer.parseInt(byteString, 16);      
			digest[i] = (byte)byteValue;      
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
	
	/**
	 * 动态读取 spring-oauth-server.properties
	 * 需项目启动
	 * @return
	 */
	private static String IV_KEY(){
		if(StringUtils.isBlank(IV_KEY)){
			try {
				IV_KEY = PropertiesTool.getAppPropertieByKey("encrypt.iv.key");
//				System.out.println("IV_KEY"+IV_KEY);
			} catch (Exception e) {
				IV_KEY = "EDUP9999";
				e.printStackTrace();
			}
		}
		return IV_KEY;
	}
	
	/**
	 * 动态读取 spring-oauth-server.properties
	 * 需项目启动
	 * @return
	 */
	private static String PASSWORD_CRYPT_KEY(){
		if(StringUtils.isBlank(PASSWORD_CRYPT_KEY)){
			try {
				PASSWORD_CRYPT_KEY = PropertiesTool.getAppPropertieByKey("encrypt.password.crypt.key");
//				System.out.println("PASSWORD_CRYPT_KEY"+PASSWORD_CRYPT_KEY);
			} catch (Exception e) {
				PASSWORD_CRYPT_KEY = "EDUP9999";
				e.printStackTrace();
			}
		}
		return PASSWORD_CRYPT_KEY;
	}
	
	public static void main(String[] args) throws Exception {     
		String value="13427279020";     
	    System.out.println("加密数据:"+value);   
	    System.out.println("密码为:"+"EDUP9999");   
	    String a=encrypt(value);     
	    System.out.println("加密后的数据为:"+a);     
	    String b=decrypt("41498C01983A326174603F3C9FC9017D");
	  // String aaa= UUID.randomUUID().toString().replace("-","");
	    System.out.println("sss="+b);
	  /*  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date d=new Date();
			  String  yesterdayStart = "2016-5-6 11:00:00";
		  String  yesterdayEnd = "2016-5-6  19:00:00";
		try {
			Date d1=sdf.parse(yesterdayStart);
			Date d2=sdf.parse(yesterdayEnd);
			System.out.println(d1.getTime());
			System.out.println(d2.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
		/*Date d = null;
	        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        try {
	            formater.setLenient(false);
	            d = formater.parse("null");
	        } catch (Exception e) {
	            // log.error(e);
	            d = null;
	        }
		System.out.println(d);*/
	}
}
