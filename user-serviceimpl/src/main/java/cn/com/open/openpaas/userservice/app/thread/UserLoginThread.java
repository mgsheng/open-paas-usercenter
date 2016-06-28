package cn.com.open.openpaas.userservice.app.thread;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import cn.com.open.openpaas.userservice.app.common.model.Common;
import cn.com.open.openpaas.userservice.app.tools.NetworkUtil;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.model.UserLoadDetail;
import cn.com.open.openpaas.userservice.app.user.service.UserLoadDetailService;

/**
 * 用户登录后的记录操作
 * @author dongminghao
 *
 */
public class UserLoginThread implements Runnable {

	private UserLoadDetailService userLoadDetailService;
	private User user;
	private HttpServletRequest request;
	
	public UserLoginThread(User user, HttpServletRequest request,UserLoadDetailService userLoadDetailService){
		this.user = user;
		this.request = request;
		this.userLoadDetailService = userLoadDetailService;
	}
	
	@Override
	public void run() {
		if(user == null || request == null){
			return;
		}
		String ip = NetworkUtil.getIpAddress(request);
		String province = NetworkUtil.getIpLookup(ip);
		//记录登录信息
		UserLoadDetail userLoadDetail = new UserLoadDetail();
    	userLoadDetail.setAppId(Common.APPID);
    	userLoadDetail.setAppName("");
    	userLoadDetail.setLoadAdd(province);
    	userLoadDetail.setLoadIp(ip);
    	userLoadDetail.setLoadTime(new Date().getTime());
    	userLoadDetail.setUserId(user.getId());
    	userLoadDetail.setUserName(user.getUsername());
    	userLoadDetailService.insert(userLoadDetail);
	}

}
