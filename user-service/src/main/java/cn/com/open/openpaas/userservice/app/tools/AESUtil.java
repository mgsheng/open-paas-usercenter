package cn.com.open.openpaas.userservice.app.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;


public class AESUtil {
	private static  String iv="user-service-api";
	
	/**
	 * AES加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data,String key) throws Exception {
	    try {
	      key=key.substring(0,16);
	      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	      int blockSize = cipher.getBlockSize();
	      byte[] dataBytes = data.getBytes();
	      int plaintextLength = dataBytes.length;
	      if (plaintextLength % blockSize != 0) {
	        plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
	      }
	      byte[] plaintext = new byte[plaintextLength];
	      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
	      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
	      IvParameterSpec ivspec = new IvParameterSpec(IV_KEY().getBytes("UTF-8"));
	      cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
	      byte[] encrypted = cipher.doFinal(plaintext);
	      String encrypt=new sun.misc.BASE64Encoder().encode(encrypted);
		   	encrypt = encrypt.replaceAll(new String("\r"), "");
		    encrypt = encrypt.replaceAll(new String("\n"), "");
	      return encrypt;
	    } catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
	  }
	/**
	 * AES解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	  public static String decrypt(String data,String key) throws Exception {
	    try
	    {
		  key=key.substring(0,16);
	      byte[] encrypted1 = new sun.misc.BASE64Decoder().decodeBuffer(data);
	      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
	      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
	      IvParameterSpec ivspec = new IvParameterSpec(IV_KEY().getBytes("UTF-8"));
	      cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
	      byte[] original = cipher.doFinal(encrypted1);
	      String originalString = new String(original);
	      return originalString;
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	      return null;
	    }
}
		/**
		 * 动态读取 spring-oauth-server.properties
		 * 需项目启动
		 * @return
		 */
		private static String IV_KEY(){
			if(StringUtils.isBlank(iv)){
				try {
					iv = PropertiesTool.getAppPropertieByKey("aes-iv-key");
//					System.out.println("IV_KEY"+IV_KEY);
				} catch (Exception e) {
					iv = "";
					e.printStackTrace();
				}
			}
			return iv;
		}
		
		public static String getNewPwd(String password){
	    	if(password.contains("+")){
	    		password=password.replaceAll("\\+", "%2B");
	    	}
	    	return password;
	    };
		
	  public static void main(String[] args) {
	      String data = "a1111111";
	      String key = "67d73cec5d6b4c8a8a9883748f4066fe";
	     try {
			String a= encrypt(data,key);
			System.out.println(a);
			String b=decrypt("eiWBoX/811/tsvuY4XZwlw==",key);
			System.out.println(b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
