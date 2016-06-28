package cn.com.open.openpaas.userservice.app.user.model;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;

/**
 * 
 */

public interface UserWorkRepository extends Repository {

	 void saveUserWork(UserWork userWork);

     UserWork findWorkByID(int app_uid);
     UserWork findWorkByUserID(int user_id);

     void updateUserWork(UserWork userWork);
}