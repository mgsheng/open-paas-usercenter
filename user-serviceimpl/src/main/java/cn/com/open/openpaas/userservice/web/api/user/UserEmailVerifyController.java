package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.kafka.KafkaProducer;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 用户邮箱验证是否存在接口
 */
@Controller
@RequestMapping("/user/")
public class UserEmailVerifyController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserEmailVerifyController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private UserserviceDev userserviceDev;
	 private UserCacheService userCacheService;
     /**
     * 用户注册接口
     * @return Json
     */
    @RequestMapping("email/verify")
    public void userCenterReg(HttpServletRequest request,HttpServletResponse response,UserCenterRegDto userCenterReg) {
    	long startTime = System.currentTimeMillis();
    	String client_id=userCenterReg.getClient_id();
    	String access_token=userCenterReg.getAccess_token();
    
        String source_id=userCenterReg.getSource_id();
      
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("client_id:"+client_id+"access_token:"+access_token+"source_id:"+source_id);


        if(!paraMandatoryCheck(Arrays.asList(client_id,source_id,access_token))){
            paraMandaChkAndReturn(3, response,"必传参数有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
        if(app==null)
		{
			 app=appService.findIdByClientId(client_id);
			 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
		}
		map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
		if(map.get("status").equals("1")){//client_id和access_token正确
			Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
			if(!hmacSHA1Verification){
				paraMandaChkAndReturn(5, response,"认证失败");
				return;
			}
			AppUser	appUser	=appUserService.findByCidSid(app.getId(), source_id);
			if(appUser!=null){
				User user=userService.findUserById(appUser.userId());
				if(user!=null){
						map.clear();
						map.put("status", "1");
						map.put("email",user.getEmail());
				}else{
					UserCache userCache=userCacheService.findUserById(appUser.userId());
					if(userCache!=null){
						map.clear();
						map.put("status", "1");
						map.put("email",userCache.email());
					}else{
						map.clear();
						map.put("status","0");
						map.put("error_code", "6");//source_id已存在 
						map.put("errMsg", "用户不存在");//单独请求接口时，用户名绑定
					}
				}
			}else{
				map.clear();
				map.put("status","0");
				map.put("error_code", "7");//source_id已存在 
				map.put("errMsg", "用户source_id不存在");//单独请求接口时，用户名绑定
			}
		}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
    	OauthControllerLog.log(startTime,source_id,"",app,map,userserviceDev);
        return;
    }
} 