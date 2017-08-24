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
import cn.com.open.openpaas.userservice.web.MessageGZIP;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	 @Value("#{properties['app.localhost.url']}")
	 private String serverHost;
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
            String sessionTime=request.getParameter("session_time");/*有效时间，默认是分钟，如果为空则默认30分钟*/
            String platform=request.getParameter("platform");
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
	        //判断是否锁定用户
	        String  frozenLoginInfo = (String) redisClient.getObject(RedisConstant.USER_SERVICE_FORZENLOGIN+app.getId()+"_"+username);
    		if(!nullEmptyBlankJudge(frozenLoginInfo)){
    			JSONObject  validateJson=JSONObject.parseObject(frozenLoginInfo);
    			long timeSub= DateTools.timeSub(DateTools.dateToString(new Date(), DateTools.FORMAT_ONE),validateJson.get("frozenTime").toString());
    			if((int)timeSub>0){
    				long c = Math.abs(timeSub)/60;
    				map.clear();
    			    map.put("status", "0");
    			    map.put("error_code", 8);
    			    map.put("frozenTime",String.valueOf(c));
    			    map.put("faliureTimes",app.getLoginFaliureTime());
    			    map.put("tryTimes",0);
    			    map.put("errMsg", "您的账号已经锁定请"+c+"分钟后在进行尝试登陆！");
    				paraMandaChkAndReturn(response,map);
    	            return;
    			}
    		}
	        //判断是否在规定时间内超过登陆失败次数
	        String  validateInfo = (String) redisClient.getObject(RedisConstant.USER_SERVICE_VALIDATELOGIN+app.getId()+"_"+username);
