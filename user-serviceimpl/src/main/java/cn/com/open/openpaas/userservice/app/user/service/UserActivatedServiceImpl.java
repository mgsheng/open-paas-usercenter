package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.infrastructure.repository.UserActivatedRepository;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivated;
import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivatedHis;


/**
 * 
 */
@Service("userActivatedService")
public class UserActivatedServiceImpl implements UserActivatedService {

	  @Autowired
	    private UserActivatedRepository userActivatedRepository;
	    @Autowired
	    private UserActivatedHisService userActivatedHisService;

		@Override
		public UserActivated findByCode(String code) {
			return userActivatedRepository.findByCode(code);
		}

		@Override
		public List<UserActivated> findByUserActivated(UserActivated userActivated){
			return userActivatedRepository.findByUserActivated(userActivated);
		}

		@Override
		public int deleteById(Integer id) {
			return userActivatedRepository.deleteById(id);
		}
		
		@Override
		public Boolean save(UserActivated userActivated) {
			try{
				userActivatedRepository.save(userActivated);
				return true;
			}catch(Exception e){
				return false;
			}
		}
		@Override
		public Boolean update(UserActivated userActivated) {
			try{
				userActivatedRepository.update(userActivated);
				return true;
			}catch(Exception e){
				return false;
			}
		}
		@Override
		public Boolean moveUserActivated(UserActivated userActivated){
			try {
				UserActivatedHis userActivatedHis = new UserActivatedHis();
				userActivatedHis.setCode(userActivated.getCode());
				userActivatedHis.setCreateTime(userActivated.getCreateTime());
				userActivatedHis.setEmail(userActivated.getEmail());
				userActivatedHis.setPhone(userActivated.getPhone());
				userActivatedHis.setUserId(userActivated.getUserId());
				userActivatedHis.setUserType(userActivated.getUserType());
				userActivatedHisService.save(userActivatedHis);
				this.deleteByCodeAndPhone(userActivated.getCode(),userActivated.getPhone());
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public UserActivated findByCodeAndUserid(String code, int userId) {
			// TODO Auto-generated method stub
			return userActivatedRepository.findByCodeAndUserid(code,userId);
		}

		@Override
		public Boolean updateUserActivated(UserActivated userActivated) {
			try{
				userActivatedRepository.updateUserActivated(userActivated);
				return true;
			}catch(Exception e){
				return false;
			}
		}

		@Override
		public int deleteByCodeAndPhone(String code, String phone) {
			// TODO Auto-generated method stub
			return userActivatedRepository.deleteByCodeAndPhone(code, phone);
		}

}