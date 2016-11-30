package cn.com.open.user.platform.manager.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.com.open.user.platform.manager.app.service.AppService;
import cn.com.open.user.platform.manager.appuser.service.AppUserService;
import cn.com.open.user.platform.manager.dev.UserManagerDev;
import cn.com.open.user.platform.manager.kafka.KafkaConsumer;
import cn.com.open.user.platform.manager.redis.impl.RedisClientTemplate;
import cn.com.open.user.platform.manager.user.service.UserService;

/**
 * 项目启动执行任务
 * @author dongminghao
 *
 */
public class InitJob implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger log = Logger.getLogger(InitJob.class);
	@Autowired
	private UserManagerDev userManagerDev;  
    @Autowired
	private UserService userService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private AppService appService; 
	@Autowired
	private RedisClientTemplate redisClient;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("onApplicationEvent:~~~~~~~~~~~~~~~~~~~~~~~~~"+event.getApplicationContext().getDisplayName());
		if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
         log.info("~~~~~~~~~~~~~~~Kafka message service start~~~~~~~~~~~~~~~~");
		Thread thread = new Thread( new KafkaConsumer(userManagerDev,userService,appUserService,appService,redisClient));
		thread.run();/*
		   log.info("~~~~~~~~~~~~~~~Kafka message service start2~~~~~~~~~~~~~~~~");
			Thread thread2 = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
			thread2.run();*/
		}
	}
}
