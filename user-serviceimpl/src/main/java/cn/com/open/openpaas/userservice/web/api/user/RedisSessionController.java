package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;
import org.codehaus.jackson.map.ObjectMapper;
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
     * username：sessionid键值对存储到redis
     * 存储sessionid 相应数据库到redis
     * 根据username获取sessionid 判断是否存在session
     * 存在则踢下线，并将最新数据更新为有效数据
     * @return Json
     */
    @RequestMapping(value = "saveRedis",method = RequestMethod.POST)
    public void saveRedis(HttpServletRequest request,HttpServletResponse response) {
        String client_id=request.getParameter("client_id");
        String access_token=request.getParameter("access_token");
        String service_name = request.getParameter("service_name");
        String redis_key = request.getParameter("redis_key");/*单点登录key值为jsessionid*/
        String redis_value=request.getParameter("redis_value");
        String sessionTime=request.getParameter("session_time");/*有效时间，默认是分钟，如果为空则默认30分钟*/
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
            /*业务数据rediskey 单点登录格式为{”status”:1, ”info”:”有效”, ” businessData”:{业务数据}.}*/
            String bussinessRedisKey = client_id+RedisConstant.USER_SERVICE+redis_key;
            mapRedis.put("status",1);
            mapRedis.put("info","有效");
            mapRedis.put("businessData",redis_value);
            if(null == sessionTime || "" == sessionTime || "0" == sessionTime){
			    /*默认sessiontime 30分钟*/
                sessionTime = "30";
            }
            mapRedis.put("sessiontime",sessionTime);
            redisClient.setObjectByTime(bussinessRedisKey,mapRedis,Integer.parseInt(sessionTime)*60);
            map.clear();
            map.put("status",1);
        }
        if(map.get("status")=="0"){
            writeErrorJson(response,map);
        }else{
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
        String redis_key = request.getParameter("redis_key");/*单点登录key值为jsessionid*/
        log.info("client_id:"+client_id+"access_token:"+access_token+"service_name:"+service_name+"redis_key:"+redis_key);
        Map<String ,Object> map=new HashMap<String,Object>();
        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,service_name,redis_key))){
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
            /*业务数据rediskey*/
            String bussinessRedisKey = client_id+RedisConstant.USER_SERVICE+redis_key;
            Object bussinessRedisValue = null;
            /*判断是否存在key 否则是报错*/
            if(redisClient.existKey(bussinessRedisKey)){
                bussinessRedisValue = redisClient.getObject(bussinessRedisKey);
            }
            map.clear();
            map.put("status",1);
            map.put("redisValue",bussinessRedisValue);
            if(null != bussinessRedisValue){
                int sessionTime = 30;/*默认sessionTime 30分钟*/
                net.sf.json.JSONObject jsonObjectBussiness= net.sf.json.JSONObject.fromObject(bussinessRedisValue);
                if(null != jsonObjectBussiness && jsonObjectBussiness.size()>0){
                    /*获取用户自定义sessiontime*/
                    Object sessionBussinessTime = jsonObjectBussiness.get("sessiontime");
                    if(null != sessionBussinessTime && "" != sessionBussinessTime && "0" != sessionBussinessTime){
                        sessionTime = Integer.parseInt(sessionBussinessTime.toString());
                    }
                }
                /*当redis value 无数据 则不刷新时间*/
                redisClient.setObjectByTime(bussinessRedisKey,bussinessRedisValue,sessionTime*60);
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
