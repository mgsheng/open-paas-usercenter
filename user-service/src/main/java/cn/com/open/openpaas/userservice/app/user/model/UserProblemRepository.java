package cn.com.open.openpaas.userservice.app.user.model;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;

/**
 * 
 */

public interface UserProblemRepository extends Repository {

	 void save(UserProblem userProblem);
     UserProblem getById(
    		 @Param("id")Integer id);
     void update(UserProblem userProblem);
     void delete(
    		 @Param("id")Integer id);
     UserProblem getByUserIdAndProblemId(
    		 @Param("userId")Integer userId,
    		 @Param("problemId")int problemId);
     List<UserProblem> findListByUserId(
    		 @Param("userId")Integer userId);
     void deleteByUserId(
    		 @Param("userId")Integer userId);
}