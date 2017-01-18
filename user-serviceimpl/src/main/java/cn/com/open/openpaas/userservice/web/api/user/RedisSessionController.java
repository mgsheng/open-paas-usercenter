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
            String strUrl = "http://" + request.getServerName() //服务器地址
                    + ":"
                    + request.getServerPort()           //端口号
                    + request.getContextPath()      //项目名称
                    + request.getServletPath()      //请求页面或其他地址
                    + "?" + (request.getQueryString()); //参数
            String domain = getDomain(strUrl);
            if(null == redisClient.getObject(username+"_"+session_id)){
                Cookie cookie = new Cookie(redisKey,session_id);
                if(null != domain && "" != domain)
                {
                    cookie.setDomain(domain);
                }
                /*此用户第一次登陆*/
                redisClient.setObject(username+"_"+session_id,service_name);
                mapRedis.put("status",1);
                mapRedis.put("info","有效");
                mapRedis.put("redis_key",redis_key);
                mapRedis.put("redis_value",redis_value);
                redisClient.setObject(redisKey,mapRedis);
            }else{
                /*获取上次的sessionid*/
                String sessionid = null;
                Cookie[] cookies = request.getCookies();
                for (Cookie cookieSingle : cookies){
                    if(cookieSingle.getName().equals(redisKey)){
                        sessionid = cookieSingle.getValue();
                    }
                }
                redisKey = client_id+"_"+service_name+"_"+sessionid;
                /*此用户再次登陆 将之前的状态设置为3*/
                mapRedis.put("status",3);
                mapRedis.put("info","被踢下线");
                mapRedis.put("redis_key",redis_key);
                mapRedis.put("redis_value",redis_value);
                redisClient.del(redisKey);
                redisClient.setObject(redisKey,mapRedis);
                /*将最新状态设置为有效*/
                mapRedis=new HashMap<String,Object>();
                redisClient.setObject(username+"_"+session_id,service_name);
                mapRedis.put("status",1);
                mapRedis.put("info","有效");
                mapRedis.put("redis_key",redis_key);
                mapRedis.put("redis_value",redis_value);
                Cookie cookie = new Cookie(redisKey,session_id);
                if(null != domain && "" != domain)
                {
                    cookie.setDomain(domain);
                }
                redisClient.setObject(redisKey,mapRedis);
            }
            map=new HashMap<String,Object>();
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
            map=new HashMap<String,Object>();
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
            redisClient.del(redisKey);
            map=new HashMap<String,Object>();
            map.put("status",1);
            writeSuccessJson(response,map);
        }
        return;
    }
    /**
     * 根据URL获取domain
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
