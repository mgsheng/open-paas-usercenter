package cn.com.open.openpaas.userservice.web.api.user;


import java.util.Arrays;
import java.util.HashMap;
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
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;


/**
 * 验证用户密码是否符合规则
 */
@Controller
@RequestMapping("/user/")
public class UserCenterverifyPwdController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserCenterverifyPwdController.class);
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserserviceDev userserviceDev;

	/**
	 * 验证用户密码是否符合规则
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("verifyPassWord")
	public void verifyPassWord(HttpServletRequest request,
			HttpServletResponse response) {
		String client_id = request.getParameter("client_id");
		String access_token = request.getParameter("access_token");
		String password = request.getParameter("password");
		 long startTime = System.currentTimeMillis();
		log.info("client_id:"+client_id+"access_token:"+access_token+"password:"+password);
    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
		Map<String, Object> map = new HashMap<String, Object>();
		if (!paraMandatoryCheck(Arrays
				.asList(client_id, access_token, password))) {
			paraMandaChkAndReturn(3, response, "必传参数中有空值");
			return;
		}
		App app = (App) redisClient.getObject(RedisConstant.APP_INFO
				+ client_id);
		if (app == null) {
			app = appService.findIdByClientId(client_id);
			redisClient.setObject(RedisConstant.APP_INFO + client_id, app);
		}
		map = checkClientIdOrToken(client_id, access_token, app, tokenServices);
		if (map.get("status").equals("1")) {// client_id,access_token正确
			try {
				Boolean hmacSHA1Verification = OauthSignatureValidateHandler
						.validateSignature(request, app);
				if (!hmacSHA1Verification) {
					paraMandaChkAndReturn(5, response, "认证失败");
					return;
				}
				password = AESUtil.decrypt(password, app.getAppsecret()).trim();
				log.info("解密后 password：" + password);
				log.info("client_id:" + client_id);
				if (StringTool.isNumeric(password)) {
					paraMandaChkAndReturn(4, response, "密码不能为纯数字");
					return;
				}
				if (judgeInputNotNo(password) == 1) {
					paraMandaChkAndReturn(4, response,
							"密码必须为6-20位字母、数字或英文下划线符号");
					return;
				} else {
					String passwordLevel = GetPwdSecurityLevel(password)
							.toString();
					String strength = "";
					if (passwordLevel.equals("WEAK")) {
						strength = "1";
					}
					if (passwordLevel.equals("STRONG")) {
						strength = "2";
					}
					if (passwordLevel.equals("VERY_STRONG")) {
						strength = "3";
					}
					if (passwordLevel.equals("VERY_SECURE")) {
						strength = "4";
					}
					map.clear();
					map.put("status", "1");
					map.put("strength", strength); 
				}
			} catch (Exception e) {
				map.clear();
				map.put("status", "0");
				map.put("error_code", "4");// 用户名、手机或邮箱不正确
				map.put("errMsg", e.getMessage());// 用户名、手机或邮箱不正确
			}

		}
		if (map.get("status") == "0") {
			writeErrorJson(response, map);
		} else {
			writeSuccessJson(response, map);
		}
		//OauthControllerLog.log(startTime,"","",app,map,userserviceDev);
		return;
	}

}