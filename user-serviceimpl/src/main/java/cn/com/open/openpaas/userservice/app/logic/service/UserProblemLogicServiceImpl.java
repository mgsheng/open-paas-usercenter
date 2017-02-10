package cn.com.open.openpaas.userservice.app.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.logic.UserProblemLogicService;
import cn.com.open.openpaas.userservice.app.user.model.UserProblem;
import cn.com.open.openpaas.userservice.app.user.service.UserProblemService;

/**
 * 用户密码问题logic
 */
@Service("userProblemLogicService")
public class UserProblemLogicServiceImpl implements UserProblemLogicService {

    @Autowired
    private UserProblemService userProblemService;

	/**
	 * 保存用户密保问题
	 * @param userId
	 * @param problemId
	 * @param answer
	 * @return
	 */
	public boolean saveUserProblem(int userId,int problemId,String answer){
		UserProblem userProblem = userProblemService.getByUserIdAndProblemId(userId, problemId);
    	if(userProblem != null){
    		userProblem.setAnswer(answer);
    		userProblemService.update(userProblem);
			return true;
    	}else{
    		userProblem = new UserProblem(userId, problemId,answer);
    		userProblemService.save(userProblem);
    	}
		return false;
	}
	
    /**
	 * 验证密码问题
	 * @param userId
	 * @param problemId
	 * @param newAnswer
	 * @return
	 */
    public boolean verifiedUserProblem(int userId,int problemId,String newAnswer){
    	UserProblem userProblem = userProblemService.getByUserIdAndProblemId(userId, problemId);
    	if(userProblem != null && userProblem.getAnswer().equals(newAnswer)){
			return true;
    	}
    	return false;
    }
}