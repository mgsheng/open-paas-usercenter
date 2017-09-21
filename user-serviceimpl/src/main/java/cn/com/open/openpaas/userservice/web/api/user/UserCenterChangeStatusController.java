package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
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
 * 用户账号封停以及启用
 */
@Controller
@RequestMapping("/user/")
public class UserCenterChangeStatusController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterChangeStatusController.class);
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
	/**
	 * 用户账号封停以及启用
	 * @param request
	 * @param response
	 */
    @RequestMapping("change/status")
    public void userCenterRetrievePwd(HttpServletRequest request,HttpServletResponse response) {
    	long startTime = System.currentTimeMillis();
    	String client_id=request.getParameter("client_id");
    	String access_token=request.getParameter("access_token");
        String guid= request.getParameter("guid");
        String status=request.getParameter("status");
        String username="";
        log.info("client_id:"+client_id+"access_token:"+access_token+"guid:"+guid);
    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
    	Map<String, Object> map=new HashMap<String,Object>();
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,guid,status))){
            paraMandaChkAndReturn(4,response,"必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
        if(app==null)
		{
			 app=appService.findIdByClientId(client_id);
			 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
		}
        Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
		if(!hmacSHA1Verification){
			paraMandaChkAndReturn(4, response,"认证失败");
			return;
		}
		map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
		
		if(map.get("status").equals("1")){//client_id,access_token正确
			User  user = userService.findByGuid(guid);
			if(user!=null){
				username=user.getUsername();
				userService.updateUserStatus(guid,Integer.parseInt(status));	
				
			}else{
					UserCache userCache = userCacheService.findByGuid(guid);
					if(userCache!=null)
					{
						userCacheService.updateUserStatus(guid, status);
						username=userCache.username();
					}else{
						map.clear();
						map.put("status", "0");
						map.put("error_code", "5");
						map.put("errMsg", "用户不存在");
					}
			}
		}		
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
    	//OauthControllerLog.log(startTime, username, "", app, map,userserviceDev);
        return;
    }

   
    
}