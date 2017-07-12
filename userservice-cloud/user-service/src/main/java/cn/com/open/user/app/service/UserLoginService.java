package cn.com.open.user.app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.mapper.UserMapper;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.vo.UserJsonVo;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;
import cn.com.open.user.app.vo.UserVo;


@Service
public class UserLoginService {
	@Autowired
	UserMapper userMapper;
	
	public  UserMergeVo  findUserAccount(User entity){
		return userMapper.findUserAccount(entity.getUsername(),entity.getPassword());
	}
	
	public ArrayList<User> findUserAccountByUsername(UserVo user) {
		return userMapper.findUserAccountByUsername(user.getUsername());
	}


	public App findIdByClientId(String client_id) {
		return userMapper.findIdByClientId(client_id);
	}

	public void updateUserAccount(UserVo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String str = sdf.format(new Date());//日期转字符串
		map.put("id", user.getId());
		map.put("aesPassword", user.getPassword());
		map.put("lastloginTime", str);
		userMapper.updateUserAccount(map);
	}

	public ArrayList<UserListVo> findUserAccountList(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		return userMapper.findUserAccountList(map);
	}
}
