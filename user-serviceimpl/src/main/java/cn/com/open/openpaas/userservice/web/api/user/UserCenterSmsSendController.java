package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.logic.UserLogicService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 用户发送短信接口
 */
@Controller
@RequestMapping("/user/")
public class UserCenterSmsSendController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterSmsSendController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private UserLogicService userLogicService;
	 @Autowired
	 private UserCacheService userCacheService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private UserserviceDev userserviceDev;
  /**
    * 用户账号验证接口
	* @return Json
	*/
    @RequestMapping("sms/send")
	public void smsSend(HttpServletRequest request,HttpServletResponse response) {
	    	String client_id=request.getParameter("client_id");
	    	String access_token=request.getParameter("access_token");
	    	String phone=request.getParameter("phone");
	    	String type=request.getParameter("type");
	    	if(nullEmptyBlankJudge(type)){
	    		type="1";
	    	}
	    	Map<String, Object> map=new HashMap<String,Object>();
	        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,phone,type))){
	            paraMandaChkAndReturn(3, response,"参数传递不全");
	            return;
	        }
	        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
	        if(app==null)
			{
				 app=appService.findIdByClientId(client_id);
				 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
			}
	        Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
			if(!f){
				WebUtils.paraMandaChkAndReturn(5, response,"认证失败");
				return;
			}
	        map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
			if(map.get("status").equals("1")){
				boolean flag = false;
				if(type.equals("2")){
					//注册发送验证码
					String code=StringTool.getRandomNum(6);
					int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
					String content = "当前注册验证码："+code+"，验证码时效为"+validTime+"分钟,请在规定时间完成验证操作！";
					flag = userLogicService.sendRegPhone(code,content,phone,UserActivated.USERTYPE_USER);
					if(!flag){
    					WebUtils.paraMandaChkAndReturn(8, response,"短信发送失败");
	    				return;
    				}else{
    					map.clear();
    					map.put("status", "1");
    				}
				}else if (type.equals("1")){
					//找回密码发送验证码
		    		User user = userService.findByUsername(phone);
		    		if(user == null){
		    			//String desPhone=Help_Encrypt.encrypt(phone);
		    			List<User> userList = userService.findByPhone(phone);
		    			if(userList != null&&userList.size()>0){
		            		user = userList.get(0);
		    			}
		    		}
		    		if(user != null){
		    			AppUser appUser=appUserService.findByCidAUid(app.getId(), user.getId());
		    			if(appUser!=null){
		    				if(user.userState().equals("2")){
			    				WebUtils.paraMandaChkAndReturn(6, response,"用户已停用");
			    				return;
			    			}else if(!user.userState().equals("2")&&(user.username()==null||"".equals(user.username()))){
			    				WebUtils.paraMandaChkAndReturn(7, response,"用户名为空");
			    			}else{
			    				//发送短信找回密码验证码
			        			flag = userLogicService.sendResetPassWordPhone(user.getId(),phone,UserActivated.USERTYPE_USER);
			    				if(!flag){
			    					WebUtils.paraMandaChkAndReturn(8, response,"短信发送失败");
				    				return;
			    				}else{
			    					map.clear();
			    					map.put("status", "1");
			    				}	
			    			}
		    			}else{
		    				List<User> list=userService.findByPid(String.valueOf(user.getId()));
		    				if(list!=null&&list.size()>0){
		    					for(int i=0;i<list.size();i++){
		    						User cUser=new User();
		    						cUser=list.get(i);
	                                if(cUser.getAppId().intValue()==app.getId().intValue()){
	                                	if(cUser.userState().equals("2")){
	    	    		    				WebUtils.paraMandaChkAndReturn(6, response,"用户已停用");
	    	    		    				return;
	    	    		    			}else if(!cUser.userState().equals("2")&&(cUser.username()==null||"".equals(cUser.username()))){
	    	    		    				WebUtils.paraMandaChkAndReturn(7, response,"用户名为空");
	    	    		    			}else{
	    	    		    				//发送短信找回密码验证码
	    	    		        			flag = userLogicService.sendResetPassWordPhone(cUser.getId(),phone,UserActivated.USERTYPE_USER);
	    	    		    				if(!flag){
	    	    		    					WebUtils.paraMandaChkAndReturn(8, response,"短信发送失败");
	    	    			    				return;
	    	    		    				}else{
	    	    		    					map.clear();
	    	    		    					map.put("status", "1");
	    	    		    				}	
	    	    		    			}	
	                                }
		    					}
		    				}else{
		    					WebUtils.paraMandaChkAndReturn(10, response,"手机号没有应用权限");
		    				}
		    			}
		    		}else{//不存在
		    			UserCache userCache=null;
	    				//存在缓存信息，用户存在于用户异常表中
	    					userCache = checkCacheUsername(phone,userCacheService,app.getId());
	    					if(userCache!=null){
	    		    			if(userCache.userState().equals("2")){
	    		    				WebUtils.paraMandaChkAndReturn(6, response,"用户已停用");
	    		    				return;
	    		    			}else if(!userCache.userState().equals("2")&&(userCache.username()==null||"".equals(userCache.username()))){
	    		    				WebUtils.paraMandaChkAndReturn(7, response,"用户名为空");
	    		    			}else{
	    		    				//发送短信找回密码验证码
	    		        			flag = userLogicService.sendResetPassWordPhone(userCache.id(),phone,UserActivated.USERTYPE_USER);
	    		    				if(!flag){
	    		    					WebUtils.paraMandaChkAndReturn(8, response,"短信发送失败");
	    			    				return;
	    		    				}else{
	    		    					map.put("status", "1");
	    		    					writeSuccessJson(response,map);
	    		    				    return;
	    		    				}	
	    		    			}
	    					}else{
	    						WebUtils.paraMandaChkAndReturn(9, response,"手机号未注册");
	    	    				return;
	    				}
		    		}
				}else if(type.equals("3")){

					//找回密码发送验证码
		    		User user = userService.findByUsername(phone);
		    		if(user == null){
		    			//String desPhone=Help_Encrypt.encrypt(phone);
		    			List<User> userList = userService.findByPhone(phone);
		    			if(userList != null&&userList.size()>0){
		            		user = userList.get(0);
		    			}
		    		}
		    		if(user != null){
		    			AppUser appUser=appUserService.findByCidAUid(app.getId(), user.getId());
		    			if(appUser!=null){
		    				if(user.userState().equals("2")){
			    				WebUtils.paraMandaChkAndReturn(6, response,"用户已停用");
			    				return;
			    			}else if(!user.userState().equals("2")&&(user.username()==null||"".equals(user.username()))){
			    				WebUtils.paraMandaChkAndReturn(7, response,"用户名为空");
			    			}else{
			    				//发送短信找回密码验证码
			    				String code=StringTool.getRandomNum(6);
								int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
								String content = "当前修改密码验证码："+code+"，验证码时效为"+validTime+"分钟,请在规定时间完成验证操作！";
								flag = userLogicService.sendRegPhone(code,content,phone,UserActivated.USERTYPE_USER);
								if(!flag){
			    					WebUtils.paraMandaChkAndReturn(8, response,"短信发送失败");
				    				return;
			    				}else{
			    					map.clear();
			    					map.put("status", "1");
			    				}
			    			}
		    			}else{
		    				List<User> list=userService.findByPid(String.valueOf(user.getId()));
		    				if(list!=null&&list.size()>0){
		    					for(int i=0;i<list.size();i++){
		    						User cUser=new User();
		    						cUser=list.get(i);
	                                if(cUser.getAppId().intValue()==app.getId().intValue()){
	                                	if(cUser.userState().equals("2")){
	    	    		    				WebUtils.paraMandaChkAndReturn(6, response,"用户已停用");
	    	    		    				return;
	    	    		    			}else if(!cUser.userState().equals("2")&&(cUser.username()==null||"".equals(cUser.username()))){
	    	    		    				WebUtils.paraMandaChkAndReturn(7, response,"用户名为空");
	    	    		    			}else{
	    	    		    				//发送短信找回密码验证码
	    	    		    				String code=StringTool.getRandomNum(6);
	    	    							int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
	    	    							String content = "当前修改密码验证码："+code+"，验证码时效为"+validTime+"分钟,请在规定时间完成验证操作！";
	    	    							flag = userLogicService.sendRegPhone(code,content,phone,UserActivated.USERTYPE_USER);
	    	    							if(!flag){
	    	    		    					WebUtils.paraMandaChkAndReturn(8, response,"短信发送失败");
	    	    			    				return;
	    	    		    				}else{
	    	    		    					map.clear();
	    	    		    					map.put("status", "1");
	    	    		    				}
	    	    		    			}	
	                                }
		    					}
		    				}else{
		    					WebUtils.paraMandaChkAndReturn(10, response,"手机号没有应用权限");
		    				}
		    			}
		    		}else{//不存在
		    			UserCache userCache=null;
	    				//存在缓存信息，用户存在于用户异常表中
	    					userCache = checkCacheUsername(phone,userCacheService,app.getId());
	    					if(userCache!=null){
	    		    			if(userCache.userState().equals("2")){
	    		    				WebUtils.paraMandaChkAndReturn(6, response,"用户已停用");
	    		    				return;
	    		    			}else if(!userCache.userState().equals("2")&&(userCache.username()==null||"".equals(userCache.username()))){
	    		    				WebUtils.paraMandaChkAndReturn(7, response,"用户名为空");
	    		    			}else{
	    		    				//发送短信找回密码验证码
	    		        			flag = userLogicService.sendResetPassWordPhone(userCache.id(),phone,UserActivated.USERTYPE_USER);
	    		    				if(!flag){
	    		    					WebUtils.paraMandaChkAndReturn(8, response,"短信发送失败");
	    			    				return;
	    		    				}else{
	    		    					map.put("status", "1");
	    		    					writeSuccessJson(response,map);
	    		    				    return;
	    		    				}	
	    		    			}
	    					}else{
	    						WebUtils.paraMandaChkAndReturn(9, response,"手机号未注册");
	    	    				return;
	    				}
		    		}
				
				}
			}
	    	if(map.get("status").equals("0")){
	    		writeErrorJson(response,map);
	    	}else{
	    		  writeSuccessJson(response,map);
	    	}
	        return;
	    }

	

} 