package cn.com.open.openpaas.userservice.app.user.model;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;

/**
 * 
 */

public interface UserSocialRepository extends Repository {

	 void saveUserSocial(UserSocial userSocial);
     UserSocial findSocailByID(int app_uid);
     UserSocial findSocailByUserID(int user_id);
     void updateUserSocial(UserSocial userSocial);
}