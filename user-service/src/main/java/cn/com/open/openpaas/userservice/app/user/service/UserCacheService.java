package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import cn.com.open.openpaas.userservice.app.user.model.UserCache;

/**
 * 
 */
public interface UserCacheService {
	
	UserCache findByUsername(String username);

	List<UserCache> findByEmail(String account);

	List<UserCache> findByPhone(String account);
	
	List<UserCache> findByCardNo(String cardNo);
	
	void updateUserCache(UserCache userCache);
	
	List<UserCache> findAll();
	UserCache findUserById(Integer userId);
	UserCache findByUserInfo(String username,Integer appid);
	List<UserCache> findByEmail(String account,Integer appid);
	List<UserCache> findByPhone(String account,Integer appid);
	List<UserCache> findByCardNo(String cardNo,Integer appid);
	
}