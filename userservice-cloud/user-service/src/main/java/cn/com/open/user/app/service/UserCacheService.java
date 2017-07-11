package cn.com.open.user.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.mapper.UserCacheMapper;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;
import cn.com.open.user.app.vo.UserVo;


@Service
public class UserCacheService {
	@Autowired
	UserCacheMapper usercacheMapper;
	
	public ArrayList<User> findUserCacheByUsername(UserVo user) {
		return usercacheMapper.findUserCacheByUsername(user.getUsername());
	}
	
	public void updateUserCache(UserVo cache) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", cache.getId());
		map.put("aesPassword", cache.getPassword());
		usercacheMapper.updateUserCache(map);
	}

	public ArrayList<UserListVo> findUserCacheList(int userCacheId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userCacheId);
		return usercacheMapper.findUserCacheList(map);
	}
	public UserMergeVo findByUserCache(User entity) {
		return usercacheMapper.findByUserCache(entity.getUsername(),entity.getPassword());
	}

	public App findAppById(int appid) {
		return usercacheMapper.findAppById(appid);
	}

}
