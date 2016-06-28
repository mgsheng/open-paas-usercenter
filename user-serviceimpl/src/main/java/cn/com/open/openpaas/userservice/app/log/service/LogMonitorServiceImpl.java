package cn.com.open.openpaas.userservice.app.log.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.infrastructure.repository.LogMonitorRepository;
import cn.com.open.openpaas.userservice.app.log.model.LogMonitor;

/**
 * 
 */
@Service("logMonitorService")
public class LogMonitorServiceImpl implements LogMonitorService {

    @Autowired
    private LogMonitorRepository logMonitorRepository;

	@Override
    public void insert(LogMonitor logMonitor) {
		logMonitorRepository.insert(logMonitor);
    }

}