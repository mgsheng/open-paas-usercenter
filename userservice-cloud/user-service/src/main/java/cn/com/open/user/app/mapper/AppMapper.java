package cn.com.open.user.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

@CacheConfig(cacheNames = "users")
@Mapper
public interface AppMapper {
	@Cacheable
    @Select("select appsecret from app where appkey = #{appKey}")
    String findAppSecretByAppkey(String appKey);

}
