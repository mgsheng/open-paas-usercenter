package cn.com.open.openpaas.userservice.app.user.model;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;

/**
 * 
 */
public interface UserLoadDetailRepository extends Repository {

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
	UserLoadDetail getLastByUserId(
			@Param("userId")int userId
			);
	
	/**
	 * 根据用户Id获取登录记录
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 */
	List<UserLoadDetail> pageByUserId(
			@Param("userId")int userId,
			@Param("start")int start,
			@Param("limit")int limit
			);
}