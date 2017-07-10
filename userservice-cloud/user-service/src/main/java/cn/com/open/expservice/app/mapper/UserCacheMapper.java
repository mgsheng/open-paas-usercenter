package cn.com.open.expservice.app.mapper;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import cn.com.open.expservice.app.entiy.User;
import cn.com.open.expservice.app.entiy.UserCache;
import cn.com.open.expservice.app.model.App;
import cn.com.open.expservice.app.mybatis.UserCacheProvider;
import cn.com.open.expservice.app.mybatis.UserProvider;
import cn.com.open.expservice.app.vo.UserJsonVo;
import cn.com.open.expservice.app.vo.UserMergeVo;

 
@CacheConfig(cacheNames = "users")
@Mapper
public interface UserCacheMapper {
	
	@Cacheable
	@Select("select id,appsecret,app_authorities,webServerRedirectUri from app where appkey = #{client_Id}")
	App findIdByClientId(@Param("client_id") String client_id);
	
	@Cacheable
	@Select("select id,user_name username,aes_password aesPassword,md5_password md5Password,sha1_password sha1Password ,archived from user_cache where user_name=#{username}")
	ArrayList<User> findUserCacheByUsername(@Param("username") String username);
	 
	@Cacheable
	@Select("select id, user_name,guid,email,phone from user_cache where user_name=#{username} and aes_password=#{password} limit 1")
	UserMergeVo findByUserCache(@Param("username") String username,@Param("password") String password);
	
	@UpdateProvider(method="updateUserCache", type = UserProvider.class)
	void updateUserCache(User cache);

	@SelectProvider(method = "findUserCacheList", type = UserCacheProvider.class)
	ArrayList<UserJsonVo> findUserCacheList(@Param("userCacheId")int userCacheId);

}
