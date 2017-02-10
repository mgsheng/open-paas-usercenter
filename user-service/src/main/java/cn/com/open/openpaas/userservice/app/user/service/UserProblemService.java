package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import cn.com.open.openpaas.userservice.app.user.model.UserProblem;

/**
 * 
 */
public interface UserProblemService {

	Boolean save(UserProblem userProblem);
	Boolean delete(Integer id);
	UserProblem getById(Integer id);
	UserProblem getByUserIdAndProblemId(Integer userId,int problemId);
	Boolean update(UserProblem userProblem);
	List<UserProblem> findListByUserId(Integer userId);
	boolean deleteByUserId(Integer userId);
}