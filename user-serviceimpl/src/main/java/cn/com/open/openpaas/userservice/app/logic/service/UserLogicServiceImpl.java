package cn.com.open.openpaas.userservice.app.logic.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.logic.SendEmailLogicService;
import cn.com.open.openpaas.userservice.app.logic.UserLogicService;
import cn.com.open.openpaas.userservice.app.tools.AESUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.userservice.app.tools.SmsTools;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserDetail;
import cn.com.open.openpaas.userservice.app.user.model.UserEducation;
import cn.com.open.openpaas.userservice.app.user.model.UserSocial;
import cn.com.open.openpaas.userservice.app.user.model.UserWork;
import cn.com.open.openpaas.userservice.app.user.service.UserActivatedService;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;

/**
 * 用户logic
 */
@Service("userLogicService")
public class UserLogicServiceImpl implements UserLogicService {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserActivatedService userActivatedService;
    @Autowired
    private SendEmailLogicService sendEmailLogicService;
    @Autowired
	 private UserserviceDev userserviceDev;

    /**
     * 修改用户密码
     * 返回值：1：成功，-1：用户不存在，-2：旧密码错误
     * @param id
     * @param newPassword
     * @param oldPassword
     */
    public int updatePassword(int userId,String newPassword,String oldPassword){
    	User user= userService.findUserById(userId);
    	if(user != null){
    		if(user.checkPasswod(oldPassword)){
    			user.setPlanPassword(newPassword);
    			userService.updateUser(user);
        		return 1;
    		}else{
    			return -2;
    		}
    	}else{
    		return -1;
    	}
    }
    
    /**
     * 激活邮箱
     * 返回码：1：激活成功  ；-1：无效的参数code；-2:链接已过期！请重新激活；-3:无效的用户数据
     * @param code
     * @return
     */
    public int activatedEmail(HttpServletRequest request,String sessionKey,String code){
    	int validTime = Integer.valueOf(PropertiesTool.getAppPropertieByKey("email.verify.valid"));
    	UserActivated userActivated =  userActivatedService.findByCode(code);
    	if(userActivated != null){
    		if(DateTools.timeDiffCurr(userActivated.getCreateTime()) > validTime*60*1000){
    			System.out.println("链接已过期！请重新激活。");
    			return -2;
    		}else{
    			User user = userService.findUserById(userActivated.getUserId());
	    		if(null != user){
	        		userActivatedService.moveUserActivated(userActivated);
	        		//修改用户对应邮箱激活状态接口
	        		user.setEmailActivation(User.ACTIVATION_YES);
	        		user.setEmail(userActivated.getEmail());
	        		if(user.getUserState() == User.STATUS_INACTIVE){
	        			user.setUserState(User.STATUS_ENABLE);
	        		}
	        		userService.updateUser(user);
	        		request.getSession().setAttribute(sessionKey, user);
	        		System.out.println("激活成功");
	        		return 1;
	    		}else{
	    			System.out.println("无效的用户数据");
	    			return -3;
	    		}
    		}
    	}else{
    		System.out.println("无效的参数code");
    		return -1;
    	}
    }
    
