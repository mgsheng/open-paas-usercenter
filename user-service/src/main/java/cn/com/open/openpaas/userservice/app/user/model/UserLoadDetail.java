package cn.com.open.openpaas.userservice.app.user.model;

public class UserLoadDetail {

	private Integer id;
	private Integer appId;
	private String appName;
	private String loadAdd;
	private String loadIp;
	private Long loadTime;
	private Integer userId;
	private String userName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLoadAdd() {
		return loadAdd;
	}
	public void setLoadAdd(String loadAdd) {
		this.loadAdd = loadAdd;
	}
	public String getLoadIp() {
		return loadIp;
	}
	public void setLoadIp(String loadIp) {
		this.loadIp = loadIp;
	}
	public Long getLoadTime() {
		return loadTime;
	}
	public void setLoadTime(Long loadTime) {
		this.loadTime = loadTime;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
