package cn.com.open.user.app.mybatis;

import java.util.Map;

public class UserCacheProvider {
	public String findUserCacheList(Map<String, Object> map) {
		Integer id = (Integer)map.get("id");
		StringBuilder sb = new StringBuilder();
		sb.append("select ap.icon,au.source_id,ap.id,ap.name,ap.webServerRedirectUri ");
		sb.append(" from user_cache ua,app_user au,app ap ");
		sb.append(" where ua.id=au.user_id and au.app_id=ap.id");
		if (id >0) {
			sb.append(" and");
			sb.append(" ua.id = "+id);
		} 
		return sb.toString();
	}
	
	public String updateUserCache(Map<String, Object> map) {
		Integer id=(Integer)map.get("id");
		String aesPassword=(String)map.get("aesPassword");
		StringBuffer sb = new StringBuffer();
		sb.append("update user_cache set");
		if (aesPassword != null && aesPassword.length()>0) {
			sb.append(" aes_password='" + aesPassword + "'");
		}
		sb.append(" where id="+id);
		return sb.toString();
	}
	
}
