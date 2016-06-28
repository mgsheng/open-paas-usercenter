package cn.com.open.openpaas.userservice.app.user.model;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;

/**
 * 
 */

public interface UserDetailRepository extends Repository {

	 void saveUserDetail(UserDetail userDetail);
	 
     UserDetail findDetailByID(int app_uid);
     UserDetail findDetailByUserID(int user_id);

     void updateUserDetail(UserDetail userDetail);
}