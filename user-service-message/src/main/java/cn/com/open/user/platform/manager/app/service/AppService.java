package cn.com.open.user.platform.manager.app.service;

import java.util.List;

import cn.com.open.user.platform.manager.app.model.App;
import cn.com.open.user.platform.manager.appuser.model.AppUser;


/**
 * 
 */
public interface AppService {

	App findIdByClientId(String client_Id);

	List<App> findAll();
	
	List<App> findByAppIds(String appIds);

	App findById(Integer appId);
	
	/**
	 * 根据App和AppUser生成回调URL
	 * @param app
	 * @param appUser
	 * @return
	 */
	String findCallbackUrl(App app, AppUser appUser);
}