package cn.com.open.openpaas.userservice.app.appuser.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.infrastructure.repository.AppUserRepository;

/**
 * 
 */
@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public int countByAppId(int appId) {
        return appUserRepository.countByAppId(appId);
    }
    
    @Override
    public List<AppUser> pageByParameters(int start,int limit,String sortBy,String dir,int appId) {
    	return appUserRepository.pageByParameters(start, limit, sortBy, dir,appId);
    }

    @Override
	public void save(AppUser appUser) {
		appUser.createTime(new Date().getTime());
		appUser.lastloginTime(new Date().getTime());
		if(appUser.loginTimes()==null){
			appUser.loginTimes(0);
		}
		appUserRepository.saveAppUser(appUser);
	}
    
	@Override
	public Boolean saveAppUser(AppUser appUser) {
		appUser.createTime(new Date().getTime());
		appUser.lastloginTime(new Date().getTime());
		if(appUser.loginTimes()==null){
			appUser.loginTimes(1);
		}else{
			appUser.loginTimes(appUser.loginTimes()+1);
		}
		try{
			appUserRepository.saveAppUser(appUser);
			return true;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public AppUser findByCidSid(Integer appId, String sourceId) {
		Map map=new HashMap();
		map.put("appId", appId);
		map.put("sourceId", sourceId);
		return appUserRepository.findByCidSid(map);
	}
	@Override
	public AppUser findByCidAUid(Integer appId, Integer uid) {
		Map map=new HashMap();
		map.put("appId", appId);
		map.put("userId", uid);
		return appUserRepository.findByCidAUid(map);
	}
	@Override
	public void updateAppUser(AppUser au) {
		au.lastloginTime(new Date().getTime());
		if(au.loginTimes()==null){
			au.loginTimes(0);
		}
		appUserRepository.updateAppUser(au);
	}

	@Override
	public List<AppUser> findByCidUid(Integer id, Integer uid) {
		Map map=new HashMap();
		map.put("appId", id);
		map.put("userId", uid);
		return appUserRepository.findByCidUid(map);
	}

	@Override
	public List<AppUser> findByUserId(Integer uid) {
		List<AppUser> list=appUserRepository.findByUserId(uid);
		if(list==null || list.size()==0){
			return null;
		}else{
			return list;
		}
	}

	@Override
	public List<AppUser> findAuthoritiesAppsByUserId(Integer userId,String appAuthorities,Integer appId,boolean isCache) {
		Map map=new HashMap();
		Integer[] b ;
		if(appAuthorities==null || appAuthorities.length()==0){
			b = new Integer[]{0};
		}else{
			String[] a=appAuthorities.split(",");
			b = new Integer[a.length];
			if(a!=null && a.length!=0){
				for(int i=0;i<a.length;i++){
					b[i]=Integer.parseInt(a[i]);
				}
			}
		}
		map.put("appAuthorities", b);
		map.put("userId", userId);
		map.put("appId", appId);
		map.put("isCache", isCache?1:0);
		List<AppUser> list=appUserRepository.findAuthoritiesAppsByUserId(map);
		if(list==null || list.size()==0){
			return null;
		}else{
			return list;
		}
	}

	@Override
	public void deleteAppUser(Integer id) {
		appUserRepository.deleteAppUser(id);
	}
}