package cn.com.open.user.app.mapper;

import java.util.ArrayList;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.mybatis.UserProvider;
import cn.com.open.user.app.vo.OAUserVo;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;

 
@CacheConfig(cacheNames = "users")
@Mapper
public interface UserMapper {
	
	@Cacheable
	@Select("select id,appsecret,app_authorities,webServerRedirectUri from app where appkey = #{client_id} limit 1")
	App findIdByClientId(@Param("client_id") String client_id);
	
	@Cacheable
	@Select("select id,user_name username,aes_password aesPassword,md5_password md5Password,sha1_password sha1Password,md5_salt md5Salt,archived,guid,email,phone from user_account where user_name=#{username}")
	ArrayList<User> findUserAccountByUsername(@Param("username") String username);
	 
	@Cacheable
	@Select("select id, user_name,guid,email,phone from user_account where user_name=#{username} and aes_password=#{password} limit 1")
	UserMergeVo findUserAccount(@Param("username") String username,@Param("password") String password);
	
	@UpdateProvider(method="updateUserAccount", type = UserProvider.class)
	void updateUserAccount(Map<String, Object> map);


	@SelectProvider(method = "findUserAccountList", type = UserProvider.class)
	ArrayList<UserListVo> findUserAccountList(Map<String, Object> map);
 
	@SelectProvider(method = "getOAUserCacheModel", type = UserProvider.class)
	OAUserVo getOAUserCacheModel(Map<String, Object> map);
	
	@SelectProvider(method = "getOAUserModel", type = UserProvider.class)
	OAUserVo getOAUserModel(Map<String, Object> map);
	
}
