package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.MyBatisSystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.thread.SendOesThread;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.Help_Encrypt;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 通过用户名和手机(或邮箱)
 */
@Controller
@RequestMapping("/user/")
public class UserCenterRetrievePwdController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterRetrievePwdController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserserviceDev userserviceDev;
	 @Autowired
	 private UserCacheService userCacheService;
	 @Autowired
	 private AppUserService appUserService;
	    
    /**
     * 
     * @return Json
     * status的含义：0表示有问题、1表示通过
     * error_code的含义：1用户名、表示手机或邮箱不正确、2表示accessToken无效、3表示用户名对应多个用户
     * 查询成功返回：randomString(64位随机字符串)
     */
    @RequestMapping("userCenterRetrievePwd")
    public void userCenterRetrievePwd(HttpServletRequest request,HttpServletResponse response) {
    	long startTime = System.currentTimeMillis();
    	String client_id=request.getParameter("client_id");
    	String access_token=request.getParameter("access_token");
    	String password=request.getParameter("new_pwd");
    	String oldPassword = password;
        String username = request.getParameter("username");
        String guid= request.getParameter("guid");
        String  isValidate =request.getParameter("isValidate");
        String phone="";
        log.info("client_id:"+client_id+"access_token:"+access_token+"guid:"+guid+"username:"+username+"new_pwd:"+password);
    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
    	Map<String, Object> map=new HashMap<String,Object>();
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,username,password))){
            paraMandaChkAndReturn(4,response,"必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
		String new_pwd="";
		try {
				new_pwd=AESUtil.decrypt(password, app.getAppsecret()).trim();
				log.info("加密后的 new_pwd："+new_pwd);
				if(!nullEmptyBlankJudge(isValidate)&&isValidate.equals("1")){
				if(nullEmptyBlankJudge(new_pwd)){
					 paraMandaChkAndReturn(5, response,"密码不能为空");
		       	     return;
				}
				if(StringTool.isNumeric(new_pwd)){
					 paraMandaChkAndReturn(5, response,"密码不能为纯数字");
		        	 return;
				}if(judgeInputNotNo(new_pwd)==1){
					paraMandaChkAndReturn(5, response,"密码必须为6-20位字母、数字或英文下划线符号");
		        	 return;
				}}else{
					if(judgePwdNo(new_pwd)==1){
					map = paraMandaChkAndReturnMap(5, response,"密码必须为6-20位");
						writeErrorJson(response,map);
			        	return;
					}
				}
		
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        
        if(app==null)
		{
			 app=appService.findIdByClientId(client_id);
			 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
		}
		map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
		
		if(map.get("status").equals("1")){//client_id,access_token正确
			Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
			if(!hmacSHA1Verification){
				paraMandaChkAndReturn(6, response,"认证失败");
				return;
			}
			
			Object userCacheInfoObj = redisClient.getObject(RedisConstant.USER_CACHE_INFO+username);
			if(userCacheInfoObj!=null)
			{
				UserCache userCache = checkCacheUsername(username,userCacheService,app.getId());
				if(userCache!=null)
				{
					userCache.setPlanPasswordByAes(new_pwd, userserviceDev.getAes_userCenter_key());
					userCache.setUpdatePwdTime(new Date());
					userCacheService.updateUserCache(userCache);
		    		map.clear();
		    		map.put("status", "1");
		    		writeSuccessJson(response,map);
		    		return;
				}
			}
			if(username==null || username.length()==0 ){
				map.clear();
				map.put("status", "0");
	    		map.put("error_code", "3");//用户名、手机或邮箱不正确
	    		map.put("errMsg", "用户名不存在");
	    		
			}else{
				User user=null;
				boolean bool=false;//捕获查询过多的异常
				try {
					if((null==user||user.getId()<1)&&!nullEmptyBlankJudge(guid)){
			          	  user = userService.findByGuid(guid);
			          	if(user!=null)
			          	{
			          		 phone=user.getPhone();
			          	}
			           }
					bool=true;
				} catch (MyBatisSystemException e) {
					map.clear();
					map.put("status", "0");
	        		map.put("error_code", "3");
	        		map.put("errMsg", "用户名不存在");
				} catch (Exception e) {
					map.clear();
					map.put("status", "0");
	        		map.put("error_code", "3");//
	        		map.put("errMsg", "用户名不存在");
				}
				if(bool){
	    			if(user==null){
	    				map.clear();
	    				map.put("status", "0");
	            		map.put("error_code", "3");//用户名、手机或邮箱不正确
	            		map.put("errMsg", "用户名不存在");
	    			}else if(StringUtils.isNotBlank(user.username())&&!user.username().equals(username)&&StringUtils.isNotBlank(phone)&&!phone.equals(username)&&StringUtils.isNotBlank(user.email())&&!user.email().equals(username)){
    					map.clear();
	    				map.put("status", "0");
	            		map.put("error_code", "3");//用户名、手机或邮箱不正确
	            		map.put("errMsg", "用户名不存在");
    			    }else{    
	    				map.clear();
	    		
	    				   //用户中心加密
	    				    try {
								password=AESUtil.encrypt(new_pwd, userserviceDev.getAes_userCenter_key());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				    PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
	    				    String sha1Password=passwordEncoder.encodePassword(new_pwd, null);
	    		    		//刷新盐值，重新加密
	    		    		user.setAesPassword(password);
	    		    		user.setSha1Password(sha1Password);
	    		    		user.setUpdatePwdTime(new Date());
	    		    		userService.updateUser(user);
	    		    		List<AppUser>appUserList=appUserService.findByUserId(user.getId());
	    					try {
	    						Thread thread = new Thread(new SendOesThread(new_pwd, userService,appUserList,userserviceDev.getOes_interface_addr(),userserviceDev.getOes_interface_key()));
	    						thread.run();
	    					} catch (Exception e) {
	    						e.printStackTrace();
	    					}
	    		    		map.clear();
	    		    		map.put("status", "1");
	    		              		   
	    			}   
				}
	    	}
		}		
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
    	OauthControllerLog.log(startTime, username, oldPassword, app, map,userserviceDev);
        return;
    }

   
    
}