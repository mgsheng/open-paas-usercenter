package com.andaily.springoauth.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.andaily.springoauth.service.dto.UserCenterLoginDto;
import com.andaily.springoauth.service.dto.UserCenterRegDto;
import com.andaily.springoauth.tools.AESUtil;
import com.andaily.springoauth.tools.DES;
import com.andaily.springoauth.tools.DESUtil;
import com.andaily.springoauth.tools.DateTools;
import com.andaily.springoauth.tools.HMacSha1;
import com.andaily.springoauth.tools.LoadPopertiesFile;

/**
 * Handle 'authorization_code'  type actions
 *
 * 
 */
@Controller
public class UserInterfaceController {


    private static final Logger LOG = LoggerFactory.getLogger(UserInterfaceController.class);

    @Value("#{properties['user-center-reg-uri']}")
    private String userCenterRegUri;
    
    @Value("#{properties['user-save-info-uri']}")
    private String saveUserInfo;
    @Value("#{properties['user-syn-info-uri']}")
    private String synUserInfoUri;
    @Value("#{properties['dynmic-login-uri']}")
    private String dynamicLoginUri;
    @Value("#{properties['user-bind-info-uri']}")
    private String bindUserInfoUri;
    @Value("#{properties['user-center-verify-uri']}")
    private String userCenterVerifyUri;
    
    @Value("#{properties['user-center-password-uri']}")
    private String userCenterPasswordUri;
    
    @Value("#{properties['user-center-modipwd-uri']}")
    private String userCenterModiPwdUri;
 
    
    @Value("#{properties['user-center-checktoken-uri']}")
    private String userCenterCheckTokenUri;
    
    @Value("#{properties['user-center-retrievepwd-uri']}")
    private String userCenterRetrievePwdUri;
  
    
    @Value("#{properties['user-center-getValidationRule-uri']}")
    private String getValidationRuleUri;
    
    @Value("#{properties['user-center-verifyPassWord-uri']}")
    private String verifyPassWordUri;

    @Value("#{properties['validate-login-uri']}")
    private String validateLoginUri;
    @Value("#{properties['validate-login-noToken-uri']}")
    private String validateLoginNoTokenUri;
    
    @Value("#{properties['user-unbind-info-uri']}")
    private String unBindUserInfoUri;
    @Value("#{properties['user-verify-payUser-uri']}")
    private String verifyPayUserUri;
    @Value("#{properties['user-auto-login-uri']}")
    private String autoLoginUri;
    @Value("#{properties['verify-session-uri']}")
    private String verifySessionUri;
    @Value("#{properties['destory-session-uri']}")
    private String destorySessionUri;
    @Value("#{properties['user-guid-info-uri']}")
    private String userGuidInfoUri;
    
   @Value("#{properties['verify-auto-login-uri']}")
    private String verifyAutoLogin;
    @Value("#{properties['aes-key']}")
    
    final static String  SEPARATOR = "&";
	@Value("#{properties['get-redis-uri']}")
	private String getRedisUri;
	@Value("#{properties['save-redis-uri']}")
	private String saveRedisUri;
	@Value("#{properties['user-status-change-uri']}")
	private String userStatusChangeUri;
    private Map<String,String> map=LoadPopertiesFile.loadProperties();
    
      /**
       * 用户注册接口
       * @param model
       * @return
       */
      @RequestMapping(value = "userCenterReg", method = RequestMethod.GET)
      public String reg(Model model) {
      	model.addAttribute("userCenterRegUri", userCenterRegUri);
      	
        return "usercenter/user_center_reg";
      }
  
       @RequestMapping(value = "userCenterReg", method = RequestMethod.POST)
       public String reg(UserCenterRegDto userCenterRegDto) throws Exception {
        	final String fullUri = getUri(userCenterRegDto);
            LOG.debug("Send to Oauth-Server URL: {}", fullUri);

            return "redirect:" + fullUri;
        }
       /**
        * 验证自动登录接口
        * @param model
        * @return
        */
       @RequestMapping(value = "verifyAutoLogin", method = RequestMethod.GET)
       public String verifyAutoLogin(Model model) {
       	model.addAttribute("verifyAutoLogin", verifyAutoLogin);
       	
         return "usercenter/verify_auto_login";
       }
        @RequestMapping(value = "verifyAutoLogin", method = RequestMethod.POST)
        public String verifyAutoLogin(String secret,String clientId,String accessToken) throws Exception {
        	String key=map.get(clientId);
        	  String signature="";
	    	  String timestamp="";
	    	  String signatureNonce="";
           	if(key!=null){
           		secret=DES.encrypt(secret, key);
           		secret=DES.getNewSecert(secret);
           		timestamp=DateTools.getSolrDate(new Date());
	  		 	StringBuilder encryptText = new StringBuilder();
	  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	  		 	encryptText.append(clientId);
	  			encryptText.append(SEPARATOR);
	  		 	encryptText.append(accessToken);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(timestamp);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(signatureNonce);
	  		 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
	  		 	signature=HMacSha1.getNewResult(signature);
           	    }
                   final String fullUri = verifyAutoLogin+"?client_id="+clientId+"&secret="+secret+"&access_token="+accessToken+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
                   LOG.debug("Send to Oauth-Server URL: {}", fullUri);
                   return "redirect:" + fullUri;
        }
       /**
        * 用户保存信息接口
        * @param model
        * @return
        */
       @RequestMapping(value = "userSaveInfo", method = RequestMethod.GET)
       public String saveInfo(Model model) {
       	model.addAttribute("saveUserInfo", saveUserInfo);
       	
         return "usercenter/user_center_saveInfo";
       }
   
