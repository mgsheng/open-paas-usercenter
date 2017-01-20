package cn.com.open.openpaas.userservice.web.api.user;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 *  用户登录接口(通过用户名-密码)
 */
@Controller
@RequestMapping("/user/")
public class UserCenterLoginController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterLoginController.class);
	
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private AppService appService;
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserCacheService userCacheService;
	 final static String  SEPARATOR = "&";
	 @Autowired
	 private UserserviceDev userserviceDev;
	    /**
	     * 用户登录接口(通过用户名-密码)
	     * @return Json
	     */
	    @RequestMapping("userCenterPassword")
	    public void userCenterPassword(HttpServletRequest request,HttpServletResponse response) {
	    	long startTime=System.currentTimeMillis();
	    	String client_id=request.getParameter("client_id");
	    	String access_token=request.getParameter("access_token");
	        String grant_type = request.getParameter("grant_type");
	        String scope = request.getParameter("scope");
	    	String username=request.getParameter("username");
	    	String password=request.getParameter("password");
	    	String pwdtype=request.getParameter("pwdtype");
	    	String aesPassword="";
	    	String oldPassword = password;
	    	log.info("client_id:"+client_id+"access_token:"+access_token+"grant_type:"+grant_type+"scope:"+scope+"password:"+password+"username:"+username+"Password:"+password);
	    	log.info("signature:"+request.getParameter("signature")+"timestamp:"+request.getParameter("timestamp")+"signatureNonce:"+request.getParameter("signatureNonce"));
	    	Map<String ,Object> map=new HashMap<String,Object>();
	        if(!paraMandatoryCheck(Arrays.asList(client_id,access_token,grant_type,scope,username,password))){
	            paraMandaChkAndReturn(5, response,"必传参数中有空值");
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
					paraMandaChkAndReturn(7, response,"认证失败");
					return;
				}
					try { 
						if(password!=null&&password.length()>0)
						{
							password=AESUtil.decrypt(password, app.getAppsecret()).trim();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}	
				//检查异常表中是否存在（不存在也不会有错误信息，再继续按普通用户查）
				boolean isCache = false;
				boolean isUser = false;
				UserCache userCache = null;
				User user = null;
				Map<String,String> errorMap = new LinkedHashMap<String, String>();
				//检查用户表中是否存在
				user=checkUsername(username,userService);
				if(user==null){
					errorMap.put("status", "0");
					errorMap.put("error_code", "3");
					errorMap.put("errMsg", "用户名不存在");
				}else if(user!=null&&!user.checkPasswodByAes(password,userserviceDev.getAes_userCenter_key(),pwdtype)){
					User user1=checkUserByphone(username,userService);
					if(user1==null){
						errorMap.put("status", "0");
						errorMap.put("error_code", "4");
						errorMap.put("errMsg", "密码错误");
					}else if(!user1.checkPasswodByAes(password,userserviceDev.getAes_userCenter_key(),pwdtype)){
						errorMap.put("status", "0");
						errorMap.put("error_code", "4");
						errorMap.put("errMsg", "密码错误");
					}else{
						user=user1;
						isUser = true;
					}
				}
				//暂不判断
				else if(User.STATUS_DISABLED.equals(user.userState())){
					errorMap.put("status", "0");
					errorMap.put("error_code", "6");//用户已被封停
					errorMap.put("errMsg", "用户已被封停");
				}
				else{
					isUser = true;
				}
				//正式表中不存在，查询userCache表
				if(!isUser){
					Object userCacheInfoObj = redisClient.getObject(RedisConstant.USER_CACHE_INFO+username);
					//存在缓存信息，用户存在于用户异常表中
					if(userCacheInfoObj!=null){
						userCache = checkCacheUsername(username,userCacheService,app.getId());
						if(userCache == null){
							//不存在则什么都不做，保留useraccount的错误信息
						}
						else if(userCache!=null){
							//密码不正确
							if(!userCache.checkPasswodByAes(password,userserviceDev.getAes_userCenter_key(),pwdtype)){
								UserCache userCache1=checkCacheUserByphone(username,app.getId(),userCacheService);
								if(userCache1==null){
									errorMap.put("status", "0");
									errorMap.put("error_code", "4");
									errorMap.put("errMsg", "密码错误");
								}else if(userCache1!=null&&!userCache1.checkPasswodByAes(password,userserviceDev.getAes_userCenter_key(),pwdtype)){
									errorMap.put("status", "0");
									errorMap.put("error_code", "4");
									errorMap.put("errMsg", "密码错误");
								}else{
									userCache=userCache1;
									isCache = true;
								}
							}
							//用户被停用
							//暂不判断
							else if(User.STATUS_DISABLED.equals(userCache.userState())){
								errorMap.put("status", "0");
								errorMap.put("error_code", "6");//用户已被封停
								errorMap.put("errMsg", "用户已被封停");
							}
							else{
								isCache = true;
							}
					}
				  }
				}
				//存在用户数据
				if(isCache ||isUser){
					boolean isLink = false;//是否有其他用户关联
					String pid = "";
					boolean pwdTimeout = true;//true没超时 false超时
				
					if(isCache){
						map.put("userName", userCache.username());
						map.put("cardNo", userCache.cardNo()==null?"":userCache.cardNo());
						map.put("guid", userCache.guid()==null?"":userCache.guid());
						map.put("phone", userCache.phone()==null?"":userCache.phone());
						map.put("email", userCache.email()==null?"":userCache.email());
						//TODO 补充密码超时验证
						pwdTimeout = true;
						isLink = userCache.defaultUser();
						pid = userCache.id()+"";
						userCache.lastLoginTime(new Date());
						try {
							aesPassword=AESUtil.encrypt(password, userserviceDev.getAes_userCenter_key());
						} catch (Exception e1) {
							log.info("aes加密出错："+password);
							e1.printStackTrace();
						}
						userCache.setAesPassword(aesPassword);
						userCacheService.updateUserCache(userCache);
					}
					//返回基本信息
					else if(isUser){
						if(user.getAesPassword()==null||"".equals(user.getAesPassword())){
							try {
								aesPassword=AESUtil.encrypt(password, userserviceDev.getAes_userCenter_key());
							} catch (Exception e1) {
								log.info("aes加密出错："+password);
								e1.printStackTrace();
							}
							userService.updateAesPwdById(user.getId(),aesPassword);
						}
						map.put("userName", user.getUsername());
						map.put("guid", user.guid()==null?"":user.guid());
						map.put("phone", user.phone()==null?"":user.phone());
						map.put("email", user.email()==null?"":user.email());
						pwdTimeout = true;
						if(null != user.getUpdatePwdTime()){
							Long detectPasswordTime = Long.valueOf(userserviceDev.getDetect_password_time());
							if(DateTools.timeDiffCurr(user.getUpdatePwdTime().getTime()) > detectPasswordTime){
								pwdTimeout = false;
							}
				    	}
						isLink = user.defaultUser();
						//存在关联，则判断该账号是子账号还是父账号
						if(isLink){
							//存在Pid则证明该User为子账号
							if(StringUtils.isNotBlank(user.getPid()) && !user.getPid().equals("0")){
								pid = user.getPid();
							}
							//不存在Pid则证明该User为父账号
							else{
								pid = user.getId()+"";
							}
						}
						//不存在关联，则该账号为父账号
						else{
							pid = user.getId()+"";
						}
						if(user.userState()==null || ("0").equals(user.userState())){
		    				user.userState("1");
						}
						//用户频繁登录不做更新，仅限10分钟之内用户登录
						if(user.getLastLoginTime()==null||user.getLastLoginTime().getTime()<(new Date().getTime()-600000))
						{
							userService.updateUserLastLoginTimeById(user.getId(),new Date());
							//改为异步，若想改回同步直接注释掉，并释放上面一行即可
//							try {
//								Thread thread = new Thread(new UserUpdatePwdThread(user.getId(), userService));
//								thread.run();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
						}
					}
					boolean pwdRole = true;//true符合规则 false不符合规则
					if(StringTool.isNumeric(password) || StringTool.judgeInputNotNo(password)==1){
						pwdRole=false;
					}
					String pwdRoleCode = "";
					String pwdRoleMsg = "";
					//全部通过
					if(pwdTimeout && pwdRole){
						pwdRoleCode = "0";
					}
					//规则不通过
					else if(pwdTimeout && !pwdRole){
						pwdRoleCode = "1";
						pwdRoleMsg="您的密码强度不符合验证规则";
					}
					//超时不通过
					else if(!pwdTimeout && pwdRole){
						pwdRoleCode = "2";
						pwdRoleMsg = "您的密码已经6个月没有修改过了";
					}
					//全不通过
					else{
						pwdRoleCode = "3";
						pwdRoleMsg = "您的密码强度不符合验证规则,且您的密码已经6个月没有修改过了";
					}
					map.put("pwdRule",pwdRoleCode);
					map.put("pwdMsg",pwdRoleMsg);
					
					List<Map<String,String>> allInfoList= new LinkedList<Map<String,String>>();;
					List<Map<String,String>> infoList= null;
	    		    Map<String,String> appMap = null;
	    		    boolean flag=false;
	    		    //查询父节点的app信息
					infoList=infoListbyPassWord(Integer.parseInt(pid), app, isCache);
					//查看用户可以访问应用是否包含当前应用
					appMap = new HashMap<String,String>();
					for(int i=0;i<infoList.size();i++){
	    		    	appMap=infoList.get(i);
	    		    	if(appMap.get("appId").equals(app.getId()+"")){
//	    		    		UserSocial usersocail = userService.findSocailByAppUserId(Integer.parseInt(appMap.get("appUId")));
//	    					map.put("qq","");
//	    					map.put("weixin","");
	    		    		flag=true;
	    		    	}
	    		    }
					//添加App应用集合，且当前用户在各App中sourceId
					allInfoList.addAll(infoList);
					//只有正常用户并且有关联才查询子节点
					if(isUser && isLink){
						//根据父账号ID查询存在的所有子账号id
						List<User> userList = userService.findByPid(pid);
						if(userList!=null && userList.size()>0){
							//遍历子节点的app信息
							for(User userTemp:userList){
								infoList=infoListbyPassWord(userTemp.getId(), app, isCache);
								if(infoList==null || infoList.size()==0){
									continue;
								}
								//查看用户可以访问应用是否包含当前应用
								appMap = new HashMap<String,String>();
								for(int i=0;i<infoList.size();i++){
				    		    	appMap=infoList.get(i);
				    		    	if(appMap.get("appId").equals(app.getId()+"")){
//				    		    		UserSocial usersocail = userService.findSocailByAppUserId(Integer.parseInt(appMap.get("appUId")));
//				    					map.put("qq","");
//				    					map.put("weixin","");
				    		    		flag=true;
				    		    	}
				    		    }
								//添加App应用集合，且当前用户在各App中sourceId 
								allInfoList.addAll(infoList);
							}
						}
					}
					//补充用户中心的信息（暂不支持异常用户表登录）
					/*if(isUser){
						AppUser au = new AppUser(Common.APPID, user.getId(),String.valueOf(user.getId()));
			    		App appUc=	(App) redisClient.getObject(RedisConstant.APP_INFO+au.appId());
			    		if(appUc==null){
			    			appUc=appService.findById(au.appId());
			    			 redisClient.setObject(RedisConstant.APP_INFO+au.appId(), appUc);
			    		}
			    		Map<String,String> mapUc=new HashMap<String,String>();
			    		mapUc.put("callbackUrl", appService.findCallbackUrl(appUc, au));
			    		mapUc.put("name", appUc.getName());
			    		mapUc.put("icon", appUc.getIcon());
			    		mapUc.put("sourceId", au.sourceId());
			    		mapUc.put("appId", au.appId().toString());
			    		allInfoList.add(mapUc);
					}*/
	    		    if(!flag){
	    		    	map.put("status", "2");
	    		    }
	    		    map.put("infoList", allInfoList);
	    		    //将登录成功用户存入session中
	    		    HttpSession session = request.getSession();
	    		    /*session.setAttribute(userserviceDev.getSingle_sign_user(), user);*/
					map.put("jsessionId", session.getId());
					/*写入redis*/
					String localRedisKey = RedisConstant.USER_SERVICE_JSESSIONID+session.getId();
					String bussinessSessionId = redisClient.getString(username);
					Map<String,Object> redisMap = new HashMap<String, Object>();
					redisMap.put("status",1);
					redisMap.put("info","有效");
					redisMap.put("guid",map.get("guid"));
					redisMap.put("user",JSON.toJSONString(user));
					redisClient.setObject(localRedisKey,redisMap);
					if(null != bussinessSessionId && "" != bussinessSessionId){
						/*业务数据更新为 被踢下线*/
						Map<String,Object> redisMaps = new HashMap<String, Object>();
						String bussinessRedisKey = client_id+RedisConstant.USER_SERVICE+bussinessSessionId;
						net.sf.json.JSONObject jsonObject= net.sf.json.JSONObject.fromObject(redisClient.getObject(bussinessRedisKey));
						redisMaps.put("status",3);
						redisMaps.put("info","被踢下线");
						redisMaps.put("businessData",jsonObject.get("businessData"));
						redisClient.setObject(bussinessRedisKey, JSON.toJSONString(redisMaps));
					}
					redisClient.setObject(username,session.getId());
				}
				//没有符合条件的用户，则返回错误消息
				else{
					map.clear();
					map.putAll(errorMap);
				}
			}
	    	if(map.get("status")=="0"){
	    		writeErrorJson(response,map);
	    	}else{
	    		writeSuccessJson(response,map);
	    	}
	    	OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
	        return;
	    }	
    /**
     * 登录接口中返回应用列表
     * @param userId
     * @return
     */
    public List<Map<String,String>> infoList(Integer userId){
    	List<Map<String,String>> infoList=new ArrayList<Map<String,String>>();
	    List<AppUser> appUsers=appUserService.findByUserId(userId);
	    if(appUsers!=null){
	    	for(AppUser au:appUsers){
	    		Map<String,String> map1=new HashMap<String,String>();
	    		App app1=appService.findById(au.appId());
	    		if(app1!=null){
//		    		map1.put("callbackUrl", app1.getWebServerRedirectUri());
	    			map1.put("callbackUrl", appService.findCallbackUrl(app1, au));
		    		map1.put("name", app1.getName());
		    		map1.put("icon", app1.getIcon());
	    		}
	    		map1.put("sourceId", au.sourceId());
	    		//map1.put("sourceId", au.appId().toString());
	    		infoList.add(map1);
	    	}
	    }
    	return infoList;
    }
    /**
     * 登录接口中返回应用列表
     * @param userId
     * @param app
     * @param isCache	是否来自缓存表
     * @return
     */
    public List<Map<String,String>> infoListbyPassWord(Integer userId, App app, boolean isCache){
    	List<Map<String,String>> infoList=new ArrayList<Map<String,String>>();
	    //List<AppUser> appUsers=appUserService.findByUserId(userId);
	    List<AppUser> appUsers=appUserService.findAuthoritiesAppsByUserId(userId,app.getAppAuthorities(),app.getId(), isCache);
	    if(appUsers!=null){
	    	for(AppUser au:appUsers){
	    		Map<String,String> map1=new HashMap<String,String>();
	    		getAppMap(map1, au, app);
	    		infoList.add(map1);
	    	}
	    }
    	return infoList;
    }
    /**
     * 获取App的Map信息
     * @param map
     * @param au
     * @param app
     */
    private void getAppMap(Map<String,String> map, AppUser au, App app){
		App app1=	(App) redisClient.getObject(RedisConstant.APP_INFO+au.appId());
		if(app1==null){
			 app1=appService.findById(au.appId());
			 redisClient.setObject(RedisConstant.APP_INFO+au.appId(), app1);
		}
		if(app1!=null){
//    		map.put("callbackUrl", app1.getWebServerRedirectUri());
			map.put("callbackUrl", appService.findCallbackUrl(app1, au));
			map.put("name", app1.getName());
			map.put("icon", app1.getIcon());
			
			if(au.appId().intValue()==app.getId().intValue()){
				//暂不更新app_user登录时间和登录次数
//				au.lastloginTime(new Date().getTime());
//				au.loginTimes(au.loginTimes()==null?1:au.loginTimes()+1);
//				appUserService.updateAppUser(au); 
				map.put("appUId", au.appUid()+"");
			}
		}
		map.put("sourceId", au.sourceId());
		map.put("appId", au.appId().toString());
    }
  
}