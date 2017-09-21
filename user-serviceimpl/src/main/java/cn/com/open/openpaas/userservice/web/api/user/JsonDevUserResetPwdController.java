package cn.com.open.openpaas.userservice.web.api.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.log.UserResetPwdControllerLog;
import cn.com.open.openpaas.userservice.app.logic.UserLogicService;
import cn.com.open.openpaas.userservice.app.thread.SendOesThread;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.Help_Encrypt;
import cn.com.open.openpaas.userservice.app.tools.HttpTools;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;
import cn.com.open.openpaas.userservice.app.user.model.UserDict;
import cn.com.open.openpaas.userservice.app.user.model.UserProblem;
import cn.com.open.openpaas.userservice.app.user.service.UserActivatedHisService;
import cn.com.open.openpaas.userservice.app.user.service.UserActivatedService;
import cn.com.open.openpaas.userservice.app.user.service.UserCacheService;
import cn.com.open.openpaas.userservice.app.user.service.UserDictService;
import cn.com.open.openpaas.userservice.app.user.service.UserProblemService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivatedHis;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;

/**
 * 
 */
@Controller
public class JsonDevUserResetPwdController extends BaseDevUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserLogicService userLogicService;
    
    @Autowired
    private UserProblemService userProblemService;
    
    @Autowired
    private UserDictService userDictService;
    
    @Autowired
    private UserActivatedService userActivatedService;
    
    @Autowired
    private UserActivatedHisService userActivatedHisService;
    @Autowired
	 private AppUserService appUserService;
    @Autowired
	 private AppService appService; 
    @Autowired
	private UserserviceDev userserviceDev;
    
	@Autowired
	private UserCacheService userCacheService;
    /**
     * 用户找回密码，发送邮件
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "/dev/user/send_reset_password_email", method = RequestMethod.POST)
	public void reset_pass_email(HttpServletRequest request,HttpServletResponse response,String email) {
    	boolean flag = false;
    	String errorCode = "";
    	if(StringUtils.isBlank(email)){
    		errorCode = "invalid_email";
    	}
    	else{
    		List<User> userList = userService.findByEmail(email);
        	if(userList != null && userList.size() == 1){
        		User user =userList.get(0);
    			//判断当前找回密码的用户邮箱验证状态，需为：已验证 状态
    			if (user.getEmailActivation()!=null && user.getEmailActivation() == User.ACTIVATION_YES) {
    				//发送找回密码邮件
    				boolean bool = userLogicService.sendResetPassWordEmail(user.getId(),user.getEmail(),UserActivated.USERTYPE_USER);
    				flag = bool;
    				if(!bool){
    					errorCode = "send_error";
    				}
    			} else {
    				errorCode = "email_activation_no";
    			}
        	}
        	else if(userList != null && userList.size() > 1){
        		errorCode = "invalid_email";
        	}
        	else{
    			errorCode = "no_email";
        	}
    	}
    	//跳转到发送成功后的页面，提示：当你收到邮件信息，请按照提示 进行密码重置
    	Map<String, Object> map = new LinkedHashMap<String, Object>();
    	map.put("flag",flag);
    	map.put("errorCode",errorCode);
		WebUtils.writeJsonToMap(response, map);
    }
    
    /**
     * 用户找回密码，发送手机短信
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "/dev/user/send_reset_password_phone", method = RequestMethod.POST)
	public void reset_pass_phone(HttpServletRequest request,HttpServletResponse response,
			String phone) {
    	boolean flag = false;
    	String errorCode = "";
    	if(StringUtils.isBlank(phone)){
    		errorCode = "invalid_phone";
    	}
    	else{
    		User user = userService.findByUsername(phone);
    		if(user == null){
    			//String desPhone=Help_Encrypt.encrypt(phone);
    			List<User> userList = userService.findByPhone(phone);
    			if(userList != null&&userList.size()>0){
            		user = userList.get(0);
    			}
    		}
    		if(user != null){
    			if(user.userState().equals("2")){
    				errorCode = "invalid_stat";
    			}else if(!user.userState().equals("2")&&(user.username()==null||"".equals(user.username()))){
    				errorCode = "invalid_name";
    			}else{
    				//发送短信找回密码验证码
        			flag = userLogicService.sendResetPassWordPhone(user.getId(),phone,UserActivated.USERTYPE_USER);
    				if(!flag){
    					errorCode = "send_error";
    				}	
    			}
				
    		}else{//不存在
    			UserCache usercache = userCacheService.findByUsername(phone);
        		if(usercache == null){
        			//String desPhone=Help_Encrypt.encrypt(phone);
        			List<UserCache> userCacheList = userCacheService.findByPhone(phone);
        			if(userCacheList != null&&userCacheList.size()>0){
        				usercache = userCacheList.get(0);
        			}
        		}
        		if(usercache != null){
        			if(usercache.userState().equals("2")){
        				errorCode = "invalid_stat";
        			}else if(!usercache.userState().equals("2")&&(usercache.username()==null||"".equals(usercache.username()))){
        				errorCode = "invalid_name";
        			}else{
        				//发送短信找回密码验证码
            			flag = userLogicService.sendResetPassWordPhone(usercache.id(),phone,UserActivated.USERTYPE_USER);
        				if(!flag){
        					errorCode = "send_error";
        				}	
        			}
    		}else{
    			errorCode = "invalid_phone";
    		}
    		}
    	}
    	Map<String, Object> map = new LinkedHashMap<String, Object>();
    	map.put("flag",flag);
    	map.put("errorCode",errorCode);
		WebUtils.writeJsonToMap(response, map);
    }
    
    /**
     * 验证用户找回密码手机验证码有效性
     * @param request
     * @param model
     * @param bool
     * @return
     */
    @RequestMapping(value = "/dev/user/activated_reset_password_phone")
	public void activated_reset_password_phone(HttpServletRequest request,HttpServletResponse response,String code,String phone) {
    	boolean flag = false;
    	String errorCode = "";
    	Map<String, Object> map = new LinkedHashMap<String, Object>();
    	if(StringUtils.isBlank(code) || StringUtils.isBlank(phone)){
    		errorCode = "invalid_data";
    	}
    	else{
        	//激活有效数据
        	int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
        	try {
    			UserActivated userActivated = new UserActivated();
    	    	userActivated.setCode(code);
    	    	userActivated.setPhone(phone);
    	    	List<UserActivated> userActivatedList = userActivatedService.findByUserActivated(userActivated);
    	    	if(userActivatedList != null && userActivatedList.size()>0){
    	    		userActivated=userActivatedList.get(0);
    				if(null != userActivated){
    					if(DateTools.timeDiffCurr(userActivated.getCreateTime()) > (validTime*60*1000)){
    						errorCode = "time_out";
    					}else{
    						User user = userService.findUserById(userActivated.getUserId());
    						if(null != user && phone.equals(userActivated.getPhone())){
    				    		flag = true;
    				    		map.put("code", userActivated.getCode());
    						}else{
    							UserCache usercache = userCacheService.findUserById(userActivated.getUserId());
    							if(usercache != null && phone.equals(userActivated.getPhone())){
        				    		flag = true;
        				    		map.put("code", userActivated.getCode());
    						  }else{
    							  errorCode = "invalid_phone";
    						  }
    						}
    					}
    				}else{
    					errorCode = "invalid_data";
    				}
    	    	}else{
    	    		errorCode = "invalid_data";
    	    	}
    		} catch (Exception e) {
    			e.printStackTrace();
    			flag = false;
    			errorCode = "system_error";
    		}
    	}
    	map.put("flag",flag);
    	map.put("errorCode",errorCode);
		WebUtils.writeJsonToMap(response, map);
    }
    private boolean nullEmptyBlankJudge(String str){
        return null==str||str.isEmpty()||"".equals(str.trim());
    }
    /**
     * 
     * 验证密码为 6～20位,字母、数字或者英文符号，最短6位，区分大小写
     * @param value
     * @return
     */
    public int judgeInputNotNo(String value){
    	int returnValue=0;
    	if(value.length()>20||value.length()<6){
    		returnValue=1;
    		return returnValue;
    	}else{
    	//Pattern p = Pattern.compile("[a-zA-Z][a-zA-Z0-9]{5,20}"); 
    		Pattern p = Pattern.compile("[0-9A-Za-z_]*");
    	//Pattern p = Pattern.compile("^[a-zA-Z]/w{5,17}$");
    	Matcher m = p.matcher(value);
    	boolean chinaKey = m.matches();
    	if(chinaKey){
    		returnValue=0;
    	} else{
    		returnValue=1;
    		return returnValue;
    	 }
    	}
    	return  returnValue;
    }
    
    /**
     * 用户重置密码
     * @param request
     * @param response
     * @param code
     * @param password
     * @param type		1:email 2:phone
     */
    @RequestMapping(value = "/dev/user/reset_password", method = RequestMethod.POST)
	public void reset_password(HttpServletRequest request,HttpServletResponse response,
			String code,String password,String type,String phone,String email) {
    	boolean flag = false;
    	String errorCode = "";
    	String username="";
    	//跳转到发送成功后的页面，提示：当你收到邮件信息，请按照提示 进行密码重置
    	long startTime = System.currentTimeMillis();
    	Map<String, Object> map = new LinkedHashMap<String, Object>();
    	if(StringUtils.isBlank(code) || StringUtils.isBlank(password) || StringUtils.isBlank(type)){
    		errorCode = "invalid_data";
    	}
    	else if("1".equals(type) || "2".equals(type)){
    		List<UserActivatedHis> list =null;
    		if("1".equals(type)){
    			list=userActivatedHisService.getByCodeAndEmail(code,email);
    		}else{
    			list= userActivatedHisService.getByCodeAndPhone(code,phone);
    		}
    		int validTime = Integer.valueOf(userserviceDev.getEmail_verify_valid());
    		if(list != null && list.size()>0){
    			UserActivatedHis	userActivatedHis = list.get(0);
        	if(null != userActivatedHis){
        		if(DateTools.timeDiffCurr(userActivatedHis.getCreateTime())< (validTime*60*1000)){
        		User user = userService.findUserById(userActivatedHis.getUserId());
        		int userId=0;
        		boolean isUser = false;
        		boolean isCache= false;
        		UserCache usercache =null;
        		if(user!=null){
        			userId=user.getId();
        			isUser=true;
        		}else{
        			usercache= userCacheService.findUserById(userActivatedHis.getUserId());
        			if(usercache!=null){
        				userId=usercache.id();
        				isCache=true;
        			}
        		}
        		List<AppUser>appUserList=appUserService.findByUserId(userId);
        		if( "1".equals(type)){
        			if(isUser){
        				if(user.getEmail().equals(userActivatedHis.getEmail())){
                			//重置密码
            				username=user.getUsername();
            				userLogicService.userResetPwdByEmail(user, password);
            				UserResetPwdControllerLog.log(type,username);
            				//send_password(password, appUserList);
            				flag = true;
                		}else{
                			errorCode = "invalid_data";
                		}	
        			}else{

        				if(usercache.email().equals(userActivatedHis.getEmail())){
                			//重置密码
            				username=usercache.username();
            				userLogicService.userResetPwdByEmail(usercache, password);
            				UserResetPwdControllerLog.log(type,username);
            				//send_password(password, appUserList);
            				flag = true;
                		}else{
                			errorCode = "invalid_data";
                		}	
        			
        			}
        			
        		}
        		else if("2".equals(type)){
        			if(isUser){
        				if(user.getUsername().equals(userActivatedHis.getPhone()) || user.getPhone().equals(userActivatedHis.getPhone())){
                			//重置密码
            				username=user.getUsername();
            				//手机号为空时，设置用户名为手机号
            				if(nullEmptyBlankJudge(user.getPhone()))
            				{
            					user.setPhone(user.getUsername());
            				}
            				userLogicService.userResetPwdByPhone(user, password);
            				UserResetPwdControllerLog.log(type,username);
            				try {
    							Thread thread = new Thread(new SendOesThread(password, userService,appUserList,userserviceDev.getOes_interface_addr(),userserviceDev.getOes_interface_key()));
    							thread.run();
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
            				//flag = send_oes(password,appUserList);
            				flag = true;	
                		}else{
                			errorCode = "invalid_data";
                		}	
        			}else if(isCache){
                        //缓存表数据
        				if(usercache.username().equals(userActivatedHis.getPhone()) || usercache.phone().equals(userActivatedHis.getPhone())){
                			//重置密码
            				username=usercache.username();
            				//手机号为空时，设置用户名为手机号
            				if(nullEmptyBlankJudge(usercache.phone()))
            				{
            					usercache.phone(usercache.username());
            				}
            				userLogicService.userResetPwdByPhone(usercache, password);
            				UserResetPwdControllerLog.log(type,username);
            				try {
    							Thread thread = new Thread(new SendOesThread(password, userService,appUserList,userserviceDev.getOes_interface_addr(),userserviceDev.getOes_interface_key()));
    							thread.run();
    						} catch (Exception e) {
    							e.printStackTrace();
    						}
            				//flag = send_oes(password,appUserList);
            				flag = true;	
                		}else{
                			errorCode = "invalid_data";
                		}	
        			
        				
        			}else{
            			errorCode = "invalid_data";
            		}
        			
        		 }
        		else{
        			errorCode = "invalid_data";
        		  }
        	     }else{
        	    	 errorCode = "time_out"; 
        	     }
    		  }else{
      			errorCode = "invalid_data";
    		  }
        	}else{
        		errorCode = "invalid_data";
        	}
    	}
    	else if("3".equals(type)){
    		User user = userService.findByUsername(code);
    		
    		if(null != user){
    			//重置密码
    			username=user.getUsername();
    			userLogicService.userResetPwd(user, password);
    			UserResetPwdControllerLog.log(type,username);
	    		flag = true;
    		}
    		else{
    			errorCode = "invalid_data";
    		}
		}
    	else{
    		errorCode = "invalid_data";
    	}
    	
    	map.put("flag",flag);
    	map.put("errorCode",errorCode);
    	WebUtils.writeJsonToMap(response, map);
    	App app=new App();
    	app.setId(9999);
    	//OauthControllerLog.log(startTime,username,password,app,map);
    	//OauthControllerLog.log(startTime, username, password, app, map,userserviceDev);
    }

	private boolean send_oes(String password,List<AppUser> appUserList) {
		String returnValue="";
		boolean flag;
		int appid;
		String sourceid;
		User user1;
		App app;
		String pwd="";
		if(appUserList!=null&&appUserList.size()>0){
			for(int i=0;i<appUserList.size();i++){
				appid=appUserList.get(i).appId();
				sourceid=appUserList.get(i).sourceId();
			
				if(appid==1||appid==8){
					List<Map<String,String>> infoList=new ArrayList<Map<String,String>>();
					app=appService.findById(appid);
					String url=userserviceDev.getOes_interface_addr()+userserviceDev.getOes_interface_key();
					try {
						pwd=Help_Encrypt.encrypt(password);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					user1=userService.findUserById(appUserList.get(i).userId());
					Map<String,String> map1=new HashMap<String,String>();
					map1.put("sourceid", sourceid);
					map1.put("username", user1.getUsername());
					map1.put("password", pwd);
					map1.put("key", app.getAppsecret());
		    		infoList.add(map1);
		    		returnValue=HttpTools.post(url, infoList);
		    		break;
				}else{
					returnValue="";
				}
			}
		}
		Map<String,String> map=new HashMap<String,String>();
		//解析OES返回的json数据
		if(nullEmptyBlankJudge(returnValue)){
			flag = false;
		}else{
		  JSONObject reqjson = JSONObject.fromObject(returnValue);
		if(analysisValue(reqjson)){
			flag = true;	
		}else{
			flag=false;
			
		}
   			}
		
		return flag;
	}
    /**
     * 回调oes接口发送修改后的密码
     * @param password
     * @param appUserList
     */
	private String send_password(String password, List<AppUser> appUserList) {
		int appid;
		String sourceid;
		String returnValue="";
		User user1;
		if(appUserList!=null&&appUserList.size()>0){
			for(int i=0;i<appUserList.size();i++){
				appid=appUserList.get(i).appId();
				sourceid=appUserList.get(i).sourceId();
				if(appid==1||appid==8){
					List<Map<String,String>> infoList=new ArrayList<Map<String,String>>();
					App app=appService.findById(appid);
					String url=userserviceDev.getOes_interface_addr()+userserviceDev.getOes_interface_key();
					String pwd="";
					try {
						pwd=Help_Encrypt.encrypt(pwd);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					user1=userService.findUserById(appUserList.get(i).userId());
					Map<String,String> map1=new HashMap<String,String>();
					map1.put("sourceid", sourceid);
					map1.put("username", user1.getUsername());
					map1.put("password", pwd);
					map1.put("key", app.getAppsecret());
		    		infoList.add(map1);
		    		returnValue=HttpTools.post(url, infoList);
		    		break;
				}else{
					returnValue="";
				}
			}
		}
		return returnValue;
	}
    public Boolean analysisValue(JSONObject obj ){
    	String state = obj.getString("state");
		if(!state.equals("ok")){
			return false;
		}else{
			return true;
		}
    }
    public Boolean analysisValue(String obj ){
    	String a[]=obj.split(":");
    	
    	String state = a[1].toString();
		if(!state.equals("ok")){
			return true;
		}else{
			return false;
		}
    }
    /**
     * 查询用户已经设置的密保问题列表
     * @param request
     * @param response
     */
    @RequestMapping(value = "/dev/user/user_problem_by_username",method = RequestMethod.POST)
    public void userProblemByUsername(HttpServletRequest request,HttpServletResponse response,String userName){
    	//返回数据
    	boolean flag = false;
    	String errorCode = "";
    	Map<String,Object> map = new HashMap<String, Object>();
    	if(StringUtils.isNotBlank(userName)){
	    	User user = userService.findByUsername(userName);
	    	if(user!=null){
	        	List<Map<String,String>> mapList=null;
	        	//问题列表
	        	List<UserProblem> problemList=this.userProblemService.findListByUserId(user.getId());
	        	if(problemList!=null && problemList.size()>0){
	        		mapList = new LinkedList<Map<String,String>>();
	        		Map<String,String> mapObj = null;
	        		UserDict userDict=null;
	        		//只查询一个问题
	        		userDict=this.userDictService.getById(problemList.get(0).getProblemId());
	        		if(userDict!=null){
    					mapObj = new HashMap<String, String>();
    	    			mapObj.put("id", userDict.getId()+"");
    	    			mapObj.put("name", userDict.getName());
    	    			mapList.add(mapObj);
    				}
	        		//用户只有一个密保问题，该方法屏蔽
//	        		for(UserProblem up : problemList){
//	        			if(up!=null && up.getProblemId()>0){
//	        				userDict=this.userDictService.getById(up.getProblemId());
//	        				if(userDict!=null){
//	        					mapObj = new HashMap<String, String>();
//	        	    			mapObj.put("id", userDict.getId()+"");
//	        	    			mapObj.put("name", userDict.getName());
//	        	    			mapList.add(mapObj);
//	        				}
//	        			}
//	        		}
	        		flag = true;
	        		map.put("problemData", mapList);
	        	}
	        	else{
	        		errorCode = "no_problem";
	        	}
	    	}else{
	    		errorCode = "invalid_username";
	    	}
    	}else{
    		errorCode = "invalid_username";
    	}
    	map.put("flag",flag);
    	map.put("errorCode",errorCode);
    	WebUtils.writeJsonToMap(response, map);
    }
    
    /**
     * 查询用户已经设置的密保问题列表
     * @param request
     * @param response
     */
    @RequestMapping(value = "/dev/user/activated_reset_password_problem",method = RequestMethod.POST)
    public void activatedResetPasswordProblem(HttpServletRequest request,HttpServletResponse response,String userName,String problemId,String answer){
    	//返回数据
    	boolean flag = false;
    	String errorCode = "";
    	Map<String,Object> map = new HashMap<String, Object>();
    	if(StringUtils.isNotBlank(userName)){
	    	User user = userService.findByUsername(userName);
	    	if(user!=null){
	    		UserProblem userProblem = userProblemService.getByUserIdAndProblemId(user.getId(),Integer.valueOf(problemId));
	        	if(null != userProblem){
	        		if(userProblem.getAnswer().equals(answer)){
	        			flag = true;
	        		}else{
	        			errorCode = "answer_error";
	        		}
	        	}else{
	        		errorCode = "invalid_problem";
	        	}
	    	}else{
	    		errorCode = "invalid_username";
	    	}
    	}else{
    		errorCode = "invalid_username";
    	}
    	map.put("flag",flag);
    	map.put("errorCode",errorCode);
    	WebUtils.writeJsonToMap(response, map);
    }
}