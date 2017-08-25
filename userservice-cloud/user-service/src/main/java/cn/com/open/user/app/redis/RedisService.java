package cn.com.open.user.app.redis;

import java.util.List;

public interface RedisService {
	public boolean set(String key, String value);  
    
    public String get(String key);  
      
    public boolean expire(String key,long expire);  
      
    public <T> boolean setList(String key ,List<T> list);  
      
    public <T> List<T> getList(String key,Class<T> clz);  
      
    public long lpush(String key,Object obj);  
      
    public long rpush(String key,Object obj);  
      
    public String lpop(String key);

    public void deleteAll(String key);

    public void delete(List<String> keys);

    public void delete(String key);  
}
