package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.HashMap;
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
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;


/**
 * 获取密码验证规则
 */
@Controller
@RequestMapping("/user/")
public class UserCenterGetPwdRuleController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterGetPwdRuleController.class);

	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserserviceDev userserviceDev;
	
    /**
     * 获取密码验证规则
     * @param request
     * @param response
     */
    @RequestMapping("getValidationRule")
    public void  getValidationRule(HttpServletRequest request,HttpServletResponse response) {
    	String client_id=request.getParameter("client_id");
    	String access_token=request.getParameter("access_token");
    	Map<String, Object> map=new HashMap<String,Object>();
    	log.info("client_id:"+client_id+"access_token:"+access_token);
    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token))){
            paraMandaChkAndReturn(3,response,"必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
        if(app==null)
		{
			 app=appService.findIdByClientId(client_id);
			 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
		}
		map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
	    if(map.get("status").equals("1")){//client_id,access_token正确
				Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
				if(!hmacSHA1Verification){
					paraMandaChkAndReturn(4, response,"认证失败");
					return;
				}
				map.clear();
				String backMsg=userserviceDev.getPassword_rule();
				map.put("status", "1");
				map.put("backMsg",backMsg );
				
		}		
		if(map.get("status")=="0"){
			writeErrorJson(response,map);
		}else{
			writeSuccessJson(response,map);
		}
	    return;
     }
    
}