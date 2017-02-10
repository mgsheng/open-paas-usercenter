package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import cn.com.open.openpaas.userservice.app.user.model.UserDict;

/**
 * 用户字典service
 */
public interface UserDictService {

	List<UserDict> findListByType(Integer type);
	UserDict getById(Integer id);
}