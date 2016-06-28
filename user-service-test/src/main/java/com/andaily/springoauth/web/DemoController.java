package com.andaily.springoauth.web;

import com.andaily.springoauth.service.dto.UserCenterLoginDto;
import com.andaily.springoauth.service.dto.UserCenterRegDto;
import com.andaily.springoauth.tools.DESUtil;
import com.andaily.springoauth.tools.LoadPopertiesFile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Handle 'authorization_code'  type actions
 *
 * 
 */
@Controller
public class DemoController {


    private static final Logger LOG = LoggerFactory.getLogger(DemoController.class);

    @Value("#{properties['user-center-reg-uri']}")
    private String userCenterRegUri;
    
    @Value("#{properties['user-center-login-uri']}")
    private String userCenterLoginUri;

    @Value("#{properties['user-center-verify-uri']}")
    private String userCenterVerifyUri;
    
    @Value("#{properties['user-center-password-uri']}")
    private String userCenterPasswordUri;
    
    private Map<String,String> properties=LoadPopertiesFile.loadProperties();
    
    
    /*
     * Entrance:   step-1
     * */
      @RequestMapping(value = "prepare", method = RequestMethod.GET)
      public String prepareLogin(Model model,HttpServletRequest request) {
    	  if(("1").equals(request.getParameter("flag"))){
    		  model.addAttribute("userCenterPasswordUri", userCenterPasswordUri);
    		  return "demo/prepareLogin";
    	  }else{
    		  model.addAttribute("userCenterRegUri", userCenterRegUri);
    		  model.addAttribute("userCenterVerifyUri", userCenterVerifyUri);
    		  return "demo/prepareReg";
    	  }
      }
      
      /*
       * Entrance:   step-1
       * */
        @RequestMapping(value = "prepareLogin", method = RequestMethod.POST)
        public String prepareLogin(Model model,UserCenterLoginDto userCenterLoginDto,HttpServletRequest request) {
        	String url="login?client_id="+userCenterLoginDto.getClient_id()+"&access_token="+userCenterLoginDto.getAccess_token()+"&grant_type="+userCenterLoginDto.getGrant_type()+"&scope="+userCenterLoginDto.getScope();
            return "redirect:"+url;
        }
        
        /*
         * Entrance:   step-1
         * */
          @RequestMapping(value = "prepareReg", method = RequestMethod.POST)
          public String prepareReg(Model model,UserCenterLoginDto userCenterLoginDto,HttpServletRequest request) {
        	  String url="reg.do?client_id="+userCenterLoginDto.getClient_id()+"&access_token="+userCenterLoginDto.getAccess_token()+"&grant_type="+userCenterLoginDto.getGrant_type()+"&scope="+userCenterLoginDto.getScope();
              return "redirect:"+url;
          }
    
      /*
       * Entrance:   step-1
       * */
        @RequestMapping(value = "reg.do", method = RequestMethod.GET)
        public String reg(Model model,HttpServletRequest request,String mess) {
      	  model.addAttribute("userCenterVerifyUri", userCenterVerifyUri);
      	  model.addAttribute("userCenterRegUri", userCenterRegUri);
      	  model.addAttribute("clientId",request.getParameter("client_id"));
      	  model.addAttribute("scope",request.getParameter("scope"));
      	  model.addAttribute("grantType",request.getParameter("grant_type"));
      	  model.addAttribute("accessToken",request.getParameter("access_token"));
      	  if(mess!=null && mess!=""){
      		  model.addAttribute("mess", mess);
      	  }
          return "demo/reg";
        }
    
    /* 
     * Redirect to oauth-server bind page:   step-2
     * */
      @RequestMapping(value = "reg", method = RequestMethod.POST)
      public String reg(UserCenterRegDto userCenterRegDto,Model model,HttpServletRequest request) throws Exception {
          final String fullUri = userCenterRegDto.getFullUri();
          LOG.debug("Send to Oauth-Server URL: {}", fullUri);
          String param="access_token="+userCenterRegDto.getAccess_token()+"&account="+userCenterRegDto.getUsername();
          String result=sendGet(userCenterVerifyUri,param);
          if(result==null || result.length()==0){
        	  model.addAttribute("mess", "access_token无效");
          }else{
	          Map map=(Map) JSONObject.toBean(JSONObject.fromObject(result),Map.class);
	          if(("1").equals(map.get("status"))){
	        	  String key=properties.get(userCenterRegDto.getClientId());
	        	  if(key!=null){
	        		  String password=userCenterRegDto.getPassword();
	        		  password=DESUtil.encrypt(password, key);
	        		  password=DESUtil.getNewPwd(password);
	        		  userCenterRegDto.setPassword(password);
	        	  }
	        	  //调用注册接口
	        	  String result1=sendPost(fullUri.split("\\?")[0], fullUri.split("\\?")[1]);
	        	  if(result1==null || result1.length()==0){
	        		  model.addAttribute("mess", "access_token无效");
	        	  }else{
	        		  Map map1=(Map) JSONObject.toBean(JSONObject.fromObject(result1), Map.class);
		        	  if(("1").equals(map1.get("status"))){
		            	  model.addAttribute("json", URLDecoder.decode(result1,"utf-8"));
		          	      model.addAttribute("client_id",request.getParameter("client_id"));
		          	      model.addAttribute("scope",request.getParameter("scope"));
		          	      model.addAttribute("grant_type",request.getParameter("grant_type"));
		          	      model.addAttribute("accessToken",request.getParameter("accessToken"));
		            	  return "demo/regok";
		              }else if(("1").equals(map1.get("error_code"))){
		            	  model.addAttribute("mess", "client_id错误");
		              }else if(("2").equals(map1.get("error_code"))){
		            	  model.addAttribute("mess", "access_token与client_id不匹配");
		              }
	        	  }	        	  
	          }else if(("3").equals(map.get("error_code"))){
	        	  model.addAttribute("mess", "username已存在");
	          }else if(("2").equals(map.get("error_code"))){
	        	  model.addAttribute("mess", "access_token与client_id不匹配");
	          }
          }
    	  model.addAttribute("client_id",userCenterRegDto.getClientId());
    	  model.addAttribute("client_secret",userCenterRegDto.getClientSecret());
    	  model.addAttribute("scope",userCenterRegDto.getScope());
    	  model.addAttribute("grant_type",userCenterRegDto.getGrantType());
    	  model.addAttribute("accessToken",userCenterRegDto.getAccess_token());
          return "redirect:reg.do";
      }
        