        @RequestMapping(value = "userSaveInfo", method = RequestMethod.POST)
        public String saveInfo(UserCenterRegDto userCenterRegDto) throws Exception {
        	String key=map.get(userCenterRegDto.getClientId());
	    	  String result="";
	    	  String timestamp="";
	    	  String signatureNonce="";
		      	if(key!=null){
		      		String password=userCenterRegDto.getPassword().trim();
		      		password=AESUtil.encrypt(password, key);
		      		password=AESUtil.getNewPwd(password);
		      		userCenterRegDto.setAesPassword(password);
		      		timestamp=DateTools.getSolrDate(new Date());
		  		 	StringBuilder encryptText = new StringBuilder();
		  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		  		 	encryptText.append(userCenterRegDto.getClientId());
		  			encryptText.append(SEPARATOR);
		  		 	encryptText.append(userCenterRegDto.getAccess_token());
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(timestamp);
		  		 	encryptText.append(SEPARATOR);
		  		 	encryptText.append(signatureNonce);
		  			result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
		  			result=HMacSha1.getNewResult(result);
		      	}
          final String fullUri = userCenterRegDto.getSaveUri(result,timestamp,signatureNonce);

         return "redirect:" + fullUri;
         }
       /**
        * 获取界面返回地址
        * @param userCenterRegDto
        * @return
        * @throws Exception
        * @throws UnsupportedEncodingException
        */
	   public String getUri(UserCenterRegDto userCenterRegDto) throws Exception,
				UnsupportedEncodingException {
			String key=map.get(userCenterRegDto.getClientId());
	    	  String result="";
	    	  String timestamp="";
	    	  String signatureNonce="";
        	if(key!=null){
        		String password=userCenterRegDto.getPassword().trim();
        		password=AESUtil.encrypt(password, key);
        		password=AESUtil.getNewPwd(password);
        		userCenterRegDto.setAesPassword(password);
        		timestamp=DateTools.getSolrDate(new Date());
    		 	StringBuilder encryptText = new StringBuilder();
    		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
    		 	encryptText.append(userCenterRegDto.getClientId());
    			encryptText.append(SEPARATOR);
    		 	encryptText.append(userCenterRegDto.getAccess_token());
    		 	encryptText.append(SEPARATOR);
    		 	encryptText.append(timestamp);
    		 	encryptText.append(SEPARATOR);
    		 	encryptText.append(signatureNonce);
    			result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
    			result=HMacSha1.getNewResult(result);
        	}
            final String fullUri = userCenterRegDto.getAesFullUri(result,timestamp,signatureNonce)+"&";
			return fullUri;
		}
	  /**
	   * 获取 HMAC-SHA1 签名方法对对encryptText进行签名 值
	   * @param request
	   * @param response
	   * @param clientId
	   * @param accessToken
	   */
	    @RequestMapping(value = "/userCenterReg/getSignature",method = RequestMethod.POST)
	    public void getSignature(HttpServletRequest request,HttpServletResponse response,String clientId,String accessToken,String password,String oldPassword){
	    	//返回数据
	    	boolean flag = false;
	    	String key=map.get(clientId);
	    	  String signature="";
	    	  String timestamp="";
	    	  String signatureNonce="";
	      	if(key!=null){ 
	      		timestamp=DateTools.getSolrDate(new Date());
	  		 	StringBuilder encryptText = new StringBuilder();
	  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	  		 	encryptText.append(clientId);
	  			encryptText.append(SEPARATOR);
	  		 	encryptText.append(accessToken);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(timestamp);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(signatureNonce);
	  			try {
	  				signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
	  				if(password!=null){
	  					password=AESUtil.encrypt(password, key);
	  					password=AESUtil.getNewPwd(password);
	  				}
	  				if(oldPassword!=null){
	  					oldPassword=AESUtil.encrypt(oldPassword, key);
	  					oldPassword=AESUtil.getNewPwd(oldPassword);
	  				}
				} catch (Exception e) {
					e.printStackTrace();
					flag=false;
				}
	  			signature=HMacSha1.getNewResult(signature);
	  			flag=true;
	      	}
	      	Map<String,Object> returnMap = new HashMap<String, Object>();
	      	returnMap.put("flag",flag);
	      	returnMap.put("signature",signature);
	      	returnMap.put("timestamp",timestamp);
	      	returnMap.put("signatureNonce",signatureNonce);
	      	returnMap.put("password",password);
	      	returnMap.put("oldPassword",oldPassword);
	    	WebUtils.writeJsonToMap(response, returnMap);
	    }
	    
       /**
        * 用户同步信息跳转界面方法
        * @param model
        * @return
        */
        @RequestMapping(value = "synUserInfo", method = RequestMethod.GET)
        public String synUserInfo(Model model) {
          model.addAttribute("synUserInfo", synUserInfoUri);
          return "usercenter/user_syn_info";
        }
       
