package cn.com.open.user.platform.manager.infrastructure.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.com.open.user.platform.manager.appuser.model.AppUser;


/**
 * 
 */

public interface AppUserRepository extends Repository {
	
	int countByAppId(int appId);
	
	List<AppUser> pageByParameters(
			@Param("start")int start,
			@Param("limit")int limit,
			@Param("sortBy")String sortBy,
			@Param("dir")String dir,
			@Param("appId")int appId
			);

	void saveAppUser(AppUser appUser);

	AppUser findByCidSid(Map map);
	AppUser findByCidAUid(Map map);
	void updateAppUser(AppUser au);

	List<AppUser> findByCidUid(Map map);

	List<AppUser> findByUserId(Integer uid);
	
	List<AppUser> findAuthoritiesAppsByUserId(Map map);
	void deleteAppUser(Integer id);
	
}