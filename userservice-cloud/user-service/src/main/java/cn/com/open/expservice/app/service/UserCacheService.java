package cn.com.open.expservice.app.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.expservice.app.entiy.User;
import cn.com.open.expservice.app.mapper.UserCacheMapper;
import cn.com.open.expservice.app.vo.UserJsonVo;
import cn.com.open.expservice.app.vo.UserMergeVo;


@Service
public class UserCacheService {
	@Autowired
	UserCacheMapper usercacheMapper;
	
	public ArrayList<User> findUserCacheByUsername(User user) {
		return usercacheMapper.findUserCacheByUsername(user.getUsername());
	}
	
	public void updateUserCache(User cache) {
		usercacheMapper.updateUserCache(cache);
	}

	public ArrayList<UserJsonVo> findUserCacheList(int userCacheId) {
		return usercacheMapper.findUserCacheList(userCacheId);
	}
	public UserMergeVo findByUserCache(User entity) {
		return usercacheMapper.findByUserCache(entity.getUsername(),entity.getPassword());
	}
}
