package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.infrastructure.repository.UserCacheRepository;
import cn.com.open.openpaas.userservice.app.user.model.UserCache;

/**
 * 
 */
@Service("userCacheService")
public class UserCacheServiceImpl implements UserCacheService {
	@Autowired
	private UserCacheRepository userCacheRepository;

	@Override
	public UserCache findByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserCache> findByEmail(String account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserCache> findByPhone(String account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserCache> findByCardNo(String cardNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserCache(UserCache userCache) {
		userCacheRepository.updateUserCache(userCache);
		
	}

	@Override
	public List<UserCache> findAll() {
		// TODO Auto-generated method stub
		return userCacheRepository.findAll();
	}

	@Override
	public UserCache findUserById(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserCache findByUserInfo(String username, Integer appid) {
	   
		return userCacheRepository.findByUserInfo(username, appid);
	}

	@Override
	public List<UserCache> findByEmail(String account, Integer appid) {
		// TODO Auto-generated method stub
		return userCacheRepository.findByEmailAndAppid(account, appid);
	}

	@Override
	public List<UserCache> findByPhone(String account, Integer appid) {
		// TODO Auto-generated method stub
		return userCacheRepository.findByPhoneAndAppid(account, appid);
	}

	@Override
	public List<UserCache> findByCardNo(String cardNo, Integer appid) {
		// TODO Auto-generated method stub
		return userCacheRepository.findByCardNoAndAppid(cardNo, appid);
	}

 /*   @Autowired
    private UserCacheRepository userCacheRepository;

	@Override
	public UserCache findByUsername(String username) {
		return userCacheRepository.findByUsername(username);
	}

	@Override
	public List<UserCache> findByEmail(String email) {
		return userCacheRepository.findByEmail(email);
	}

	@Override
	public List<UserCache> findByPhone(String phone) {
		return userCacheRepository.findByPhone(phone);
	}
	
	@Override
    public List<UserCache> findByCardNo(String cardNo) {
    	return userCacheRepository.findByCardNo(cardNo);
    }
	
	@Override
	public void updateUserCache(UserCache userCache){
		userCacheRepository.updateUserCache(userCache);
	}
	
	@Override
	public List<UserCache> findAll() {
		return userCacheRepository.findAll();
	}

	@Override
	public UserCache findUserById(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}*/
}