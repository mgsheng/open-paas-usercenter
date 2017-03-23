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
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 用户guid查询
 */
@Controller
@RequestMapping("/user/")
public class UserCenterGuidController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterGuidController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private UserCacheService userCacheService;
  
	 /**
	     * 用户账号验证接口
	     * @return Json
	     */
	    @RequestMapping("userGuidInfo")
	    public void userGuidInfo(HttpServletRequest request,HttpServletResponse response) {
	    	String client_id=request.getParameter("client_id");
	    	String access_token=request.getParameter("access_token");
	    	String account=request.getParameter("account");
	    	String accountType=request.getParameter("accountType");
	    	Map<String, Object> map=new HashMap<String,Object>();

	        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,account))){
	            paraMandaChkAndReturn(4, response,"参数传递不全");
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
	       UserCache userCache=null;
			if(map.get("status").equals("1")){//client_id,access_token正确
				User user=null;
		    	if(account!=null&&!"".equals(account)){
		    		user=checkUsername(account,accountType,userService);
					if(user.getAppId()!=app.getId()){
						userCache= checkCacheUsername(account,userCacheService,app.getId());
				    }
		    	}else{
		    		map.clear();
		    		map.put("status", "0");
		    		map.put("error_code", "4");//参数不全
		    		map.put("errMsg", "参数传递不全");//参数不全
		    	}
		    	//判断account是否存在(对应判断属性：username,phone,email)
			    //	checkUsername(account);
		    	if(user!=null &&user.getAppId()==app.getId()){
		            map.clear();
		            map.put("guid", user.guid()==null?"":user.guid());
		    		map.put("status", "1");
		    	}else if(userCache!=null){
		            map.clear();
		            map.put("guid", userCache.guid()==null?"":userCache.guid());
		    		map.put("status", "1");
		    	}else{
		    		map.clear();
		    		map.put("status", "0");
		    		map.put("error_code", "3");//用户已存在
		    		map.put("errMsg", "account不存在");//参数不全
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