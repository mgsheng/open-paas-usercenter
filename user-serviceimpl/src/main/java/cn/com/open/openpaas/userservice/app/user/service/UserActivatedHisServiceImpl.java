package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.infrastructure.repository.UserActivatedHisRepository;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivatedHis;


/**
 * 
 */
@Service("userActivatedHisService")
public class UserActivatedHisServiceImpl implements UserActivatedHisService {

	@Override
	public UserActivatedHis getByCode(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserActivatedHis> getByCodeAndUserId(String code, String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserActivatedHis> getByCodeAndPhone(String code, String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean save(UserActivatedHis userActivatedHis) {
		// TODO Auto-generated method stub
		return null;
	}

//    @Autowired
//    private UserActivatedHisRepository userActivatedHisRepository;


//	@Override
//	public UserActivatedHis getByCode(String code) {
//		return userActivatedHisRepository.getByCode(code);
//	}
//
//	@Override
//	public Boolean save(UserActivatedHis userActivatedHis){
//		try{
//			userActivatedHisRepository.save(userActivatedHis);
//			return true;
//		}catch(Exception e){
//			return false;
//		}
//	}
//
//	@Override
//	public  List<UserActivatedHis>  getByCodeAndUserId(String code, String userId) {
//		// TODO Auto-generated method stub
//		return userActivatedHisRepository.getByCodeAndUserId(code,userId);
//	}

//	@Override
//	public List<UserActivatedHis> getByCodeAndPhone(String code, String phone) {
//		// TODO Auto-generated method stub
//		return userActivatedHisRepository.getByCodeAndPhone(code,phone);
//	}


}