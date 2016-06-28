package com.andaily.springoauth.web;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import javax.servlet.http.HttpServletResponse;

import com.andaily.forLoad.util.MD5;
import com.andaily.forLoad.util.StringTools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 
 */
public abstract class WebUtils {


    public static final String STATE_SESSION_KEY = "state_key";

    public static final String AUTHORIZATION_CODE_DTO_SESSION_KEY = "AuthorizationCodeDto_key";


    private WebUtils() {
    }


    public static void writeJson(HttpServletResponse response, JSON json) {
        response.setContentType("application/json;charset=UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            json.write(writer);
            writer.flush();
        } catch (IOException e) {
            throw new IllegalStateException("Write json to response error", e);
        }
    }
    
    
    public static String loadUserCenterDevLoginUrl(String userCenterDevLoginUri,int userId,String appkey,String appsecret) {
    	StringBuffer url = new StringBuffer(userCenterDevLoginUri);
	     //salt=4位随机数
	   	 String salt = StringTools.getRandomString(4);
		     //secret=MD5(sourceId+salt+appKey+appSecret);
	   	 String secret = MD5.Md5(userId+salt+appkey+appsecret);
	   	 url.append("?sourceId=").append(userId);
	   	 url.append("&salt=").append(salt);
	   	 url.append("&secret=").append(secret);
	   	 return url.toString();
    }


	  public static void writeJsonToMap(HttpServletResponse response,Map<String,Object> map) {
    	try {
			JsonConfig jsonConfig = new JsonConfig();
			// 排除,避免循环引用 There is a cycle in the hierarchy!
			jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			jsonConfig.setIgnoreDefaultExcludes(true);
			jsonConfig.setAllowNonStringKeys(true);
			writeJson(response, JSONObject.fromObject(map,jsonConfig));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

}