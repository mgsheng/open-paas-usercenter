package cn.com.open.openpaas.userservice.app.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;


/**
 * 
 */

public interface UserActivatedRepository extends Repository {

	UserActivated findByCode(String code);
	UserActivated findByCodeAndUserid(String code,int userId);
	List<UserActivated> findByUserActivated(UserActivated userActivated);
	int deleteById(Integer id);
	int deleteByCodeAndPhone(@Param("code")String code,@Param("phone")String phone);
	void save(UserActivated userActivated);
	void update(UserActivated userActivated);
	void updateUserActivated(UserActivated userActivated);

}