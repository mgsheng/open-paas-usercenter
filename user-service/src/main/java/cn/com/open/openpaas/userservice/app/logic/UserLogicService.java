package cn.com.open.openpaas.userservice.app.logic;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;




/**
 * 用户logic
 */
public interface UserLogicService {

	/**
     * 修改用户密码
     * 返回值：1：成功，-1：用户不存在，-2：旧密码错误
     * @param id
     * @param newPassword
     * @param oldPassword
     */
    int updatePassword(int userId,String newPassword,String oldPassword);
    
    /**
     * 激活邮箱(邮件请求)
     * 返回码：1：激活成功  ；-1：无效的参数code；-2:链接已过期！请重新激活；-3:无效的用户数据
     * @param code
     * @return
     */
    int activatedEmail(HttpServletRequest request,String sessionKey,String code);
    
    /**
     * 发送验证邮箱邮件，保存验证信息
     * @param userId
     * @param oldEmail 用户表中email
     * @param activatedEmail 激活表中email
     * @param userType
     */
	void emailActivated(int userId,String oldEmail,String activatedEmail,int userType);
	
	/**
	 * 发送找回密码邮件
	 * @param userId
	 * @param email
	 * @param userType
	 */
	boolean sendResetPassWordEmail(int userId,String email,int userType);
	
	/**
	 * 根据条件获取用户激活数据
	 * @param userId
	 * @param email
	 * @param userType
	 * @return
	 */
	UserActivated loadUserActivated(int userId,String email,int userType);
	/**
	 * 发送找回密码手机短信
	 * @param userId
	 * @param phone
	 * @param userType
	 */
	boolean sendResetPassWordPhone(int userId,String phone,int userType);
	
	/**
	 * 发送注册手机短信
	 * @param code
	 * @param content
	 * @param phone
	 * @param userType
	 */
	boolean sendRegPhone(String code,String content,String phone,int userType);
	
	/**
	 *  重置手机号，发送手机短信
	 * @param phone
	 * @return
	 */
	boolean sendPhoneSecurityCode(String phone);
	
	/**
	 * 验证重置手机号，发送手机验证码,是否有效
	 * @param phone
	 * @param code
	 * @return
	 */
	boolean verificationPhoneSecurityCode(String phone,String code);
	
	/**
     * 加载model返回数据
     * @param model
     * @param appUid
     */
    void loadControllerModel(User user,Map<String,Object> model,int appUid);
    
	/**
	 * 用户重置密码
	 * @param user
	 * @return
	 */
	Boolean userResetPwd(User user,String newPwd);
	Boolean userResetPwdByPhone(User user, String password);
	Boolean userResetPwdByEmail(User user, String password);
	
}