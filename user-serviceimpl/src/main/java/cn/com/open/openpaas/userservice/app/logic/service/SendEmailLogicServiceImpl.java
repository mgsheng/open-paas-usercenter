package cn.com.open.openpaas.userservice.app.logic.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.logic.SendEmailLogicService;
import cn.com.open.openpaas.userservice.app.tools.EmailTools;
import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;

@Service("sendEmailLogicService")
public class SendEmailLogicServiceImpl implements SendEmailLogicService {

	@Resource
	UserService userService;
	
	/**
	 * 发送邮件激活
	 * @param userActivated
	 * @return
	 */
	public boolean emailActivated(UserActivated userActivated){
		User user = userService.findUserById(userActivated.getUserId());
		String title = "奥鹏用户中心-邮箱激活";
		int validTime = Integer.valueOf(PropertiesTool.getAppPropertieByKey("email.verify.valid")); 
		String localhost = PropertiesTool.getAppPropertieByKey("app.localhost.url");
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", user==null?"用户":user.getNickName());
		map.put("url", localhost+"/dev/user/activated_email.html?code="+userActivated.getCode());
		map.put("validtime", validTime+"");
		return EmailTools.sendTemplateMail(userActivated.getEmail(),title,"activation", map);
	}
	
	/**
	 * 发送邮件,找回密码
	 * @param userActivated
	 * @return
	 */
	public boolean sendResetPassWordEmail(UserActivated userActivated){
		User user = userService.findUserById(userActivated.getUserId());
		String title = "奥鹏用户中心-找回密码";
		int validTime = Integer.valueOf(PropertiesTool.getAppPropertieByKey("email.verify.valid")); 
		String localhost = PropertiesTool.getAppPropertieByKey("app.localhost.url");
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", user==null?"用户":(user.getNickName()==null?user.getUsername():user.getNickName()));
		map.put("url", localhost+"/dev/user/activated_reset_password_email.html?code="+userActivated.getCode());
		map.put("validtime", validTime+"");
		return EmailTools.sendTemplateMail(userActivated.getEmail(),title,"resetpassword", map);
	}
}
