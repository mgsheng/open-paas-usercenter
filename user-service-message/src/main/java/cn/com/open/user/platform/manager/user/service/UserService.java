package cn.com.open.user.platform.manager.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.com.open.user.platform.manager.user.model.User;


/**
 * 
 */
@Service
public interface UserService/* extends UserDetailsService*/ {

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
	
	List<User> findByEmail(String account);

	List<User> findByPhone(String account);
	
	Integer connectionTest();
	
	void deleteUser(Integer id);
	User findByGuid(String email);
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
    public Boolean updateDefaultUserById(int id, Boolean defaultUser);
    public Boolean updateAesPwdById(int id, String pwd);
    public Boolean updateSha1PwdById(int id, String pwd);
    public Boolean updateDAPById(String id, Boolean defaultUser,String pid);
    public Boolean updateParentUser(String username,String phone,String email,String catdno,Integer id);
	
}