        @RequestMapping(value = "synUserInfo", method = RequestMethod.POST)
        public String synUserInfo(UserCenterRegDto userCenterRegDto) throws Exception {
        	  
        	  String key=map.get(userCenterRegDto.getClientId());
	    	  String result="";
	    	  String timestamp="";
	    	  String signatureNonce="";
        	if(key!=null){
        		timestamp=DateTools.getSolrDate(new Date());
    		 	StringBuilder encryptText = new StringBuilder();
    		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
    		 	encryptText.append(userCenterRegDto.getClientId());
    			encryptText.append(SEPARATOR);
    		 	encryptText.append(userCenterRegDto.getAccess_token());
    		 	encryptText.append(SEPARATOR);
    		 	encryptText.append(timestamp);
    		 	encryptText.append(SEPARATOR);
    		 	encryptText.append(signatureNonce);
    			result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
    			result=HMacSha1.getNewResult(result);
        	}
            final String fullUri = userCenterRegDto.getSynInfoUri(result,timestamp,signatureNonce);        	  
              //final String fullUri = userCenterRegDto.getsynUserInfoUri();
        	  LOG.debug("Send to Oauth-Server URL: {}", fullUri);

              return "redirect:" + fullUri;
          }
        
	   /**
	    * 动态密码登录跳转界面方法
	    * @param model
	    * @return
	    */
          @RequestMapping(value = "dynamicLogin", method = RequestMethod.GET)
          public String dynamicLogin(Model model) {
          	model.addAttribute("dynamicLogin", dynamicLoginUri);
            return "usercenter/dynamic_login";
          }
          
   	   /**
   	    * 用户信息绑定跳转界面方法
   	    * @param model
   	    * @return
   	    */
        @RequestMapping(value = "bindUserInfo", method = RequestMethod.GET)
        public String bindUserInfo(Model model) {
            model.addAttribute("bindUserInfo", bindUserInfoUri);
            return "usercenter/user_bind_info";
        }
           
       @RequestMapping(value = "bindUserInfo", method = RequestMethod.POST)
       public String bindUserInfo(String access_token,String grant_type ,String scope,String client_id,String guid,String source_id,String phone,String email) throws Exception {
             	  String key=map.get(client_id);
             	  String fullUri ="";
             	  String signature="";
             	  String timestamp="";
             	  String signatureNonce="";
         	      	if(key!=null){
         	      	   timestamp=DateTools.getSolrDate(new Date());
         			 	StringBuilder encryptText = new StringBuilder();
         			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
         			 	encryptText.append(client_id);
         				encryptText.append(SEPARATOR);
         			 	encryptText.append(access_token);
         			 	encryptText.append(SEPARATOR);
         			 	encryptText.append(timestamp);
         			 	encryptText.append(SEPARATOR);
         			 	encryptText.append(signatureNonce);
         			 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
         			 	signature=HMacSha1.getNewResult(signature);
         	      		
         	   }
         fullUri = bindUserInfoUri+"?access_token="+access_token+"&client_id="+client_id+"&guid="+guid+"&source_id="+source_id+"&grant_type="+grant_type+"&scope="+scope+"& phone="+ phone+"&email="+email+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
         LOG.debug("Send to Oauth-Server URL: {}", fullUri);
         return "redirect:" + fullUri;
      }  
      
      /**
       * 用户登录接口
       * @param model
       * @return
       */
      @RequestMapping(value = "userCenterPassword", method = RequestMethod.GET)
      public String password(Model model,HttpServletRequest request, HttpServletResponse response) {
//        	 Cookie cookie[]=request.getCookies();
//           	 for (Cookie c:cookie) {
//    			if(c.getName().equals("singleSignUser"))	
//    			{
//    			 	model.addAttribute("singleSignUser", c.getValue());
//    			    return "usercenter/success";
//    			}
//    			}
    	model.addAttribute("userCenterPasswordUri", userCenterPasswordUri);
        return "usercenter/user_center_password";
      }
        
      @RequestMapping(value = "userCenterPassword", method = RequestMethod.POST)
      public String password(Model model,UserCenterLoginDto userCenterLoginDto,HttpServletResponse res) throws Exception {
    
    	  String key=map.get(userCenterLoginDto.getClient_id());
    	  String fullUri ="";
    	  String result="";
    	  String timestamp="";
    	  String signatureNonce="";
	      	if(key!=null){
	      	   timestamp=DateTools.getSolrDate(new Date());
			 	StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			 	encryptText.append(userCenterLoginDto.getClient_id());
				encryptText.append(SEPARATOR);
			 	encryptText.append(userCenterLoginDto.getAccess_token());
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
			 	result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
				result=HMacSha1.getNewResult(result);
	      		String password=userCenterLoginDto.getPassword().trim();
	      		password=AESUtil.encrypt(password,key);
	      		password=AESUtil.getNewPwd(password);
	      		userCenterLoginDto.setPassword(password);
	      		
	      	}
	      	fullUri=userCenterLoginDto.getAESFullUri(result,timestamp,signatureNonce);
          LOG.debug("Send to Oauth-Server URL: {}", fullUri);
      	String data = sendPost(fullUri,"");
      	JSONObject a=JSONObject.fromObject(data);
      	System.out.println(data);
      	Cookie c=new  Cookie("singleSignUser", (String) a.get("jsessionId"));
      	c.setMaxAge(-1);
      	res.addCookie(c);
      	model.addAttribute("data",	data);
    	model.addAttribute("singleSignUser",	 (String) a.get("jsessionId"));
        return "usercenter/success";
      }

