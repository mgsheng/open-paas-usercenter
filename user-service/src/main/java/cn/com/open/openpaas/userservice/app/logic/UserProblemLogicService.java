package cn.com.open.openpaas.userservice.app.logic;


/**
 * 用户密保问题logic
 */
public interface UserProblemLogicService {

	/**
	 * 保存用户密保问题
	 * @param userId
	 * @param problemId
	 * @param answer
	 * @return
	 */
	public boolean saveUserProblem(int userId,int problemId,String answer);
	
	/**
	 * 验证密保问题
	 * @param userId
	 * @param problemId
	 * @param newAnswer
	 * @return
	 */
	public boolean verifiedUserProblem(int userId,int problemId,String newAnswer);
}