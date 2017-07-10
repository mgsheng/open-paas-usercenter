package cn.com.open.expservice.app.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;


public class BaseController {
	protected static boolean nullAndEmpty(String str){
        return null==str||str.isEmpty()||"".equals(str.trim());
    }
	 protected boolean nullEmptyBlankJudge(String str){
        return null==str||str.isEmpty()||"".equals(str.trim());
    }
    protected boolean paraMandatoryCheck(List<String> params){
        for(String param:params){
            if(nullEmptyBlankJudge(param)){
                return false;
            }
        }
        return true;
    }
    protected void paraMandaChkAndReturn(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=paraMandaChkAndReturnMap(errorNum, response, errMsg);
        writeErrorJson(response,map);
    }
    
    protected Map<String, Object> paraMandaChkAndReturnMap(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=new HashMap<String,Object>();
        map.clear();
        map.put("status", "0");
        map.put("errorCode", errorNum);
        map.put("errMsg", errMsg);
        return map;
    }
    
    protected Map<String, Object> paraMandaChkAndReturnMap(Map<String, Object> map,int errorNum,HttpServletResponse response,String errMsg){
        map.clear();
        map.put("status", "0");
        map.put("errorCode", errorNum);
        map.put("errMsg", errMsg);
        return map;
    }
    
    /**
     * 返回成功json
     * @param response
     * @param obj 数据�?
     */
    protected void writeSuccessJson(HttpServletResponse response, Map map){
    	writeJson(response, JSONObject.fromObject(map));
    }
    
    /**
     * 返回失败json
     * @param response
     * @param error_code 错误�?
     */
    protected void writeErrorJson(HttpServletResponse response, Map map){
    	writeJson(response, JSONObject.fromObject(map));
    }
   
    protected static Cookie getCookieByName(HttpServletRequest request,String name){
	 	  Map<String,Cookie> cookieMap = ReadCookieMap(request);
	 	    if(cookieMap.containsKey(name)){
	 	        Cookie cookie = (Cookie)cookieMap.get(name);
	 	        return cookie;
	 	    }else{
	 	        return null;
	 	    }
	}

	protected static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
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
	    
	    public static void writeJson(HttpServletResponse response,String str) {
	    	try {
				response.getWriter().write(str);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
    
	    
	    protected  Map<String, Object> paramMapAndReturn(Map<String, Object> map,int errorNum,HttpServletResponse response,String errMsg){
	    	return paraMandaChkAndReturnMap(map,errorNum, response, errMsg);
	    }
}