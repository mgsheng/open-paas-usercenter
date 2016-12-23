package cn.com.open.openpaas.userservice.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;

import static cn.com.open.openpaas.userservice.web.WebUtils.writeJson;


public class BaseController {
	 @Autowired
	 protected AppService appService; 
	 @Autowired
	 protected DefaultTokenServices tokenServices;
	 @Autowired
	 protected RedisClientTemplate redisClient;
	 protected boolean nullEmptyBlankJudge(String str){
        return null==str||str.isEmpty()||"".equals(str.trim());
    }
    protected boolean paraMandatoryCheck(List<String> params){
        for(String param:params){
            if(nullEmptyBlankJudge(param)){
                return false;
            }
        }
        return true;
    }
    protected void paraMandaChkAndReturn(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=paraMandaChkAndReturnMap(errorNum, response, errMsg);
        writeErrorJson(response,map);
    }
    
    protected Map<String, Object> paraMandaChkAndReturnMap(int errorNum,HttpServletResponse response,String errMsg){
        Map<String, Object> map=new HashMap<String,Object>();
        map.clear();
        map.put("status", "0");
        map.put("error_code", errorNum);
        map.put("errMsg", errMsg);
        return map;
    }
    /**
     * 返回成功json
     * @param response
     * @param obj 数据集
     */
    protected void writeSuccessJson(HttpServletResponse response, Map map){
    	writeJson(response, JSONObject.fromObject(map));
    }
    
    /**
     * 返回失败json
     * @param response
     * @param error_code 错误码
     */
    protected void writeErrorJson(HttpServletResponse response, Map map){
    	writeJson(response, JSONObject.fromObject(map));
    }
    //判断client_id和access_token是否正确
    protected Map<String,Object> checkClientIdOrToken(String client_id,String access_token){
    	Map<String,Object> map=new HashMap<String,Object>();
    	if(client_id==null || client_id.length()==0){
    		map.put("status", "0");
    		map.put("error_code", "1");//client_id错误
    	}else{
    		App app=	(App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
			if(app==null)
			{
				 app=appService.findIdByClientId(client_id);
				 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
			}
			OAuth2AccessToken accessToken=   tokenServices.readAccessToken(access_token);
			if(app==null){//client_id错误
				map.put("status", "0");
	    		map.put("error_code", "1");//client_id错误
	    		map.put("errMsg","Client_id错误");
			}else if(!tokenServices.getClientId(accessToken.getValue()).equals(client_id)){
				map.put("status", "0");
				map.put("error_code", "2");//access_token和client_id不匹配
				map.put("errMsg","access_token和client_id不匹配");
			}else{
				map.put("status","1");//client_id,access_token正确
				map.put("access_token", accessToken.getValue());
				map.put("token_type", accessToken.getTokenType());
				Object[] at=accessToken.getScope().toArray();
				String scope_s="";
				for(Object ss:at){ 
					scope_s+=(ss+",");
				}
				map.put("scope",scope_s.substring(0, scope_s.length()-1));
				map.put("expires_in", accessToken.getExpiresIn());
				map.put("refresh_token",accessToken.getRefreshToken().getValue());
			}
    	}
		return map;
    }
    protected static Cookie getCookieByName(HttpServletRequest request,String name){
	 	  Map<String,Cookie> cookieMap = ReadCookieMap(request);
	 	    if(cookieMap.containsKey(name)){
	 	        Cookie cookie = (Cookie)cookieMap.get(name);
	 	        return cookie;
	 	    }else{
	 	        return null;
	 	    }
	}

	protected static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}
    
}