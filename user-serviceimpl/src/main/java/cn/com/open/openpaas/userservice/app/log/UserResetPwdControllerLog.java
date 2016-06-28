package cn.com.open.openpaas.userservice.app.log;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.user.model.User;

/**
 * 输出日志类，用于记录找回密码各方式的使用频率，配置在log4j中的dailyRollingFileUserResetPwdControllerLog
 * @author dongminghao
 *
 */
public class UserResetPwdControllerLog {
	
	private static final Logger logger = LoggerFactory.getLogger(UserResetPwdControllerLog.class);

	/**
	 * 输出日志
	 * 格式：
	 * 日期#用户名#找回类型
	 * 找回类型：1：邮箱 2：手机 3：密保问题
	 * @param type
	 * @param user
	 */
	public static void log(String type,User user){
		try {
			Throwable ex = new Throwable();
			StringBuffer msg = new StringBuffer();
			//根据堆栈[1]获取到调用的方法名 0是自身
			if (ex.getStackTrace() != null && ex.getStackTrace()[1] != null) {
				//获取时间
				msg.append(DateTools.dateToString(new Date(), "yyyyMMddHHmmss")).append("#");
				if(user!=null){
					msg.append(user.getUsername()).append("#");
				}
				else{
					msg.append("#");
				}
				msg.append(type).append("#");
			}
			logger.info(msg.toString());
			ex = null;
			msg = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}