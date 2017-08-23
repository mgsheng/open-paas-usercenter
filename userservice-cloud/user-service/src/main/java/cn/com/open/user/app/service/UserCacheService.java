package cn.com.open.user.app.service;

import java.util.ArrayList;

import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;
import cn.com.open.user.app.vo.UserVo;


 
public interface UserCacheService {
	public ArrayList<User> findUserCacheByUsername(UserVo user);
	public void updateUserCache(UserVo cache);
	public ArrayList<UserListVo> findUserCacheList(int userCacheId);
	public UserMergeVo findByUserCache(User entity);
	public App findAppById(int appid);

}
