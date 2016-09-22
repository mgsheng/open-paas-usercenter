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
            User user = new User();
            if((null==user||user.id()<1)&&!nullEmptyBlankJudge(guid)){
          	  user = userService.findByGuid(guid);
            }
            if((null==user||user.getId()<1)&&!nullEmptyBlankJudge(phone)){
            	List<User> userList = userService.findByPhone(phone);
            	if(userList!=null && userList.size()>0){
            		user = userList.get(0);
            	}
            }
            if((null==user||user.getId()<1)&&!nullEmptyBlankJudge(email)){
            	List<User> userList = userService.findByEmail(email);
                if(userList!=null && userList.size()>0){
            		user = userList.get(0);
            	}
            }
			if(user==null){
				map.clear();
				map.put("status","0");
				map.put("error_code", "5");//单独请求接口时，用户名绑定
				map.put("errMsg", "guid不存在");//单独请求接口时，用户名绑定
			}else{
				//appUser
				AppUser appUser;
				if(source_id==null || source_id.length()==0){
					appUser=null;
				}else{
					appUser=appUserService.findByCidSid(app.getId(), source_id);
				}
				if(appUser!=null){
					//删除已插入的user
//					userService.deleteUser(user.id());
					map.clear();
					map.put("status","0");
					map.put("error_code", "4");//source_id已存在 
					map.put("errMsg", "原业务系统用户已经绑定");//单独请求接口时，用户名绑定
				}else{
					if(null==source_id||"".equals(source_id.trim())){
						appUser=new AppUser(app.getId(),user.getId(),user.guid());
					}else{
						appUser=new AppUser(app.getId(),user.getId(),source_id);
					}
					Boolean f=appUserService.saveAppUser(appUser);
					if(f){
						if(!nullEmptyBlankJudge(card_no)){
							userService.updateUserCardNoById(user.getId(),card_no);
						}
						if(!nullEmptyBlankJudge(phone)){
							userService.updatePhoneById(user.getId(), phone);
						}
	            		map.put("guid", user.guid());
					}
				}
			}
		}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    		writeSuccessJson(response,map);
    	}
    	OauthControllerLog.log(startTime, guid, source_id, app, map,userserviceDev);
        return;
    }
}