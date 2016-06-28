package com.andaily.springoauth.service.dto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class UserBindDto extends AbstractOauthDto {


    private Integer userId;

    private String phone;
    private String realName;
    private String email;
    private Integer identifyType;
    private String identifyNo;
    private String userBindUri;
    
    private String clientId;
    private String redirectUri;
    private String errorUri;

    private List<String> privileges = new ArrayList<String>();


    public UserBindDto() {
    }

    public UserBindDto(String error, String errorDescription) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentifyNo() {
		return identifyNo;
	}

	public void setIdentifyNo(String identifyNo) {
		this.identifyNo = identifyNo;
	}

	public String getUserBindUri() {
		return userBindUri;
	}

	public void setUserBindUri(String userBindUri) {
		this.userBindUri = userBindUri;
	}

	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	public String getErrorUri() {
		return errorUri;
	}

	public void setErrorUri(String errorUri) {
		this.errorUri = errorUri;
	}

	public Integer getIdentifyType() {
		return identifyType;
	}

	public void setIdentifyType(Integer identifyType) {
		this.identifyType = identifyType;
	}

	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?sourceId=%s&realName=%s&phone=%s&email=%s&identifyType=%s&identifyNo=%s&clientId=%s&redirect=%s&error=%s", userBindUri, userId, realName, phone, email, identifyType, identifyNo,clientId,redirectUri,errorUri);
    }
}