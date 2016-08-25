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
	
	
	
}
