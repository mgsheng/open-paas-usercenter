package cn.com.open.openpaas.userservice.web.api.user;

import java.lang.reflect.InvocationTargetException;
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
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
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
	 private UserCacheService userCacheService;
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
                    userCenterReg.getAccess_token(),userCenterReg.getScope(),userCenterReg.getSource_id(),userCenterReg.getWhetherBind()))){
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
        				WebUtils.paraMandaChkAndReturn(6, response,"认证失败");
        				return;
        			}
                	AppUser appUser = appUserService.findByCidSid(app.getId(), userCenterReg.getSource_id());
                    if(null!=appUser){
                        try {
                        /* user */
                            User user = userService.findUserById(appUser.userId());
                           
                            if (null != user) {
                            	     username=user.getUsername();
                            		//绑定
                            		if("0".equals(userCenterReg.getWhetherBind())){

            						 	List<User> userList = userService.findByPhone(userCenterReg.getPhone());
            						 	String pid="";
            	     	                if(userList!=null && userList.size()>0){
            	     	                	for(int i=0;i<userList.size();i++){
            	     	                		User userPhone=userList.get(i);
            	     	                		AppUser appUsernew=appUserService.findByCidAUid(app.getId(), userPhone.getId());
            	     	                		if(appUsernew!=null){
            	     	                			//更新原有手机号为phone+"_bak"
            	     			           			userService.updatePhoneById(userPhone.getId(),userCenterReg.getPhone()+"_bak");
            	     			           			//更新查询出的手机号为输入的手机号
            	     			           		    userService.updatePhoneById(user.getId(),userCenterReg.getPhone());
            	     	                		}else if(user.getId()!=userPhone.getId()){
            	     	                			userService.updateDefaultUserById(userPhone.getId(), true);
            	                    				pid=String.valueOf(userPhone.getId());
            	                    				user.setPid(pid);
            	                    				user.setPhone("");
            	                    				user.setDefaultUser(true);
            	                    				userService.updateUser(user);
            	     	                		}
            	     	                	}
            	     	                }else{
            	     	                	userService.updatePhoneById(user.getId(),userCenterReg.getPhone());
            	     	                }
                            		}if("1".equals(userCenterReg.getWhetherBind())){


            						 	List<User> userList = userService.findByEmail(userCenterReg.getEmail());
            						 	String pid="";
            	     	                if(userList!=null && userList.size()>0){
            	     	                	for(int i=0;i<userList.size();i++){
            	     	                		User userEmail=userList.get(i);
            	     	                		AppUser appUsernew=appUserService.findByCidAUid(app.getId(), userEmail.getId());
            	     	                		if(appUsernew!=null){
            	     	                			//更新原有手机号为phone+"_bak"
            	     			           			userService.updateEmailById(userEmail.getId(),userCenterReg.getEmail()+"_bak");
            	     			           			//更新查询出的手机号为输入的手机号
            	     			           		    userService.updateEmailById(user.getId(),userCenterReg.getEmail());
            	     	                		}else if (user.getId()!=userEmail.getId()){
            	     	                			userService.updateDefaultUserById(userEmail.getId(), true);
            	                    				pid=String.valueOf(userEmail.getId());
            	                    				user.setPid(pid);
            	                    				user.setEmail("");
            	                    				user.setDefaultUser(true);
            	                    				userService.updateUser(user);
            	     	                		}
            	     	                	}
            	     	                }else{
            	     	                	userService.updateEmailById(user.getId(),userCenterReg.getEmail());
            	     	                }
                            			//user.email(userCenterReg.getEmail());	
                           		     }
                            	 
                                //userService.updateUser(user);
                                map.put("guid", user.guid());
                            }else{
                            	
                            	UserCache userCache=userCacheService.findUserById(appUser.userId());
                            	if(userCache!=null){
                            		//绑定
                            		if("0".equals(userCenterReg.getWhetherBind())){

            						 	List<UserCache> userList = userCacheService.findByPhone(userCenterReg.getPhone());
            						 	String pid="";
            	     	                if(userList!=null && userList.size()>0){
            	     	                	for(int i=0;i<userList.size();i++){
            	     	                		UserCache userPhone=userList.get(i);
            	     	                		AppUser appUsernew=appUserService.findByCidAUid(app.getId(), userPhone.id());
            	     	                		if(appUsernew!=null){
            	     	                			//更新原有手机号为phone+"_bak"
            	     	                			userCacheService.updatePhoneById(userPhone.id(),userCenterReg.getPhone()+"_bak");
            	     			           			//更新查询出的手机号为输入的手机号
            	     	                			userCacheService.updatePhoneById(userCache.id(),userCenterReg.getPhone());
            	     	                		}else if(userPhone.id()!=userCache.id()){
            	     	                			userCacheService.updateDefaultUserById(userPhone.id(), true);
            	                    				pid=String.valueOf(userPhone.id());
            	                    				userCache.pid(pid);
            	                    				userCache.phone("");
            	                    				userCache.setDefaultUser(true);
            	                    				userCacheService.updateUserCache(userCache);
            	     	                		}
            	     	                	}
            	     	                }else{
            	     	                	userCacheService.updatePhoneById(userCache.id(),userCenterReg.getPhone());
            	     	                }
            	     	            
                            			//userCache.phone(userCenterReg.getPhone());	
                            		}if("1".equals(userCenterReg.getWhetherBind())){


            						 	List<UserCache> userList = userCacheService.findByEmail(userCenterReg.getEmail());
            						 	String pid="";
            	     	                if(userList!=null && userList.size()>0){
            	     	                	for(int i=0;i<userList.size();i++){
            	     	                		UserCache userEmail=userList.get(i);
            	     	                		AppUser appUsernew=appUserService.findByCidAUid(app.getId(), userEmail.id());
            	     	                		if(appUsernew!=null){
            	     	                			//更新原有手机号为phone+"_bak"
            	     	                			userCacheService.updateEmailById(userEmail.id(),userCenterReg.getEmail()+"_bak");
            	     			           			//更新查询出的手机号为输入的手机号
            	     	                			userCacheService.updateEmailById(userCache.id(),userCenterReg.getEmail());
            	     	                		}else if(userEmail.id()!=userCache.id()){
            	     	                			userCacheService.updateDefaultUserById(userEmail.id(), true);
            	                    				pid=String.valueOf(userEmail.id());
            	                    				userCache.pid(pid);
            	                    				userCache.email("");
            	                    				userCache.setDefaultUser(true);
            	                    				userCacheService.updateUserCache(userCache);
            	     	                		}
            	     	                	}
            	     	                }else{
            	     	                	userCacheService.updateEmailById(userCache.id(),userCenterReg.getEmail());
            	     	                }
                            			//userCache.email(userCenterReg.getEmail());	
                           		     }
                            		//userCacheService.updateUserCache(userCache);
                                     map.put("guid", userCache.guid());
                            	}else{
                            		  map.put("status", "0");
                                      map.put("errMsg","source_id不存在");
                                      map.put("error_code", "4");//source_id不存在
                            	}
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