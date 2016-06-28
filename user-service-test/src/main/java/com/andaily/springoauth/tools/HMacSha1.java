package com.andaily.springoauth.tools;

import java.util.Date;

import javax.crypto.Mac;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;  

import com.sun.org.apache.xml.internal.security.utils.Base64;
  
public class HMacSha1 {  
  
    private static final String ALGORITHM = "HmacSHA1";    
    private static final String ENCODING = "UTF-8"; 
    final static String  SEPARATOR = "&";
      public static void main(String[] args) {
    	 try {
    		    String timestamp="";
    		  	String signatureNonce="";
    		  	String sign="";
    		  	String key="9ebada02676c4ccbbbdaeae27362896b";
    		  	String client_id="91d921e029d0470b9eb41e39d895a0e0";
    		  	String access_token="6006155a-a127-42eb-8fec-3fd642ec94cc";
    		  	timestamp=DateTools.getSolrDate(new Date());
    		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
    		 	StringBuilder encryptText = new StringBuilder();
    		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
    		 	encryptText.append(client_id);
    			encryptText.append(SEPARATOR);
    		 	encryptText.append(access_token);
    		 	encryptText.append(SEPARATOR);
    		 	encryptText.append(timestamp);
    		 	encryptText.append(SEPARATOR);
    		 	encryptText.append(signatureNonce);
    		 	sign=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			System.out.println(sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**  
     * 使用 HMAC-SHA1 签名方法对对encryptText进行签名  
     * @param encryptText 被签名的字符串  
     * @param encryptKey  密钥  
     * @return  
     * @throws Exception  
     */    
    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception     
    {           
    	    Mac mac = Mac.getInstance(ALGORITHM);
    	    mac.init(new SecretKeySpec(encryptKey.getBytes(ENCODING), ALGORITHM));
    	    byte[] signData = mac.doFinal(encryptText.getBytes(ENCODING));

    	    String signature = new String(Base64.encode(signData));
    	    signature = signature.replaceAll(new String("\r"), "");
    	    signature = signature.replaceAll(new String("\n"), "");
        //完成 Mac 操作   
        return      signature;    
    } 
    public static String getNewResult(String result){
    	if(result.contains("+")){
    		result=result.replaceAll("\\+", "%2B");
    	}
    	return result;
    };

}  
