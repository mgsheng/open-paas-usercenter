package cn.com.open.user.app.entiy;

import java.io.Serializable;

public class User implements Serializable{
	private int id;
	private String username;
	private String password;
	private String archived;
	private String key;
	
	private String guid;
	private String email;
	private String phone;
	
	private String aesPassword;
	private String md5Password;
	private String sha1Password;
	private String md5Salt;
	
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public String getArchived() {
		return archived;
	}
	public void setArchived(String archived) {
		this.archived = archived;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getAesPassword() {
		return aesPassword;
	}
	public void setAesPassword(String aesPassword) {
		this.aesPassword = aesPassword;
	}
	public String getMd5Password() {
		return md5Password;
	}
	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}
	public String getSha1Password() {
		return sha1Password;
	}
	public void setSha1Password(String sha1Password) {
		this.sha1Password = sha1Password;
	}
	public String getMd5Salt() {
		return md5Salt;
	}
	public void setMd5Salt(String md5Salt) {
		this.md5Salt = md5Salt;
	}
	
	
}
