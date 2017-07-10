package cn.com.open.expservice.app.mybatis;

import cn.com.open.expservice.app.entiy.UserCache;
import cn.com.open.expservice.app.vo.UserMergeVo;

public class UserProvider {
	public String findUserAccountList(int id) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ap.icon,au.source_id,ap.id,ap.name,ap.webServerRedirectUri ");
		sb.append(" from user_account ua,app_user au,app ap ");
		sb.append(" where ua.id=au.user_id and au.app_id=ap.id");
		if (id >0) {
			sb.append(" and");
			sb.append(" ua.id = "+id);
		} 
		return sb.toString();
	}
	
	
	public String findUserAll(UserMergeVo mergeVo) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ap.icon,au.source_id,ap.id,ap.name,ap.webServerRedirectUri ");
		sb.append(" from user_cache ua,app_user au,app ap ");
		sb.append(" where ua.id=au.user_id and au.app_id=ap.id");
		if (mergeVo.getId() >0) {
			sb.append(" and");
			sb.append(" ua.id = "+mergeVo.getId());
		} 
		return sb.toString();
	}
	
	
	public String updateUserCache(UserCache cache) {
		StringBuffer sb = new StringBuffer();
		sb.append("update user_cache set");
		if (cache.getAesPassword() != null && cache.getAesPassword().length()>0) {
			sb.append(" aes_password()='" + cache.getAesPassword() + "'");
		}
		sb.append(" where id="+cache.id());
		return sb.toString();
	}
	
	
	public String updateUserAccount(UserCache cache) {
		StringBuffer sb = new StringBuffer();
		sb.append("update user_account set");
		if (cache.getAesPassword() != null && cache.getAesPassword().length()>0) {
			sb.append(" aes_password()='" + cache.getAesPassword() + "'");
		}
		sb.append(" where id="+cache.id());
		return sb.toString();
	}
	
}