     /**
      * 用户找回密码
      * @param model
      * @return
      */
    @RequestMapping(value = "userCenterRetrievePwd", method = RequestMethod.GET)
    public String userCenterRetrievePwd(Model model) {
          	model.addAttribute("userCenterRetrievePwdUri", userCenterRetrievePwdUri);
              return "usercenter/user_center_retrievepwd";
       }
         
     @RequestMapping(value = "userCenterRetrievePwd", method = RequestMethod.POST)
     public String userCenterRetrievePwd(String access_token,String username,String new_pwd,String client_id,String guid,String isValidate) throws Exception {
            String key=map.get(client_id);
	      	String timestamp="";
		  	String signatureNonce="";
		  	String signature="";
		  	if(key!=null){
		  		new_pwd=AESUtil.encrypt(new_pwd, key);
	    		new_pwd=AESUtil.getNewPwd(new_pwd);
		  	    timestamp=DateTools.getSolrDate(new Date());
		  	    StringBuilder encryptText = new StringBuilder();
			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			 	encryptText.append(client_id);
				encryptText.append(SEPARATOR);
			 	encryptText.append(access_token);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(timestamp);
			 	encryptText.append(SEPARATOR);
			 	encryptText.append(signatureNonce);
			 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			 	signature=HMacSha1.getNewResult(signature);
		  	}
       final String fullUri = userCenterRetrievePwdUri+"?access_token="+access_token+"&username="+username+"&new_pwd="+new_pwd+"&client_id="+client_id+"&guid="+guid+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce+"&isValidate="+isValidate;
       LOG.debug("Send to Oauth-Server URL: {}", fullUri);
       return "redirect:" + fullUri;
       }
    
      /**
       * 用户验证接口
       * @param model
       * @return
       */
      @RequestMapping(value = "userCenterVerify", method = RequestMethod.GET)
      public String varify(Model model) {
      	  model.addAttribute("userCenterVerifyUri", userCenterVerifyUri);
          return "usercenter/user_center_verify1";
      }
    
       
       @RequestMapping(value = "userCenterVerify", method = RequestMethod.POST)
      public String varify(String userCenterVerifyUri,String access_token,String account,String client_id,String accountType) throws Exception {  
	  	String key=map.get(client_id);
	  	String timestamp="";
	  	String signatureNonce="";
	  	String result="";
	  	if(key!=null){
	  	    timestamp=DateTools.getSolrDate(new Date());
	  	    StringBuilder encryptText = new StringBuilder();
		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		 	encryptText.append(client_id);
			encryptText.append(SEPARATOR);
		 	encryptText.append(access_token);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(timestamp);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(signatureNonce);
			result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			result=HMacSha1.getNewResult(result);
	  	}
	  final String fullUri = userCenterVerifyUri+"?access_token="+access_token+"&account="+account+"&client_id="+client_id+"&accountType="+accountType+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
      LOG.debug("Send to Oauth-Server URL: {}", fullUri);

      return "redirect:" + fullUri;
  }
    
    /*
     * Entrance:   step-1
     * */
      @RequestMapping(value = "userCenterModiPwd", method = RequestMethod.GET)
      public String modipwd(Model model) {
      	model.addAttribute("userCenterModiPwdUri", userCenterModiPwdUri);
          return "usercenter/user_center_modipwd";
      }
      
  /* 
   * Redirect to oauth-server bind page:   step-2
   * */
    @RequestMapping(value = "userCenterModiPwd", method = RequestMethod.POST)
    public String modipwd(String access_token,String account,String old_pwd,String new_pwd,String client_id,String pwdtype,String isValidate) throws Exception {
    	//DES对密码加密
		String key=map.get(client_id);
	  	String timestamp="";
	  	String signatureNonce="";
	  	String result="";
	  	if(key!=null){
	  	    timestamp=DateTools.getSolrDate(new Date());
	  	    StringBuilder encryptText = new StringBuilder();
		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		 	encryptText.append(client_id);
			encryptText.append(SEPARATOR);
		 	encryptText.append(access_token);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(timestamp);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(signatureNonce);
			result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			result=HMacSha1.getNewResult(result);
			/*old_pwd=DESUtil.encrypt(old_pwd, key);
    		old_pwd=DESUtil.getNewPwd(old_pwd);
    		new_pwd=DESUtil.encrypt(new_pwd, key);
    		new_pwd=DESUtil.getNewPwd(new_pwd);*/
    		old_pwd=AESUtil.encrypt(old_pwd, key);
        	old_pwd=AESUtil.getNewPwd(old_pwd);
    		new_pwd=AESUtil.encrypt(new_pwd, key);
    		new_pwd=AESUtil.getNewPwd(new_pwd);
	  	}
        final String fullUri = userCenterModiPwdUri+"?access_token="+access_token+"&account="+account+"&pwdtype="+pwdtype+"&old_pwd="+old_pwd+"&new_pwd="+new_pwd+"&client_id="+client_id+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce+"&isValidate="+isValidate;
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
    }
    
  
    
    /*
     *  Entrance:   step-1
     * */
	@RequestMapping(value = "userCenterCheckToken", method = RequestMethod.GET)
	public String checktoken(Model model) {
		model.addAttribute("userCenterCheckTokenUri", userCenterCheckTokenUri);
		return "usercenter/user_center_checktoken";
	}
	
