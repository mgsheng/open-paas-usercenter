package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.user.model.UserProblem;
import cn.com.open.openpaas.userservice.app.user.model.UserProblemRepository;

/**
 * 
 */
@Service("userProblemService")
public class UserProblemServiceImpl implements UserProblemService {

    @Autowired
    private UserProblemRepository userProblemRepository;


	@Override
	public UserProblem getById(Integer id) {
		return userProblemRepository.getById(id);
	}
	
	@Override
	public Boolean save(UserProblem userProblem) {
		try{
			userProblemRepository.save(userProblem);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	@Override
	public Boolean update(UserProblem userProblem){
		try {
			userProblemRepository.update(userProblem);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean delete(Integer id){
		try {
			userProblemRepository.delete(id);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public UserProblem getByUserIdAndProblemId(Integer userId,int problemId){
		return userProblemRepository.getByUserIdAndProblemId(userId, problemId);
	}
	
	public List<UserProblem> findListByUserId(Integer userId){
		return userProblemRepository.findListByUserId(userId);
	}

	public boolean deleteByUserId(Integer userId){
		try {
			userProblemRepository.deleteByUserId(userId);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}