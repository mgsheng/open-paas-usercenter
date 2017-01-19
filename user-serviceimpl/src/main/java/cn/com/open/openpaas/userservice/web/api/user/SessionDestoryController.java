package cn.com.open.openpaas.userservice.web.api.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.domain.model.AbstractDomain;
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
public class SessionDestoryController  extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(SessionDestoryController.class);

	 @Autowired
	 private UserserviceDev userserviceDev;
    /**
     * 用户账号退出注销Session用户接口
     * @return Json
     */
    @RequestMapping("remove")
    public void userCenterDelSession(HttpServletRequest request,HttpServletResponse response) {
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
			HttpSession session = request.getSession();
			session.removeAttribute(userserviceDev.getSingle_sign_user());
			/*删除redis中的值*/
			/*从redis读取用户信息*/
			String redisKey = client_id+"_userService_"+jsessionId;
			redisClient.del(redisKey);
			Cookie[] cookies = request.getCookies();//这样便可以获取一个cookie数组
			String data = "";
			for (Cookie cookie:cookies){
				if(cookie.getValue().equals(jsessionId)){
					cookie.setMaxAge(-1);
				}
			}
			map.clear();
			map.put("status","1");
		}
    	if(map.get("status")=="0"){
    		writeErrorJson(response,map);
    	}else{
    	   writeSuccessJson(response,map);
    	}
        return;
    }
}