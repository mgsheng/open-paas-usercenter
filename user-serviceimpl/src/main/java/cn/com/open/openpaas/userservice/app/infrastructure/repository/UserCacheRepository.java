package cn.com.open.openpaas.userservice.app.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.openpaas.userservice.app.user.model.UserCache;

/**
 * 
 */

public interface UserCacheRepository extends Repository {

	UserCache findByUsername(@Param("username")String username);

	List<UserCache> findByEmail(@Param("email")String email);

	List<UserCache> findByPhone(@Param("phone")String phone);
	
	List<UserCache> findByCardNo(@Param("cardNo")String cardNo);
	
	List<UserCache> findByEmailAndAppid(@Param("email")String email,@Param("appid")Integer appid);

	List<UserCache> findByPhoneAndAppid(@Param("phone")String phone,@Param("appid")Integer appid);
	
	List<UserCache> findByCardNoAndAppid(@Param("cardNo")String cardNo,@Param("appid")Integer appid);
	
	
	void updateUserCache(UserCache userCache);
	UserCache findByUserInfo(@Param("username")String username,@Param("appid")Integer appid);
	List<UserCache> findAll();
	UserCache findUserById(Integer userId);

}