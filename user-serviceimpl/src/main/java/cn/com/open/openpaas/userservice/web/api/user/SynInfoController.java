package cn.com.open.openpaas.userservice.web.api.user;

import java.lang.reflect.InvocationTargetException;
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
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.Help_Encrypt;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 手机号绑定解绑
 */
@Controller
@RequestMapping("/user/")
public class SynInfoController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(SynInfoController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserserviceDev userserviceDev;
    @RequestMapping("synUserInfo")
    public void userSynUserInfo(HttpServletRequest request,HttpServletResponse response,UserCenterRegDto userCenterReg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        
    	 String username="";
         long startTime = System.currentTimeMillis();
         App app=null;
         Map<String, Object> map=new HashMap<String, Object>();
    	if(null!=userCenterReg){
            if(!WebUtils.paraMandatoryCheck(Arrays.asList(userCenterReg.getGrant_type(),userCenterReg.getClient_id(),
                    userCenterReg.getAccess_token(),userCenterReg.getScope(),userCenterReg.getSource_id()))){
            	WebUtils.paraMandaChkAndReturn(3, response,"必传参数中有空值");
                return;
            }
            log.info("client_id："+userCenterReg.getClient_id());
            
            app = (App) redisClient.getObject(RedisConstant.APP_INFO+userCenterReg.getClient_id());
           
	        if(app==null)
			{
				 app=appService.findIdByClientId(userCenterReg.getClient_id());
				 redisClient.setObject(RedisConstant.APP_INFO+userCenterReg.getClient_id(), app);
			}
	        //String desPhone=Help_Encrypt.encrypt(userCenterReg.getPhone());
			map=checkClientIdOrToken(userCenterReg.getClient_id(),userCenterReg.getAccess_token(),app,tokenServices);
            if(map.get("status").equals("1")) {//client_id和access_token正确
                if(null!=app){
                	Boolean f1=OauthSignatureValidateHandler.validateSignature(request,app);
                	if(!f1){
        				WebUtils.paraMandaChkAndReturn(9, response,"认证失败");
        				return;
        			}
                	AppUser appUser = appUserService.findByCidSid(app.getId(), userCenterReg.getSource_id());
                    if(null!=appUser){
                        try {
                        /* user */
                            User user = userService.findUserById(appUser.userId());
                           
                            if (null != user) {
                            	 username=user.getUsername();
                                if (!WebUtils.nullEmptyBlankJudge(userCenterReg.getPhone())) {
                                	
                                    user.phone(userCenterReg.getPhone());
                                }
                                if(!WebUtils.nullEmptyBlankJudge(userCenterReg.getEmail())){
                                	user.email(userCenterReg.getEmail());
                                }
                                userService.updateUser(user);
                                map.put("guid", user.guid());
                            }else{
                            	 map.clear();
                                 map.put("status", "0");
                                 map.put("errMsg","source_id不存在");
                                 map.put("error_code", "4");//source_id不存在	
                            }
                           
                        }catch(Exception e){
                            map.clear();
                            map.put("status", "0");
                            map.put("error_code", "5");
                            map.put("errMsg",e.getMessage());
                        }
                    }else {
                        map.clear();
                        map.put("status", "0");
                        map.put("errMsg","source_id不存在");
                        map.put("error_code", "4");//source_id不存在
                    }
                }
            }
            if(map.get("status")=="0"){
            	WebUtils.writeErrorJson(response,map);
            }else{
            	WebUtils.writeSuccessJson(response,map);
            }
        }
        OauthControllerLog.log(startTime,username,"",app,map,userserviceDev);
        return;
    }


}