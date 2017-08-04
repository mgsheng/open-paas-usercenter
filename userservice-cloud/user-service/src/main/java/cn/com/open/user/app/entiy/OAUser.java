package cn.com.open.user.app.entiy;

import java.io.Serializable;

public class OAUser implements Serializable{
	private String idNo;
	private String userName;
	private String salt;
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) { 
		this.idNo = idNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	 
	
}
