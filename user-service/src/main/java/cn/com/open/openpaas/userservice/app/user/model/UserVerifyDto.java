package cn.com.open.openpaas.userservice.app.user.model;

import java.io.Serializable;

public class UserVerifyDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String clientId;
	private String account;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
