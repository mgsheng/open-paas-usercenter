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
		return userCacheRepository.findByUsername(username);
	}

	@Override
	public List<UserCache> findByEmail(String account) {
		// TODO Auto-generated method stub
		return userCacheRepository.findByEmail(account);
	}

	@Override
	public List<UserCache> findByPhone(String account) {
		// TODO Auto-generated method stub
		return userCacheRepository.findByPhone(account);
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
		return userCacheRepository.findUserById(userId);
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

	@Override
	public Boolean updateUserStatus(String guid, String status) {
		// TODO Auto-generated method stub
		try {
			userCacheRepository.updateUserStatus(guid, status);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public Boolean save(UserCache userCache) {
		try{
			userCacheRepository.saveUserCache(userCache);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public void deleteUserCache(Integer id) {
		userCacheRepository.deleteUser(id);
		
	}

	@Override
	public Boolean updateParentUserCache(String username, String phone, String email, String cardno, Integer id) {
		// TODO Auto-generated method stub
		try {
			userCacheRepository.updateParentUser(username, phone, email, cardno, id);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public Boolean updateUserCardNoById(int id, String cardNo) {
		// TODO Auto-generated method stub
		try {
			userCacheRepository.updateUserCardNoById(id, cardNo);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public List<UserCache> findUnprocessed() {
		return userCacheRepository.findUnprocessed();
	}

	@Override
	public boolean updateUserCacheUnprocessed(List<UserCache> userCacheList) {
		try{
			userCacheRepository.updateUserCacheUnprocessed(userCacheList);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public UserCache findByGuid(String guid) {
		// TODO Auto-generated method stub
		return userCacheRepository.findByGuid(guid);
	}

	@Override
	public Boolean updatePhoneById(int id, String phone) {
		try{
			userCacheRepository.updatePhoneById(id, phone);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean updateEmailById(int id, String email) {
		// TODO Auto-generated method stub
		try{
			userCacheRepository.updateEmailById(id, email);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public List<UserCache> findByPid(String pid) {
		// TODO Auto-generated method stub
		return  userCacheRepository.findByPid(pid);
	}

	@Override
	public boolean updateDefaultUserById(int id, boolean b) {
		try{
			userCacheRepository.updateDefaultUserById(id, b);
			return true;
		}catch(Exception e){
			return false;
		}
		
	}

}