package cn.com.open.user.app.entiy;

public class LoginVaildate{
private String appId;    
private String userName; 
private String firstLoginTime; 
private String lastLoginTime; 
private String ip; 
private int faliureTimes;
private int tryTimes;

 
public String getAppId() {
	return appId;
}
public void setAppId(String appId) {
	this.appId = appId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getFirstLoginTime() {
	return firstLoginTime;
}
public void setFirstLoginTime(String firstLoginTime) {
	this.firstLoginTime = firstLoginTime;
}
public String getLastLoginTime() {
	return lastLoginTime;
}
public void setLastLoginTime(String lastLoginTime) {
	this.lastLoginTime = lastLoginTime;
}
public String getIp() {
	return ip;
}
public void setIp(String ip) {
	this.ip = ip;
}
public int getFaliureTimes() {
	return faliureTimes;
}
public void setFaliureTimes(int faliureTimes) {
	this.faliureTimes = faliureTimes;
}
public int getTryTimes() {
	return tryTimes;
}
public void setTryTimes(int tryTimes) {
	this.tryTimes = tryTimes;
}

}
