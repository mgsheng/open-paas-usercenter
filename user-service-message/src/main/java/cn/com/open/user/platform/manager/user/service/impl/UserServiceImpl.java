package cn.com.open.user.platform.manager.user.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import cn.com.open.user.platform.manager.infrastructure.repository.UserRepository;
import cn.com.open.user.platform.manager.tools.StringTool;
import cn.com.open.user.platform.manager.user.model.User;
import cn.com.open.user.platform.manager.user.service.UserService;


/**
 * 
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

	@Override
	public Boolean save(User user) {
		try{
			userRepository.saveUser(user);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	@Override
	public List<User> findByRealname(String realname) {
		return userRepository.findByRealname(realname);
	}

	@Override
	public User findUserById(Integer userId) {
		return userRepository.findUserById(userId);
	}

	@Override
	public Boolean updateUser(User user) {
		try{
			userRepository.updateUser(user);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	/*
	 *按真实姓名+身份证号查找用户 
	 */
	@Override
	public User findByRnIn(String realName, String identifyNo) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("realName", realName);
		map.put("identifyNo", identifyNo);
		return userRepository.findByRnIn(map);
	}

	/*
	 *按真实姓名+手机号查找用户 
	 */
	@Override
	public User findByRnPn(String realName, String phone) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("realName", realName);
		map.put("phone", phone);
		return userRepository.findByRnPn(map);
	}

	/*
	 *按真实姓名+邮箱查找用户 
	 */
	@Override
	public User findByRnEm(String realName, String email) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("realName", realName);
		map.put("email", email);
		return userRepository.findByRnEm(map);
	}

	@Override
	public List<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	@Override
	public User findByGuid(String guid) {
		return userRepository.findByGuid(guid);
	}

	@Override
	public List<User> findByPhone(String phone) {
		
		if(phone.length()<7)
		{
			return null;
		}
		return userRepository.findByPhone(phone);
	}
	
	
	@Override
	public Integer connectionTest() {
		return userRepository.connectionTest();
	}

	@Override
	public void deleteUser(Integer id) {
		userRepository.deleteUser(id);
	}


    /*
     * 根据奥鹏卡号查询用户（只取user_state=1的激活用户）
     * @see cc.wdcy.service.UserService#findByCardNo(java.lang.String)
     */
    @Override
    public List<User> findByCardNo(String cardNo) {
    	return userRepository.findByCardNo(cardNo);
    }

	/*
	 * 根据Pid查询用户
	 * @see cc.wdcy.service.UserService#findByPid(java.lang.String)
	 */
	@Override
    public List<User> findByPid(String pid) {
    	return userRepository.findByPid(pid);
    }
	
	/**
     * 根据id或Pid查询绑定用户
     * @param id
     * @param pid
     * @return
     */
    public List<User> findBindUserById(int id){
    	return userRepository.findBindUserById(id);
    }
    
    @Override
	public Boolean updateUserLastLoginTimeById(int id, Date lastLoginTime) {
		try{
			userRepository.updateUserLastLoginTimeById(id, lastLoginTime);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean updateUserCardNoById(int id, String cardNo) {
		try{
			userRepository.updateUserCardNoById(id, cardNo);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean updatePhoneById(int id, String phone) {
		try{
			userRepository.updatephoneById(id, phone);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean updateDefaultUserById(int id, Boolean defaultUser) {
		try{
			userRepository.updateDefaultUserById(id, defaultUser);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean updateDAPById(String id, Boolean defaultUser, String pid) {
		try{
			userRepository.updateDAPById(id, defaultUser,pid);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean updateParentUser(String username, String phone,
			String email, String cardno,Integer id) {
		try{
			userRepository.updateParentUser(username,phone,email,cardno ,id);
			return true;
		}catch(Exception e){
			return false;
		}
	}


	@Override
	public Boolean updateAesPwdById(int id, String pwd) {
		try{
			userRepository.updateAesPwdById(id, pwd);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Boolean updateSha1PwdById(int id, String pwd) {
		try{
			userRepository.updateSha1PwdById(id, pwd);
			return true;
		}catch(Exception e){
			return false;
		}
	}
}