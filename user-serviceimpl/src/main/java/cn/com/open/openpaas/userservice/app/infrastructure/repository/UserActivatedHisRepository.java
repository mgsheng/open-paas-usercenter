package cn.com.open.openpaas.userservice.app.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivatedHis;


/**
 * 
 */

public interface UserActivatedHisRepository extends Repository {

	UserActivatedHis getByCode(String code);
	List<UserActivatedHis> getByCodeAndUserId(@Param("code")String code,@Param("userId")String userId);
	List<UserActivatedHis> getByCodeAndPhone(@Param("code")String code,@Param("phone")String phone);
	void save(UserActivatedHis userActivatedHis);

}