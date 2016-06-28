package com.andaily.forLoad.util;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MD5 {
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
//	transdata={"exorderno":"20130916170536433711","transid":"05513091617060802334","waresid":1,"appid":"1288409","feetype":0,"money":500,"count":1,"result":0,"transtype":0,"transtime":"2013-09-16 17:07:54","cpprivate":"20130916170536433711","paytype":2}&sign=4ceb82aec68a82c836a0b64b5d4776a3
	public static void main(String[] args) {
		String str2="{\"exorderno\":\"20130916170536433711\",\"transid\":\"05513091617060802334\",\"waresid\":1,\"appid\":\"1288409\",\"feetype\":0,\"money\":500,\"count\":1,\"result\":0,\"transtype\":0,\"transtime\":\"2013-09-16 17:07:54\",\"cpprivate\":\"20130916170536433711\",\"paytype\":2}1oeTKG4GI5QIHqYWtl0yOB7k";
		System.out.println(Md5(str2));
	}
}