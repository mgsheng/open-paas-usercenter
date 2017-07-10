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
import cn.com.open.expservice.app.model.App;
import cn.com.open.expservice.app.mybatis.UserProvider;
import cn.com.open.expservice.app.vo.UserJsonVo;
import cn.com.open.expservice.app.vo.UserMergeVo;

 
@CacheConfig(cacheNames = "users")
@Mapper
public interface UserMapper {
	
	@Cacheable
	@Select("select id,appsecret,app_authorities,webServerRedirectUri from app where appkey = #{client_Id}")
	App findIdByClientId(@Param("client_id") String client_id);
	
	@Cacheable
	@Select("select id,user_name username,aes_password aesPassword,md5_password md5Password,sha1_password sha1Password ,archived from user_account where user_name=#{username}")
	ArrayList<User> findUserAccountByUsername(@Param("username") String username);
	 
	@Cacheable
	@Select("select id, user_name,guid,email,phone from user_account where user_name=#{username} and aes_password=#{password} limit 1")
	UserMergeVo findUserAccount(@Param("username") String username,@Param("password") String password);
	
	@SelectProvider(method = "findUserAll", type = UserProvider.class)
	ArrayList<UserJsonVo> findUserAll(UserMergeVo mergeVo);

	@UpdateProvider(method="updateUserAccount", type = UserProvider.class)
	void updateUserAccount(User user2);


	@SelectProvider(method = "findUserAccountList", type = UserProvider.class)
	ArrayList<UserJsonVo> findUserAccountList(@Param("userId")int userId);
 
	

}
