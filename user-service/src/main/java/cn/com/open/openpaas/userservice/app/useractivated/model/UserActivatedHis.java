package cn.com.open.openpaas.userservice.app.useractivated.model;

import java.util.Date;

/**
 * 功能：用户激活数据历史
 * lingliangliang
 */

public class UserActivatedHis {
	
	//状态
	public static int USERTYPE_USER = 1;		//普通用户（前台一般用户注册验证）
	public static int USERTYPE_DEVUSER = 2;		//开发者（开发者信息验证）
	
	int id;
    int userId;
    String email;
    String phone;
    String code;
    int userType = 0;
    long createTime = new Date().getTime();
    
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}