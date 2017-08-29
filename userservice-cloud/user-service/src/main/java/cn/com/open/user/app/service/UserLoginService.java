package cn.com.open.user.app.service;

import java.util.ArrayList;
import java.util.Map;

import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.redis.RedisServiceImpl;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;
import cn.com.open.user.app.vo.UserVo;


public interface UserLoginService {
	
	public  UserMergeVo  findUserAccount(User entity);
	
	public ArrayList<User> findUserAccountByUsername(UserVo user) ;


	public App findIdByClientId(String client_id) ;

	public void updateUserAccount(UserVo user) ;

	public ArrayList<UserListVo> findUserAccountList(int userId);
	/**
    * 清空redis登录锁定限制缓存
    * @param request
    * @param response
    */
	public void redisInit(RedisServiceImpl redisService, String appId, String userName);
	//验证该用户是否已经锁定，超过24小时自动解除锁
	public Map<String, Object> lockUserNames(RedisServiceImpl redisService, Map<String, String> maps);
	 //清空所以缓存
	public Map<String, Object> loginValidates(RedisServiceImpl redisService, Map<String, String> maps);

	 
	
}
