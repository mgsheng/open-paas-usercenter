package cn.com.open.openpaas.userservice.app.infrastructure.repository;

import cn.com.open.openpaas.userservice.app.log.model.LogMonitor;


/**
 * 
 */
public interface LogMonitorRepository extends Repository {
	
	void insert(LogMonitor logMonitor);

}