    /*
     * Entrance:   step-1
     * */
      @RequestMapping(value = "login", method = RequestMethod.GET)
      public String login(Model model,HttpServletRequest request,String mess) {
    	  model.addAttribute("userCenterPasswordUri", userCenterPasswordUri);
    	  model.addAttribute("clientId",request.getParameter("client_id"));
    	  model.addAttribute("accessToken",request.getParameter("access_token"));
    	  model.addAttribute("scope",request.getParameter("scope"));
    	  model.addAttribute("grantType",request.getParameter("grant_type"));
    	  if(mess!=null && mess!=""){
    		  model.addAttribute("mess", mess);
    	  }
          return "demo/login";
      }
      
  /* 
   * Redirect to oauth-server bind page:   step-2
   * */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(UserCenterLoginDto userCenterLoginDto,Model model,HttpServletRequest request) throws Exception {
        String key=properties.get(userCenterLoginDto.getClient_id());
        if(key!=null){
	        String password=userCenterLoginDto.getPassword();
	        password=DESUtil.encrypt(password, key);
	        password=DESUtil.getNewPwd(password);
	        userCenterLoginDto.setPassword(password);
        }
    	final String fullUri = userCenterLoginDto.getFullUri1();
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        String s=sendGet(fullUri.split("\\?")[0],fullUri.split("\\?")[1]);
        if(s==null || s.length()==0){//access_token无效
        	model.addAttribute("mess", "accesse_token无效");
        }else{
	        Map map=(Map) JSONObject.toBean(JSONObject.fromObject(s), Map.class);
	        if(("1").equals(map.get("status"))){
	        	model.addAttribute("map", map);
	        	model.addAttribute("json", URLDecoder.decode(s,"utf-8"));
	      	    model.addAttribute("client_id",request.getParameter("client_id"));
	      	    model.addAttribute("access_token",request.getParameter("access_token"));
	      	    model.addAttribute("scope",request.getParameter("scope"));
	      	    model.addAttribute("grant_type",request.getParameter("grant_type"));
	      	    model.addAttribute("infoList", JSONArray.fromObject(map.get("infoList")));
	      	    return "demo/loginok";
	        }else if(("2").equals(map.get("error_code"))){
	        	model.addAttribute("mess", "access_token与client_id不匹配");
	        }else if(("3").equals(map.get("error_code"))){
	        	model.addAttribute("mess","用户中心不存在该用户");
	        }else if(("1").equals(map.get("error_code"))){
	        	model.addAttribute("mess", "client_id错误");
	        }else if(("4").equals(map.get("error_code"))){
	        	model.addAttribute("mess", "密码错误");
	        }
        }
  	    model.addAttribute("client_id",request.getParameter("client_id"));
  	    model.addAttribute("access_token",request.getParameter("access_token"));
  	    model.addAttribute("scope",request.getParameter("scope"));
  	    model.addAttribute("grant_type",request.getParameter("grant_type"));
        return "redirect:login";
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
               result += line;
            }
        } catch (Exception e) {
            /*System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();*/
        	return result;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
    
    
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(),"utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
    
    public static Map jsonToObject(String jsonStr) throws Exception {
    	String str=jsonStr.substring(1, jsonStr.length()-1);
    	String[] strs=str.split(",");
		String name;
		String value;
		Map<String, String> outMap = new HashMap<String, String>();
		for(String s:strs){
			name=s.split(":")[0].substring(1, s.split(":")[0].length()-1);
			if(!("expires_in").equals(name)){
				value=s.split(":")[1].substring(1, s.split(":")[1].length()-1);
			}else{
				value=s.split(":")[1];
			}
			outMap.put(name,value);
		}
		return outMap;
	}
}