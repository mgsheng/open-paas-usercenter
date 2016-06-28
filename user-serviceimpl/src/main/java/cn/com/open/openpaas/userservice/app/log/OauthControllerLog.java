package cn.com.open.openpaas.userservice.app.log;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.tools.DateTools;

/**
 * 输出日志类，用于统计各接口执行效率和结果，配置在log4j中的dailyRollingFileOauthControllerLog
 * @author dongminghao
 *
 */
public class OauthControllerLog {
	
	private static final Logger logger = LoggerFactory.getLogger(OauthControllerLog.class);

	/**
	 * 输出日志
	 * 格式：
	 * 日期#方法名#执行时间#用户名#密码#应用ID#执行status#错误号#
	 * @param startTime long startTime = System.currentTimeMillis();
	 * @param username
	 * @param password
	 * @param app
	 * @param map
	 */
	public static void log(long startTime,String username,String password,App app, Map<String,Object> map){
		try {
			Throwable ex = new Throwable();
			StringBuffer msg = new StringBuffer();
			long endTime = System.currentTimeMillis(); //获取结束时间
			//根据堆栈[1]获取到调用的方法名 0是自身
			if (ex.getStackTrace() != null && ex.getStackTrace()[1] != null) {
				//获取时间
				msg.append(DateTools.dateToString(new Date(), "yyyyMMddHHmmss")).append("#");
				//获取方法名
				msg.append(ex.getStackTrace()[1].getMethodName()).append("#");
				//获取执行时间
				msg.append(endTime-startTime).append("#");
				//获取用户名
				msg.append(username).append("#");
				//获取密码
				msg.append(password).append("#");
				//获取appId
				if(app!=null){
					msg.append(app.getId()).append("#");
				}
				else{
					msg.append("#");
				}
				//获取执行状态
				if(map!=null && map.get("status") !=null){
					msg.append(map.get("status")).append("#");
					//获取错误号status=0为错误
					if(map.get("status").toString().equals("0") && map.get("error_code")!=null){
						msg.append(map.get("error_code")).append("#");
					}
					else{
						msg.append("#");
					}
				}
				if(map!=null && map.get("flag")!=null){
					if(map.get("flag").equals("false")){
						msg.append("0").append("#").append(map.get("error_code"));
					}else{
						msg.append("1").append("#").append("#");
					}
				}
			}
			logger.info(msg.toString());
			ex = null;
			msg = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}