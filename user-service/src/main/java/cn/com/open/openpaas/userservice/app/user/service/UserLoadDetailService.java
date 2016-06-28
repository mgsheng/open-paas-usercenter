package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import cn.com.open.openpaas.userservice.app.user.model.UserLoadDetail;

/**
 * 
 */
public interface UserLoadDetailService {

	/**
	 * 插入
	 * @param userLoadDetail
	 * @return
	 */
	int insert(UserLoadDetail userLoadDetail);
	
	/**
	 * 根据用户Id获取最近一次登录信息
	 * @param userId
	 * @return
	 */
	UserLoadDetail getLastByUserId(int userId);
	
	/**
	 * 根据用户Id获取登录记录
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 */
	List<UserLoadDetail> pageByUserId(int userId,int start,int limit);
}