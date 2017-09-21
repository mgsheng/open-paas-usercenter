package cn.com.open.openpaas.userservice.web.api.user;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.DES;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 验证自动登录地址（不需要验证密码规则）
 */
@Controller
@RequestMapping("/user/")
public class VerfiyAutoLoginController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(VerfiyAutoLoginController.class);
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserserviceDev userserviceDev;
	 @Autowired
	 private UserService userService;

	 
    /**
     * 验证自动登录地址（不需要验证密码规则）
     * @param request
     * @param response
     */
     @RequestMapping("autoLogin")
     public void  validateLogin(HttpServletRequest request,HttpServletResponse response) {
     	String client_id=request.getParameter("client_id");
     	String access_token=request.getParameter("access_token");
         String secret= request.getParameter("secret");
         String guid="";
         String time="";
         String username="";
         long startTime = System.currentTimeMillis();
     	Map<String, Object> map=new HashMap<String,Object>();
         if(!WebUtils.paraMandatoryCheck(Arrays.asList(client_id,access_token,secret))){
        	 WebUtils.paraMandaChkAndReturn(4,response,"必传参数中有空值");
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
			map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
 		 if(map.get("status").equals("1")){//client_id,access_token正确
 				try {					
					secret=	DES.decrypt(secret, app.getAppsecret());
					//secret=	AESUtil.decrypt(secret, app.getAppsecret());
					log.info("解密后 secret："+secret);
				    	String sercret[]=secret.split("#");
				    	guid=sercret[0];
				    	time=sercret[1];
				    	long timeSub = 0;
				    	String nowTime=DateTools.dateToString(new Date(),"yyyyMMddHHmmss");
				    	if(!WebUtils.nullEmptyBlankJudge(time)){
				    		timeSub=DateTools.timeSub2(time,nowTime)/60;
				    	}
				    	if(WebUtils.nullEmptyBlankJudge(guid)){
				    		WebUtils.paraMandaChkAndReturn(3,response,"用户不存在");
 				             return;
				    	}
				    	if(timeSub>30){
				    		WebUtils.paraMandaChkAndReturn(5,response,"时间超时-超过30分钟有效期");
 				             return;
				    	}
				    	User  user = userService.findByGuid(guid);
				    	if(user==null){
				    		//获取token
					    	SortedMap<Object,Object> sParaTemp1 = new TreeMap<Object,Object>();
							sParaTemp1.put("grant_type","client_credentials");
							sParaTemp1.put("client_id", client_id);
							sParaTemp1.put("client_secret", app.getAppsecret());
							sParaTemp1.put("scope", "read,write");
					    	String result=sendPost(userserviceDev.getAccess_token_uri(), sParaTemp1);
							 JSONObject obj = JSONObject.fromObject(result);
							 String accessToken="";
							if(!nullEmptyBlankJudge(result)){
									accessToken= obj.getString("access_token");
							}else{
									WebUtils.paraMandaChkAndReturn(7,response,"token获取失败");
		 				             return;
							}
							//根据guid 获取用户信息
							//获取token
					    	SortedMap<Object,Object> sParaTemp2 = new TreeMap<Object,Object>();
							sParaTemp2.put("client_id", client_id);
							sParaTemp2.put("access_token", accessToken);
							sParaTemp2.put("guid", guid);
					    	String infoResult=sendPost(userserviceDev.getUser_center_getInfoList_uri(), sParaTemp2);
							if(infoResult.indexOf("\"status\":\"1\"")==-1)
							{
								WebUtils.paraMandaChkAndReturn(7,response,"用户不存在");
		 				        return;	
							}
					    	// JSONObject infoObj = JSONObject.fromObject(infoResult);
//							 String infoStatus = infoObj.getString("status");
//							if(!infoStatus.equals("1")){
//								WebUtils.paraMandaChkAndReturn(7,response,"用户不存在");
//		 				        return;
//							}
				    	}
				    
 				}catch (Exception e) {
 					e.printStackTrace();
 					map.clear();
 					map.put("status", "0");
 	        		map.put("error_code", "6");//用户名、手机或邮箱不正确
 	        		map.put("errMsg","后台程序异常");//用户名、手机或邮箱不正确
 				}
 				
 		}		
     	if(map.get("status")=="0"){
     		WebUtils.writeErrorJson(response,map);
     	}else{
     		WebUtils.writeSuccessJson(response,map);
     	}
     	//OauthControllerLog.log(startTime,guid,"",app,map,userserviceDev);
         return;
         
     }
     
}