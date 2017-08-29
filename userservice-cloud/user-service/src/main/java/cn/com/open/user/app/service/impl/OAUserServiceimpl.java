package cn.com.open.user.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.user.app.entiy.OAUser;
import cn.com.open.user.app.log.ThirdPartyCallAssistant;
import cn.com.open.user.app.mapper.UserMapper;
import cn.com.open.user.app.service.OAUserService;
import cn.com.open.user.app.sign.MD5;
import cn.com.open.user.app.tools.StringTools;
import cn.com.open.user.app.vo.OAUserVo;
import net.sf.json.JSONObject;


@Service
public class OAUserServiceimpl implements OAUserService {
	@Autowired
	UserMapper userMapper;
	
	@Autowired
	ThirdPartyCallAssistant thirdPartyCallAssistant;
	
	
	 
	@Override
	public OAUserVo getOAUserModel(int userId,String appId) {
		OAUserVo oaUserVo=new OAUserVo();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("appId", appId);
		oaUserVo=userMapper.getOAUserCacheModel(map);
		if(oaUserVo==null)
		oaUserVo=userMapper.getOAUserModel(map);
		return oaUserVo;
	}
	@Override
	public OAUserVo GetOAUserModel(OAUser user, Map<String, Object> maps,String url,String appSercret,String appId) {
		    OAUserVo userVo=null;
		 //   JSONObject jsonObj = null;
		    try {
			    Map<String, String> map = new HashMap<String, String>();
			    String salt=StringTools.getRandom(100,1);
		    	map.put("idNo", user.getIdNo());
		    	map.put("userName", user.getUserName());
		    	map.put("salt", salt);
		    	map.put("secret", md5Init(user,salt, appSercret));
		    	
		    	String backJson= thirdPartyCallAssistant.doOAPost(url,map);
				 
		    //	String backJson = HttpClientUtil.httpPost(url, map);
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
			    					maps.put("message", "用户信息不存在!"); 
			    					maps.put("status", "2");
			    					return null;
			    				}
			    				userVo=OAUserVoInit(userVo, backJson);
			    				//json.put("guid", userVo.getGuid());
			    				//jsonObj = JSONObject.fromObject(json);
		 	    		    }
			    		 }else{
			    			String errorCode=json.getString("errorCode");
			    			if("1".equals(errorCode)){
			    			   maps.put("message", "签名认证失败!");
			    			   maps.put("status", "1");
			    			}else if("2".equals(errorCode)){
			    				maps.put("message", "用户不存在!");
			    				maps.put("status", "2");
			    			}else if("3".equals(errorCode)){
			    				maps.put("message", "账号已禁用!");  
			    				maps.put("status", "3");
			    			}
			    		 }
			    	 }
		    	}else{
		    		maps.put("message", "请求OA验证接口异常!");
		    		maps.put("status", "4");
		    	}
		    } catch (Exception e) {
		    	maps.put("message", "查询异常!");
		    	maps.put("status", "4");
		    	e.printStackTrace();
		    }
			return userVo;
	}
	
	

	public OAUserVo OAUserVoInit(OAUserVo oaUserVo,String data) {
		OAUserVo userVo=new OAUserVo();
		JSONObject json = JSONObject.fromObject(data);
    	 if(json.containsKey("status")){
    		 String status=json.getString("status");
    		 if("1".equals(status)){
    			if(json.containsKey("payload")){
    				JSONObject payload = JSONObject.fromObject(json.getString("payload"));
    				if(payload.containsKey("deptName"))
    				userVo.setDeptName(payload.getString("deptName"));
    				if(payload.containsKey("position"))
    				userVo.setPosition(payload.getString("position"));
    				if(payload.containsKey("phone"))
    				userVo.setPhone(payload.getString("phone"));
    				if(payload.containsKey("email"))
    				userVo.setEmail(payload.getString("email"));
    				if(payload.containsKey("userName"))
    				userVo.setUserName(payload.getString("userName"));
    				if(payload.containsKey("realName"))
    				userVo.setRealName(payload.getString("realName"));
    				if(payload.containsKey("idNo"))
    				userVo.setIdNo(payload.getString("idNo"));
    				if(payload.containsKey("userId"))
    				userVo.setUserId(payload.getString("userId"));
    				if(payload.containsKey("deptName"))
    				userVo.setDeptName(payload.getString("deptName"));
    				if(payload.containsKey("sex"))
    				userVo.setSex(payload.getString("sex"));
    				if(payload.containsKey("workPhone"))
    				userVo.setWorkPhone(payload.getString("workPhone"));
    				if(payload.containsKey("phoneSplitNo"))
    				userVo.setPhoneSplitNo(payload.getString("phoneSplitNo"));
    				userVo.setGuid(oaUserVo.getGuid());
 	    		 }
    		 }
    	 }
		return userVo;
	}
	 
	public String md5Init(OAUser user,String salt,String appSercret) {
		String md5=user.getUserName()+user.getIdNo()+salt+appSercret;
		return MD5.Md5(md5);
	}
	 
	
}
