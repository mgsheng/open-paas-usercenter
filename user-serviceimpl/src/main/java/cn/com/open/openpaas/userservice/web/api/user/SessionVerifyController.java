package cn.com.open.openpaas.userservice.web.api.user;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.BaseController;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 
 */
@Controller
@RequestMapping("/user/session")
public class SessionVerifyController  extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(SessionVerifyController.class);
	@Autowired
	private AppUserService appUserService;
	 @Autowired
	 private UserserviceDev userserviceDev;
	/**
     * 用户账号验证接口
     * @return Json
     */
    @RequestMapping("verify")
    public void userCenterVerifySession(HttpServletRequest request,HttpServletResponse response) throws IOException {
    	String client_id=request.getParameter("client_id");
    	String access_token=request.getParameter("access_token");
		String jsessionId=request.getParameter("jsessionId");
    	log.info("client_id:"+client_id+"access_token:"+access_token+"access_token:"+access_token);
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,jsessionId))){
            paraMandaChkAndReturn(3, response,"必传参数有空值");
            return;
        }
        Map<String ,Object> map=new HashMap<String,Object>();
		map=checkClientIdOrToken(client_id,access_token);
		
		App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
        if(app==null){
			 app=appService.findIdByClientId(client_id);
			 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
		}
		
		if(map.get("status").equals("1")){
			Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
			if(!hmacSHA1Verification){
				paraMandaChkAndReturn(4, response,"认证失败");
				return;
			}
			/*从redis读取用户信息*/
			String localRedisKey = RedisConstant.USER_SERVICE_JSESSIONID+jsessionId;
			Map<String,Object> redisValue = null;
			if(redisClient.existKey(localRedisKey)){
				redisValue = (Map<String, Object>) redisClient.getObject(localRedisKey);
			}
			/*HttpSession session = request.getSession();
			Object o = session.getAttribute(userserviceDev.getSingle_sign_user());*/
			if(null != redisValue && redisValue.size()>0){
				/*如果存在sessionId 则返回相应的数据 否则提示相应的信息验证失败*/
				if(null != redisValue && redisValue.size()>0){
					Object userObj = redisValue.get("user");
					if(null != userObj)
					{
						Map<String,Object> user = new ObjectMapper().readValue(userObj.toString(), HashMap.class);

						if(null != user && user.size()>0 && null != app){
							AppUser appUser = appUserService.findByCidAUid(app.getId(), Integer.parseInt(user.get("id").toString()));
							map.put("userName", user.get("username"));
							map.put("guid", redisValue.get("guid"));
							map.put("phone", user.get("phone"));
							map.put("email", user.get("email"));
							if(appUser!=null){
								map.put("sourceId", appUser.sourceId());
							}else{
								map.put("sourceId", "");
							}
							map.put("appId", app.getId());
						}
					}
				}else{
					paraMandaChkAndReturn(5, response,"session验证失败");
					return;
				}
				//map.put("verifyValue", "success");
			}else{
				//map.clear();
				//map.put("status","0");
				//map.put("verifyValue", "error");
				paraMandaChkAndReturn(5, response,"session验证失败");
				return;
			}
		}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    	   writeSuccessJson(response,map);
    	}
        return;
    }
}