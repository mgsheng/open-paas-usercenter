package cn.com.open.user.app.service;

import java.util.Map;

import cn.com.open.user.app.entiy.OAUser;
import cn.com.open.user.app.vo.OAUserVo;


public interface OAUserService {
	 
	public OAUserVo getOAUserModel(int userId,String appId) ;

	public OAUserVo GetOAUserModel(OAUser user, Map<String, Object> maps,String url,String appSercret,String appId) ;
 
	
}
