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
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.Help_Encrypt;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 *  用户信息绑定接口
 */
@Controller
@RequestMapping("/user/")
public class UserCenterBindInfoController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserCenterLoginController.class);
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
	 @Autowired
	 private UserCacheService userCacheService;
    /**
     * 用户信息绑定接口
     * @return Json
     */
    @RequestMapping("bindUserInfo")
    public void bindUserInfo(HttpServletRequest request,HttpServletResponse response,UserCenterRegDto userCenterReg) {
    	long startTime = System.currentTimeMillis();
    	String client_id=userCenterReg.getClient_id();
    	String access_token=userCenterReg.getAccess_token();
    	String grant_type = userCenterReg.getGrant_type();
        String scope = userCenterReg.getScope();  
        String source_id=userCenterReg.getSource_id();
        String guid=userCenterReg.getGuid();
        //user_account
        String phone=userCenterReg.getPhone();
        String email=userCenterReg.getEmail();
        //user_contact
        String card_no=userCenterReg.getCard_no();
        log.info("client_id:"+client_id+"access_token:"+access_token+"grant_type:"+grant_type+"scope:"+scope+"guid:"+guid+"phone:"+phone+"email:"+email);
    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
    	Map<String, Object> map=new HashMap<String, Object>();
        if(!paraMandatoryCheck(Arrays.asList(client_id,grant_type,access_token,scope,source_id,guid))){
            paraMandaChkAndReturn(3, response,"必传参数中有空值");
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
				paraMandaChkAndReturn(6, response,"认证失败");
				return;
			}
            User user = userService.findByGuid(guid);
            UserCache userCache = null;
          	  
            if(null==user){
              userCache = userCacheService.findByGuid(guid);
            }
			if(user==null&&userCache==null){
				map.clear();
				map.put("status","0");
				map.put("error_code", "5");//单独请求接口时，用户名绑定
				map.put("errMsg", "guid不存在");//单独请求接口时，用户名绑定
			}else{
				AppUser	appUser	=appUserService.findByCidSid(app.getId(), source_id);
				if(appUser!=null){
					//删除已插入的user
//					userService.deleteUser(user.id());
					map.clear();
					map.put("status","0");
					map.put("error_code", "4");//source_id已存在 
					map.put("errMsg", "原业务系统用户已经绑定");//单独请求接口时，用户名绑定
				}else{
					String returnGuid="";
					if(user!=null){
						appUser=new AppUser(app.getId(),user.getId(),source_id);
						if(!nullEmptyBlankJudge(card_no)){
							userService.updateUserCardNoById(user.getId(),card_no);
						}
						if(!nullEmptyBlankJudge(phone)){
						 	List<User> userList = userService.findByPhone(phone);
						 	String pid="";
	     	                if(userList!=null && userList.size()>0){
	     	                	for(int i=0;i<userList.size();i++){
	     	                		User userPhone=userList.get(i);
	     	                		boolean isbind=false;
	     	                		List<AppUser> list=appUserService.findByUserId(userPhone.getId());
	     	                		if(list!=null&&list.size()>0){
	     	                			for(int j=0;j<list.size();j++){
	     	                				AppUser appUsernew=appUserService.findByCidAUid(list.get(j).appId(), user.getId());
	     	                				if(appUsernew!=null){
	     	                					isbind=true;
	     	                					break;
	     	                				}
	     	                			}
	     	                		}
	     	                		if(isbind){
	     	                			//更新原有手机号为phone+"_bak"
	     			           			userService.updatePhoneById(userPhone.getId(),phone+"_bak");
	     			           			//更新查询出的手机号为输入的手机号
	     			           		    userService.updatePhoneById(user.getId(),phone);
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
	     	                	userService.updatePhoneById(user.getId(),phone);
	     	                }
	     	            }
						if(!nullEmptyBlankJudge(email)){

						 	List<User> userList = userService.findByEmail(email);
						 	String pid="";
	     	                if(userList!=null && userList.size()>0){
	     	                	for(int i=0;i<userList.size();i++){
	     	                		User userEmail=userList.get(i);
	     	                		
	     	                		boolean isbind=false;
	     	                		List<AppUser> list=appUserService.findByUserId(userEmail.getId());
	     	                		if(list!=null&&list.size()>0){
	     	                			for(int j=0;j<list.size();j++){
	     	                				AppUser appUsernew=appUserService.findByCidAUid(list.get(j).appId(), user.getId());
	     	                				if(appUsernew!=null){
	     	                					isbind=true;
	     	                					break;
	     	                				}
	     	                			}
	     	                		}
	     	                		
	     	                		if(isbind){
	     	                			//更新原有手机号为phone+"_bak"
	     			           			userService.updateEmailById(userEmail.getId(),email+"_bak");
	     			           			//更新查询出的手机号为输入的手机号
	     			           		    userService.updateEmailById(user.getId(),email);
	     	                		}else if(user.getId()!=userEmail.getId()){
	     	                			userService.updateDefaultUserById(userEmail.getId(), true);
	                    				pid=String.valueOf(userEmail.getId());
	                    				user.setPid(pid);
	                    				user.setEmail("");
	                    				user.setDefaultUser(true);
	                    				userService.updateUser(user);
	     	                		}
	     	                	}
	     	                }else{
	     	                	userService.updateEmailById(user.getId(), email);
	     	                }
						}
						returnGuid=user.guid();
					}else if(userCache!=null){
						appUser=new AppUser(app.getId(),userCache.id(),source_id);
						if(!nullEmptyBlankJudge(card_no)){
							userCacheService.updateUserCardNoById(userCache.id(),card_no);
						}
						if(!nullEmptyBlankJudge(phone)){
						 	List<UserCache> userList = userCacheService.findByPhone(phone);
						 	String pid="";
	     	                if(userList!=null && userList.size()>0){
	     	                	for(int i=0;i<userList.size();i++){
	     	                		UserCache userPhone=userList.get(i);
	     	                		
	     	                		boolean isbind=false;
	     	                		List<AppUser> list=appUserService.findByUserId(userPhone.id());
	     	                		if(list!=null&&list.size()>0){
	     	                			for(int j=0;j<list.size();j++){
	     	                				AppUser appUsernew=appUserService.findByCidAUid(list.get(j).appId(), userCache.id());
	     	                				if(appUsernew!=null){
	     	                					isbind=true;
	     	                					break;
	     	                				}
	     	                			}
	     	                		}
	     	                		if(isbind){
	     	                			//更新原有手机号为phone+"_bak"
	     	                			userCacheService.updatePhoneById(userPhone.id(),phone+"_bak");
	     			           			//更新查询出的手机号为输入的手机号
	     	                			userCacheService.updatePhoneById(userCache.id(),phone);
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
	     	                	userCacheService.updatePhoneById(userCache.id(),phone);
	     	                }
	     	            }
						if(!nullEmptyBlankJudge(email)){

						 	List<UserCache> userList = userCacheService.findByEmail(email);
						 	String pid="";
	     	                if(userList!=null && userList.size()>0){
	     	                	for(int i=0;i<userList.size();i++){
	     	                		UserCache userEmail=userList.get(i);
	     	                		boolean isbind=false;
	     	                		List<AppUser> list=appUserService.findByUserId(userEmail.id());
	     	                		if(list!=null&&list.size()>0){
	     	                			for(int j=0;j<list.size();j++){
	     	                				AppUser appUsernew=appUserService.findByCidAUid(list.get(j).appId(), userCache.id());
	     	                				if(appUsernew!=null){
	     	                					isbind=true;
	     	                					break;
	     	                				}
	     	                			}
	     	                		}
	     	                		if(isbind){
	     	                			//更新原有手机号为phone+"_bak"
	     	                			userCacheService.updateEmailById(userEmail.id(),email+"_bak");
	     			           			//更新查询出的手机号为输入的手机号
	     	                			userCacheService.updateEmailById(userCache.id(),email);
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
	     	                	userCacheService.updateEmailById(userCache.id(),email);
	     	                }
						}
						returnGuid=userCache.guid();
					}
					Boolean f=appUserService.saveAppUser(appUser);
					if(f){
						map.put("guid", returnGuid);
					}
				}
			}
		}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
    	//OauthControllerLog.log(startTime, guid, source_id, app, map,userserviceDev);
        return;
    }
}