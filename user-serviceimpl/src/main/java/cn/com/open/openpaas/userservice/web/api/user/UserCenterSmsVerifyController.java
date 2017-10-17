package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserActivatedService;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 用户短信码验证接口
 */
@Controller
@RequestMapping("/user/")
public class UserCenterSmsVerifyController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterSmsVerifyController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private UserActivatedService userActivatedService;
	 @Autowired
	 private UserserviceDev userserviceDev;
	 @Autowired
	 private UserCacheService userCacheService;
  /**
    * 用户短信码验证接口
	* @return Json
	*/
    @RequestMapping("sms/verify")
	public void smsVerify(HttpServletRequest request,HttpServletResponse response) {
	    	String client_id=request.getParameter("client_id");
	    	String access_token=request.getParameter("access_token");
	    	String phone=request.getParameter("phone");
	    	String code=request.getParameter("code");
	    	String type=request.getParameter("type");
	    	if(nullEmptyBlankJudge(type)){
	    		type="1";
	    	}
	    	Map<String, Object> map=new HashMap<String,Object>();
	        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,phone,code,type))){
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
				WebUtils.paraMandaChkAndReturn(4, response,"认证失败");
				return;
			}
	        map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
			if(map.get("status").equals("1")){
		        	//激活有效数据
		        	int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
		        	try {
		    			UserActivated userActivated = new UserActivated();
		    	    	userActivated.setCode(code);
		    	    	userActivated.setPhone(phone);
		    	    	List<UserActivated> userActivatedList = userActivatedService.findByUserActivated(userActivated);
		    	    	if(userActivatedList != null && userActivatedList.size()>0){
		    	    		userActivated=userActivatedList.get(0);
		    				if(null != userActivated){
		    					if(DateTools.timeDiffCurr(userActivated.getCreateTime()) > (validTime*60*1000)){
		    						map.clear();
		    						map.put("status", "0");
		    						map.put("error_code", "5");
		    						map.put("errMsg", "验证码超时");
		    						
		    					}else{
		    						if(type.equals("1")||type.equals("3")){
		    							//找回密码
		    							User user = userService.findUserById(userActivated.getUserId());
			    						if(null != user && user.getPhone().equals(userActivated.getPhone())){
			    							map.clear();
			    							map.put("status", "1");
				    						map.put("error_code", "");
				    						map.put("errMsg", "");
				    						map.put("guid", user.guid());
			    						}else{
			    						UserCache userCache=userCacheService.findUserById(userActivated.getUserId());
			    						if(userCache!=null&&userCache.phone().equals(userActivated.getPhone())){
			    							map.clear();
			    							map.put("status", "1");
				    						map.put("error_code", "");
				    						map.put("errMsg", "");
				    						map.put("guid", userCache.guid());
			    						}else{
			    							map.clear();
			    							map.put("status", "0");
				    						map.put("error_code", "6");
				    						map.put("errMsg", "手机号不对应");	
			    						}
			    					   }	
		    						}else{
		    							map.clear();
		    							map.put("status", "1");
			    						map.put("error_code", "");
			    						map.put("errMsg", "");
			    						map.put("guid", "");
		    						}
		    						
		    					}
		    				}else{
		    					map.clear();
	    						map.put("status", "0");
	    						map.put("error_code", "6");
	    						map.put("errMsg", "验证码错误");
		    				}
		    	    	}else{
		    	    		map.clear();
    						map.put("status", "0");
    						map.put("error_code", "6");
    						map.put("errMsg", "验证码错误");
		    	    	}
		    		} catch (Exception e) {
		    			map.clear();
						map.put("status", "0");
						map.put("error_code", "7");
						map.put("errMsg", "系统异常");
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