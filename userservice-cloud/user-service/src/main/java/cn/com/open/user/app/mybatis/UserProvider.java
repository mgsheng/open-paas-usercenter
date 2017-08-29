package cn.com.open.user.app.mybatis;

import java.util.Map;
public class UserProvider {
	public String findUserAccountList(Map<String, Object> map ) {
		Integer id=(Integer)map.get("id");
		StringBuilder sb = new StringBuilder();
		sb.append("select ap.icon,au.source_id sourceId,ap.id appId,ap.name,ap.webServerRedirectUri callbackUrl,ap.appkey,ap.appsecret");
		sb.append(" from user_account ua,app_user au,app ap ");
		sb.append(" where ua.id=au.user_id and au.app_id=ap.id");
		if (id >0) {
			sb.append(" and");
			sb.append(" ua.id = "+id);
		} 
		return sb.toString();
	}
	
	public String updateUserAccount(Map<String, Object> map) {
		Integer id=(Integer)map.get("id");
		String aesPassword=(String)map.get("aesPassword");
		String lastloginTime=(String)map.get("lastloginTime");
		StringBuffer sb = new StringBuffer();
		sb.append("update user_account set");
		if (aesPassword != null && aesPassword.length()>0) {
			sb.append(" aes_password='" + aesPassword + "'");
		}
			sb.append(", last_login_time='"+ lastloginTime+ "'");
		sb.append(" where id="+id);
		return sb.toString();
	}
	
	public String getOAUserCacheModel(Map<String, Object> map ) {
		Integer userId=(Integer)map.get("userId");
		String  appId=(String)map.get("appId");
		StringBuilder sb = new StringBuilder();
		sb.append("select au.source_id sourceId,ua.guid");
		sb.append(" from user_cache ua,app_user au ");
		sb.append(" where ua.id=au.user_id");
		if (userId >0) {
			sb.append(" and au.app_id = "+appId);
			sb.append(" and au.source_id = "+userId+" limit 1");
		} 
		return sb.toString();
	}
	
	public String getOAUserModel(Map<String, Object> map ) {
		Integer userId=(Integer)map.get("userId");
		String  appId=(String)map.get("appId");
		StringBuilder sb = new StringBuilder();
		sb.append("select au.source_id sourceId,ua.guid");
		sb.append(" from user_account ua,app_user au ");
		sb.append(" where ua.id=au.user_id");
		if (userId >0) {
			sb.append(" and au.app_id = "+appId);
			sb.append(" and au.source_id = "+userId+" limit 1");
		} 
		return sb.toString();
	}
	
}
