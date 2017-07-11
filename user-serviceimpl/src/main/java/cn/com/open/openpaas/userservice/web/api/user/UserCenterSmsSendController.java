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
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.logic.UserLogicService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
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
  /**
    * 用户账号验证接口
	* @return Json
	*/
    @RequestMapping("sms/send")
	public void smsSend(HttpServletRequest request,HttpServletResponse response) {
	    	String client_id=request.getParameter("client_id");
	    	String access_token=request.getParameter("access_token");
	    	String phone=request.getParameter("phone");
	    	String userName=request.getParameter("userName");
	    	Map<String, Object> map=new HashMap<String,Object>();
	        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,phone,userName))){
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
				UserCache userCache=null;
				Object userCacheInfoObj = redisClient.getObject(RedisConstant.USER_CACHE_INFO+userName);
				//存在缓存信息，用户存在于用户异常表中
				if(userCacheInfoObj!=null ){
					userCache = checkCacheUsername(userName,userCacheService,app.getId());
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
					}
			    }
	    		User user = userService.findByUsername(phone);
	    		if(user == null){
	    			//String desPhone=Help_Encrypt.encrypt(phone);
	    			List<User> userList = userService.findByPhone(phone);
	    			if(userList != null&&userList.size()>0){
	            		user = userList.get(0);
	    			}
	    		}
	    		if(user != null){
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
	    		}else{//不存在
	    			WebUtils.paraMandaChkAndReturn(9, response,"用户不存在");
    				return;
	    		}
			}
	    	if(map.get("status")=="0"){
	    		writeErrorJson(response,map);
	    	}else{
	    		  writeSuccessJson(response,map);
	    	}
	        return;
	    }

	

} 