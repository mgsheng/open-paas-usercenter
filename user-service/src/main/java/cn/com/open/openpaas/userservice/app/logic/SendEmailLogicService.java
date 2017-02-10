package cn.com.open.openpaas.userservice.app.logic;

import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;

public interface SendEmailLogicService {

	
	/**
	 * 发送邮件激活
	 * @param userActivated
	 * @return
	 */
	public boolean emailActivated(UserActivated userActivated);
	
	/**
	 * 发送邮件,找回密码
	 * @param userActivated
	 * @return
	 */
	public boolean sendResetPassWordEmail(UserActivated userActivated);
}
