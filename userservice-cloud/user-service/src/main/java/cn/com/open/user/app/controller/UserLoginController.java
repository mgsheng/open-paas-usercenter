package cn.com.open.user.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.open.user.app.constant.ConstantMessage;
import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.redis.RedisConstant;
import cn.com.open.user.app.redis.RedisServiceImpl;
import cn.com.open.user.app.service.UserCacheService;
import cn.com.open.user.app.service.UserLoginService;
import cn.com.open.user.app.sign.MD5;
import cn.com.open.user.app.tools.AESUtil;
import cn.com.open.user.app.tools.DES;
import cn.com.open.user.app.tools.DateTools;
import cn.com.open.user.app.tools.StringTool;
import cn.com.open.user.app.vo.UserJsonVo;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;
import cn.com.open.user.app.vo.UserVo;
import net.sf.json.JSONObject;
@RestController
public class UserLoginController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	   @Autowired
	   UserLoginService userLoginService;
	   
	   @Autowired
	   UserCacheService userCacheService;
	   
	   @Autowired
	   RedisServiceImpl redisService;
	   
	   @Value("${aes-userCenter-key}")
	   String key;
	   
	   @Value("${app.localhost.url}")
	   String app_localhost_url;
	   
	   @Value("${app.appId}")
	   String app_appId;
	   
	   /**
	    * 登录
	    * @param request
	    * @param response
	    */
	    @SuppressWarnings("null")
		@RequestMapping(value = "/usercenter/publicLogin", method = RequestMethod.POST)
		public void login(HttpServletRequest request, HttpServletResponse response,UserVo user) {
 	     	log.info("UserLoginController usercenter/login username"+user.getUsername()+"password:"+user.getPassword());
 	     	boolean flag=false;
	    	ArrayList<User> cacheList=null;
	    	ArrayList<UserListVo> infoList =null;
	    	ArrayList<UserJsonVo> jsonList =new ArrayList<UserJsonVo>();
	    	UserMergeVo mergeVo=null;
	    	String platform = request.getHeader("platform");
		   try {
			   Map<String, Object> map = new HashMap<String, Object>();
	 	    	if (!paraMandatoryCheck(Arrays.asList(user.getUsername(),user.getPassword()))) {
				    paraMandaChkAndReturn(7, response, "必传参数中有空值");
	 				return;
	 			}
	 	    	
	 	    	Object userCacheInfoObj = redisService.get(RedisConstant.USER_CACHE_INFO+user.getUsername());
	 	    	//获取缓存数据
				if(userCacheInfoObj!=null ){
					cacheList=userCacheService.findUserCacheByUsername(user);
					if(cacheList!=null||cacheList.size()>0){
						for (User cache : cacheList) {
							int i=checkPasswodByAes(user.getPassword(), cache, key);
							if(i==ConstantMessage.USER_ZERO)continue;
							if(user.getId()>0){//一个密码能登录俩用户时
								paraMandaChkAndReturn(5, response,"登陆异常!");
								return;
							}
							user.setId(cache.getId());
							if(i==ConstantMessage.USER_TWO||i==ConstantMessage.USER_THREE){
								userCacheService.updateUserCache(user);
							}
							mergeVo=new UserMergeVo(cache);
							flag=true;
					   }
					}
					log.info("查询缓存数据 username"+user.getUsername()+"password:"+user.getPassword());
    		    }
				if(!flag){//查询数据库信息
					ArrayList<User> accountList=userLoginService.findUserAccountByUsername(user);
					if(accountList==null||accountList.size()==0){
						paraMandaChkAndReturn(2, response,"用户不存在!");
						return;
					}else{
						for (User userAccount : accountList) {
							int i=checkPasswodByAes(user.getPassword(), userAccount, key);
							if(i==ConstantMessage.USER_ZERO)continue;
							if(user.getId()>0){//一个密码能登录俩用户时
								paraMandaChkAndReturn(5, response,"登陆异常!");
								return;
							}
							user.setId(userAccount.getId());
							if(i==ConstantMessage.USER_TWO||i==ConstantMessage.USER_THREE){
								userLoginService.updateUserAccount(user);
							}
							mergeVo=new UserMergeVo(userAccount);
							flag=true;
						}
						if(user.getId()==0){
							paraMandaChkAndReturn(3, response,"密码错误!");
							return;
						}
						user.setType(ConstantMessage.USER_TWO);
					}
					log.info("查询库数据 username"+user.getUsername()+"password:"+user.getPassword());
    		    }
				
				if(flag){
				   if(user.getId()!=0&&user.getType()==ConstantMessage.USER_ONE)infoList = userCacheService.findUserCacheList(user.getId());
				   if(user.getId()!=0&&user.getType()==ConstantMessage.USER_TWO)infoList = userLoginService.findUserAccountList(user.getId());
				   if(infoList!=null&&infoList.size()>0){
					   UserJsonVo jsonVo=null;
					   for (UserListVo listVo : infoList) {
						   jsonVo=new UserJsonVo(listVo,findCallbackUrl(listVo,platform));
						   jsonList.add(jsonVo);
					   }
					   map.clear();
					   map.put("status", "1");//接口返回状态：1-正确 0-错误
					   map.put("msg","登陆成功");
					   map.put("errorCode","");
					   mergeVo.setInfoList(jsonList);
					   map.put("payload", mergeVo);
					   writeSuccessJson(response,map);
				   }else{
					   paraMandaChkAndReturn(6, response,"登陆失败!");
				   }
			   }else{
				   map.clear();
				   map.put("status", "2");//接口返回状态：1-正确 2-失败
				   map.put("msg","密码错误");
				   map.put("errorCode","3");
			   }
		   } catch (Exception e) {
			   paraMandaChkAndReturn(6, response,"系统异常!");
			   log.info("登陆异常失败 username"+user.getUsername()+"password:"+user.getPassword());
			   e.printStackTrace();
		   }
			return ;
		}
	    
	    

		/*
		 * 根据App和AppUser生成回调URL
		 * DES加密方式
		 * @see cn.com.open.user.app.controller#findCallbackUrl(cn.com.open.user.app.vo.AppUser)
		 */
		public String findCallbackUrl(UserListVo listVo,String platform){
	    	Map<String, Object> map=new HashMap<String,Object>();
			if( listVo==null){
				return "";
			}
			StringBuffer url = new StringBuffer(app_localhost_url+"/usercenter/validateLogin");
			//time：格式yyyyMMddHHmmss
			String time = DateTools.dateToString(new Date(), "yyyyMMddHHmmss");
			
  		    String secret = "";
			try {
				map.put("sourceId", listVo.getSourceId());
				map.put("time", time);
				map.put("appkey", listVo.getAppkey());
				map.put("appId", listVo.getAppId());
				map.put("platform",platform);
				secret = DES.encrypt(JSONObject.fromObject(map).toString(),key);
			} catch (Exception e) {
				e.printStackTrace();
			}
			secret = secret.replaceAll("\\+", "%2B");
			url.append("?secret=").append(secret);
			log.info("生成回调URL路径"+url);
			log.info("生成回调json格式"+JSONObject.fromObject(map));
			return url.toString();
		}
	    
	    
	     /**
	      * 用户中心公共登录接口
	      * @param request
	      * @param response
	      */
    	 @RequestMapping(value = "/usercenter/validateLogin", method = RequestMethod.GET)
 		 public void userCenterPublicLogin(HttpServletRequest request, HttpServletResponse response) {
	    	Map<String, Object> map=new HashMap<String,Object>();
	    	Map<String, Object> mapJson=new HashMap<String,Object>();
	    	map.put("status", "0");
	    	String secret=request.getParameter("secret");
	    	if(StringUtils.isBlank(secret)){
	    		//参数错误
	     		map.put("error_code", "8");
	     		map.put("errMsg", "参数错误");
	     		writeErrorJson(response,map);
	    		return;
	    	}
			 
			String secretDecrypt = "";
	    	try {
				secretDecrypt = DES.decrypt(secret, key.substring(0,8));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	if(StringUtils.isBlank(secretDecrypt)){
	    		//参数解密错误
	    		map.put("error_code", "8");
	     		map.put("errMsg", "参数解密错误");
	     		writeErrorJson(response,map);
	    		return;
	    	}
	    	
		    JSONObject json = JSONObject.fromObject(secretDecrypt);
	      	String sourceId=json.getString("sourceId");
	      	String time=json.getString("time");
	      	String appKey=json.getString("appkey");
	      	String appId=json.getString("appId");
	      	String platform=json.getString("platform");
	    	String businessDate="";
	      	if(json.containsKey("businessDate"))
	     	 businessDate=json.getString("businessDate");
	      	mapJson.put("sourceId", sourceId);
	      	mapJson.put("time", time);
	      	mapJson.put("appkey", appKey);
	      	mapJson.put("appId", appId);
	      	mapJson.put("platform",platform);
	      	mapJson.put("businessDate",businessDate);
	    	App app = userLoginService.findIdByClientId(appKey);
	    	if(app==null){
	    		//App不存在
	    		map.put("error_code", "9");
	     		map.put("errMsg", "该App信息不存在");
	     		writeErrorJson(response,map);
	    		return;
	    	}
	    	StringBuffer url = new StringBuffer(app.getWebServerRedirectUri());
	    	//非教师培训App采用des加密方式
	    	if(app.getId()!=5){
	    		//time：格式yyyyMMddHHmmss
	    		secret = "";
	    		try {
	    			secret = DES.encrypt(JSONObject.fromObject(mapJson).toString(), key);
	    		} catch (Exception e) {
	    			e.printStackTrace();
	    			//异常
	        		map.put("error_code", "6");
	         		map.put("errMsg", "系统异常");
	         		writeErrorJson(response,map);
	        		return;
	    		}
	    		secret = secret.replaceAll("\\+", "%2B");
	    		url.append("?secret=").append(secret);
	    	}
	    	//教师培训App采用MD5加密方式
	    	else{
	    		//salt=4位随机数
	    		String salt = StringTool.getRandomString(4);
	    		secret = MD5.Md5(JSONObject.fromObject(mapJson).toString());
	    		url.append("?sourceId=").append(sourceId);
	    		url.append("&salt=").append(salt);
	    		url.append("&secret=").append(secret);
	    	}
			try {
				response.sendRedirect(url.toString());
			} catch (IOException e) {
				e.printStackTrace();
				//异常
	    		map.put("error_code", "6");
	     		map.put("errMsg", "系统异常");
	     		writeErrorJson(response,map);
	    		return;
			} 
		    log.info("公共方法跳转路径 url"+url);
			return;
	     }
	    
	    /**
	     * 验证密码是否正确
	     * @param password
	     * @return
	     */
	    public int checkPasswodByAes(String aesPassword,User obj,String key){
	    	String aespwd="";
	    	try {
		    	//aes密码验证
		    	if(obj.getAesPassword()!=null&&StringUtils.isNotBlank(obj.getAesPassword())){
		        		if(aesPassword.equals(obj.getAesPassword())){
		        			return ConstantMessage.USER_ONE;
		        		}
		    	}else{
		    		    // md5加密方式验证
			    		if(obj.getMd5Password()!=null&&!"".equals(obj.getMd5Password())){
			    			aespwd=AESUtil.decrypt(aesPassword,key).trim();
			    			aespwd=MD5.Md5(aespwd+obj.getMd5Salt());
			    			String pwd=obj.getMd5Password();
			    			if(pwd.equals(aespwd)){
				        		return ConstantMessage.USER_TWO;
				        	}	
			    		}else if(obj.getSha1Password()!=null&&!"".equals(obj.getSha1Password())){//sha1_password 密码验证
				    		 aespwd=AESUtil.decrypt(aesPassword,key).trim();
			    			 PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
			        		 String password=passwordEncoder.encodePassword(aespwd, null).toLowerCase();
		        		 if(password.equals(obj.getSha1Password().toLowerCase())){
		     				return ConstantMessage.USER_THREE;
		     			}
		        	}
		    	}
	    	} catch (Exception e1) {
	    		e1.printStackTrace();
	    	}
	    	return ConstantMessage.USER_ZERO;
	    }
	    
	   
}
