package cn.com.open.expservice.app.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import cn.com.open.expservice.app.entiy.User;
import cn.com.open.expservice.app.redis.RedisConstant;
import cn.com.open.expservice.app.redis.RedisServiceImpl;
import cn.com.open.expservice.app.service.UserCacheService;
import cn.com.open.expservice.app.service.UserLoginService;
import cn.com.open.expservice.app.sign.MD5;
import cn.com.open.expservice.app.tools.AESUtil;
import cn.com.open.expservice.app.vo.UserJsonVo;
import cn.com.open.expservice.app.vo.UserMergeVo;
@RestController
public class UserLoginController extends BaseController{
	   @Autowired
	   UserLoginService userLoginService;
	   
	   @Autowired
	   UserCacheService userCacheService;
	   
	   @Autowired
	   RedisServiceImpl redisService;
	   
	   @Value("${aes-userCenter-key}")
		String key;
	   
	    
	   final static String  SEPARATOR = "&";
	   private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	   
	   /**
	    * 添加快递订单信息
	    * @param request
	    * @param response
	    */
	    @SuppressWarnings("null")
		@RequestMapping(value = "/usercenter/login", method = RequestMethod.GET)
		public void login(HttpServletRequest request, HttpServletResponse response,User user) {
 	     	log.info("UserLoginController login username"+user.getUsername()+"password:"+user.getPassword());
 	    	  
	    	int userId=0;
	    	int userCacheId=0;
	    	ArrayList<User> cacheList=null;
		   try {
			   Map<String, Object> map = new HashMap<String, Object>();
	 	    	if (!paraMandatoryCheck(Arrays.asList(user.getUsername(),user.getPassword()))) {
				   paraMandaChkAndReturn(9, response, "提交的数据不完整");
	 				return;
	 			}
	 	        
	 	    	Object userCacheInfoObj = redisService.get(RedisConstant.USER_CACHE_INFO+user.getUsername());
				if(userCacheInfoObj==null ){
					cacheList=userCacheService.findUserCacheByUsername(user);
					if(cacheList!=null||cacheList.size()>0){
						for (User cache : cacheList) {
							int i=checkPasswodByAes(user.getPassword(), cache, key);
							if(i==0)continue;
							if(i==2||i==3){
								userCacheService.updateUserCache(cache);
							}
							if(userCacheId>0){
								paraMandaChkAndReturn(2, response,"登陆异常!");
								return;
							}
							userCacheId=cache.getId();
						}
					}
    		    }
					if(userCacheInfoObj==null||cacheList==null||cacheList.size()==0){
						ArrayList<User> accountList=userLoginService.findUserAccountByUsername(user);
						if(accountList==null||accountList.size()==0){
							paraMandaChkAndReturn(2, response,"用户不存在!");
							return;
						}else{
							for (User user2 : accountList) {
								int i=checkPasswodByAes(user.getPassword(), user2, key);
								if(i==0)continue;
								if(i==2||i==3){
									userLoginService.updateUserAccount(user2);
								}
								if(userId>0){
									paraMandaChkAndReturn(2, response,"登陆异常!");
									return;
								}
								userId=user2.getId();
							}
							
							if(userCacheId==0&&userId==0){
								paraMandaChkAndReturn(3, response,"密码错误!");
								return;
							}
							 
						}
    		    }
				ArrayList<UserJsonVo> infoList =null;
				if(userCacheId!=0||userId!=0){
					 if(userCacheId!=0)infoList = userCacheService.findUserCacheList(userCacheId);
					 if(userId!=0)infoList = userLoginService.findUserAccountList(userId);
				   if(infoList!=null&&infoList.size()>0){	
					   UserMergeVo mergeVo=new UserMergeVo();
					   map.clear();
					   map.put("status", "1");//接口返回状态：1-正确 0-错误
					   map.put("msg","登陆成功");
					   map.put("errorCode","");
					   mergeVo.setInfoList(infoList);
					   map.put("payload", mergeVo);
					   writeSuccessJson(response,map);
				   }else{
					   paraMandaChkAndReturn(1, response,"登陆失败!");
				   }
			   }else{
				   map.clear();
				   map.put("status", "2");//接口返回状态：1-正确 2-失败
				   map.put("msg","密码错误");
				   map.put("errorCode","3");
			   }
		   } catch (Exception e) {
			   paraMandaChkAndReturn(1, response,"登陆失败!");
			   e.printStackTrace();
		   }
			return ;
		}
	    
	    
	    
	    /**
	     * 验证密码是否正确
	     * @param password
	     * @return
	     */
	    public int checkPasswodByAes(String aesPassword,User user2,String key){
	    	String aespwd="";
	    	try {
		    	//aes密码验证
		    	if(user2.getAesPassword()!=null&&StringUtils.isNotBlank(user2.getAesPassword())){
		        		if(aesPassword.equals(user2.getAesPassword())){
		        			return 1;
		        		}
		    		
		    	}else{
		    		    // md5加密方式验证
			    		if(user2.getMd5Password()!=null&&!"".equals(user2.getMd5Password())){
			    			aespwd=AESUtil.decrypt(aesPassword,key.substring(0,16)).trim();
			    			if(user2.getMd5Password().equals(MD5.Md5(aespwd))){
				        		return 2;
				        	}	
			    		}
			    	//sha1_password 密码验证
			    	else if(user2.getSha1Password()!=null&&!"".equals(user2.getSha1Password())){
		    			 PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
		        		 String password=passwordEncoder.encodePassword(user2.getSha1Password(), null);
		        		 if(aesPassword.equals(password)){
		     				return 3;
		     			}
		        	}
		    	  }
	    	} catch (Exception e1) {
	    		e1.printStackTrace();
	    	}
	    	return 0;
	    }
	    
	   
}
