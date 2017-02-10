package cn.com.open.openpaas.userservice.app.infrastructure.repository;

import java.util.List;

import cn.com.open.openpaas.userservice.app.user.model.UserDict;

/**
 * 
 */

public interface UserDictRepository extends Repository {

	List<UserDict> findListByType(Integer type);
	UserDict getById(Integer id);

}