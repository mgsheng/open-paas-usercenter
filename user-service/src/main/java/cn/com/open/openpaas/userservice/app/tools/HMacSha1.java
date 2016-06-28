package cn.com.open.openpaas.userservice.app.tools;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import com.sun.org.apache.xml.internal.security.utils.Base64;
  
public class HMacSha1 {  
  
    private static final String ALGORITHM = "HmacSHA1";    
    private static final String ENCODING = "UTF-8";    
      public static void main(String[] args) {
    	 try {
    		 PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
    		// String a=	 passwordEncoder.encodePassword("abc123", null);
    		 String data="sdfsdfesdfsfd";
			String a= HmacSHA1Encrypt("a014d6d5ca534a9eb90126c9a326d6a9&7a81a91b-1996-4ed9-99f1-5c1ab95b4122&2016-04-14T01:46:08Z&874714","945fa18c666a4e0097809f6727bc6997");
			System.out.println(a);
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
}  