    /**
     * 发送验证邮箱邮件，保存验证信息
     * @param userId
     * @param oldEmail 用户表中email
     * @param activatedEmail 激活表中email
     * @param userType
     */
	public void emailActivated(int userId,String oldEmail,String activatedEmail,int userType){
		UserActivated userActivated = loadUserActivated(userId, activatedEmail, userType);
		if(userActivated == null){
			System.out.println("没有找到匹配的Email激活数据，系统根据当前用户邮箱发送验证邮件！");
			userActivated = new UserActivated();
			userActivated.setEmail(oldEmail);
			userActivated.setCreateTime(new Date().getTime());
			userActivated.setUserId(userId);
			userActivated.setUserType(UserActivated.USERTYPE_USER);
		}
		userActivated.setCode(StringTool.getRandomString(64));
		userActivated.setCreateTime(new Date().getTime());
		boolean bool = sendEmailLogicService.emailActivated(userActivated);
		if(bool){
			if(userActivated.getId() > 0){
				userActivatedService.update(userActivated);
			}else{
				userActivatedService.save(userActivated);
			}
		}
	}
	
	
	/**
	 * 发送找回密码邮件
	 * @param userId
	 * @param email
	 * @param userType
	 */
	public boolean sendResetPassWordEmail(int userId,String email,int userType){
		try {
			UserActivated activatedReset = new UserActivated();
			activatedReset.setUserId(userId);
			activatedReset.setUserType(userType);
			activatedReset.setEmail(email);
			activatedReset.setCode(StringTool.getRandomString(64));
			boolean bool = sendEmailLogicService.sendResetPassWordEmail(activatedReset);
			if(bool){
				userActivatedService.save(activatedReset);
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 发送找回密码手机短信
	 * @param userId
	 * @param phone
	 * @param userType
	 */
	public boolean sendResetPassWordPhone(int userId,String phone,int userType){
		try {
			
			UserActivated activatedReset = new UserActivated();
			activatedReset.setPhone(phone);
			List<UserActivated> list=userActivatedService.findByUserActivated(activatedReset);
			String code=StringTool.getRandomNum(6);
			//发送短信
			int validTime = Integer.valueOf(PropertiesTool.getAppPropertieByKey("email.verify.valid"));
			String content = "当前找回密码验证码："+code+"，验证码时效为"+validTime+"分钟,请在规定时间完成验证操作！";
			String bool = SmsTools.sendSms(phone, content);
			if(StringUtils.isNotBlank(bool)){
				if(list!=null&&list.size()>0){
					for(UserActivated userActivated:list){
						userActivated.setCreateTime(new Date().getTime());
						userActivated.setCode(code);
						userActivated.setUserId(userId);
						activatedReset.setUserType(userType);
						userActivatedService.updateUserActivated(userActivated);
					}
				}else{
					activatedReset.setUserId(userId);
					activatedReset.setUserType(userType);
					activatedReset.setCode(code);
					activatedReset.setCreateTime(new Date().getTime());
					userActivatedService.save(activatedReset);
				}
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 重置手机号，发送手机短信
	 * @param userId
	 * @param phone
	 * @param userType
	 */
	public boolean sendPhoneSecurityCode(String phone){
		UserActivated userActivated = new UserActivated();
		userActivated.setPhone(phone);
		userActivated.setCreateTime(new Date().getTime());
		userActivated.setCode(StringTool.getRandomNum(6));
		//发送短信
		String content = "当前找回密码验证码："+userActivated.getCode()+",请在规定时间完成验证操作！";
		String bool = SmsTools.sendSms(phone, content);
		if(StringUtils.isNotBlank(bool)){
			userActivatedService.save(userActivated);
			return true;
		}
		return false;
	}
	
	/**
	 * 验证重置手机号，发送手机验证码,是否有效
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean verificationPhoneSecurityCode(String phone,String code){
		UserActivated userActivated = userActivatedService.findByCode(code);
		//激活有效数据
    	int validTime = Integer.valueOf(PropertiesTool.getAppPropertieByKey("email.verify.valid"));
		if(userActivated != null && userActivated.getPhone().equals(phone) && DateTools.timeDiffCurr(userActivated.getCreateTime()) < validTime*60*1000){
			userActivatedService.moveUserActivated(userActivated);
			return true;
		}
		return false;
	}
	/**
	 * 根据条件获取用户激活数据
	 * @param userId
	 * @param email
	 * @param userType
	 * @return
	 */
	public UserActivated loadUserActivated(int userId,String email,int userType){
		UserActivated userActivated = new UserActivated();
		userActivated.setUserId(userId);
		userActivated.setUserType(userType);
		userActivated.setEmail(email);
		List<UserActivated> userActivatedList = userActivatedService.findByUserActivated(userActivated);
		if(userActivatedList != null && userActivatedList.size() > 0 ){
			for(UserActivated activated : userActivatedList){
				if(StringUtils.isNotBlank(activated.getEmail())){
					return activated;
				}
			}
		}
		return null;
	}
	
	/**
     * 加载model返回数据
     * @param model
     * @param appUid
     */
    public void loadControllerModel(User user,Map<String,Object> model,int appUid){
    	model.put("id",user.getId());
    	model.put("email",user.getEmail());
    	model.put("nickName",user.getNickName());
    	model.put("phone",user.getPhone());
    	model.put("realName",user.getRealName());
    	model.put("userName",user.getUsername());
    	model.put("emailActivation",user.getEmailActivation());
    	model.put("headPicture",user.getHeadPicture());
    	model.put("appUid",appUid);
    	
    	UserActivated userActivated = loadUserActivated(user.getId(), "",UserActivated.USERTYPE_USER);
    	if(userActivated != null){
    		model.put("activatedEmail",userActivated.getEmail());
		}else{
			model.put("activatedEmail",user.getEmail());
		}
    	
    	UserDetail userDetail = userService.findDetailByAppUserId(appUid);
    	if(userDetail != null){
        	model.put("userDetail_age",userDetail.getAge());
        	model.put("userDetail_birthday",DateTools.dateToString(userDetail.getBirthday(),"yyyy-MM-dd"));
        	model.put("userDetail_city",userDetail.getCity());
        	model.put("userDetail_education",userDetail.getEducation());
        	model.put("userDetail_marriage",userDetail.getMarriage());
        	model.put("userDetail_nation",userDetail.getNation());
        	model.put("userDetail_occupation",userDetail.getOccupation());
        	model.put("userDetail_politics",userDetail.getPolitics());
        	model.put("userDetail_province",userDetail.getProvince());
        	model.put("userDetail_region",userDetail.getRegion());
        	model.put("userDetail_sex",userDetail.getSex());
    	}
    	
    	UserEducation userEducation = userService.findEducationByAppUserId(appUid);
    	if(userEducation != null){
        	model.put("userEducation_address",userEducation.getAddress());
        	model.put("userEducation_description",userEducation.getDescription());
        	model.put("userEducation_endDate",DateTools.dateToString(userEducation.getEndDate(),"yyyy-MM-dd"));
        	model.put("userEducation_major",userEducation.getMajor());
        	model.put("userEducation_school",userEducation.getSchool());
        	model.put("userEducation_startDate",DateTools.dateToString(userEducation.getStartDate(),"yyyy-MM-dd"));
        	model.put("userEducation_studyLevel",userEducation.getStudyLevel());
    	}
    	
    	UserWork userWork = userService.findWorkByAppUserId(appUid);
    	if(userWork != null){
        	model.put("userWork_address",userWork.getAddress());
        	model.put("userWork_company",userWork.getCompany());
        	model.put("userWork_endDate",DateTools.dateToString(DateTools.stringtoDate(String.valueOf(userWork.getEndDate()),"yyyyMMdd") ,"yyyy-MM-dd"));
        	model.put("userWork_description",userWork.getDescription());
        	model.put("userWork_position",userWork.getPosition());
        	model.put("userWork_beginDate",DateTools.dateToString(DateTools.stringtoDate(String.valueOf(userWork.getBeginDate()),"yyyyMMdd") ,"yyyy-MM-dd"));
        	model.put("userWork_responsibility",userWork.getResponsibility());
        	model.put("userWork_workContent",userWork.getWorkContent());
    	}
    	
    	UserSocial userSocial = userService.findSocailByAppUserId(appUid);
    	if(userSocial != null){
        	model.put("userSocial_others",userSocial.getOthers());
        	model.put("userSocial_qq",userSocial.getQq());
        	model.put("userSocial_weibo",userSocial.getWeibo());
        	model.put("userSocial_weixin",userSocial.getWeixin());
        	model.put("userSocial_blog",userSocial.getBlog());
    	}
    }
    
    /**
	 * 用户重置密码（该方法只适用于用户中心找回密码）
	 * @param user
	 * @return
	 */
	public Boolean userResetPwd(User user,String newPwd){
		try {
			Boolean isLink;
			isLink = user.defaultUser();
			List<User> parentUserlist;
			if(isLink){
				//存在Pid则证明该User为子账号
				if(StringUtils.isNotBlank(user.getPid()) && !user.getPid().equals("0")){
					//刷新盐值，重新加密
					user.buildPasswordSalt();
					user.setPlanPassword(newPwd);
					user.setUpdatePwdTime(new Date());
					userService.updateUser(user);
					
				}
				else{
					//刷新盐值，重新加密
					user.buildPasswordSalt();
					user.setPlanPassword(newPwd);
					user.setUpdatePwdTime(new Date());
					userService.updateUser(user);
					parentUserlist=userService.findByPid(String.valueOf(user.getId()));
					User parentUser=new User();
					for(int i=0;i<parentUserlist.size();i++){
						parentUser=parentUserlist.get(i);
						parentUser.buildPasswordSalt();
						parentUser.setPlanPassword(newPwd);
						parentUser.setUpdatePwdTime(new Date());
						userService.updateUser(parentUser);
					}
					
				}
			}else{
				//刷新盐值，重新加密
				user.buildPasswordSalt();
				user.setPlanPassword(newPwd);
				user.setUpdatePwdTime(new Date());
				userService.updateUser(user);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	 /**
		 * 用户重置密码（该方法只适用于用户中心找回密码）
		 * @param user
		 * @return
		 */
		public Boolean userResetPwdByPhone(User user,String newPwd){
			List<User> userList=userService.findByPhone(user.getPhone());
			 if(userList!=null&&userList.size()>0){
			for(int i=0;i<userList.size();i++){
				User newUser=userList.get(i);
				try {
				 String	aesPassword=AESUtil.encrypt(newPwd, userserviceDev.getAes_userCenter_key());
					Boolean isLink;
					isLink =newUser.defaultUser();
					List<User> parentUserlist;
					if(isLink){
						//存在Pid则证明该User为子账号
						if(StringUtils.isNotBlank(user.getPid()) && !user.getPid().equals("0")){
							//刷新盐值，重新加密
							newUser.buildPasswordSalt();
							newUser.setPlanPassword(newPwd);
							newUser.setUpdatePwdTime(new Date());
							userService.updateUser(newUser);
							
						}
						else{
							//刷新盐值，重新加密
							newUser.buildPasswordSalt();
							newUser.setPlanPassword(newPwd);
							newUser.setUpdatePwdTime(new Date());
							userService.updateUser(newUser);
							parentUserlist=userService.findByPid(String.valueOf(newUser.getId()));
							User parentUser=new User();
							for(int j=0;j<parentUserlist.size();j++){
								parentUser=parentUserlist.get(j);
								parentUser.buildPasswordSalt();
								parentUser.setPlanPassword(newPwd);
								parentUser.setUpdatePwdTime(new Date());
								userService.updateUser(parentUser);
							}
						}
					}else{
						//刷新盐值，重新加密
						
						newUser.buildPasswordSalt();
						newUser.setPlanPassword(newPwd);
						newUser.setUpdatePwdTime(new Date());
						newUser.setAesPassword(aesPassword);
						userService.updateUser(newUser);
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			 }
			 }
			return false;
		}
		
		 /**
		 * 用户重置密码（该方法只适用于用户中心找回密码）
		 * @param user
		 * @return
		 */
		public Boolean userResetPwdByEmail(User user,String newPwd){
			List<User> userList=userService.findByEmail(user.getEmail());
			for(int i=0;i<userList.size();i++){
				User newUser=userList.get(i);
				try {
					Boolean isLink;
					isLink =newUser.defaultUser();
					List<User> parentUserlist;
					if(isLink){
						//存在Pid则证明该User为子账号
						if(StringUtils.isNotBlank(user.getPid()) && !user.getPid().equals("0")){
							//刷新盐值，重新加密
							newUser.buildPasswordSalt();
							newUser.setPlanPassword(newPwd);
							newUser.setUpdatePwdTime(new Date());
							userService.updateUser(newUser);
						}
						else{
							//刷新盐值，重新加密
							newUser.buildPasswordSalt();
							newUser.setPlanPassword(newPwd);
							newUser.setUpdatePwdTime(new Date());
							userService.updateUser(newUser);
							parentUserlist=userService.findByPid(String.valueOf(newUser.getId()));
							User parentUser=new User();
							for(int j=0;j<parentUserlist.size();j++){
								parentUser=parentUserlist.get(j);
								parentUser.buildPasswordSalt();
								parentUser.setPlanPassword(newPwd);
								parentUser.setUpdatePwdTime(new Date());
								userService.updateUser(parentUser);
							}
						}
					}else{
						//刷新盐值，重新加密
						newUser.buildPasswordSalt();
						newUser.setPlanPassword(newPwd);
						newUser.setUpdatePwdTime(new Date());
						userService.updateUser(newUser);
					}
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return false;
		}

		@Override
		public boolean sendRegPhone(String code,String content,String phone, int userType) {

			try {
				UserActivated activatedReset = new UserActivated();
				activatedReset.setPhone(phone);
				List<UserActivated> list=userActivatedService.findByUserActivated(activatedReset);
				//发送短信
				String bool = SmsTools.sendSms(phone, content);
				if(StringUtils.isNotBlank(bool)){
					if(list!=null&&list.size()>0){
						for(UserActivated userActivated:list){
							userActivated.setCreateTime(new Date().getTime());
							userActivated.setCode(code);
							activatedReset.setUserType(userType);
							userActivatedService.updateUserActivated(userActivated);
						}
					}else{
						activatedReset.setUserType(userType);
						activatedReset.setCode(code);
						activatedReset.setCreateTime(new Date().getTime());
						userActivatedService.save(activatedReset);
					}
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		
		}
		
		
}