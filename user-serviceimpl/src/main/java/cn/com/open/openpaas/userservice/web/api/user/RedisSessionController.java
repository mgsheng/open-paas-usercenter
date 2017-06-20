package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.MessageGZIP;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于redis的单点登录
 */
@Controller
@RequestMapping("/redis/")
public class RedisSessionController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(RedisSessionController.class);

    @Autowired
    private AppService appService;
    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private RedisClientTemplate redisClient;
    @Autowired
    UserserviceDev userserviceDev;

    /**
     * redis保存接口
     * username：sessionid键值对存储到redis
     * 存储sessionid 相应数据库到redis
     * 根据username获取sessionid 判断是否存在session
     * 存在则踢下线，并将最新数据更新为有效数据
     *
     * @return Json
     */
    @RequestMapping(value = "saveRedis", method = RequestMethod.POST)
    public void saveRedis(HttpServletRequest request, HttpServletResponse response) {
        String client_id = request.getParameter("client_id");
        String access_token = request.getParameter("access_token");
        String service_name = request.getParameter("service_name");
        String redis_key = request.getParameter("redis_key");/*单点登录key值为jsessionid*/
        String redis_value = request.getParameter("redis_value");
        String sessionTime = request.getParameter("session_time");/*有效时间，默认是分钟，如果为空则默认30分钟*/
        log.info("client_id:" + client_id + "access_token:" + access_token + "service_name:" + service_name + "redis_key:" + redis_key + "redis_value:" + redis_value);
        Map<String, Object> map = new HashMap<String, Object>();
        if (!paraMandatoryCheck(Arrays.asList(client_id, access_token, service_name, redis_key, redis_value))) {
            paraMandaChkAndReturn(3, response, "必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + client_id);
        if (app == null) {
            app = appService.findIdByClientId(client_id);
            redisClient.setObject(RedisConstant.APP_INFO + client_id, app);
        }
        map = checkClientIdOrToken(client_id, access_token, app, tokenServices);
        if (map.get("status").equals("1")) {
            Boolean hmacSHA1Verification = OauthSignatureValidateHandler.validateSignature(request, app);
            if (!hmacSHA1Verification) {
                paraMandaChkAndReturn(4, response, "认证失败");
                return;
            }
            /*业务数据rediskey 单点登录格式为{”status”:1, ”info”:”有效”, ” businessData”:{业务数据}.}*/
            /*String bussinessRedisKey = client_id+RedisConstant.USER_SERVICE+redis_key;*/
            redis_value = MessageGZIP.returnGzipString(redis_value);
            if (null == sessionTime || "" == sessionTime || "0" == sessionTime) {
                /*默认sessiontime 30分钟*/
                sessionTime = userserviceDev.getRedisExpireTime();
            }
            redisClient.setStringByTime(redis_key, redis_value, Integer.parseInt(sessionTime) * 60);
            map.clear();
            map.put("status", 1);
        }
        if (map.get("status") == "0") {
            writeErrorJson(response, map);
        } else {
            writeSuccessJson(response, map);
        }
        return;
    }

    /**
     * Redis获取接口
     *
     * @return Json
     */
    @RequestMapping(value = "getRedis", method = RequestMethod.POST)
    public void getRedis(HttpServletRequest request, HttpServletResponse response) {
        String client_id = request.getParameter("client_id");
        String access_token = request.getParameter("access_token");
        String service_name = request.getParameter("service_name");
        String redis_key = request.getParameter("redis_key");/*单点登录key值为jsessionid*/
        String sessionTime = request.getParameter("session_time");/*有效时间，默认是分钟，如果为空则默认30分钟*/
        log.info("client_id:" + client_id + "access_token:" + access_token + "service_name:" + service_name + "redis_key:" + redis_key);
        Map<String, Object> map = new HashMap<String, Object>();
        if (!paraMandatoryCheck(Arrays.asList(client_id, access_token, service_name, redis_key))) {
            paraMandaChkAndReturn(3, response, "必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + client_id);
        if (app == null) {
            app = appService.findIdByClientId(client_id);
            redisClient.setObject(RedisConstant.APP_INFO + client_id, app);
        }
        map = checkClientIdOrToken(client_id, access_token, app, tokenServices);
        if (map.get("status").equals("1")) {
            Boolean hmacSHA1Verification = OauthSignatureValidateHandler.validateSignature(request, app);
            if (!hmacSHA1Verification) {
                paraMandaChkAndReturn(4, response, "认证失败");
                return;
            }
            /*业务数据rediskey*/
            /*String bussinessRedisKey = client_id+RedisConstant.USER_SERVICE+redis_key;*/
            String bussinessRedisValue = null;
            /*判断是否存在key 否则是报错*/
            if (redisClient.existKey(redis_key)) {
                bussinessRedisValue = MessageGZIP.returnGzipString(redisClient.getString(redis_key));
            }
            map.clear();
            map.put("status", 1);
            map.put("redisValue", bussinessRedisValue);
            if (null != bussinessRedisValue) {
                if (null == sessionTime || "" == sessionTime || "0" == sessionTime) {
			    /*默认sessiontime 30分钟*/
                    sessionTime = userserviceDev.getRedisExpireTime();
                }
                /*当redis value 无数据 则不刷新时间*/
                redisClient.setStringByTime(redis_key, bussinessRedisValue, Integer.parseInt(sessionTime) * 60);
            }
        }
        if (map.get("status") == "0") {
            writeErrorJson(response, map);
        } else {
            writeSuccessJson(response, map);
        }
        return;
    }
    /**
     * Redis获取接口
     *
     * @return Json
     */
    @RequestMapping(value = "delRedis", method = RequestMethod.POST)
    public void delRedis(HttpServletRequest request, HttpServletResponse response) {
        String client_id = request.getParameter("client_id");
        String access_token = request.getParameter("access_token");
        String redis_key = request.getParameter("redis_key");
        log.info("client_id:" + client_id + "access_token:" + access_token  + "redis_key:" + redis_key);
        Map<String, Object> map = new HashMap<String, Object>();
        if (!paraMandatoryCheck(Arrays.asList(client_id, access_token, redis_key))) {
            paraMandaChkAndReturn(3, response, "必传参数中有空值");
            return;
        }
        App app = (App) redisClient.getObject(RedisConstant.APP_INFO + client_id);
        if (app == null) {
            app = appService.findIdByClientId(client_id);
            redisClient.setObject(RedisConstant.APP_INFO + client_id, app);
        }
        map = checkClientIdOrToken(client_id, access_token, app, tokenServices);
        if (map.get("status").equals("1")) {
            Boolean hmacSHA1Verification = OauthSignatureValidateHandler.validateSignature(request, app);
            if (!hmacSHA1Verification) {
                paraMandaChkAndReturn(4, response, "认证失败");
                return;
            }
            map.clear();
            /*判断是否存在key 否则是报错*/
            if (redisClient.existKey(redis_key)) {
            	redisClient.del(redis_key);
                 map.put("status", 1);
            }
            else
            {
            	  map.put("status", 0);
            	  map.put("error_code", 5);
            	  map.put("errMsg", "redis key值不存在");
            }
        }
        if (map.get("status") == "0") {
            writeErrorJson(response, map);
        } else {
            writeSuccessJson(response, map);
        }
        return;
    }

}