	/* 
    * Redirect to oauth-server bind page:   step-2
    * */
    @RequestMapping(value = "userCenterCheckToken", method = RequestMethod.POST)
    public String checktoken(String access_token,String client_id) throws Exception {
    	String key=map.get(client_id);
	  	String timestamp="";
	  	String signatureNonce="";
	  	String result="";
	  	if(key!=null){
	  	    timestamp=DateTools.getSolrDate(new Date());
	  	    StringBuilder encryptText = new StringBuilder();
		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		 	encryptText.append(client_id);
			encryptText.append(SEPARATOR);
		 	encryptText.append(access_token);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(timestamp);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(signatureNonce);
			result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			result=HMacSha1.getNewResult(result);
	  	}
    	final String fullUri = userCenterCheckTokenUri+"?access_token="+access_token+"&client_id="+client_id+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
    }   

    /**
     *获取密码规则
     * @param model
     * @return
     */
     @RequestMapping(value = "userCentergetValidationRule", method = RequestMethod.GET)
     public String UserCentergetValidationRule(Model model) {
     	model.addAttribute("getValidationRuleUri", getValidationRuleUri);
       return "usercenter/user_center_getPasswordRule";
     }
     @RequestMapping(value = "userCentergetValidationRule", method = RequestMethod.POST)
     public String UserCentergetValidationRule(String access_token,String client_id,String pwdRule) throws Exception {
    	   String key=map.get(client_id);
	   	  	String timestamp="";
	   	  	String signatureNonce="";
	   	  	String signature="";
	   	  	if(key!=null){
	   	  	    timestamp=DateTools.getSolrDate(new Date());
	   	  	    StringBuilder encryptText = new StringBuilder();
	   		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	   		 	encryptText.append(client_id);
	   			encryptText.append(SEPARATOR);
	   		 	encryptText.append(access_token);
	   		 	encryptText.append(SEPARATOR);
	   		 	encryptText.append(timestamp);
	   		 	encryptText.append(SEPARATOR);
	   		 	encryptText.append(signatureNonce);
	   		    signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
	   		    signature=HMacSha1.getNewResult(signature);
	   	  	}
           final String fullUri = getValidationRuleUri+"?access_token="+access_token+"&client_id="+client_id+"&pwdRule="+pwdRule+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
           LOG.debug("Send to Oauth-Server URL: {}", fullUri);
           return "redirect:" + fullUri;
       }
   
       /**
        *查询密码是否符合规则
        * @param model
        * @return
        */
      @RequestMapping(value = "userCenterVerifyPassWord", method = RequestMethod.GET)
      public String userCenterVerifyPassWord(Model model) {
        	model.addAttribute("verifyPassWordUri", verifyPassWordUri);
           return "usercenter/user_center_verifyPassWord";
        }
    

      @RequestMapping(value = "userCenterVerifyPassWord", method = RequestMethod.POST)
      public String userCenterVerifyPassWord(String access_token,String client_id,String password) throws Exception {
        	    String key=map.get(client_id);
	      	  	String timestamp="";
	      	  	String signatureNonce="";
	      	  	String signature="";
	      	  	if(key!=null){
	        		password=AESUtil.encrypt(password.trim(), key);
	        		password=AESUtil.getNewPwd(password);
	      	  	    timestamp=DateTools.getSolrDate(new Date());
	      	  	    StringBuilder encryptText = new StringBuilder();
	      		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	      		 	encryptText.append(client_id);
	      			encryptText.append(SEPARATOR);
	      		 	encryptText.append(access_token);
	      		 	encryptText.append(SEPARATOR);
	      		 	encryptText.append(timestamp);
	      		 	encryptText.append(SEPARATOR);
	      		 	encryptText.append(signatureNonce);
	      		 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
	      		 	signature=HMacSha1.getNewResult(signature);
	      	  	}
              final String fullUri = verifyPassWordUri+"?access_token="+access_token+"&client_id="+client_id+"&password="+password+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
              LOG.debug("Send to Oauth-Server URL: {}", fullUri);
              return "redirect:" + fullUri;
          }

