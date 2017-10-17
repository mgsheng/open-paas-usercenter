package cn.com.open.openpaas.userservice.app.thread;

import java.io.IOException;
import java.util.Map;

import cn.com.open.openpaas.userservice.app.tools.HttpTools;

/**
 * 用户日志发送
 * @author ws
 *
 */
public class UserLogSendThread implements Runnable {

	private String kong_log_url;
	private  Map <String,String>logMap;
	
	public UserLogSendThread(String kong_log_url,Map <String,String>logMap){
		this.kong_log_url = kong_log_url;
		this.logMap = logMap;
	}
	
	@Override
	public void run() {
		//发送用户日志
		try {
			HttpTools.URLPost(kong_log_url, logMap,"UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