/*    		if(!nullEmptyBlankJudge(validateInfo)){
    			JSONObject  validateJson=JSONObject.parseObject(validateInfo);
    			long timeSub= DateTools.timeSub(validateJson.get("firstLoginTime").toString(),DateTools.dateToString(new Date(), DateTools.FORMAT_ONE));
    			int faliureTimes=validateJson.getIntValue("faliureTimes");
    			if((int)timeSub>0&&faliureTimes==app.getLoginFaliureTime()){
    				map.clear();
    			    map.put("status", "0");
    			    map.put("error_code", 8);
    			    map.put("faliureTimes",app.getLoginFaliureTime());
    			    map.put("tryTimes",0);
    			    map.put("errMsg", "您的账号"+app.getLoginValidateTime()+"分钟内连续登陆失败"+app.getLoginFaliureTime()+"次,请"+app.getLoginFrozenTime()+"分钟后再尝试！");
    				paraMandaChkAndReturn(response,map);
    	            return;
    			}
    		}
*/			map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
			if(map.get("status").equals("1")){
				if(nullEmptyBlankJudge(platform)){
					platform="1";
				}
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
				if(nullEmptyBlankJudge(pwdtype)){
					pwdtype="MD5";
				}
				Object userCacheInfoObj = redisClient.getObject(RedisConstant.USER_CACHE_INFO+username);
				//存在缓存信息，用户存在于用户异常表中
				if(userCacheInfoObj!=null ){
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
				if(!isCache){
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
						if(userCache.getUpdatePwdTime()!=null){
							  map.put("updatePwdTime", DateTools.dateToString(userCache.getUpdatePwdTime(), DateTools.FORMAT_ONE));	
							}else{
								map.put("updatePwdTime", "");	
							}
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
						if(user.getUpdatePwdTime()!=null){
						  map.put("updatePwdTime", DateTools.dateToString(user.getUpdatePwdTime(), DateTools.FORMAT_ONE));	
						}else{
							map.put("updatePwdTime", "");	
						}
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
					infoList=infoListbyPassWord(Integer.parseInt(pid), app, isCache,platform);
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
								infoList=infoListbyPassWord(userTemp.getId(), app, isCache,platform);
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
					Object bussinessSessionId = null;
					/*username 在redis 中的名称 RedisConstant.SSO_USER_CHECK+username*/
					String redisUserName = RedisConstant.SSO_USER_CHECK+username;
					/*是否存在 存储sessionid的 redisUserName*/
					if(redisClient.existKey(redisUserName)){
						bussinessSessionId = redisClient.getString(redisUserName);
					}
					Map<String,Object> localRedisMap = new HashMap<String, Object>();
					localRedisMap.put("status",1);
					localRedisMap.put("info","有效");
					localRedisMap.put("guid",map.get("guid"));
					localRedisMap.put("user",user);
					if(null == sessionTime || "".equals(sessionTime)   || "0".equals(sessionTime)||"null".equals(sessionTime) ){
					    /*默认sessiontime 30分钟*/
                        sessionTime = userserviceDev.getRedisExpireTime();
                    }
					localRedisMap.put("sessiontime",sessionTime);
                    redisClient.setStringByTime(localRedisKey, MessageGZIP.returnGzipString(JSON.toJSONString(localRedisMap)),Integer.parseInt(sessionTime)*60);
					if(null != bussinessSessionId && "" != bussinessSessionId){
						/*业务数据更新为 被踢下线*/
						Map<String,Object> businessRedisMaps = new HashMap<String, Object>();
						String bussinessRedisKey = client_id+RedisConstant.USER_SERVICE+bussinessSessionId;
						Object businessRedisValue = null;
						/*是否存在业务数据的key*/
						if(redisClient.existKey(bussinessRedisKey)){
							businessRedisValue = redisClient.getObject(bussinessRedisKey);
						}
						if(null != businessRedisValue){
							net.sf.json.JSONObject jsonObjectBussiness= net.sf.json.JSONObject.fromObject(businessRedisValue);
							if(null != jsonObjectBussiness && jsonObjectBussiness.size()>0){
								businessRedisMaps.put("status",3);
								businessRedisMaps.put("info","被踢下线");
								businessRedisMaps.put("businessData",jsonObjectBussiness.get("businessData"));
								businessRedisMaps.put("sessiontime",jsonObjectBussiness.get("sessiontime"));
								redisClient.setStringByTime(bussinessRedisKey, MessageGZIP.returnGzipString(JSON.toJSONString(businessRedisMaps)),Integer.parseInt(sessionTime)*60);
							}
						}
					}
					redisClient.setStringByTime(redisUserName,session.getId(),Integer.parseInt(sessionTime)*60);
				}
				//没有符合条件的用户，则返回错误消息
				else{
					map.clear();
					map.putAll(errorMap);
				}
			}
	    	if(map.get("status")=="0"){
	    		if(!nullEmptyBlankJudge(validateInfo)){
	    			JSONObject  validateJson=JSONObject.parseObject(validateInfo);
	    			Map<String ,Object> validateLoginMap=new HashMap<String,Object>();	
	    			validateLoginMap.put("appId",validateJson.get("appId"));
	    			validateLoginMap.put("userName", validateJson.get("userName"));
	    			validateLoginMap.put("firstLoginTime", validateJson.get("firstLoginTime"));
	    			validateLoginMap.put("ip", validateJson.get("ip"));
	    			int tryTimes=validateJson.getIntValue("tryTimes");
	    			if(tryTimes>1){
	    				//判断密码登陆次数是否超过可尝试次数
	    				int faliureTimes=validateJson.getIntValue("faliureTimes");
	    				long timeSub= DateTools.timeSub(validateJson.get("firstLoginTime").toString(),DateTools.dateToString(new Date(), DateTools.FORMAT_ONE));
	    				int c =(int) Math.abs(timeSub)/60;
		    			if(c<app.getLoginValidateTime()){
		    			  validateLoginMap.put("lastLoginTime",DateTools.dateToString(new Date(), DateTools.FORMAT_ONE));
		    			  validateLoginMap.put("faliureTimes", faliureTimes+1);
		    			  validateLoginMap.put("tryTimes",app.getLoginFaliureTime()-(faliureTimes+1));
			    		  redisClient.setObjectByTime(RedisConstant.USER_SERVICE_VALIDATELOGIN+app.getId()+"_"+username, JSON.toJSONString(validateLoginMap),app.getLoginValidateTime()-c);
		    			}
		    			map.put("faliureTimes",faliureTimes+1);
		    			map.put("frozenTime","");
		    			map.put("tryTimes", app.getLoginFaliureTime()-(faliureTimes+1));
	    			}else if(tryTimes==1){
	    				//添加用户锁定信息
	    				Map<String ,Object> frozenLoginMap=new HashMap<String,Object>();
	    				frozenLoginMap.put("appId",validateJson.get("appId"));
	    				frozenLoginMap.put("userName", validateJson.get("userName"));
	    				frozenLoginMap.put("frozenTime", DateTools.getTimeByMinute(app.getLoginFrozenTime()));
		    			map.put("frozenTime", DateTools.getTimeByMinute(app.getLoginFrozenTime()));
		    			map.put("faliureTimes",app.getLoginFaliureTime());
		    			map.put("tryTimes",0);
	    				redisClient.setObjectByTime(RedisConstant.USER_SERVICE_FORZENLOGIN+app.getId()+"_"+username, JSON.toJSONString(frozenLoginMap),app.getLoginFrozenTime());
	    				redisClient.del(RedisConstant.USER_SERVICE_VALIDATELOGIN+app.getId()+"_"+username);
	    			}
	    		}else{
	    			//创建登录失败信息
	    			Map<String ,Object> validateLoginMap=new HashMap<String,Object>();	
	    			validateLoginMap.put("appId", app.getId());
	    			validateLoginMap.put("userName", username);
	    			validateLoginMap.put("firstLoginTime", DateTools.dateToString(new Date(), DateTools.FORMAT_ONE));
	    			validateLoginMap.put("lastLoginTime",DateTools.dateToString(new Date(), DateTools.FORMAT_ONE));
	    			validateLoginMap.put("ip", "");
	    			validateLoginMap.put("faliureTimes", 1);
	    			validateLoginMap.put("tryTimes", app.getLoginFaliureTime()-1);
	    			redisClient.setObjectByTime(RedisConstant.USER_SERVICE_VALIDATELOGIN+app.getId()+"_"+username, JSON.toJSONString(validateLoginMap),app.getLoginValidateTime());
	    			map.put("faliureTimes", 1);
	    			map.put("tryTimes", app.getLoginFaliureTime()-1);
	    		}
	    		writeErrorJson(response,map);
	    	}else{
	    		if(!nullEmptyBlankJudge(validateInfo)){
	    			redisClient.del(RedisConstant.USER_SERVICE_VALIDATELOGIN+app.getId()+"_"+username);
	    		}
	    		writeSuccessJson(response,map);
	    	}
	    	OauthControllerLog.log(startTime,username,oldPassword,app,map,userserviceDev);
	        return;
	    }	
    /**
     * 登录接口中返回应用列表
     * @param userId
     * @param app
     * @param isCache	是否来自缓存表
     * @return
     */
    public List<Map<String,String>> infoListbyPassWord(Integer userId, App app, boolean isCache,String platform){
    	List<Map<String,String>> infoList=new ArrayList<Map<String,String>>();
	    //List<AppUser> appUsers=appUserService.findByUserId(userId);
	    List<AppUser> appUsers=appUserService.findAuthoritiesAppsByUserId(userId,app.getAppAuthorities(),app.getId(), isCache);
	    if(appUsers!=null){
	    	for(AppUser au:appUsers){
	    		Map<String,String> map1=new HashMap<String,String>();
	    		getAppMap(map1, au, app,platform);
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
    private void getAppMap(Map<String,String> map, AppUser au, App app,String platform){
		App app1=	(App) redisClient.getObject(RedisConstant.APP_INFO+au.appId());
		if(app1==null){
			 app1=appService.findById(au.appId());
			 redisClient.setObject(RedisConstant.APP_INFO+au.appId(), app1);
		}
		if(app1!=null){
//    		map.put("callbackUrl", app1.getWebServerRedirectUri());
			map.put("callbackUrl", appService.findCallbackUrl(app1, au,serverHost,platform));
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