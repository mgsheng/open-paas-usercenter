package cn.com.open.openpaas.userservice.web.api.user;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.common.model.Common;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.DES;
import cn.com.open.openpaas.userservice.app.tools.DESUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.MD5;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/user/")
public class UserCenterPublicLoginController extends BaseControllerUtil{
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private AppService appService;
	  /**
     * 用户中心公共登录接口
     * @param request
     * @param response
     */
    @RequestMapping("userCenterPublicLogin")
	public void  userCenterPublicLogin(HttpServletRequest request,HttpServletResponse response) {
	   	Map<String, Object> map=new HashMap<String,Object>();
	   	map.put("status", "0");
	   	String secret=request.getParameter("secret");
	   	if(StringUtils.isBlank(secret)){
	   		paraMandaChkAndReturn(1, response,"secert为空");
			return;
	   	}
	   	App appUc=	(App) redisClient.getObject(RedisConstant.APP_INFO+Common.APPID);
			if(appUc==null){
				appUc=appService.findById(Common.APPID);
				redisClient.setObject(RedisConstant.APP_INFO+Common.APPID, appUc);
			}
			if(appUc==null){
				//用户中心App不存在
				map.put("error_code", "2");
	    		map.put("errMsg", "用户中心App信息不存在");
	    		writeErrorJson(response,map);
				return;
			}
			String secretDecrypt = "";
	   	try {
				secretDecrypt = DES.decrypt(secret, appUc.getAppsecret().substring(0,8));
			} catch (Exception e) {
				e.printStackTrace();
			}
	   	if(StringUtils.isBlank(secretDecrypt)){
	   		//参数解密错误
	   		map.put("error_code", "3");
	    		map.put("errMsg", "参数解密错误");
	    		writeErrorJson(response,map);
	   		return;
	   	}
	    JSONObject json = JSONObject.fromObject(secretDecrypt);
      	String sourceId=json.getString("sourceId");
      	String time=json.getString("time");
      	String appKey=json.getString("appkey");
      	String appId=json.getString("appId");
      	String platform=json.getString("platform");
      	String businessData="";
    	if(json.containsKey("businessData")){
    		businessData=json.getString("businessData");
    	}
      	Map<String, Object> mapJson=new HashMap<String,Object>();
      	mapJson.put("sourceId", sourceId);
      	mapJson.put("time", time);
      	mapJson.put("appkey", appKey);
      	mapJson.put("appId", appId);
      	mapJson.put("platform",platform);
      	mapJson.put("businessData",businessData);
	   	//验证时间是否超时
	   	Date timeDate = DateTools.stringtoDate(time, "yyyyMMddHHmmss");
	   	if(new Date().getTime()-timeDate.getTime()>1800000){
	   		//超时
	   		map.put("error_code", "4");
	    		map.put("errMsg", "该链接已超时");
	    		writeErrorJson(response,map);
	   		return;
	   	}
	   	App app = appService.findIdByClientId(appKey);
	   	if(app==null){
	   		//App不存在
	   		map.put("error_code", "4");
	    		map.put("errMsg", "该App信息不存在");
	    		writeErrorJson(response,map);
	   		return;
	   	}
	   	StringBuffer url = new StringBuffer(app.getWebServerRedirectUri());
	   	//channel:wechat_wap#client_ip:127.0.0.1#app:app_e0030RoV8Z56l1fL#currency:CNY#private_key:MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPAUpQgB9NOhjNJnTxQP1R2Glw7xRoTzRTQ9cWq/VCy2Qjv6MhifhoV8m1MMUX4u3coWk+KVVKmlex9F+O6NoqwBUhjKVoJekwEx6NO75r7v1aYd8+BttOsN8T7nqLR1RJpeeKd7JwkrA3V/jurUYBn1HMtKrIxPesxP8pzGNu29AgMBAAECgYEA0nLGV0afvgOW1bkJcKSQQ9l/APIuxswYlbpmeRROTQNW/AxNbZgo60pXPW1G4j1i3Yr9B1mvZSZRzxAfLb2qIToiD4dsSqtAh0Q8YsSB8LAj4Uqif6lNGuWCv7ihaltW8XJBn++mqlLcLj7msEw8LNk8j2N1Z4IrH95P22WACwECQQD+MWU+jwPIlVObRJuQT8TTCC/HWNKYnVNDSlpclJjVZsaVYAlCm0nOHSAhlNhcwaWh4rP/Nwy95uvKIQu4FaR5AkEA8cmQ9ZICvbnce2yuheeqJCBFHN891eQPfc1DmxSFu3Qa1HRn9Np9Hnvg9RLaKdjhGomofuFKIbunJ/RRQQ7aZQJANZZEOcP8kDSqw3jbMrkeTT7Uk7nt665+9xidpBbPgW2BP8xXJ2uFS6ZoN6whUKyiNlaMi6kXTpF388yzo8MsQQJAKE5OsPE3LcOHArg6W64jycPrYYXBfB95iaInK/n+1SHkuxfwoHuvsd46wP/0iFdUJyVdhlEuF3N9yQZMaUj9lQJBAPS5Wgr2G/a8JZNem2LrNrhu1o/OU0RgIcItzLVkd5AVUa7uRfZxEhbCEeD1n+7Sglv4g5Koat6z3BjbwrFE4KM=#secert_key:ca8e2aca50dd41ab8f27e7b07ecbb498#paymax_public_key:MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDE0gdZi/icODd0WAQlxfw8FgoJ/BwCgMLn+Fh1DquRXqYkgGtv35B5j+CCSpag3wxNwtrHRHpar3dpo38+KIqaMy8bhQj1hmBJmHkgM1yZZpVC5S4uc4FYT21zQn0HBI1E1gg2nAA7JkFV1+VnvFKk9rzOA2UtrYXID+ZG4BMBwwIDAQAB
	   	//非教师培训App采用des加密方式
	   	
	   	String appSecret = app.getAppsecret().substring(0, 8);
	  //Des(（sourceId+“#”+time+“#”+appKey）, appSecret)
   		secret = "";
   		try {
   			secret = DES.encrypt(JSONObject.fromObject(mapJson).toString(), appSecret);
   		} catch (Exception e) {
   			e.printStackTrace();
   			//异常
       		map.put("error_code", "5");
            map.put("errMsg", "系统异常");
            writeErrorJson(response,map);
       		return;
   		}
   		
	   	if(app.getId()!=5){
	   		//time：格式yyyyMMddHHmmss
	   		secret = secret.replaceAll("\\+", "%2B");
	   		url.append("?secret=").append(secret);
	   	}
	   	//教师培训App采用MD5加密方式
	   	else{
	   		//salt=4位随机数
	   		try {
	   		String salt = StringTool.getRandomString(4);
	   		//secret=MD5(sourceId+salt+appKey+appSecret);
	   		secret = MD5.Md5(JSONObject.fromObject(mapJson).toString());
	   		secret = secret.replaceAll("\\+", "%2B");
	   		url.append("?sourceId=").append(sourceId);
	   		url.append("&salt=").append(salt);
	   		url.append("&secret=").append(secret);
	   		} catch (Exception e) {
	   			e.printStackTrace();
	   			//异常
	       		map.put("error_code", "5");
	            map.put("errMsg", "系统异常");
	            writeErrorJson(response,map);
	       		return;
	   	  }
	   	}
			try {
				
				response.sendRedirect(url.toString());
			} catch (IOException e) {
				e.printStackTrace();
				//异常
	   		map.put("error_code", "5");
	    		map.put("errMsg", "系统异常");
	    		writeErrorJson(response,map);
	   		return;
			}
			return;
	    }
}
