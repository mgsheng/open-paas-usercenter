package cn.com.open.user.platform.manager.infrastructure.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.com.open.user.platform.manager.app.model.App;


/**
 * 
 */
public interface AppRepository extends Repository {
	
	App findIdByClientId(String client_Id);

	List<App> findAll();

	App findById(Integer id);
	
	List<App> findByAppIds(Map map);
}