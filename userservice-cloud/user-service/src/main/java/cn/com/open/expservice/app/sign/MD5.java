package cn.com.open.expservice.app.sign;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

public class MD5 {
	 final static String  SEPARATOR = "&";
	public static String Md5(String str){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5"); 
			md.update(str.getBytes()); 
			byte b[] = md.digest(); 
			int i; 
			StringBuffer buf = new StringBuffer(""); 
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) 
				buf.append("0"); 
				buf.append(Integer.toHexString(i)); 
			} 
			return buf.toString();
		}catch(Exception e){
			e.printStackTrace();
			return "false";
		}
	}
	

	  public static boolean validateSignature(HttpServletRequest request,String key){
		  String customer=request.getParameter("customer");
		  String company=request.getParameter("company");
		  String orderid=request.getParameter("orderid");
		 /* String from_city=request.getParameter("from_city");
		  String to_city=request.getParameter("to_city");*/
		  String signature=request.getParameter("signature");
		 	StringBuilder encryptText = new StringBuilder();
		 	encryptText.append(company);
		//	encryptText.append(SEPARATOR);
		 	encryptText.append(orderid);
		 	/*encryptText.append(SEPARATOR);
		 	encryptText.append(from_city);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(to_city);*/
		 	try {
			String result=getNewResult(encryptText.toString()+key, customer);
			if(signature.equals(result))
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return false;
	    }  


	    public static String getNewResult(String result,String customer){
	    	return Md5(result+customer);
	    }
	    
	    
	    public final static String Md5_20(String str) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(str.getBytes("utf-8"));
				byte b[] = md.digest();
				int i;
				StringBuffer buf = new StringBuffer("");
				for (int offset = 0; offset < b.length; offset++) {
					i = b[offset];
					if (i < 0)
						i += 256;
					if (i < 16)
						buf.append("0");
					buf.append(Integer.toHexString(i));
				}
				StringBuffer buffer=new StringBuffer();
				buffer.append(buf.substring(14, 24) + buf.substring(27, 31)
						+ buf.substring(25, 27) + buf.substring(10, 14));
				return buffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
		}
	  
	
//	transdata={"exorderno":"20130916170536433711","transid":"05513091617060802334","waresid":1,"appid":"1288409","feetype":0,"money":500,"count":1,"result":0,"transtype":0,"transtime":"2013-09-16 17:07:54","cpprivate":"20130916170536433711","paytype":2}&sign=4ceb82aec68a82c836a0b64b5d4776a3
	public static void main(String[] args) {
		String str2="{\"exorderno\":\"20130916170536433711\",\"transid\":\"05513091617060802334\",\"waresid\":1,\"appid\":\"1288409\",\"feetype\":0,\"money\":500,\"count\":1,\"result\":0,\"transtype\":0,\"transtime\":\"2013-09-16 17:07:54\",\"cpprivate\":\"20130916170536433711\",\"paytype\":2}1oeTKG4GI5QIHqYWtl0yOB7k";
		System.out.println(Md5(str2));
	}
}