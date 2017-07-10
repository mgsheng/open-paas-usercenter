package cn.com.open.expservice.app.redis;
/**
 * redis 存储开开头
 * @author admin
 *
 */
public class RedisConstant {
 public static final String APP_INFO="appInfo_";
 public static final String USER_NAME_CHECK="userNameCheck_";
 public static final String USER_CACHE_INFO="userCacheInfo_";//存储方式：key:userCacheInfo_username value:""

 /*业务key clientid+USER_SERVICE+jsessionid*/
 public static final String USER_SERVICE="_userService_";
 /*本地key userservice+jsessionid+jsessionvalue*/
 public static final String USER_SERVICE_JSESSIONID="userService_jsessionid_";
 /*本地cookie redis 名称*/
 public static final String USER_SERVICE_COOKIENAME="cookie_username";
 
 public static final String SSO_USER_CHECK="sso_user_check_";
}
