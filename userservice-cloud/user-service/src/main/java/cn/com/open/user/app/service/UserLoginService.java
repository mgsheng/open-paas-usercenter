package cn.com.open.user.app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.user.app.entiy.OAUser;
import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.http.HttpClientUtil;
import cn.com.open.user.app.mapper.UserMapper;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.sign.MD5;
import cn.com.open.user.app.tools.StringTools;
import cn.com.open.user.app.vo.OAUserVo;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;
import cn.com.open.user.app.vo.UserVo;
import net.sf.json.JSONObject;


@Service
public class UserLoginService {
	@Autowired
	UserMapper userMapper;
	
	public  UserMergeVo  findUserAccount(User entity){
		return userMapper.findUserAccount(entity.getUsername(),entity.getPassword());
	}
	
	public ArrayList<User> findUserAccountByUsername(UserVo user) {
		return userMapper.findUserAccountByUsername(user.getUsername());
	}


	public App findIdByClientId(String client_id) {
		return userMapper.findIdByClientId(client_id);
	}

	public void updateUserAccount(UserVo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String str = sdf.format(new Date());//日期转字符串
		map.put("id", user.getId());
		map.put("aesPassword", user.getPassword());
		map.put("lastloginTime", str);
		userMapper.updateUserAccount(map);
	}

	public ArrayList<UserListVo> findUserAccountList(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		return userMapper.findUserAccountList(map);
	}
	
	public OAUserVo getOAUserModel(int userId,String appId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("appId", appId);
		return userMapper.getOAUserModel(map);
	}

	public OAUserVo GetOAUserModel(OAUser user, Map<String, Object> maps,String url,String appSercret,String appId) {
		    OAUserVo userVo=null;
		    try {
			    Map<String, String> map = new HashMap<String, String>();
			    String salt=StringTools.getRandom(100,1);
		    	map.put("idNo", user.getIdNo());
		    	map.put("userName", user.getUserName());
		    	map.put("salt", salt);
		    	map.put("secret", md5Init(user,salt, appSercret));
		    	
		    	
		    	
		    	String backJson = HttpClientUtil.httpPost(url, map);
		    	if(backJson!=null&&!"".equals(backJson)){
			    	 JSONObject json = JSONObject.fromObject(backJson);
			    	 if(json.containsKey("status")){
			    		 String status=json.getString("status");
			    		 if("1".equals(status)){ 
			    			if(json.containsKey("payload")){
			    				JSONObject payload = JSONObject.fromObject(json.getString("payload"));
			    				String userId=payload.getString("userId");
			    				userVo=getOAUserModel(Integer.parseInt(userId),appId);
			    				if(userVo==null){
			    					maps.put("msg", "用户信息不存在!"); 
			    					maps.put("status", "2");
			    					return null;
			    				}
			    				userVo=OAUserVoInit(userVo, backJson);
		 	    		    }
			    		 }else{
			    			String errorCode=json.getString("errorCode");
			    			if("1".equals(errorCode)){
			    			   maps.put("msg", "签名认证失败!");
			    			   maps.put("status", "1");
			    			}else if("2".equals(errorCode)){
			    				maps.put("msg", "用户不存在!");
			    				maps.put("status", "2");
			    			}else if("3".equals(errorCode)){
			    				maps.put("msg", "账号已禁用!");  
			    				maps.put("status", "3");
			    			}
			    		 }
			    	 }
		    	}else{
		    		maps.put("msg", "请求OA验证接口异常!");
		    		maps.put("status", "4");
		    	}
		    } catch (Exception e) {
		    	maps.put("msg", "查询异常!");
		    	maps.put("status", "4");
		    	e.printStackTrace();
		    }
			return userVo;
	}
	
	public String md5Init(OAUser user,String salt,String appSercret) {
		String md5=user.getUserName()+user.getIdNo()+salt+appSercret;
		return MD5.Md5(md5);
	}

	public OAUserVo OAUserVoInit(OAUserVo oaUserVo,String data) {
		OAUserVo userVo=new OAUserVo();
		JSONObject json = JSONObject.fromObject(data);
    	 if(json.containsKey("status")){
    		 String status=json.getString("status");
    		 if("1".equals(status)){
    			if(json.containsKey("payload")){
    				JSONObject payload = JSONObject.fromObject(json.getString("payload"));
    				userVo.setDeptName(payload.getString("deptName"));
    				userVo.setPosition(payload.getString("position"));
    				userVo.setPhone(payload.getString("phone"));
    				userVo.setEmail(payload.getString("email"));
    				userVo.setUserName(payload.getString("userName"));
    				userVo.setRealName(payload.getString("realName"));
    				userVo.setIdNo(payload.getString("idNo"));
    				userVo.setUserId(payload.getString("userId"));
    				userVo.setDeptName(payload.getString("deptName"));
    				userVo.setGuid(oaUserVo.getGuid());
 	    		 }
    		 }
    	 }
		return userVo;
	}
	
	
}
