package cn.com.open.openpaas.userservice.app.user.service;

import java.util.List;

import cn.com.open.openpaas.userservice.app.useractivated.model.UserActivatedHis;

/**
 * 
 */
public interface UserActivatedHisService {
	UserActivatedHis getByCode(String code);
	List<UserActivatedHis>  getByCodeAndUserId(String code,String userId);
	List<UserActivatedHis>  getByCodeAndPhone(String code,String phone);
	List<UserActivatedHis>  getByCodeAndEmail(String code,String email);
	Boolean save(UserActivatedHis userActivatedHis);
}