package cn.com.open.openpaas.userservice.app.user.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.user.model.UserLoadDetail;
import cn.com.open.openpaas.userservice.app.user.model.UserLoadDetailRepository;

/**
 * 
 */
@Service("userLoadDetailService")
public class UserLoadDetailServiceImpl implements UserLoadDetailService {

    @Autowired
    private UserLoadDetailRepository userLoadDetailRepository;

	@Override
	public int insert(UserLoadDetail userLoadDetail) {
		return userLoadDetailRepository.insert(userLoadDetail);
	}

	@Override
	public UserLoadDetail getLastByUserId(int userId) {
		return userLoadDetailRepository.getLastByUserId(userId);
	}

	@Override
	public List<UserLoadDetail> pageByUserId(int userId, int start, int limit) {
		return userLoadDetailRepository.pageByUserId(userId, start, limit);
	}

}