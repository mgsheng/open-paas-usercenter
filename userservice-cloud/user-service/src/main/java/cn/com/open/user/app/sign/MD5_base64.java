/****************************************************************************  
 * Copyright © 2016 TCL Technology . All rights reserved.
 * @Title: MD5.java 
 * @Prject: cr-comm-util
 * @Package: com.tcl.cr.util.sign 
 * @Description: TODO
 * @author: heqingqing   
 * @date: 2016年10月16日 上午9:59:40 
 * @version: V1.0   
 * ****************************************************************************
 */
package cn.com.open.user.app.sign;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;


/** 
 * @ClassName: MD5 
 * @Description: TODO
 * @author: heqingqing
 * @date: 2016年10月16日 上午9:59:40  
 */
public class MD5_base64 {

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String customer, String input_charset) {
    	text = text + key;
        return Base64.encode(DigestUtils.md5Hex(getContentBytes(text, input_charset)).getBytes()) ;
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String customer, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	mysign=Base64.encode(mysign.getBytes());
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}
