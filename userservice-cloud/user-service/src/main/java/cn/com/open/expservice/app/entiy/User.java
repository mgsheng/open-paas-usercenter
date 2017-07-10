package cn.com.open.expservice.app.entiy;

import java.io.Serializable;

public class User implements Serializable{
	public static final String OBJECT_KEY = "USER";  
	public static String STATUS_INACTIVE = "0"; // 未激活
	public static String STATUS_ENABLE = "1"; // 正常
	public static String STATUS_DISABLED = "2"; // 封停
	
	private int id;
	private String username;
	private String password;
	private String archived;
	private String key;
	
	private String aesPassword;
	private String md5Password;
	private String sha1Password;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public static String getObjectKey() {
		return OBJECT_KEY;
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
	
}
