package cn.com.open.user.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.open.user.app.mapper.AppMapper;
import cn.com.open.user.app.service.AppService;

@Service
@Transactional
public class AppServiceImpl implements AppService {

	@Autowired
	private AppMapper appMapper;

	@Override
	public String findAppSecretByAppkey(String appKey) {
		return appMapper.findAppSecretByAppkey(appKey);
	}

}
