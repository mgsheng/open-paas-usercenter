package cn.com.open.expservice.app.redis;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import com.alibaba.fastjson.JSONObject;

import cn.com.open.expservice.app.entiy.User;
import cn.com.open.expservice.app.model.App;

import org.springframework.stereotype.Component;

@Component
public class RedisServiceImpl implements RedisService{  
    
    @Autowired  
    private RedisTemplate<String, ?> redisTemplate;  
      
    @Override  
    public boolean set(final String key, final String value) {  
        boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {  
            @Override  
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {  
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
                connection.set(serializer.serialize(key), serializer.serialize(value));  
                return true;  
            }  
        });  
        return result;  
    }  
    
    public String get(final String key){  
        String result = redisTemplate.execute(new RedisCallback<String>() {  
            @Override  
            public String doInRedis(RedisConnection connection) throws DataAccessException {  
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
                byte[] value =  connection.get(serializer.serialize(key));  
                return serializer.deserialize(value);  
            }  
        });  
        return result;  
    }  
  
    @Override  
    public boolean expire(final String key, long expire) {  
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);  
    }  
  
    @Override  
    public <T> boolean setList(String key, List<T> list) {  
        String value = JSONObject.toJSONString(list);
        return set(key,value);  
    }  
  
    @Override  
    public <T> List<T> getList(String key,Class<T> clz) {  
    /*    String json = get(key);  
        if(json!=null){  
            List<T> list = JSONUtil.toList(json, clz);  
            return list;  
        } */ 
        return null;  
    }  
  
    @Override  
    public long lpush(final String key, Object obj) {  
        final String value =JSONObject.toJSONString(obj);  
        long result = redisTemplate.execute(new RedisCallback<Long>() {  
            @Override  
            public Long doInRedis(RedisConnection connection) throws DataAccessException {  
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
                long count = connection.lPush(serializer.serialize(key), serializer.serialize(value));  
                return count;  
            }  
        });  
        return result;  
    }  
  
    @Override  
    public long rpush(final String key, Object obj) {  
        final String value = JSONObject.toJSONString(obj);  
        long result = redisTemplate.execute(new RedisCallback<Long>() {  
            @Override  
            public Long doInRedis(RedisConnection connection) throws DataAccessException {  
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
                long count = connection.rPush(serializer.serialize(key), serializer.serialize(value));  
                return count;  
            }  
        });  
        return result;  
    }  
  
    @Override  
    public String lpop(final String key) {  
        String result = redisTemplate.execute(new RedisCallback<String>() {  
            @Override  
            public String doInRedis(RedisConnection connection) throws DataAccessException {  
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();  
                byte[] res =  connection.lPop(serializer.serialize(key));  
                return serializer.deserialize(res);  
            }  
        });  
        return result;  
    }  
    
   
    
    
    
    RedisTemplate<String, User> redisTemplates;  
    public RedisTemplate<String, User> getRedisTemplate() {  
        return redisTemplates;  
    }  
  
    public void setRedisTemplate(RedisTemplate<String, User> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }  
      
    public void put(User user) {  
        redisTemplate.opsForHash().put(user.getObjectKey(), user.getKey(), user);  
    }  
  
    public void delete(User key) {  
        redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());  
    }  
    public Object getObject(User key) {  
        return (Object) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());  
    } 
}  

