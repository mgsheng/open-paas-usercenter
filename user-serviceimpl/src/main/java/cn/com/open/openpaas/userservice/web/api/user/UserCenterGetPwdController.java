package cn.com.open.openpaas.userservice.web.api.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.DES;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 查询用户密码
 */
@Controller
@RequestMapping("/user/")
public class UserCenterGetPwdController extends BaseControllerUtil{

@Autowired
private DefaultTokenServices tokenServices;
@Autowired
private RedisClientTemplate redisClient;
@Autowired
private UserService userService;
@Autowired
private UserserviceDev userserviceDev;
@Autowired
private AppUserService appUserService;
@Autowired
private UserCacheService userCacheService;



	private static final Logger log = LoggerFactory.getLogger(UserCenterGetPwdController.class);
		@RequestMapping("userCenterGetPassWord")
		public void getPassWord(HttpServletRequest request,HttpServletResponse response) {
	        Map<String, Object> map = new HashMap<String, Object>();
			String client_id = request.getParameter("client_id");
			String access_token = request.getParameter("access_token");
			String userName = request.getParameter("userName");
			String source_id = request.getParameter("source_id");
			User user=null;
			UserCache userCache=null;
	        String pwd="";
	 	     try{
	 	     	log.info("client_id:"+client_id+"access_token:"+access_token+"userName:"+userName);
	 	    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
	 	    	if (!paraMandatoryCheck(Arrays.asList(client_id,access_token,userName,source_id))) {
	 				paraMandaChkAndReturn(3, response, "必传参数中有空值");
	 				return;
	 			}
	 			App app = (App) redisClient.getObject(RedisConstant.APP_INFO+ client_id);
	 			map = checkClientIdOrToken(client_id, access_token, app, tokenServices);
	 			if (map.get("status").equals("1")) {// client_id,access_token正确	 	
	 				Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
	 				if(!hmacSHA1Verification){
	 					paraMandaChkAndReturn(9, response,"认证失败");
	 					return;
	 				}
		    	  if(userName!=null&&!"".equals(userName)){
		    		userName=userName.toLowerCase();
		    		log.info("用户："+userName+" 调用时间："+DateTools.getNow()+"调用接口");
		    		
		    		AppUser appUser = appUserService.findByCidSid(app.getId(), source_id);
		    		if(appUser!=null){
			    		if(appUser.isCache()==0){
		    				user=userService.findByUsername(userName);
			    		}else{
			    			Object userCacheInfoObj = redisClient.getObject(RedisConstant.USER_CACHE_INFO+userName);
							//存在缓存信息，用户存在于用户异常表中
							if(userCacheInfoObj!=null ){
								userCache = checkCacheUsername(userName,userCacheService,app.getId());
								if(userCache == null){
									user=userService.findByUsername(userName);
								}
			    		    }else{
			    		    	user=userService.findByUsername(userName);
			    		    }
			    	   }
			    	}else{
	    		    	user=userService.findByUsername(userName);
	    		    }
		    		
			    	 if(user!=null){
						if(user.getAesPassword()!=null&&!"".equals(user.getAesPassword())){
							pwd=user.getAesPassword();
						}else{
							if(user.getDesPassword()!=null&&!"".equals(user.getDesPassword()))
							pwd=user.getDesPassword();
						}
						log.info("user pwd：" + pwd);
		    		 }else if(userCache!=null){
						if(userCache.getAesPassword()!=null&&!"".equals(userCache.getAesPassword())){
							pwd=userCache.getAesPassword();
						}else{
							if(userCache.desPassword()!=null&&!"".equals(userCache.desPassword()))
							pwd=userCache.desPassword();
						}
						log.info("userCache pwd：" + pwd);
		    		 } else{
			    	    paraMandaChkAndReturn(1, response, "用户不存在");
			    	 }
			    	
			    	if(!"".equals(pwd)){
				    	log.info("解密前pwd：" + pwd);
						pwd = AESUtil.decrypt(pwd, userserviceDev.getAes_userCenter_key()).trim();
						log.info("AESUtil.decrypt解密后pwd：" + pwd);
						pwd = DES.encrypt(pwd, app.getAppsecret()).trim();
						log.info("DES.encrypt加密后pwd：" + pwd);
			    	}
					map.clear();
					map.put("status", "1");//接口返回状态：1-正确 0-错误
					map.put("pwd",pwd);
		    	  }
		    	}
			} catch (Exception e) {
				writeErrorJson(response, map);
			}
	 	    if (map.get("status") == "0") {
				writeErrorJson(response, map);
			} else {
				writeSuccessJson(response, map);
			}
	    	  log.info("接口调用查询密码为："+pwd);
	        return ;
	    }
	 
}