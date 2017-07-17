package cn.com.open.user.app.vo;

import java.io.Serializable;
import java.util.ArrayList;

import cn.com.open.user.app.entiy.User;

public class UserMergeVo implements Serializable{
	private String username;
	private String guid;
	private String phone;
	private String email;
//	private String pwdRule;
//	private String pwdMsg;
//	private String jsessionId;
	
	
	private ArrayList<UserJsonVo> infoList;
	
	public UserMergeVo(User u) {
		this.username = u.getUsername();
		this.guid = u.getGuid();
		this.phone = "NULL".equals(u.getPhone())?"":u.getPhone();
		this.email = "NULL".equals(u.getEmail())?"":u.getEmail();
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}/*
	public String getPwdRule() {
		return pwdRule;
	}
	public void setPwdRule(String pwdRule) {
		this.pwdRule = pwdRule;
	}
	public String getPwdMsg() {
		return pwdMsg;
	}
	public void setPwdMsg(String pwdMsg) {
		this.pwdMsg = pwdMsg;
	}
	public String getJsessionId() {
		return jsessionId;
	}
	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}*/
	public ArrayList<UserJsonVo> getInfoList() {
		return infoList;
	}
	public void setInfoList(ArrayList<UserJsonVo> infoList) {
		this.infoList = infoList;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
