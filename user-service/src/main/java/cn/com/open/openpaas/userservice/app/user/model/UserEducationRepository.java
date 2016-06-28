package cn.com.open.openpaas.userservice.app.user.model;

import cn.com.open.openpaas.userservice.app.shared.model.Repository;

/**
 * 
 */

public interface UserEducationRepository extends Repository {

	 void saveUserEducation(UserEducation userEducation);

     UserEducation findEducationByID(int apps_uid);
     UserEducation findEducationByUserID(int user_id);

     void updateUserEducation(UserEducation userEducation);
}