      /** 
       * 
      *验证自动登录地址（不需要验证密码规则）
      */
      @RequestMapping(value = "validateLogin", method = RequestMethod.GET)
      public String validateLogin(Model model) {
          model.addAttribute("validateLoginUri", validateLoginUri);
          return "usercenter/user_center_validate_login";
          }
       @RequestMapping(value = "validateLogin", method = RequestMethod.POST)
       public String validateLogin(String access_token,String secret,String client_id) throws Exception {            	   
            String key=map.get(client_id);
            String timestamp="";
            String signatureNonce="";
            String result="";
            if(key!=null){
                timestamp=DateTools.getSolrDate(new Date());
               	StringBuilder encryptText = new StringBuilder();
               	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
               	encryptText.append(client_id);
                encryptText.append(SEPARATOR);
                encryptText.append(access_token);
               	encryptText.append(SEPARATOR);
               	encryptText.append(timestamp);
               	encryptText.append(SEPARATOR);
               	encryptText.append(signatureNonce);
               	result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
               	result=HMacSha1.getNewResult(result);
                  		//secret=DESUtil.encrypt(secret, key);
                  		//secret=DESUtil.getNewPwd(secret);
               	secret=AESUtil.encrypt(secret, key);
                secret=AESUtil.getNewPwd(secret);
              }
               final String fullUri = validateLoginUri+"?access_token="+access_token+"&client_id="+client_id+"&secret="+secret+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
               LOG.debug("Send to Oauth-Server URL: {}", fullUri);
               return "redirect:" + fullUri;
         }
       /** 
        * 
       *验证自动登录地址（不需要验证密码规则）
       */
       @RequestMapping(value = "validateLoginNoToken", method = RequestMethod.GET)
       public String validateLoginNoToken(Model model) {
           model.addAttribute("validateLoginNoTokenUri", validateLoginNoTokenUri);
           return "usercenter/usercenter_validate_login_notoken";
           }
        @RequestMapping(value = "validateLoginNoToken", method = RequestMethod.POST)
        public String validateLoginNoToken(String secret,String client_id) throws Exception {            	   
             String key=map.get(client_id);
             if(key!=null){
                 secret=DES.encrypt(secret, key);
                 secret=DES.getNewSecert(secret);
               }
                final String fullUri = validateLoginNoTokenUri+"?client_id="+client_id+"&secret="+secret;
                LOG.debug("Send to Oauth-Server URL: {}", fullUri);
                return "redirect:" + fullUri;
          }

 
      /**
       * 用户应用解除绑定接口
       * @param model
       * @return
       */
        @RequestMapping(value = "unBindUser", method = RequestMethod.GET)
        public String unBindUser(Model model) {
        	model.addAttribute("unBindUserInfoUri", unBindUserInfoUri);
          return "usercenter/user_unbind_info";
        }
        
        /**
         * 用户应用解除绑定接口
         * 
         */
        @RequestMapping(value = "unBindUser", method = RequestMethod.POST)
        public String unBindUser(UserCenterRegDto userCenterRegDto) throws Exception {
        	  String key=map.get(userCenterRegDto.getClientId());
         	  String signature="";
         	  String timestamp="";
         	  String signatureNonce="";
     	      	if(key!=null){
     	      	   timestamp=DateTools.getSolrDate(new Date());
     			 	StringBuilder encryptText = new StringBuilder();
     			 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
     			 	encryptText.append(userCenterRegDto.getClientId());
     				encryptText.append(SEPARATOR);
     			 	encryptText.append(userCenterRegDto.getAccess_token());
     			 	encryptText.append(SEPARATOR);
     			 	encryptText.append(timestamp);
     			 	encryptText.append(SEPARATOR);
     			 	encryptText.append(signatureNonce);
     			 	signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
     			 	signature=HMacSha1.getNewResult(signature);
     	      		
     	   }
              final String fullUri = userCenterRegDto.getUnBindUserUri(signature,timestamp,signatureNonce);
              LOG.debug("Send to Oauth-Server URL: {}", fullUri);

              return "redirect:" + fullUri;
       }
        /**
         * 用户同步信息跳转界面方法
         * @param model
         * @return
         */
         @RequestMapping(value = "getUserId", method = RequestMethod.GET)
         public String getUserId(Model model) {
           model.addAttribute("verifyPayUserUri", verifyPayUserUri);
           return "usercenter/user_verify_payUser";
         }
        
         @RequestMapping(value = "getUserId", method = RequestMethod.POST)
         public String getUserId(String sourceId, String appId) throws Exception {
         	  
        	 String key=map.get(appId);
       	  	 String signature="";
       	  	 String timestamp="";
       	  	 String signatureNonce="";
    	   	 if(key!=null){
    	   		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	   		timestamp=DateTools.getSolrDate(new Date());
    	   		signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
    	   		sParaTemp.put("appId",appId);
    	   		sParaTemp.put("timestamp", timestamp);
    	   		sParaTemp.put("signatureNonce", signatureNonce);
    	   		sParaTemp.put("source_id", sourceId);
    	   		String params=createSign(sParaTemp);
    	   		signature=HMacSha1.HmacSHA1Encrypt(params, key);
    	   		signature=HMacSha1.getNewResult(signature);
    	   	 }
        	 final String fullUri =verifyPayUserUri+"?app_id="+appId+"&source_id="+sourceId+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
             LOG.debug("Send to pay-service-server URL: {}", fullUri);
             return "redirect:" + fullUri;
           }
         
         
      /**
       * 用户guid查询
       * @param model
       * @return
       */
           @RequestMapping(value = "userGuidInfo", method = RequestMethod.GET)
           public String userGuidInfo(Model model) {
           	  model.addAttribute("userGuidInfoUri", userGuidInfoUri);
               return "usercenter/user_guid_info";
       }
           
           @RequestMapping(value = "userGuidInfo", method = RequestMethod.POST)
           public String userGuidInfo(String userGuidInfoUri,String access_token,String account,String client_id,String accountType) throws Exception {  
     	  	String key=map.get(client_id);
     	  	String timestamp="";
     	  	String signatureNonce="";
     	  	String result="";
     	  	if(key!=null){
     	  	    timestamp=DateTools.getSolrDate(new Date());
     	  	    StringBuilder encryptText = new StringBuilder();
     		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
     		 	encryptText.append(client_id);
     			encryptText.append(SEPARATOR);
     		 	encryptText.append(access_token);
     		 	encryptText.append(SEPARATOR);
     		 	encryptText.append(timestamp);
     		 	encryptText.append(SEPARATOR);
     		 	encryptText.append(signatureNonce);
     			result=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
     			result=HMacSha1.getNewResult(result);
     	  	}
     	  final String fullUri = userGuidInfoUri+"?access_token="+access_token+"&account="+account+"&client_id="+client_id+"&accountType="+accountType+"&signature="+result+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
           LOG.debug("Send to Oauth-Server URL: {}", fullUri);

           return "redirect:" + fullUri;
       }
           
