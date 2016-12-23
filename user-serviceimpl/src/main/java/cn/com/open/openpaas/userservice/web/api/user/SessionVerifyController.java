package cn.com.open.openpaas.userservice.web.api.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
	/**
     * 用户账号验证接口
     * @return Json
     */
    @RequestMapping("verify")
    public void userCenterVerifySession(HttpServletRequest request,HttpServletResponse response) {
    	String client_id=request.getParameter("client_id");
    	String access_token=request.getParameter("access_token");
    	log.info("client_id:"+client_id);
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token))){
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
			HttpSession session = request.getSession();
			Object o = session.getAttribute("SingleSignOnUser");
			if(o!=null){
				User user = (User) o;
				AppUser appUser = appUserService.findByCidAUid(app.getId(), user.getId());
				map.put("userName", user.getUsername());
				map.put("guid", user.guid());
				map.put("phone", user.getPhone());
				map.put("email", user.getEmail());	
				if(appUser!=null){
					map.put("sourceId", appUser.sourceId());
				}else{
					map.put("sourceId", "");
				}
				map.put("appId", app.getId());
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