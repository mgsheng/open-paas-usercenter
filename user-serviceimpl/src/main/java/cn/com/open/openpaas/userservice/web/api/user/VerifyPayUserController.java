package cn.com.open.openpaas.userservice.web.api.user;


import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * 通过source_id验证用户是否存在
 */
@Controller
@RequestMapping("/user/")
public class VerifyPayUserController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(VerifyPayUserController.class);
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private UserService userService;

	/**
	 * 验证用户密码是否符合规则
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("verify/payUser")
	public void verifyPayUser(HttpServletRequest request,
			HttpServletResponse response) {
		String app_id = request.getParameter("app_id");
		String source_id = request.getParameter("source_id");
		String signature=request.getParameter("signature");
	    String timestamp=request.getParameter("timestamp");
	    String signatureNonce=request.getParameter("signatureNonce");
		Map<String, Object> map = new HashMap<String, Object>();
		if (!paraMandatoryCheck(Arrays
				.asList(source_id, app_id))) {
			paraMandaChkAndReturn(1, response, "必传参数中有空值");
			return;
		}
		App app = (App) redisClient.getObject(RedisConstant.APP_INFO
				+ app_id);
		if (app == null) {
			app = appService.findById(Integer.parseInt(app_id));
			redisClient.setObject(RedisConstant.APP_INFO + app_id, app);
		}
		SortedMap<Object,Object> sParaTemp = new TreeMap<Object,Object>();
    	sParaTemp.put("app_id",app_id);
   		sParaTemp.put("timestamp", timestamp);
   		sParaTemp.put("signatureNonce", signatureNonce);
   		sParaTemp.put("source_id",source_id);
   		String params=createSign(sParaTemp);
		 Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(signature,params,app.getAppsecret());
				if (!hmacSHA1Verification) {
					paraMandaChkAndReturn(2, response, "认证失败");
					return;
				}
				AppUser appUser = appUserService.findByCidSid(app.getId(), source_id);
                if(null!=appUser){
                    try {
                    /* user */
                        User user = userService.findUserById(appUser.userId());
                        if (null != user) {
                        	 map.clear();
                             map.put("status", "1");
                             map.put("user_id", user.getId());
                        }else{
                        	 map.clear();
                             map.put("status", "0");
                             map.put("errMsg","source_id不存在");
                             map.put("error_code", "3");//source_id不存在	
                        }
                    }catch(Exception e){
                        map.clear();
                        map.put("status", "0");
                        map.put("error_code", "4");
                        map.put("errMsg",e.getMessage());
                    }
                }else {
                    map.clear();
                    map.put("status", "0");
                    map.put("errMsg","source_id不存在");
                    map.put("error_code", "3");//source_id不存在
                }		

		if (map.get("status") == "0") {
			writeErrorJson(response, map);
		} else {
			writeSuccessJson(response, map);
		}
	}

}