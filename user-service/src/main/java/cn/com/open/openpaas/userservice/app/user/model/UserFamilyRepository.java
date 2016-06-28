package cn.com.open.openpaas.userservice.app.user.model;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;

/**
 * 
 */

public interface UserFamilyRepository extends Repository {

	 void saveUserFamily(UserFamily userFamily);

}