package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.infrastructure.repository.UserDictRepository;
import cn.com.open.openpaas.userservice.app.user.model.UserDict;

/**
 * 
 */
@Service("userDictService")
public class UserDictServiceImpl implements UserDictService {

    @Autowired
    private UserDictRepository userdictRepository;


    public List<UserDict> findListByType(Integer type){
    	return userdictRepository.findListByType(type);
    }
    public UserDict getById(Integer id){
		return userdictRepository.getById(id);
	}


}