package cn.com.open.openpaas.userservice.web.api.user;


import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.Help_Encrypt;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

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
	        	   if(methord!=null&&methord.equals("registerUser"))
	        	   {
	        		   flag=   registerUser(app,userCenterReg);
	        	   }
                   else if(methord!=null&&methord.equals("sysUserInfo")){
                	   flag=  sysUserInfo(app,userCenterReg);
	        	   }
                   else if(methord!=null&&methord.equals("bindUserInfo")){
                	   flag=  bindUserInfo(app,userCenterReg);
	        	   }
	        	 /*  
	                    AppUser appUser = appUserService.findByCidSid(app.getId(), userCenterReg.getSource_id());
	                    if(null!=appUser){
	                         user 
	                            User user = userService.findUserById(appUser.userId());
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
	                                userService.updateUser(user);
	                            }else{
	                                user=new User(userCenterReg.getUsername(),userCenterReg.getPassword(),userCenterReg.getPhone(),
	                                        userCenterReg.getEmail(),userCenterReg.getNickname(),
	                                        userCenterReg.getReal_name(),userCenterReg.getHead_picture());
	                                user.setEmailActivation(User.ACTIVATION_YES);
	                                user.userState("1");
	                                userService.save(user);
	                        }
	                    }*/
	                    
	                    /*    else {
	                        User user = null;
	                        if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
	                      	  user = userService.findByGuid(userCenterReg.getGuid());
	                       }
	                        if((null==user)&&!nullEmptyBlankJudge(userCenterReg.getPhone())){
	                        	
	                        	List<User> userList = userService.findByPhone(userCenterReg.getPhone());
	                        	if(userList!=null && userList.size()>0){
	                        		user = userList.get(0);
	                        	}
	                        }
	                        if((null==user)&&!nullEmptyBlankJudge(userCenterReg.getEmail())){
	                        	List<User> userList = userService.findByEmail(userCenterReg.getEmail());
	                            if(userList!=null && userList.size()>0){
	                        		user = userList.get(0);
	                        	}
	                        }
	            			if(user==null){
	            				 user=new User(userCenterReg.getUsername(),userCenterReg.getPassword(),userCenterReg.getPhone(),userCenterReg.getEmail(),"","","");
								String aesPassword="";
								try {
									aesPassword=AESUtil.encrypt(userCenterReg.getPassword(), userserviceDev.getAes_userCenter_key());
								} catch (Exception e1) {
									log.info("aes加密出错："+userCenterReg.getPassword());
									e1.printStackTrace();
								}
								user.setAesPassword(aesPassword);
								user.setAppId(app.getId());
								if(!nullEmptyBlankJudge(userCenterReg.getId())){
								  user.setId(Integer.parseInt(userCenterReg.getId()));	
								}if(!nullEmptyBlankJudge(userCenterReg.getGuid())){
								  user.guid(userCenterReg.getGuid());	
								}
								//sha1 加密
								PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
								sha1Password=passwordEncoder.encodePassword(password, null);
								
								user.setSha1Password(sha1Password);
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
								f=appUserService.saveAppUser(appUser);
								if(f){
				            		map.put("guid", user.guid());
				            		if(!nullEmptyBlankJudge(userCenterReg.getUsername())&&!nullEmptyBlankJudge(userCenterReg.getPassword())&&userCenterReg.getUsername().equals(userCenterReg.getPassword())){
				            		  map.put("msg","用户名和密码一致，建议及时更新密码");	
				            		}
				            		map.put("guid", user.guid());
								}
							 }
	            			}else{
		            				if(null==userCenterReg.getSource_id()||"".equals(userCenterReg.getSource_id().trim())){
										appUser=new AppUser(app.getId(),user.getId(),user.guid());
									}else{
										appUser=new AppUser(app.getId(),user.getId(),userCenterReg.getSource_id());
									}
		            				if(!nullEmptyBlankJudge(userCenterReg.getAppUid())){
										appUser.appUid(Integer.parseInt(userCenterReg.getAppUid()));
									}
	            					Boolean f=appUserService.saveAppUser(appUser);
	            					if(f){
	            						if(!nullEmptyBlankJudge(userCenterReg.getCard_no())){
	            							userService.updateUserCardNoById(user.getId(),userCenterReg.getCard_no());
	            						}
	            						if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
	            							userService.updatePhoneById(user.getId(), userCenterReg.getPhone());
	            						}
	            	            		map.put("guid", user.guid());
	            					}
	            				}
	            		}*/
	                }
	            if(!flag){
	                writeErrorJson(response,map);
	                log.info("[error]~~~~~~~~~~~~~~methord:"+userCenterReg.getMethordName()+",userId:"+userCenterReg.getId()+",appId:"+app.getId());
	            }else{
	                writeSuccessJson(response,map);
	            }
	        }
		     OauthControllerLog.log(startTime,username,userCenterReg.getMethordName(),app,map,userserviceDev);
	        return;
	    }

	public  boolean registerUser( App app,UserCenterRegDto userCenterReg){
		boolean flag=false;
		User user=new User(userCenterReg.getUsername(),userCenterReg.getPassword(),userCenterReg.getPhone(),userCenterReg.getEmail(),"","","");
		AppUser appUser=null;
			String aesPassword="";
			try {
				aesPassword=AESUtil.encrypt(userCenterReg.getPassword(), userserviceDev.getAes_userCenter_key());
			} catch (Exception e1) {
				log.info("aes加密出错："+userCenterReg.getPassword());
				e1.printStackTrace();
			}
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
	                	   log.info("~~~~~~~~~~~~~~sysUserInfo:not found user,userId:"+userCenterReg.getId()+",appId:"+app.getId());
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
           }
           return flag;
    
}
} 