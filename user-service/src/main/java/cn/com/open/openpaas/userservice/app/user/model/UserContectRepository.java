package cn.com.open.openpaas.userservice.app.user.model;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;


/**
 * 
 */

public interface UserContectRepository extends Repository {

	 void saveUserContect(UserContect userContect);

     UserContect findContectByID(int app_uid);

}