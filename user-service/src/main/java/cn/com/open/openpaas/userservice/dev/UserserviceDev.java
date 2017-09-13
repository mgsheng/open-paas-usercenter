package cn.com.open.openpaas.userservice.dev;

public class UserserviceDev {

	
	public  String aes_iv_key;
	public String aes_userCenter_key;
	public String password_rule;
	public String detect_password_time;
	public String encrypt_iv_key;
	public String encrypt_password_crypt_key;
	public String kong_log_url;
	public String user_type;
	public String kafka_topic;
	public String kafka_group;
	private String metadata_broker_list;
	private String zookeeper_connect;
	private String message_type;
	private String user_center_getInfoList_uri;
	private String access_token_uri;
	private String single_sign_user;
	private String redisExpireTime;
	private String oes_interface_addr;
	private String oes_interface_key;
	private String app_localhost_url;
	private String email_verify_valid;
	private String username_verify_valid;
	
	
	
	
	public String getUsername_verify_valid() {
		return username_verify_valid;
	}

	public void setUsername_verify_valid(String username_verify_valid) {
		this.username_verify_valid = username_verify_valid;
	}

	public String getEmail_verify_valid() {
		return email_verify_valid;
	}

	public void setEmail_verify_valid(String email_verify_valid) {
		this.email_verify_valid = email_verify_valid;
	}

	public String getApp_localhost_url() {
		return app_localhost_url;
	}

	public void setApp_localhost_url(String app_localhost_url) {
		this.app_localhost_url = app_localhost_url;
	}

	public String getOes_interface_addr() {
		return oes_interface_addr;
	}

	public void setOes_interface_addr(String oes_interface_addr) {
		this.oes_interface_addr = oes_interface_addr;
	}

	public String getOes_interface_key() {
		return oes_interface_key;
	}

	public void setOes_interface_key(String oes_interface_key) {
		this.oes_interface_key = oes_interface_key;
	}

	public String getUser_center_getInfoList_uri() {
		return user_center_getInfoList_uri;
	}

	public void setUser_center_getInfoList_uri(String user_center_getInfoList_uri) {
		this.user_center_getInfoList_uri = user_center_getInfoList_uri;
	}

	public String getAccess_token_uri() {
		return access_token_uri;
	}

	public void setAccess_token_uri(String access_token_uri) {
		this.access_token_uri = access_token_uri;
	}

	public String getMetadata_broker_list() {
		return metadata_broker_list;
	}

	public void setMetadata_broker_list(String metadata_broker_list) {
		this.metadata_broker_list = metadata_broker_list;
	}

	public String getZookeeper_connect() {
		return zookeeper_connect;
	}

	public void setZookeeper_connect(String zookeeper_connect) {
		this.zookeeper_connect = zookeeper_connect;
	}

	public String getKafka_topic() {
		return kafka_topic;
	}

	public void setKafka_topic(String kafka_topic) {
		this.kafka_topic = kafka_topic;
	}

	public String getKafka_group() {
		return kafka_group;
	}

	public void setKafka_group(String kafka_group) {
		this.kafka_group = kafka_group;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

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

	public String getKong_log_url() {
		return kong_log_url;
	}

	public void setKong_log_url(String kong_log_url) {
		this.kong_log_url = kong_log_url;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getSingle_sign_user() {
		return single_sign_user;
	}

	public void setSingle_sign_user(String single_sign_user) {
		this.single_sign_user = single_sign_user;
	}

    public String getRedisExpireTime() {
        return redisExpireTime;
    }

    public void setRedisExpireTime(String redisExpireTime) {
        this.redisExpireTime = redisExpireTime;
    }
}
