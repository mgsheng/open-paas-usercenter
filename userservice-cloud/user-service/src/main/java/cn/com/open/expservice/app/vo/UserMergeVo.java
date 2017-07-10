package cn.com.open.expservice.app.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class UserMergeVo implements Serializable{
	private int id;
	private String username;
	private String password;
	private String guid;
	private String phone;
	private String email;
	private String pwdRule;
	private String pwdMsg;
	private String jsessionId;
	
	private ArrayList<UserJsonVo> infoList;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	}
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
	}
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	} 
}
