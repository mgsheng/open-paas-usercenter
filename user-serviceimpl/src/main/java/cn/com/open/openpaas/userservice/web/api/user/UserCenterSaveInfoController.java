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
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.CommonConstant;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;

/**
 * 用户 信息保存接口
 */
@Controller
@RequestMapping("/user/")
public class UserCenterSaveInfoController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterSaveInfoController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserserviceDev userserviceDev;
	 @Autowired
	 private UserCacheService userCacheService;
  
	 @RequestMapping("saveUserInfo")
	    public void userSynUserInfo(HttpServletRequest request,HttpServletResponse response,UserCenterRegDto userCenterReg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
	        
		 String username="";
         long startTime = System.currentTimeMillis();
         App app=null;
         Map<String, Object> map=new HashMap<String, Object>();
		 if(null!=userCenterReg){
			   username=userCenterReg.getUsername();
	            if(!paraMandatoryCheck(Arrays.asList(
	                   userCenterReg.getClient_id(),userCenterReg.getSource_id()))){
	                paraMandaChkAndReturn(3, response,"必传参数中有空值");
	                return;
	            }
	           // String desphone="";
	            log.info("client_id："+userCenterReg.getClient_id());
	           
	             app = (App) redisClient.getObject(RedisConstant.APP_INFO+userCenterReg.getClient_id());
		        if(app==null)
				{
					 app=appService.findIdByClientId(userCenterReg.getClient_id());
					 redisClient.setObject(RedisConstant.APP_INFO+userCenterReg.getClient_id(), app);
				}
		       /* if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
		        	  desphone=Help_Encrypt.encrypt(userCenterReg.getPhone());	
		        }*/
		        boolean flag=false;
	           if(null!=app){
	        	   
	        	   String methord=userCenterReg.getMethordName();
	        	   if(methord!=null&&methord.equals(CommonConstant.METHORD_REGISTER_USER))
	        	   {
	        		   flag=   registerUser(app,userCenterReg);
	        	   }
                   else if(methord!=null&&methord.equals(CommonConstant.METHORD_SYS_USER_INFO)){
                	   flag=  sysUserInfo(app,userCenterReg);
	        	   }
                   else if(methord!=null&&methord.equals(CommonConstant.METHORD_BIND_USER_INFO)){
                	   flag=  bindUserInfo(app,userCenterReg);
	        	   }
	                }
	            if(!flag){
	                writeErrorJson(response,map);
	                log.info("[error]~~~~~~~~~~~~~~methord:"+userCenterReg.getMethordName()+",userId:"+userCenterReg.getId()+",appId:"+app.getId());
	            }else{
	                writeSuccessJson(response,map);
	            }
	        }
		    // OauthControllerLog.log(startTime,username,userCenterReg.getMethordName(),app,map,userserviceDev);
	        return;
	    }

	public  boolean registerUser( App app,UserCenterRegDto userCenterReg){
		boolean flag=false;
		 User user=userService.findByUsername(userCenterReg.getUsername());
		  String aesPassword="";
			try {
				aesPassword=AESUtil.encrypt(userCenterReg.getPassword(), userserviceDev.getAes_userCenter_key());
			} catch (Exception e1) {
				log.info("aes加密出错："+userCenterReg.getPassword());
				e1.printStackTrace();
			}
		 if(user==null){
			 user=new User(userCenterReg.getUsername(),userCenterReg.getPassword(),userCenterReg.getPhone(),userCenterReg.getEmail(),"","","");
				AppUser appUser=null;
					user.setAesPassword(aesPassword);
					user.setAppId(app.getId());
					if(!nullEmptyBlankJudge(userCenterReg.getId())){
					  user.setId(Integer.parseInt(userCenterReg.getId()));	
					}if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
					  user.guid(userCenterReg.getGuid());	
					}
					user.cardNo(userCenterReg.getCard_no());
					user.setEmailActivation(User.ACTIVATION_NO);
					user.userState("0");
					Boolean f=userService.save(user);
					if(f){
					if(null==userCenterReg.getSource_id()||"".equals(userCenterReg.getSource_id().trim())){
						appUser=new AppUser(app.getId(),user.getId(),user.guid());
					}else{
						appUser=new AppUser(app.getId(),user.getId(),userCenterReg.getSource_id());
					}
					if(!nullEmptyBlankJudge(userCenterReg.getAppUid())){
						appUser.appUid(Integer.parseInt(userCenterReg.getAppUid()));
					}
					flag =appUserService.saveAppUser(appUser);
				 } 
		  }else{
			  UserCache userCache=new UserCache(userCenterReg.getUsername(),userCenterReg.getPassword(),userCenterReg.getPhone(),userCenterReg.getEmail(),"","","");
			  userCache.setAesPassword(aesPassword);
			  userCache.appId(app.getId());
				if(!nullEmptyBlankJudge(userCenterReg.getId())){
					userCache.id(Integer.parseInt(userCenterReg.getId()));	
				}if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
					userCache.guid(userCenterReg.getGuid());	
				}
				userCache.cardNo(userCenterReg.getCard_no());
				userCache.emailActivation(User.ACTIVATION_NO);
				userCache.userState("0");
				userCache.process(0);
				Boolean f=userCacheService.save(userCache);
				if(f){
					AppUser appUser=null;
					if(null==userCenterReg.getSource_id()||"".equals(userCenterReg.getSource_id().trim())){
						appUser=new AppUser(app.getId(),userCache.id(),userCache.guid());
					}else{
						appUser=new AppUser(app.getId(),userCache.id(),userCenterReg.getSource_id());
					}
					if(!nullEmptyBlankJudge(userCenterReg.getAppUid())){
						appUser.appUid(Integer.parseInt(userCenterReg.getAppUid()));
					}
					appUser.isCache(1);
					flag =appUserService.saveAppUser(appUser);
		      }
		  }
			return flag;
	}
	public  boolean sysUserInfo( App app,UserCenterRegDto userCenterReg){
	        	boolean flag=false;
		           if(!nullEmptyBlankJudge(userCenterReg.getId()))
		           {
		               User user = userService.findUserById(Integer.parseInt(userCenterReg.getId()));
	                   if (null != user) {
	                       if (!nullEmptyBlankJudge(userCenterReg.getUsername())) {
	                       user.setUsername(userCenterReg.getUsername());
	                       }
	                       if (!nullEmptyBlankJudge(userCenterReg.getPassword())) {
	                       	String aesPassword="";
	   						try {
	   							aesPassword=AESUtil.encrypt(userCenterReg.getPassword(), userserviceDev.getAes_userCenter_key());
	   						} catch (Exception e1) {
	   							log.info("aes加密出错："+userCenterReg.getPassword());
	   							e1.printStackTrace();
	   						}
	   						user.setAesPassword(aesPassword);
	                       }
	                       if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
	                       user.setPhone(userCenterReg.getPhone());
	                       }if (!nullEmptyBlankJudge(userCenterReg.getEmail())) {
	                         user.setEmail(userCenterReg.getEmail());
	                       }if (!nullEmptyBlankJudge(userCenterReg.getCard_no())) {
	                         user.setCardNo(userCenterReg.getCard_no());
	                       }  if(nullEmptyBlankJudge(userCenterReg.getPassword())|| ("null").equals(userCenterReg.getPassword())){
	                           user.userType(1);
	                       } user.userState("1");
	                       if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
	                          user.guid(userCenterReg.getGuid());
	                        }
	                       flag=userService.updateUser(user);
	                   }
	                   else{
	                	   UserCache userCache=userCacheService.findUserById(Integer.parseInt(userCenterReg.getId()));
	                	   if(userCache!=null){
	                		   if (!nullEmptyBlankJudge(userCenterReg.getUsername())) {
		                		   userCache.username(userCenterReg.getUsername());
			                       }
			                       if (!nullEmptyBlankJudge(userCenterReg.getPassword())) {
			                       	String aesPassword="";
			   						try {
			   							aesPassword=AESUtil.encrypt(userCenterReg.getPassword(), userserviceDev.getAes_userCenter_key());
			   						} catch (Exception e1) {
			   							log.info("aes加密出错："+userCenterReg.getPassword());
			   							e1.printStackTrace();
			   						}
			   						userCache.setAesPassword(aesPassword);
			                       }
			                       if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
			                    	   userCache.phone(userCenterReg.getPhone());
			                       }if (!nullEmptyBlankJudge(userCenterReg.getEmail())) {
			                    	   userCache.email(userCenterReg.getEmail());
			                       }if (!nullEmptyBlankJudge(userCenterReg.getCard_no())) {
			                    	   userCache.cardNo(userCenterReg.getCard_no());
			                       }  if(nullEmptyBlankJudge(userCenterReg.getPassword())|| ("null").equals(userCenterReg.getPassword())){
			                    	   userCache.userType(1);
			                       } userCache.userState("1");
			                       if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
			                    	   userCache.guid(userCenterReg.getGuid());
			                        }
			                      userCacheService.updateUserCache(userCache);  
			                      flag=true;
	                	   }else{
	                		   log.info("~~~~~~~~~~~~~~sysUserInfo:not found user,userId:"+userCenterReg.getId()+",appId:"+app.getId()); 
	                	   }
	                   }  
		           }
		           return flag;
            
	}
	public  boolean bindUserInfo( App app,UserCenterRegDto userCenterReg){
    	boolean flag=false;
    	AppUser appUser=null;
           if(!nullEmptyBlankJudge(userCenterReg.getId()))
           {
        	   appUser=new AppUser(app.getId(),Integer.parseInt(userCenterReg.getId()),userCenterReg.getSource_id());
			if(!nullEmptyBlankJudge(userCenterReg.getAppUid())){
				appUser.appUid(Integer.parseInt(userCenterReg.getAppUid()));
			}
			flag=appUserService.saveAppUser(appUser);
			if(flag){
				if(!nullEmptyBlankJudge(userCenterReg.getCard_no())){
					userService.updateUserCardNoById(Integer.parseInt(userCenterReg.getId()),userCenterReg.getCard_no());
				}
				if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
					userService.updatePhoneById(Integer.parseInt(userCenterReg.getId()), userCenterReg.getPhone());
				}
			}
           }
           return flag;
    
}
} 