package cn.com.open.openpaas.userservice.app.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserContect;
import cn.com.open.openpaas.userservice.app.user.model.UserDetail;
import cn.com.open.openpaas.userservice.app.user.model.UserEducation;
import cn.com.open.openpaas.userservice.app.user.model.UserFamily;
import cn.com.open.openpaas.userservice.app.user.model.UserJsonDto;
import cn.com.open.openpaas.userservice.app.user.model.UserSocial;
import cn.com.open.openpaas.userservice.app.user.model.UserWork;


/**
 * 
 */
@Service
public interface UserService extends UserDetailsService {
	
	@Override
	UserDetails loadUserByUsername(String s);

    UserJsonDto loadCurrentUserJsonDto();

	Boolean save(User user);
	
	User findByUsername(String username);
	
	User findUserById(Integer userId);
	
	//按真实姓名获取用户列表
	List<User> findByRealname(String realname);
	
	//更新user_account信息
	Boolean updateUser(User user);

	//通过real_name+identify_no查找user_account
	User findByRnIn(String realName, String identifyNo);

	//通过real_name+phone查找user_account
	User findByRnPn(String realName, String phone);

	//通过real_name+email查找user_account
	User findByRnEm(String realName, String email);
	
	void saveUserContect(UserContect userContect);
	
	void saveUserDetail(UserDetail userDetail);
	
	void saveUserEducation(UserEducation userEducation);
	
	void saveUserFamily(UserFamily userFamily);
	
	void saveUserSocial(UserSocial userSocial);
	
	void saveUserWork(UserWork userWork);

	List<User> findByEmail(String account);

	List<User> findByPhone(String account);
	
	Integer connectionTest();
	
	void deleteUser(Integer id);

    UserContect findContectByAppUserId(int app_uid);

    UserDetail findDetailByAppUserId(int app_uid);

    UserEducation findEducationByAppUserId(int app_uid);

    UserSocial findSocailByAppUserId(int app_uid);

    UserWork findWorkByAppUserId(int app_uid);

    Boolean updateUserDetail(UserDetail userDetail);

    Boolean updateUserEducation(UserEducation userEducation);

    Boolean updateUserSocial(UserSocial userSocial);

    Boolean updateUserWork(UserWork userWork);

	User findByGuid(String email);
	//通过userid查询 userDetail
	UserDetail findDetailByUserID(int user_id);
    //通过userid 查询 usersocial
    UserSocial findSocailByUserID(int user_id);
    //通过userid 查询 userWork
    UserWork findWorkByUserID(int user_id);
    //通过 userid 查询usereducation 信息
    UserEducation findEducationByUserId(int user_id);
	/**
     * 根据奥鹏卡号查询用户（只取user_state=1的激活用户）
     * @param cardNo
     * @return
     */
    List<User> findByCardNo(String cardNo);
    
    /**
     * 根据Pid查询用户
     * @param pid
     * @return
     */
    List<User> findByPid(String pid);
    
    /**
     * 根据id或Pid查询绑定用户
     * @param id
     * @param pid
     * @return
     */
    List<User> findBindUserById(int id);
    
    public Boolean updateUserLastLoginTimeById(int id, Date lastLoginTime);
    public Boolean updatePhoneById(int id, String phone);
    public Boolean updateUserCardNoById(int id, String cardNo);
    public Boolean updateEmailById(int id, String email);
    public Boolean updateDefaultUserById(int id, Boolean defaultUser);
    public Boolean updateAesPwdById(int id, String pwd);
    public Boolean updateSha1PwdById(int id, String pwd);
    public Boolean updateDAPById(String id, Boolean defaultUser,String pid);
    public Boolean updateUserStatus(String guid,int status);
    public Boolean updateParentUser(String username,String phone,String email,String catdno,Integer id);
	
}