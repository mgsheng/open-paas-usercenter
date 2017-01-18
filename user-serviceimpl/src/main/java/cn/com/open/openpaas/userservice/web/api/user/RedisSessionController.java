package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于redis的单点登录
 */
@Controller
@RequestMapping("/redis/")
public class RedisSessionController   extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisSessionController.class);
    @Autowired
    private AppService appService;
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private RedisClientTemplate redisClient;
    /**
     * redis保存接口
     * @return Json
     */
    @RequestMapping(value = "saveRedis",method = RequestMethod.POST)
    public void saveRedis(HttpServletRequest request,HttpServletResponse response) {
        String client_id=request.getParameter("client_id");
        String access_token=request.getParameter("access_token");
        String service_name = request.getParameter("service_name");
        String username=request.getParameter("username");
        String session_id=request.getParameter("session_id");
        String redis_key = request.getParameter("redis_key");
        String redis_value=request.getParameter("redis_value");
        log.info("client_id:"+client_id+"access_token:"+access_token+"service_name:"+service_name+"redis_key:"+redis_key+"redis_value:"+redis_value);
        Map<String ,Object> map=new HashMap<String,Object>();
        Map<String ,Object> mapRedis=new HashMap<String,Object>();
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,service_name))){
            paraMandaChkAndReturn(3, response,"必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
        if(app==null)
        {
            app=appService.findIdByClientId(client_id);
            redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
        }
        map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
        if(map.get("status").equals("1")){
            Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
            if(!hmacSHA1Verification){
                paraMandaChkAndReturn(4, response,"认证失败");
                return;
            }
        }
        String redisKey = client_id+"_"+service_name+"_"+session_id;

        String strUrl = "http://" + request.getServerName()
                + ":"
                + request.getServerPort()
                + request.getContextPath()
                + request.getServletPath()
                + "?" + (request.getQueryString());
        if(map.get("status")=="0"){
            writeErrorJson(response,map);
        }else{
            String domain = getDomain(strUrl);
            if(null == redisClient.getObject(username)){
                Cookie cookie = new Cookie(username,session_id);
                if(null != domain && "" != domain)
                {
                    cookie.setDomain(domain);
                }
                /*第一次保存设置为有效的sessionid 根据用户名*/
                redisClient.setObject(username,session_id);
                mapRedis.put("status",1);
                mapRedis.put("info","有效");
                mapRedis.put("redis_key",redis_key);
                mapRedis.put("redis_value",redis_value);
                redisClient.setObject(redisKey,mapRedis);
            }else{
                /*获取上次的sessionid*/
                redisKey = client_id+"_"+service_name+"_"+redisClient.getObject(username);
                /*更新之前的session为被踢下线*/
                mapRedis.put("status",3);
                mapRedis.put("info","被踢下线");
                mapRedis.put("redis_key",redis_key);
                mapRedis.put("redis_value",redis_value);
                redisClient.del(redisKey);
                redisClient.setObject(redisKey,mapRedis);
                /*将最新的session数据设置为有效的数据*/
                mapRedis=new HashMap<String,Object>();
                redisKey = client_id+"_"+service_name+"_"+session_id;
                redisClient.setObject(username,session_id);
                mapRedis.put("status",1);
                mapRedis.put("info","有效");
                mapRedis.put("redis_key",redis_key);
                mapRedis.put("redis_value",redis_value);
                Cookie cookie = new Cookie(username,session_id);
                if(null != domain && "" != domain)
                {
                    cookie.setDomain(domain);
                }
                redisClient.setObject(redisKey,mapRedis);
            }
            map.clear();
            map.put("status",1);
            writeSuccessJson(response,map);
        }
        return;
    }
    /**
     * Redis获取接口
     * @return Json
     */
    @RequestMapping(value = "getRedis",method = RequestMethod.POST)
    public void getRedis(HttpServletRequest request,HttpServletResponse response) {
        String client_id=request.getParameter("client_id");
        String access_token=request.getParameter("access_token");
        String service_name = request.getParameter("service_name");
        String session_id=request.getParameter("session_id");
        String redis_key = request.getParameter("redis_key");
        String redis_value=request.getParameter("redis_value");
        log.info("client_id:"+client_id+"access_token:"+access_token+"service_name:"+service_name+"redis_key:"+redis_key+"redis_value:"+redis_value);
        Map<String ,Object> map=new HashMap<String,Object>();
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,service_name,redis_key,redis_value))){
            paraMandaChkAndReturn(3, response,"必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
        if(app==null)
        {
            app=appService.findIdByClientId(client_id);
            redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
        }
        map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
        if(map.get("status").equals("1")){
            Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
            if(!hmacSHA1Verification){
                paraMandaChkAndReturn(4, response,"认证失败");
                return;
            }
        }
        String redisKey = client_id+"_"+service_name+"_"+session_id;
        if(map.get("status")=="0"){
            writeErrorJson(response,map);
        }else{
            map.clear();
            map.put("status",1);
            map.put("redisValue",redisClient.getObject(redisKey));
            writeSuccessJson(response,map);
        }
        return;
    }
    /**
     * Redis删除接口
     * @return Json
     */
    @RequestMapping(value = "delRedis",method = RequestMethod.POST)
    public void delRedis(HttpServletRequest request,HttpServletResponse response) {
        String client_id=request.getParameter("client_id");
        String access_token=request.getParameter("access_token");
        String service_name = request.getParameter("service_name");
        String session_id=request.getParameter("session_id");
        log.info("client_id:"+client_id+"access_token:"+access_token+"service_name:"+service_name);
        Map<String ,Object> map=new HashMap<String,Object>();
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,service_name))){
            paraMandaChkAndReturn(3, response,"必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
        if(app==null)
        {
            app=appService.findIdByClientId(client_id);
            redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
        }
        map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
        if(map.get("status").equals("1")){
            Boolean hmacSHA1Verification=OauthSignatureValidateHandler.validateSignature(request, app);
            if(!hmacSHA1Verification){
                paraMandaChkAndReturn(4, response,"认证失败");
                return;
            }
        }
        String redisKey = client_id+"_"+service_name+"_"+session_id;
        if(map.get("status")=="0"){
            writeErrorJson(response,map);
        }else{
            redisClient.del(redisKey);
            Cookie[] cookies = request.getCookies();

            for (Cookie cookieSingle : cookies){
                if(cookieSingle.getName().equals(redisKey)){
                    cookieSingle.setMaxAge(-1);
                }
            }
            map.clear();
            map.put("status",1);
            writeSuccessJson(response,map);
        }
        return;
    }
    /**
     * 根据URL获取ȡdomain
     * @param url
     * @return
     */
    public static String getDomain(String url){

        String domainUrl = null;
        if (url == null) {
            return null;
        } else {
            Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(url);
            while(matcher.find()){
                domainUrl = matcher.group();
            }
            return domainUrl;
        }
    }

}
