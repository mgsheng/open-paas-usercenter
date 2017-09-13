package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import cn.com.open.openpaas.userservice.app.user.model.User;
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
	public Boolean updateUserStatus(String guid,String status);
	Boolean save(UserCache userCache);
	void deleteUserCache(Integer id);
	public Boolean updateParentUserCache(String username,String phone,String email,String catdno,Integer id);
	public Boolean updateUserCardNoById(int id, String cardNo);
	public Boolean updatePhoneById(int id, String phone);
	public Boolean updateEmailById(int id, String email);

	List<UserCache> findUnprocessed();

	boolean updateUserCacheUnprocessed(List<UserCache> userCacheList);
	
	UserCache findByGuid(String guid);
    /**
     * 根据Pid查询用户
     * @param pid
     * @return
     */
    List<UserCache> findByPid(String pid);

    boolean updateDefaultUserById(int id, boolean b);
}