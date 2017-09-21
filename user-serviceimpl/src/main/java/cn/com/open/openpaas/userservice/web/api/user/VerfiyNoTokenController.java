package cn.com.open.openpaas.userservice.web.api.user;

import java.util.Arrays;
import java.util.Date;
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

/**
 * 验证自动登录地址（不需要accessToken）
 */
@Controller
@RequestMapping("/user/noToken")
public class VerfiyNoTokenController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(VerfiyNoTokenController.class);
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private UserserviceDev userserviceDev;
    /**
     * 验证自动登录地址（不需要验证密码规则）
     * @param request
     * @param response
     */
     @RequestMapping("autoLogin")
     public void  validateLogin(HttpServletRequest request,HttpServletResponse response) {
     	String client_id=request.getParameter("client_id");
         String secret= request.getParameter("secret");
         String guid="";
         String time="";
         String username="";
         long startTime = System.currentTimeMillis();
     	Map<String, Object> map=new HashMap<String,Object>();
         if(!WebUtils.paraMandatoryCheck(Arrays.asList(client_id,secret))){
        	 WebUtils.paraMandaChkAndReturn(4,response,"必传参数中有空值");
             return;
         }
         App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
	        if(app==null)
			{
				 app=appService.findIdByClientId(client_id);
				 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
			}
	        if(app!=null){
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
					    	if(timeSub>Integer.parseInt(userserviceDev.getRedisExpireTime())){
					    		WebUtils.paraMandaChkAndReturn(5,response,"时间超时-超过"+userserviceDev.getRedisExpireTime()+"分钟有效期");
	 				             return;
					    	}
					    	User  user = userService.findByGuid(guid);
					    	if(user==null){
					    		WebUtils.paraMandaChkAndReturn(3,response,"用户不存在");
					            return;
					    	}
					    	username=user.getUsername();
					    	map.put("status", "1");
					    	
	 				}catch (Exception e) {
	 					map.clear();
	 					map.put("status", "0");
	 	        		map.put("error_code", "6");//用户名、手机或邮箱不正确
	 	        		map.put("errMsg","后台程序异常");//用户名、手机或邮箱不正确
	 				}
	 				
	        }else{
	        	    map.clear();
					map.put("status", "0");
	        		map.put("error_code", "7");//用户名、手机或邮箱不正确
	        		map.put("errMsg","应用id错误");//用户名、手机或邮箱不正确
	        }
 		    
     	if(map.get("status")=="0"){
     		WebUtils.writeErrorJson(response,map);
     	}else{
     		WebUtils.writeSuccessJson(response,map);
     	}
     	//OauthControllerLog.log(startTime,username,"",app,map,userserviceDev);
         return;
         
     }
     
}