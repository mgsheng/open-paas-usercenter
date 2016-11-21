package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import cn.com.open.openpaas.userservice.app.kafka.KafkaProducer;
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
 * 用户注册接口
 */
@Controller
@RequestMapping("/user/")
public class UserCenterRegController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterRegController.class);
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
	 private KafkaProducer  kafkaProducer;
     /**
     * 用户注册接口
     * @return Json
     */
    @RequestMapping("userCenterReg")
    public void userCenterReg(HttpServletRequest request,HttpServletResponse response,UserCenterRegDto userCenterReg) {
    	long startTime = System.currentTimeMillis();
    	String client_id=userCenterReg.getClient_id();
    	String access_token=userCenterReg.getAccess_token();
    	String grant_type = userCenterReg.getGrant_type(); 
        String scope = userCenterReg.getScope();  
        String source_id=userCenterReg.getSource_id();
        String username=userCenterReg.getUsername();
        String  isValidate =userCenterReg.getIsValidate();
        String cardNo=userCenterReg.getCard_no();
        if(username==null||username.trim().length()==0)
        {
        	username=null;
        }
        String password=userCenterReg.getPassword();    
        String oldPassword = password;
        log.info("client_id:"+client_id);
        log.info("password:"+password);
        String phone=userCenterReg.getPhone();
        if(phone==null||phone.trim().length()==0)
        {
        	 phone=null;
        }
        String email=userCenterReg.getEmail();
        if(email==null||email.trim().length()==0)
        {
        	 email=null;
        }
        String desPhone="";
    	Map<String, Object> map=new HashMap<String, Object>();
    	log.info("client_id:"+client_id+"access_token:"+access_token+"grant_type:"+grant_type+"scope:"+scope+"password:"+password+"username:"+username+"Password:"+password);
    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));

        if(nullEmptyBlankJudge(username)&&nullEmptyBlankJudge(phone)&&nullEmptyBlankJudge(email)){
            paraMandaChkAndReturn(5, response,"username、phone、email不能都为空");
            return;
        }

        if(!paraMandatoryCheck(Arrays.asList(client_id,grant_type,access_token,scope,source_id,password,username))){
            paraMandaChkAndReturn(6, response,"必传参数有空值");
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
				paraMandaChkAndReturn(9, response,"认证失败");
				return;
			}
			try {
				password=AESUtil.decrypt(password,app.getAppsecret()).trim();
				log.info("解密后 password："+password);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(!nullEmptyBlankJudge(isValidate)&&isValidate.equals("1")){
				if(nullEmptyBlankJudge(password)){
					map = paraMandaChkAndReturnMap(8, response,"密码不能为空");
					OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
					writeErrorJson(response,map);
					return;
				}
		    	if(StringTool.isNumeric(password)){
		    		map = paraMandaChkAndReturnMap(8, response,"密码不能为纯数字");
					OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
					writeErrorJson(response,map);
		        	return;
				}if(judgeInputNotNo(password)==1){
					map = paraMandaChkAndReturnMap(8, response,"密码必须为6-20位字母、数字或英文下划线符号");
					OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
					writeErrorJson(response,map);
		        	return;
				}	
			}else{
				if(judgePwdNo(password)==1){
					map = paraMandaChkAndReturnMap(8, response,"密码必须为6-20位");
					OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
					writeErrorJson(response,map);
		        	return;
				}	
				
			}
		
            User user = new User();
            if(!nullEmptyBlankJudge(username)){
					int returnValue; 
					returnValue=judgeInput(username);
					if(returnValue==1){
						map = paraMandaChkAndReturnMap(7, response,"用户名5~20个字符");
						OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
						writeErrorJson(response,map);
						return;
					}if(returnValue==2){
						map = paraMandaChkAndReturnMap(7, response,"用户名必须为数字、字母、下划线中最少两个组合");
						OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
						writeErrorJson(response,map);
						return;
					}if(returnValue==3){
						map = paraMandaChkAndReturnMap(7, response,"用户名不能为除手机号以外的纯数字");
						OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
						writeErrorJson(response,map);
						return;
					}
					user=userService.findByUsername(username); 
            }
            
	            if(user==null){
	             String  pid="0";
	             Boolean defaultUser = false;
	            if(!nullEmptyBlankJudge(phone)){
//	            	try {
//	        			desPhone=Help_Encrypt.decrypt(phone);
//	        			log.info("解密后 desPhone："+desPhone);
//	        		} catch (Exception e1) {
//	        			  e1.printStackTrace();
//	        			  map = paraMandaChkAndReturnMap(8, response,"手机号加密失败");
//	        	          OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
//	        		}
	            	List<User> userList = userService.findByPhone(phone);
	                if(userList!=null && userList.size()>0){
	                	for(int i=0;i<userList.size();i++){
	                		user=userList.get(i);
	                		AppUser appUser=appUserService.findByCidAUid(app.getId(), user.getId());
	                		if(appUser!=null){
//	                			 String bakDesPhone="";
//		                		   try {
//		                			   bakDesPhone=Help_Encrypt.encrypt(desPhone+"_bak");
//								} catch (Exception e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
			           			userService.updatePhoneById(user.getId(),desPhone+"_bak");
	                		}else{
	                			userService.updateDefaultUserById(user.getId(), true);
                				pid=String.valueOf(user.getId());
                				phone="";
                				defaultUser=true;
	                		}
	                		
	                	}
	                }
	            }
	            if(!nullEmptyBlankJudge(email)){
	            	List<User> userList = userService.findByEmail(email);
	                if(userList!=null && userList.size()>0){
	                	user = userList.get(0);
	                	map = paraMandaChkAndReturnMap(3, response,"邮箱已经绑定");
	    				OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
	    				writeErrorJson(response,map);
	    	        	return;
	
	                }
	            }
					//appUser
				   AppUser appUser;
					if(source_id==null || source_id.length()==0){
						appUser=null;
					}else{
						appUser=appUserService.findByCidSid(app.getId(), source_id);
					}
					if(appUser!=null){
						//删除已插入的user
//						userService.deleteUser(user.id());
						map.clear();
						map.put("status","0");
						map.put("error_code", "4");//source_id已存在 
						map.put("errMsg", "source_id已存在");//source_id已存在 
					}else{
						user=new User(username,password,phone,email,"","","");
						String aesPassword="";
						try {
							aesPassword=AESUtil.encrypt(password, userserviceDev.getAes_userCenter_key());
						} catch (Exception e1) {
							log.info("aes加密出错："+password);
							e1.printStackTrace();
						}
						user.setAesPassword(aesPassword);
						user.setAppId(app.getId());
						//sha1 加密
					/*	PasswordEncoder passwordEncoder = new ShaPasswordEncoder();
						sha1Password=passwordEncoder.encodePassword(password, null);
						
						user.setSha1Password(sha1Password);*/
						user.cardNo(cardNo);
						user.setEmailActivation(User.ACTIVATION_NO);
						user.userState("0");
						user.setPid(pid);
						user.setDefaultUser(defaultUser);
						Boolean f=userService.save(user);
						if(f){
						if(null==source_id||"".equals(source_id.trim())){
							appUser=new AppUser(app.getId(),user.getId(),user.guid());
						}else{
							appUser=new AppUser(app.getId(),user.getId(),source_id);
						}
						f=appUserService.saveAppUser(appUser);
						if(f){
		            		map.put("guid", user.guid());
		            		if(!nullEmptyBlankJudge(username)&&!nullEmptyBlankJudge(password)&&username.equals(password)){
		            		  map.put("msg","用户名和密码一致，建议及时更新密码");	
		            		}
		            		map.put("guid", user.guid());
						}
					}
					//发送支付平台用户信息
					Map<String, Object> sendPaymap=new HashMap<String, Object>();
					sendPaymap.put("userId",user.getId());
					sendPaymap.put("appId",app.getId());
					sendPaymap.put("sourceId",source_id);
					sendPaymap.put("userName", username);
					sendPaymap.put("type", userserviceDev.getUser_type());
					
					sendPaymap.put("messageType", "message_create_payaccount");
					
					String sendMsg=JSONObject.fromObject(sendPaymap).toString();
				//	KafkaProducer kafkaProducer=KafkaProducer.getKafkaProducer();
					kafkaProducer.sendMessage(userserviceDev.getKafka_topic(), sendMsg);
				//	Thread thread = new Thread( new KafkaProducer(sendMsg,userserviceDev));
				//	thread.run();
				}
			}else{
				map.clear();
				map.put("status","0");
				map.put("errMsg","用户名已存在");
				map.put("error_code", "3");//单独请求接口时，用户名已存在
			}
			
		}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
    	OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
        return;
    }
} 