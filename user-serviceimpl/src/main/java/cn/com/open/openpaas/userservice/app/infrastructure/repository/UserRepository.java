package cn.com.open.openpaas.userservice.app.infrastructure.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.open.openpaas.userservice.app.user.model.User;

/**
 * 
 */

public interface UserRepository extends Repository {

    User findByGuid(String guid);

    void saveUser(User user);

    void updateUser(User user);

    User findByUsername(String username);

	User findUserById(Integer userId);

	List<User> findByRealname(String realname);

	//按real_name+identify_no查找user_account
	User findByRnIn(Map map);

	//按real_name+phone查找user_account
	User findByRnPn(Map map);

	//按real_name+email查找user_account
	User findByRnEm(Map map);

	//按real_name+identify_no+phone查找user_account
	User findByRnInPn(Map map);

	//按real_name+identify_no+phone+email查找user_account
	User findByRnInPnEm(Map map);

	List<User> findByEmail(String email);

	List<User> findByPhone(String phone);
	
	Integer connectionTest();
	void deleteUser(Integer id);
	
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
    
    void updateUserLastLoginTimeById(@Param("id")int id,@Param("lastLoginTime")Date lastLoginTime);
    void updateUserCardNoById(@Param("id")int id,@Param("cardNo")String cardNo);
    void updatephoneById(@Param("id")int id,@Param("phone")String phone);
    void updateDefaultUserById(@Param("id")int id,@Param("defaultUser")Boolean defaultUser);
    void updateAesPwdById(@Param("id")int id,@Param("aesPassword")String aesPassword);
    void updateSha1PwdById(@Param("id")int id,@Param("sha1Password")String sha1Password);
	void updateDAPById(@Param("id")String id,@Param("defaultUser")Boolean defaultUser,@Param("pid")String pid);
	void updateUserStatus(@Param("guid")String guid,@Param("status")int status);
	void updateParentUser(@Param("username")String username,@Param("phone")String phone,@Param("email")String email,@Param("cardno")String cardno,@Param("id")int id);
    

}