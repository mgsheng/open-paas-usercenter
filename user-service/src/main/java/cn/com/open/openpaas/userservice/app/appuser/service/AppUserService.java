package cn.com.open.openpaas.userservice.app.appuser.service;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;


/**
 * 
 */
public interface AppUserService {

	int countByAppId(int appId);
	
	List<AppUser> pageByParameters(int start,int limit,String sortBy,String dir,int appId);

	Boolean saveAppUser(AppUser appUser);
	
	void save(AppUser appUser);

	AppUser findByCidSid(Integer appId, String sourceId);
	AppUser findByCidAUid(Integer appId, Integer uid);

	void updateAppUser(AppUser au);

	List<AppUser> findByCidUid(Integer id, Integer uid);

	List<AppUser> findByUserId(Integer id);

	List<AppUser> findAuthoritiesAppsByUserId(Integer userId,String appAuthorities,Integer appId,boolean isCache);
	void deleteAppUser(Integer id);
}