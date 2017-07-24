package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.web.WebUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.thread.SendOesThread;
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
 * 修改账号密码接口
 */
@Controller
@RequestMapping("/user/")
public class ModiPwdController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(ModiPwdController.class);
	 @Autowired
	 private UserService userService;
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
	 @Autowired
	 private AppUserService appUserService;
	/**
     * 修改账号密码接口
     * @return Json
     */
    @RequestMapping("userCenterModiPwd")
    public void userCenterModiPwd(HttpServletRequest request,HttpServletResponse response) {
    	long startTime = System.currentTimeMillis();
    	String client_id=request.getParameter("client_id");
    	String access_token=request.getParameter("access_token");
    	String account=request.getParameter("account");
    	String old_pwd=request.getParameter("old_pwd");
    	String new_pwd=request.getParameter("new_pwd");
    	String pwdtype=request.getParameter("pwdtype");
    	String  isValidate =request.getParameter("isValidate");
    	String password = old_pwd;
    	log.info("解密前old_pwd："+old_pwd);
		log.info("解密前new_pwd："+new_pwd);
		log.info("client_id："+client_id);
    	Map<String,Object> map=new HashMap<String,Object>();

        if(!WebUtils.paraMandatoryCheck(Arrays.asList(client_id,access_token,account,old_pwd,new_pwd))){
        	WebUtils.paraMandaChkAndReturn(5, response,"必传参数中有空值");
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
			WebUtils.paraMandaChkAndReturn(7, response,"认证失败");
			return;
		}
		try {
				//DES解密
				/*new_pwd=	DESUtil.decrypt(new_pwd, app.getAppsecret());
				old_pwd=	DESUtil.decrypt(old_pwd, app.getAppsecret());*/
				new_pwd=AESUtil.decrypt(new_pwd, app.getAppsecret()).trim();
				old_pwd=AESUtil.decrypt(old_pwd, app.getAppsecret()).trim();
				
				log.info("解密后old_pwd："+old_pwd);
				log.info("解密后new_pwd："+new_pwd);
				if(!nullEmptyBlankJudge(isValidate)&&isValidate.equals("1")){
				if(WebUtils.nullEmptyBlankJudge(old_pwd)){
					map = WebUtils.paraMandaChkAndReturnMap(6, response,"旧密码不能为空");
					OauthControllerLog.log(startTime, account, password, app, map,userserviceDev);
					WebUtils.writeErrorJson(response,map);
					return;
				}
				if(WebUtils.nullEmptyBlankJudge(new_pwd)){
					map = WebUtils.paraMandaChkAndReturnMap(6, response,"新密码不能为空");
					OauthControllerLog.log(startTime, account, password, app, map,userserviceDev);
					WebUtils.writeErrorJson(response,map);
					return;
				}
				if(StringTool.isNumeric(new_pwd)){
					map = WebUtils.paraMandaChkAndReturnMap(6, response,"新密码不能为纯数字");
					OauthControllerLog.log(startTime, account, password, app, map,userserviceDev);
					WebUtils.writeErrorJson(response,map);
		        	return;
				}if(WebUtils.judgeInputNotNo(new_pwd)==1){
					map = WebUtils.paraMandaChkAndReturnMap(6, response,"新密码必须为6-20位字母、数字或英文下划线符号");
					OauthControllerLog.log(startTime, account, password, app, map,userserviceDev);
					WebUtils.writeErrorJson(response,map);
		        	return;
				}}else{
					if(judgePwdNo(new_pwd)==1){
						map = paraMandaChkAndReturnMap(6, response,"密码必须为6-20位");
						OauthControllerLog.log(startTime, account, password, app, map,userserviceDev);
						writeErrorJson(response,map);
			        	return;
					}	
				}
		} catch (Exception e1) {
			old_pwd="";
			e1.printStackTrace();
		}
		
			map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
		if(map.get("status").equals("1")){//client_id,access_token正确
			//判断缓存表中是否存在该用户
			boolean iscache = false;
			Object userCacheInfoObj = redisClient.getObject(RedisConstant.USER_CACHE_INFO+account);
			//存在缓存信息，用户存在于用户异常表中
			if(userCacheInfoObj!=null){
				UserCache userCache = checkCacheUsername(account,userCacheService,app.getId());
				/*if(userCache!=null && userCache.checkPasswod(old_pwd)){*/
				//if(userCache!=null && userCache.checkPasswodByAes(old_pwd,app.getAppsecret())){
				if(userCache!=null && userCache.checkPasswodByAes(old_pwd,userserviceDev.getAes_userCenter_key(),pwdtype)){
					//刷新盐值，重新加密
					//userCache.buildPasswordSalt();
					//userCache.setPlanPassword(new_pwd);
		    		//userCache.setPlanPasswordByAes(new_pwd, app.getAppsecret());
					userCache.setPlanPasswordByAes(new_pwd, userserviceDev.getAes_userCenter_key());
					userCacheService.updateUserCache(userCache);
		    		map.clear();
		    		map.put("status", "1");
		    		iscache = true;
				}
			}
			//异常用户不存在，则继续正常流程
			if(!iscache){
				//判断account是否存在
		    	User user=checkUsername(account,userService); 
		    	if(user==null){
		    		map.clear();
		    		map.put("status", "0");
		    		map.put("error_code", "3");//用户不存在 
		    		map.put("errMsg", "用户不存在");//用户不存在 
		    	/*}else if(!user.checkPasswod(old_pwd)){*/
		    	//}else if(!user.checkPasswodByAes(old_pwd, app.getAppsecret())){
		    	}else if(!user.checkPasswodByAes(old_pwd, userserviceDev.getAes_userCenter_key(), pwdtype)){
		    		//旧密码不正确
		    		map.clear();
		    		map.put("status", "0");
		    		map.put("error_code", "4");//旧密码错误
		    		map.put("errMsg", "旧密码错误");//用户不存在 
		    	}else{
		    		//刷新盐值，重新加密
		    		//user.buildPasswordSalt();
		    		//user.setPlanPassword(new_pwd);
		    		//user.setPlanPasswordByAes(new_pwd, app.getAppsecret());
		    		user.setPlanPasswordByAes(new_pwd,userserviceDev.getAes_userCenter_key());
		    		user.setUpdatePwdTime(new Date());
		    		userService.updateUser(user);
		    		List<AppUser>appUserList=appUserService.findByUserId(user.getId());
					try {
						Thread thread = new Thread(new SendOesThread(new_pwd, userService,appUserList,userserviceDev.getOes_interface_addr(),userserviceDev.getOes_interface_key()));
						thread.run();
					} catch (Exception e) {
						e.printStackTrace();
					}
		    		map.clear();
		    		map.put("status", "1");
		    	}
			}
		}
    	if(map.get("status")=="0"){
    		WebUtils.writeErrorJson(response,map);
    	}else{
    		WebUtils.writeSuccessJson(response,map);
    	}
    	OauthControllerLog.log(startTime, account, password, app, map,userserviceDev);
        return;
    }
    

 
}