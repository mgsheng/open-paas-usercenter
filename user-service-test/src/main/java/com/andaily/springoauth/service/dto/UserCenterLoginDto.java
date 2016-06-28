package com.andaily.springoauth.service.dto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.andaily.springoauth.tools.DESUtil;

/**
 * 15-5-18
 * <p/>
 * http://localhost:8080/oauth/token?client_id=unity-client&client_secret=unity&grant_type=authorization_code&code=zLl170&redirect_uri=http%3a%2f%2flocalhost%3a8080%2funity%2fdashboard.htm
 *
 * 
 */
public class UserCenterLoginDto implements Serializable {


    private String userCenterLoginUri;

    private String userCenterPasswordUri;
    
    private String client_id;

    private String client_secret;
    
    private String access_token;

    private String grant_type = "authorization_code";
    
    private String scope;
    
    private String source_id;
    
    private String username;
    
    private String password;
    private String pwdtype;


    public UserCenterLoginDto() {
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUserCenterLoginUri() {
		return userCenterLoginUri;
	}

	public void setUserCenterLoginUri(String userCenterLoginUri) {
		this.userCenterLoginUri = userCenterLoginUri;
	}

	public String getClient_id() {
		return client_id;
	}

	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	public String getClient_secret() {
		return client_secret;
	}

	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}

	public String getGrant_type() {
		return grant_type;
	}

	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
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

	public String getUserCenterPasswordUri() {
		return userCenterPasswordUri;
	}

	public void setUserCenterPasswordUri(String userCenterPasswordUri) {
		this.userCenterPasswordUri = userCenterPasswordUri;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getPwdtype() {
		return pwdtype;
	}

	public void setPwdtype(String pwdtype) {
		this.pwdtype = pwdtype;
	}

	public String getFullUri() throws UnsupportedEncodingException {
        return String.format("%s?client_id=%s&access_token=%s&grant_type=%s&scope=%s&source_id=%s", userCenterLoginUri, client_id, access_token, grant_type, scope, source_id);
    }
	public String getFullUri1() throws UnsupportedEncodingException {
        return String.format("%s?client_id=%s&access_token=%s&grant_type=%s&scope=%s&username=%s&password=%s", userCenterPasswordUri, client_id, access_token, grant_type, scope, username,password);
    }
	public String getAESFullUri(String signature,String timestamp,String signatureNonce) throws UnsupportedEncodingException {
        return String.format("%s?client_id=%s&access_token=%s&grant_type=%s&scope=%s&username=%s&password=%s&pwdtype=%s&signature=%s&timestamp=%s&signatureNonce=%s", userCenterPasswordUri, client_id, access_token, grant_type, scope, username,password,pwdtype,signature,timestamp,signatureNonce);
    }
}