           /** 
            * 
            *验证自动登录地址（不需要验证密码规则）
            */
           @RequestMapping(value = "autoLogin", method = RequestMethod.GET)
           public String autoLogin(Model model) {
           	model.addAttribute("autoLoginUri", autoLoginUri);
               return "usercenter/user_center_auto_login";
           }
        @RequestMapping(value = "autoLogin", method = RequestMethod.POST)
        public String autoLogin(String secret,String app_id,String desApp_id,String desAddress) throws Exception {
     		String key=map.get(app_id);
           	if(key!=null){
           		secret=DES.encrypt(secret, key);
           		secret=DES.getNewSecert(secret);
           	    }
                   final String fullUri = autoLoginUri+"?app_id="+app_id+"&secret="+secret+"&desApp_id="+desApp_id+"&desAddress="+desAddress;
                   LOG.debug("Send to Oauth-Server URL: {}", fullUri);
                   return "redirect:" + fullUri;
               }
        

        /** 
         * 
         *验证Session
         */
        @RequestMapping(value = "verifySession", method = RequestMethod.GET)
        public String verifySession(Model model) {
        	model.addAttribute("verifySessionUri", verifySessionUri);
            return "usercenter/user_center_verify_session";
        }
        @RequestMapping(value = "verifySession", method = RequestMethod.POST)
        public String verifySession(String clientId,String accessToken,String sessionId,HttpServletRequest request) throws Exception {
        	sessionId="error";
        	Cookie cookie[]= request.getCookies();
        	for (Cookie c:cookie) {
    			if(c.getName().equals("singleSignUser"))	
    			{
    				sessionId=c.getValue();
    			}
    			}
			String key=map.get(clientId);
      	  	 String signature="";
      	  	 String timestamp="";
      	  	 String signatureNonce="";
	   	   	 if(key!=null){
	   	   		timestamp=DateTools.getSolrDate(new Date());
		   	   	 StringBuilder encryptText = new StringBuilder();
	  		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
	  		 	encryptText.append(clientId);
	  			encryptText.append(SEPARATOR);
	  		 	encryptText.append(accessToken);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(timestamp);
	  		 	encryptText.append(SEPARATOR);
	  		 	encryptText.append(signatureNonce);
	  			signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
	  			signature=HMacSha1.getNewResult(signature);
	   	   	 }
	       	 final String fullUri =verifySessionUri+"?client_id="+clientId+"&access_token="+accessToken+"&jsessionId="+sessionId+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
	            LOG.debug("Send to user-service-server URL: {}", fullUri);
	            return "redirect:" + fullUri;
       }
        
        /** 
         * 
         *注销Session用户
         */
        @RequestMapping(value = "destorySession", method = RequestMethod.GET)
        public String destorySession(Model model) {
        	model.addAttribute("destorySessionUri", destorySessionUri);
            return "usercenter/user_center_destory_session";
        }
        @RequestMapping(value = "destorySession", method = RequestMethod.POST)
        public String destorySession(String clientId,String accessToken,String sessionId) throws Exception {
        	String key=map.get(clientId);
      	  	 String signature="";
      	  	 String timestamp="";
      	  	 String signatureNonce="";
   	   	 if(key!=null){
   	   	timestamp=DateTools.getSolrDate(new Date());
  	   	 StringBuilder encryptText = new StringBuilder();
		 	signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
		 	encryptText.append(clientId);
			encryptText.append(SEPARATOR);
		 	encryptText.append(accessToken);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(timestamp);
		 	encryptText.append(SEPARATOR);
		 	encryptText.append(signatureNonce);
			signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			signature=HMacSha1.getNewResult(signature);
   	   	 }
       	 final String fullUri =destorySessionUri+"?client_id="+clientId+"&access_token="+accessToken+"&signature="+signature+"&jsessionId="+sessionId+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
            LOG.debug("Send to user-service-server URL: {}", fullUri);
            return "redirect:" + fullUri;
       } 
        
