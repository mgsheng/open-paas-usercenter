package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;

/**
 * 
 */
public interface UserActivatedService {

	UserActivated findByCode(String code);
	UserActivated findByCodeAndUserid(String code,int userId);
	List<UserActivated> findByUserActivated(UserActivated userActivated);
	int deleteById(Integer id);
	int deleteByCodeAndPhone(String code,String phone);
	Boolean save(UserActivated userActivated);
	Boolean update(UserActivated userActivated);
	Boolean updateUserActivated(UserActivated userActivated);
	
	/**
	 * 迁移验证数据到验证历史表
	 * @param userActivated
	 * @return
	 */
	Boolean moveUserActivated(UserActivated userActivated);
}