package cn.com.open.openpaas.userservice.app.thread;

import java.util.Date;

import cn.com.open.openpaas.userservice.app.user.service.UserService;

/**
 * 用户修改登录时间操作
 * @author dongminghao
 *
 */
public class UserUpdatePwdThread implements Runnable {

	private UserService userService;
	private int id;
	
	public UserUpdatePwdThread(int id, UserService userService){
		this.id = id;
		this.userService = userService;
	}
	
	@Override
	public void run() {
		if(id == 0 || userService == null){
			return;
		}
		userService.updateUserLastLoginTimeById(id,new Date());
	}

}