        /** 
         * 单点登录测试
         */
        @RequestMapping(value = "testSingleSign", method = RequestMethod.GET)
        public String testSingleSign(Model model,HttpServletRequest request) {
       	 Cookie cookie[]=request.getCookies();
       	 for (Cookie c:cookie) {
			if(c.getName().equals("singleSignUser"))	
			{
			 	model.addAttribute("singleSignUser", c.getValue());
			    return "usercenter/success";
			}
			}
          	//model.addAttribute("verifySessionUri", verifySessionUri);
          	model.addAttribute("userCenterPasswordUri", userCenterPasswordUri);
          	
           return "usercenter/test_single_sign";
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
     
     /**
		 * 生成加密串
		 * @param parameters
		 * @return
		 */
		public static String createSign(SortedMap<Object,Object> parameters){
			StringBuffer sb = new StringBuffer();
			Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
			Iterator it = es.iterator();
			while(it.hasNext()) {
				Map.Entry entry = (Map.Entry)it.next();
				String k = (String)entry.getKey();
				Object v = entry.getValue();
				if(null != v && !"".equals(v)&& !"null".equals(v) 
						&& !"sign".equals(k) && !"key".equals(k)) {
					sb.append(k + "=" + v + "&");
				}
			}
			 String temp_params = sb.toString();  
			return sb.toString().substring(0, temp_params.length()-1);
		}
	/**
	 *
	 *Redis获取接口
	 */
	@RequestMapping(value = "getRedis", method = RequestMethod.GET)
	public String getRedis(Model model) {
		model.addAttribute("getRedisUri", getRedisUri);
		model.addAttribute("data", null);
		return "usercenter/user_center_getredis";
	}
	@RequestMapping(value = "getRedis", method = RequestMethod.POST)
	public String getRedis(String clientId,String accessToken,String serviceName,String redisKey,String redisValue,Model model) throws Exception {
		String key=map.get(clientId);
		String signature="";
		String timestamp="";
		String signatureNonce="";
		if(key!=null){
			timestamp=DateTools.getSolrDate(new Date());
			StringBuilder encryptText = new StringBuilder();
			signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			encryptText.append(clientId);
			encryptText.append(SEPARATOR);
			encryptText.append(accessToken);
			encryptText.append(SEPARATOR);
			encryptText.append(timestamp);
			encryptText.append(SEPARATOR);
			encryptText.append(signatureNonce);
			signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			signature=HMacSha1.getNewResult(signature);
		}
		/*final String fullUri =getRedisUri+"?client_id="+clientId+"&access_token="+accessToken+"&service_name="+serviceName+"&session_id="+sessionId+"&redis_key="+redisKey+"&redis_value="+redisValue+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
		LOG.debug("Send to user-service-server URL: {}", fullUri);
		return "redirect:" + fullUri;*/
		String data = sendPost(getRedisUri,"client_id="+clientId+"&access_token="+accessToken+"&service_name="+serviceName+"&redis_key="+redisKey+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce);
		LOG.debug("Send to user-service-server URL: {}", data);

		model.addAttribute("getRedisUri", getRedisUri);
		model.addAttribute("data", data);
		return "usercenter/user_center_getredis";
	}
	/**
	 *
	 *Redis保存接口
	 */
	@RequestMapping(value = "saveRedis", method = RequestMethod.GET)
	public String saveRedis(Model model) {
		model.addAttribute("saveRedisUri", saveRedisUri);
		model.addAttribute("data", null);
		return "usercenter/user_center_saveredis";
	}
	@RequestMapping(value = "saveRedis", method = RequestMethod.POST)
	public String saveRedis(String clientId,String accessToken,String serviceName,String redisKey,String redisValue,String sessionTime,Model model) throws Exception {
		String key=map.get(clientId);
		String signature="";
		String timestamp="";
		String signatureNonce="";
		if(key!=null){
			timestamp=DateTools.getSolrDate(new Date());
			StringBuilder encryptText = new StringBuilder();
			signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			encryptText.append(clientId);
			encryptText.append(SEPARATOR);
			encryptText.append(accessToken);
			encryptText.append(SEPARATOR);
			encryptText.append(timestamp);
			encryptText.append(SEPARATOR);
			encryptText.append(signatureNonce);
			signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			signature=HMacSha1.getNewResult(signature);
		}
		/*final String fullUri =saveRedisUri+"?client_id="+clientId+"&access_token="+accessToken+"&service_name="+serviceName+"&session_id="+sessionId+"&redis_key="+redisKey+"&redis_value="+redisValue+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;*/
		String data = sendPost(saveRedisUri,"client_id="+clientId+"&access_token="+accessToken+"&service_name="+serviceName+"&redis_key="+redisKey+"&redis_value="+redisValue+"&session_time="+sessionTime+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce);
		LOG.debug("Send to user-service-server URL: {}", data);

		model.addAttribute("saveRedisUri", saveRedisUri);
		model.addAttribute("data", data);
		return "usercenter/user_center_saveredis";
	}
	/**
	 *
	 *用户账号封停以及启用接口
	 */
	@RequestMapping(value = "updateUserStatus", method = RequestMethod.GET)
	public String updateUserStatus(Model model) {
		model.addAttribute("userStatusChangeUri", userStatusChangeUri);
		
		return "usercenter/user_status_change";
	}
	@RequestMapping(value = "updateUserStatus", method = RequestMethod.POST)
	public String updateUserStatus(String clientId,String accessToken,String guid,String status,Model model) throws Exception {
		String key=map.get(clientId);
		String signature="";
		String timestamp="";
		String signatureNonce="";
		if(key!=null){
			timestamp=DateTools.getSolrDate(new Date());
			StringBuilder encryptText = new StringBuilder();
			signatureNonce=com.andaily.springoauth.tools.StringTools.getRandom(100,1);
			encryptText.append(clientId);
			encryptText.append(SEPARATOR);
			encryptText.append(accessToken);
			encryptText.append(SEPARATOR);
			encryptText.append(timestamp);
			encryptText.append(SEPARATOR);
			encryptText.append(signatureNonce);
			signature=HMacSha1.HmacSHA1Encrypt(encryptText.toString(), key);
			signature=HMacSha1.getNewResult(signature);
		}
		final String fullUri = userStatusChangeUri+"?access_token="+accessToken+"&client_id="+clientId+"&guid="+guid+"&status="+status+"&signature="+signature+"&timestamp="+timestamp+"&signatureNonce="+signatureNonce;
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);
        return "redirect:" + fullUri;
	}
}