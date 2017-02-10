package cn.com.open.openpaas.userservice.web.api.user;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.common.model.Common;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.thread.UserLoginThread;
import cn.com.open.openpaas.userservice.app.tools.DESUtil;
import cn.com.open.openpaas.userservice.app.tools.HttpTools;
import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserLoadDetailService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;

/**
 * 
 */
@Controller
public class BaseDevUserController {

	@Autowired
	private AppService appService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLoadDetailService userLoadDetailService;
    @Autowired
   	private RedisClientTemplate redisClient;
    
    @Autowired
    private AppUserService appUserService;
    @Value("#{properties['server-host']}")
    private String serverHost;
    
    /**
     * 获取当前登录Session（该方法用于json请求）
     * @param request
     * @return
     */
    protected User checkUser(HttpServletRequest request) {
    	return checkUser(request,null);
    }
    
    protected String redirectIndex(HttpServletRequest request,HttpServletResponse response) {
    	try {
    		String host = serverHost;
    		if(StringUtils.isBlank(host)){
    			host = request.getContextPath() + "/dev/user/no_login";
    		}
    		response.sendRedirect(host);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
//    /**
//     * （旧）获取当前登录Session（该方法用于页面请求）
//     * MD5加密方式
//     * @param request
//     * @param model
//     * @return
//     */
//    protected User checkUser(HttpServletRequest request,Map<String,Object> model) {
//    	//获取URL登录参数
//    	String sourceId = request.getParameter("sourceId");
//    	String salt = request.getParameter("salt");
//    	String secret = request.getParameter("secret");
//    	//有登录参数
//    	if(
//    			StringUtils.isNotBlank(sourceId)
//    			&& StringUtils.isNotBlank(salt)
//    			&& StringUtils.isNotBlank(secret)
//    			){
//    		//校验登录参数是否正确
//    		App app = appService.findById(Common.APPID);
//    		String newSecret = MD5.Md5(sourceId+salt+app.getAppkey()+app.getAppsecret());
//    		//验证正确
//    		if(newSecret.equals(secret)){
//    			//检查是否有现存session
//    			//有session，则检查和参数是否是一个人
//    			if(request.getSession().getAttribute(getUserInfoSessionKey(request))!=null){
//    				//是一个人则跳过
//    				User user = (User)request.getSession().getAttribute(getUserInfoSessionKey(request));
//    				if(user.getId().toString().equals(sourceId)){
//    					setModelMap(model, user);
//    					return user;
//    				}
//                	//不是一个人则改为当前登录人
//    				else{
//    					user = userService.findUserById(Integer.parseInt(sourceId));
//    					if(user!=null){
//    						saveUserSession(request, user);
//    					}
//    					setModelMap(model, user);
//    					return user;
//    				}
//    			}
//    			//-没有session，则创建session
//    			else{
//    				if(request.getAttribute("isLogin") == null || !request.getAttribute("isLogin").equals("true")){
//    		    		return null;
//    		    	}
//    				User user = userService.findUserById(Integer.parseInt(sourceId));
//					if(user!=null){
//						saveUserSession(request, user);
//					}
//					setModelMap(model, user);
//					return user;
//    			}
//    		}
//    		//验证失败，返回无效
//    		else{
//    			return null;
//    		}
//    	}
//    	//没有登录参数
//    	else{
//    		//-检查是否有现存session
//    		if(request.getSession().getAttribute(getUserInfoSessionKey(request))!=null){
//    			//-有session，返回当前人
//				User user = (User)request.getSession().getAttribute(getUserInfoSessionKey(request));
//				setModelMap(model, user);
//				return user;
//			}
//    		//-没有session，返回null
//    		else{
//    			return null;
//    		}
//    	}
//	}
    
    /**
     * 获取当前登录Session（该方法用于页面请求）
     * DES加密方式
     * @param request
     * @param model
     * @return
     */
    protected User checkUser(HttpServletRequest request,Map<String,Object> model) {
    	//获取URL登录参数
    	String secret = request.getParameter("secret");
    	//有登录参数
    	if(
    			StringUtils.isNotBlank(secret)
    			){
    		App app=(App) redisClient.getObject(RedisConstant.APP_INFO+Common.APPID);
    		if(app==null){
    			app=appService.findById(Common.APPID);
    			redisClient.setObject(RedisConstant.APP_INFO+Common.APPID, app);
    		}
    		if(app==null){
    			//用户中心App不存在
    			return null;
    		}
    		if(!checkLoginParams(app, secret)){
    			//请求不合法
    			return null;
    		}
    		//校验登录参数是否正确
    		String secretDecrypt = "";
        	try {
    			secretDecrypt = DESUtil.decrypt(secret, app.getAppsecret().substring(0,8));
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
        	if(StringUtils.isBlank(secretDecrypt)){
        		//参数解密错误
        		return null;
        	}
        	String[] params = secretDecrypt.split("#");
        	if(params.length!=3){
        		//参数解密错误
        		return null;
        	}
        	String sourceId = params[0];
        	String time = params[1];
        	String appKey = params[2];
        	AppUser appUser=null;
        	if(null!=sourceId||!"".equals(sourceId.trim())){
        		 appUser=appUserService.findByCidSid(Common.APPID, sourceId);
        	}
        	
    		//验证正确
			//检查是否有现存session
			//有session，则检查和参数是否是一个人
			if(request.getSession().getAttribute(getUserInfoSessionKey(request))!=null){
				//是一个人则跳过
				User user = (User)request.getSession().getAttribute(getUserInfoSessionKey(request));
				if(user.getId().toString().equals(appUser.userId())){
					setModelMap(model, user);
					return user;
				}
            	//不是一个人则改为当前登录人
				else{
					user = userService.findUserById(appUser.userId());
					if(user!=null){
						saveUserSession(request, user);
					}
					setModelMap(model, user);
					return user;
				}
			}
			//-没有session，则创建session
			else{
				if(request.getAttribute("isLogin") == null || !request.getAttribute("isLogin").equals("true")){
		    		return null;
		    	}
				User user = userService.findUserById(appUser.userId());
				if(user!=null){
					saveUserSession(request, user);
				}
				setModelMap(model, user);
				return user;
			}
    	}
    	//没有登录参数
    	else{
    		//-检查是否有现存session
    		if(request.getSession().getAttribute(getUserInfoSessionKey(request))!=null){
    			//-有session，返回当前人
				User user = (User)request.getSession().getAttribute(getUserInfoSessionKey(request));
				setModelMap(model, user);
				return user;
			}
    		//-没有session，返回null
    		else{
    			return null;
    		}
    	}
	}
    
    /**
     * 用户登出
     * @param request
     */
    public void logoutUser(HttpServletRequest request){
    	request.getSession().removeAttribute(getUserInfoSessionKey(request));
    }
    
    /**
     * 记录用户session
     * @param request
     * @param user
     */
    private void saveUserSession(HttpServletRequest request, User user){
    	if(user==null){
    		return;
    	}
    	request.getSession().setAttribute(getUserInfoSessionKey(request), user);
    	//异步执行用户登录信息（避免影响正常登陆，异常处理可跳过）
    	try {
			Thread thread = new Thread(new UserLoginThread(user, request,userLoadDetailService));
			thread.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 验证登录参数是否合法
     * @param app
     * @param secret
     * @return
     */
    private boolean checkLoginParams(App app, String secret){
    	//请求获取access_token
    	Map<String,String> parameters = new LinkedHashMap<String, String>();
    	parameters.put("client_id", app.getAppkey());
    	parameters.put("client_secret", app.getAppsecret());
    	parameters.put("grant_type", "client_credentials");
    	parameters.put("scope", app.getScope());
    	String content = HttpTools.doGet(serverHost+"oauth/token", parameters, "utf8");
    	if(content==null){
    		return false;
    	}
    	JSONObject obj = JSONObject.fromObject(content.toString());
    	//请求验证secret有效性
    	parameters = new LinkedHashMap<String, String>();
    	parameters.put("access_token", obj.getString("access_token"));
    	parameters.put("client_id", app.getAppkey());
    	parameters.put("secret", secret);
    	content = HttpTools.doGet(PropertiesTool.getAppPropertieByKey("server-host")+"/user/validateLogin", parameters, "utf8");
    	if(content==null){
    		return false;
    	}
    	obj = JSONObject.fromObject(content.toString());
    	//验证请求结果是否正确
    	if(obj.getString("status").equals("1")){
    		return true;
    	}
    	return false;
    }
    
    /**
     * 给页面附加返回值
     * @param model
     * @param user
     */
    private void setModelMap(Map<String,Object> model,User user){
    	if(model!=null && user!=null){
    		if(StringUtils.isNotBlank(user.getUsername())){
    			model.put("currentUsername", user.getUsername());
    		}
    		else{
    			model.put("currentUsername", "");
    		}
		}
    }
    
    /**
     * 获取UserInfo的SessionKey
     * @param request
     * @return
     */
    public String getUserInfoSessionKey(HttpServletRequest request){
//    	return "userInfo_"+request.getSession().getId();
    	return "userInfo";
    }
    
}