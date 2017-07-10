package cn.com.open.expservice.app.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.expservice.app.entiy.User;
import cn.com.open.expservice.app.mapper.UserMapper;
import cn.com.open.expservice.app.model.App;
import cn.com.open.expservice.app.vo.UserJsonVo;
import cn.com.open.expservice.app.vo.UserMergeVo;


@Service
public class UserLoginService {
	@Autowired
	UserMapper userMapper;
	
	public  UserMergeVo  findUserAccount(User entity){
		return userMapper.findUserAccount(entity.getUsername(),entity.getPassword());
	}
	
	public  ArrayList<UserJsonVo>  findUserAll(UserMergeVo mergeVo){
		return userMapper.findUserAll(mergeVo);
	}
	
	public ArrayList<User> findUserAccountByUsername(User user) {
		return userMapper.findUserAccountByUsername(user.getUsername());
	}


	public App findIdByClientId(String client_id) {
		return userMapper.findIdByClientId(client_id);
	}

	public void updateUserAccount(User user2) {
		 userMapper.updateUserAccount(user2);
	}

	public ArrayList<UserJsonVo> findUserAccountList(int userId) {
		return userMapper.findUserAccountList(userId);
	}
}
