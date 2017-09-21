package cn.com.open.openpaas.userservice.app.log;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 15-5-18
 * <p/>
 * http://localhost:8080/oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=http%3a%2f%2flocalhost%3a8080%2funity%2fdashboard.htm
 *
 * 
 */
public class UserLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
    private String clientId;
    private String clientSecret;
    private String accessToken;
    private String grantType = "authorization_code";
    private String scope;    
    private String sourceId;
    private String phone;
    private String email;
    private String type;
    private String createTime;
    private String cardNo;
    private String guid;
    private String signature;
    private String timestamp;
    private String signatureNonce;
    private String isValidate;
    private String appUid;
    private String whetherBind;
    private String name;
    private String code;
    private String refreshToken;
    private String accountType;
    private String account;
    private String oldPwd;
    private String newPwd;
    private String pwdType;
    private String desAddress;
    private String desAppId;
    private String secret;
    private String jsessionId;
    private String redisKey;
    private String redisValue;
    private String execTime;
    private String status;
    private String errorCode;
    private String errorMsg;
    private String ip;
    private String appId;
    
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
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
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getGrantType() {
		return grantType;
	}
	public void setGrantType(String grantType) {
		this.grantType = grantType;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSignatureNonce() {
		return signatureNonce;
	}
	public void setSignatureNonce(String signatureNonce) {
		this.signatureNonce = signatureNonce;
	}
	public String getIsValidate() {
		return isValidate;
	}
	public void setIsValidate(String isValidate) {
		this.isValidate = isValidate;
	}
	public String getAppUid() {
		return appUid;
	}
	public void setAppUid(String appUid) {
		this.appUid = appUid;
	}
	public String getWhetherBind() {
		return whetherBind;
	}
	public void setWhetherBind(String whetherBind) {
		this.whetherBind = whetherBind;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getOldPwd() {
		return oldPwd;
	}
	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	
	public String getDesAddress() {
		return desAddress;
	}
	public void setDesAddress(String desAddress) {
		this.desAddress = desAddress;
	}
	public String getDesAppId() {
		return desAppId;
	}
	public void setDesAppId(String desAppId) {
		this.desAppId = desAppId;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public String getJsessionId() {
		return jsessionId;
	}
	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}
	public String getRedisKey() {
		return redisKey;
	}
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
	public String getRedisValue() {
		return redisValue;
	}
	public void setRedisValue(String redisValue) {
		this.redisValue = redisValue;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getPwdType() {
		return pwdType;
	}
	public void setPwdType(String pwdType) {
		this.pwdType = pwdType;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
    
  
    
}
