package cn.com.open.openpaas.userservice.dev;

public class UserserviceDev {

	public  String aes_iv_key;
	public String aes_userCenter_key;
	public String password_rule;
	public String detect_password_time;
	public String encrypt_iv_key;
	public String encrypt_password_crypt_key;
	

	public String getEncrypt_iv_key() {
		return encrypt_iv_key;
	}

	public void setEncrypt_iv_key(String encrypt_iv_key) {
		this.encrypt_iv_key = encrypt_iv_key;
	}

	public String getEncrypt_password_crypt_key() {
		return encrypt_password_crypt_key;
	}

	public void setEncrypt_password_crypt_key(String encrypt_password_crypt_key) {
		this.encrypt_password_crypt_key = encrypt_password_crypt_key;
	}

	public String getAes_iv_key() {
		return aes_iv_key;
	}

	public void setAes_iv_key(String aes_iv_key) {
		this.aes_iv_key = aes_iv_key;
	}

	public String getAes_userCenter_key() {
		return aes_userCenter_key;
	}

	public void setAes_userCenter_key(String aes_userCenter_key) {
		this.aes_userCenter_key = aes_userCenter_key;
	}

	public String getPassword_rule() {
		return password_rule;
	}

	public void setPassword_rule(String password_rule) {
		this.password_rule = password_rule;
	}

	public String getDetect_password_time() {
		return detect_password_time;
	}

	public void setDetect_password_time(String detect_password_time) {
		this.detect_password_time = detect_password_time;
	}
	
	
	
}
