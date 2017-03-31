package cn.com.open.openpaas.userservice.app.user.service;

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

import cn.com.open.openpaas.userservice.app.infrastructure.repository.UserRepository;
import cn.com.open.openpaas.userservice.app.shared.security.model.WdcyUserDetails;
import cn.com.open.openpaas.userservice.app.tools.StringTool;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserContect;
import cn.com.open.openpaas.userservice.app.user.model.UserContectRepository;
import cn.com.open.openpaas.userservice.app.user.model.UserDetail;
import cn.com.open.openpaas.userservice.app.user.model.UserDetailRepository;
import cn.com.open.openpaas.userservice.app.user.model.UserEducation;
import cn.com.open.openpaas.userservice.app.user.model.UserEducationRepository;
import cn.com.open.openpaas.userservice.app.user.model.UserFamily;
import cn.com.open.openpaas.userservice.app.user.model.UserFamilyRepository;
import cn.com.open.openpaas.userservice.app.user.model.UserJsonDto;
import cn.com.open.openpaas.userservice.app.user.model.UserSocial;
import cn.com.open.openpaas.userservice.app.user.model.UserSocialRepository;
import cn.com.open.openpaas.userservice.app.user.model.UserWork;
import cn.com.open.openpaas.userservice.app.user.model.UserWorkRepository;


/**
 * 
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	User user = null;
    	//如果用户名为纯数字，那么先按照奥鹏卡号查询
    	if(StringTool.isNumeric(username)){
    		List<User> userList = findByCardNo(username);
    		//有且只有一个激活的奥鹏卡号的用户
    		if(userList!=null && userList.size()==1){
    			user = userList.get(0);
    		}
    	}
    	//奥鹏卡号为空或没有满足条件则按用户名查询
    	if(user == null){
    		user = userRepository.findByUsername(username);
    	}
        if (user == null) {
            throw new UsernameNotFoundException("Not found any user for username[" + username + "]");
        }
        return new WdcyUserDetails(user);
    }

    @Override
    public UserJsonDto loadCurrentUserJsonDto() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Object principal = authentication.getPrincipal();

        if (authentication instanceof OAuth2Authentication &&
                (principal instanceof String || principal instanceof org.springframework.security.core.userdetails.User)) {
            return loadOauthUserJsonDto((OAuth2Authentication) authentication);
        } else {
            final WdcyUserDetails userDetails = (WdcyUserDetails) principal;
            return new UserJsonDto(userRepository.findByGuid(userDetails.user().guid()));
        }
    }


    private UserJsonDto loadOauthUserJsonDto(OAuth2Authentication oAuth2Authentication) {
        UserJsonDto userJsonDto = new UserJsonDto();
        userJsonDto.setUsername(oAuth2Authentication.getName());

        final Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            userJsonDto.getPrivileges().add(authority.getAuthority());
        }

        return userJsonDto;
    }

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

	@Override
	public void saveUserContect(UserContect userContect) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserDetail(UserDetail userDetail) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserEducation(UserEducation userEducation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserFamily(UserFamily userFamily) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserSocial(UserSocial userSocial) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveUserWork(UserWork userWork) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserContect findContectByAppUserId(int app_uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetail findDetailByAppUserId(int app_uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserEducation findEducationByAppUserId(int app_uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserSocial findSocailByAppUserId(int app_uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserWork findWorkByAppUserId(int app_uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateUserDetail(UserDetail userDetail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateUserEducation(UserEducation userEducation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateUserSocial(UserSocial userSocial) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateUserWork(UserWork userWork) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserDetail findDetailByUserID(int user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserSocial findSocailByUserID(int user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserWork findWorkByUserID(int user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserEducation findEducationByUserId(int user_id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean updateUserStatus(String guid, int stauts) {
		try {
			userRepository.updateUserStatus(guid,stauts);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}