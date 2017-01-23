package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.BaseController;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
			/*HttpSession session = request.getSession();
			session.removeAttribute(userserviceDev.getSingle_sign_user());*/
			/*删除redis中的值*/
			/*删除业务redis*/
			String bussinessRedisKey = client_id+RedisConstant.USER_SERVICE+jsessionId;
			redisClient.del(bussinessRedisKey);
			/*从本地redis读取用户信息*/
			String localRedisKey = RedisConstant.USER_SERVICE_JSESSIONID+jsessionId;
			/*删除存储用户sessionId的redis*/
			Map<String,Object> localRedisValues = (Map<String, Object>) redisClient.getObject(localRedisKey);
			if(null != localRedisValues && localRedisValues.size()>0){
				Object userObj = localRedisValues.get("user");
				if(null != userObj){
					net.sf.json.JSONObject jsonObjectLocal= net.sf.json.JSONObject.fromObject(userObj);
					if(null != jsonObjectLocal && jsonObjectLocal.size()>0){
						if(null != jsonObjectLocal.get("username")){
							redisClient.del(jsonObjectLocal.get("username").toString());
						}
					}
				}
			}
			/*删除本地sessionid的数据*/
			redisClient.del(localRedisKey);

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