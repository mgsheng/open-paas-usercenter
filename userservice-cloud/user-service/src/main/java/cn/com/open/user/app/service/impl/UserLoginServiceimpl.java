package cn.com.open.user.app.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.user.app.constant.ConstantMessage;
import cn.com.open.user.app.entiy.LoginVaildate;
import cn.com.open.user.app.entiy.LoginValidatefrozen;
import cn.com.open.user.app.entiy.OAUser;
import cn.com.open.user.app.entiy.User;
import cn.com.open.user.app.mapper.UserMapper;
import cn.com.open.user.app.model.App;
import cn.com.open.user.app.redis.RedisServiceImpl;
import cn.com.open.user.app.service.UserLoginService;
import cn.com.open.user.app.sign.MD5;
import cn.com.open.user.app.tools.DateTools;
import cn.com.open.user.app.vo.UserListVo;
import cn.com.open.user.app.vo.UserMergeVo;
import cn.com.open.user.app.vo.UserVo;
import net.sf.json.JSONObject;


@Service
public class UserLoginServiceimpl implements UserLoginService {
	@Autowired
	UserMapper userMapper;
	
	 private final static String SEPARATOR="_"; 
	
	@Override
	public  UserMergeVo  findUserAccount(User entity){
		return userMapper.findUserAccount(entity.getUsername(),entity.getPassword());
	}
	
	@Override
	public ArrayList<User> findUserAccountByUsername(UserVo user) {
		return userMapper.findUserAccountByUsername(user.getUsername());
	}

	@Override
	public App findIdByClientId(String client_id) {
		return userMapper.findIdByClientId(client_id);
	}

	@Override
	public void updateUserAccount(UserVo user) {
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String str = sdf.format(new Date());//日期转字符串
		map.put("id", user.getId());
		map.put("aesPassword", user.getPassword());
		map.put("lastloginTime", str);
		userMapper.updateUserAccount(map);
	}
	@Override
	public ArrayList<UserListVo> findUserAccountList(int userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", userId);
		return userMapper.findUserAccountList(map);
	}
	 
	//验证该用户是否已经锁定，超过24小时自动解除锁
	@Override
	public Map<String, Object> lockUserNames(RedisServiceImpl redisService,Map<String, String> maps){
		  String appId= maps.get("appId");
		  String userName=maps.get("userName");
		  String loginFaliureTime=maps.get("loginFaliureTime");
		Map<String, Long> timeMap = new HashMap<String, Long>();
		Map<String, Object> map = new HashMap<String, Object>();
		String failNum=redisService.get(ConstantMessage.USERSERVICE_VALIDATELOGIN+appId+SEPARATOR+userName);//登录失败次数
		String lockInfo=redisService.get(ConstantMessage.USERSERVICE_FROZENLOGIN+appId+SEPARATOR+userName);//登录失败信息json格式
		 if(!nullAndEmpty(failNum)&&!nullAndEmpty(lockInfo)){
			 JSONObject jsonObjet = JSONObject.fromObject(lockInfo);
			 if(jsonObjet.containsKey("frozenTime")){
				 String frozenTime=jsonObjet.getString("frozenTime");
				 if(!nullAndEmpty(frozenTime)&&loginFaliureTime.equals(failNum)){
			//		 int time=Integer.parseInt(loginFrozenTime);
					 long lockmin=DateTools.getDateMin(frozenTime,0);
					 if(lockmin>0){//未到24小时 锁定中
						timeMap.put("faliureTimes", Long.valueOf(failNum));
						timeMap.put("tryTimes",0l);
						timeMap.put("frozenTime", lockmin);
				    		
						 map.clear();
						 map.put("status", "0");//接口返回状态：1-正确 0-错误
						 map.put("message",userName+"用户已锁定");
						 map.put("errorCode","10");
						 map.put("payload",timeMap);
					 }else{//解锁
						 redisInit(redisService,appId,userName);
					 }
				 }
			 }
		 }
		return map;
	}
	

	    //清空所有缓存
		@Override
		public Map<String, Object> loginValidates(RedisServiceImpl redisService,Map<String, String> maps) {
		  Map<String, Integer> timeMap = new HashMap<String, Integer>();
		  int code=0;
		  String appId= maps.get("appId");
		  String ip= maps.get("ip");
		  String userName=maps.get("userName");
		  String loginFaliureTime=maps.get("loginFaliureTime");
		  String loginValidateTime=	maps.get("loginValidateTime");
		  String loginFrozenTime=maps.get("loginFrozenTime");
		  String status=maps.get("status");
		  if(!nullAndEmpty(status))code=Integer.parseInt(status);
		  String message=maps.get("message");
		
		
		Map<String, Object> map = new HashMap<String, Object>();
    	LoginVaildate vaildate=null;
    	JSONObject jsonObjet=null;
    	LoginValidatefrozen vaildateFrozen=null;
    	String firstTime="";
    	try {
    		//将登陆失败次数以及失败信息存缓存
			String lockInfo=redisService.get(ConstantMessage.USERSERVICE_FROZENLOGIN+appId+SEPARATOR+userName);
			 if(!nullAndEmpty(lockInfo)){
				 jsonObjet = JSONObject.fromObject(lockInfo);
				 if(jsonObjet.containsKey("firstLoginTime")){
					 firstTime=jsonObjet.getString("firstLoginTime");
					 if(!nullAndEmpty(firstTime)){
						 long firstTimes=Integer.parseInt(loginValidateTime);
						 long min120=DateTools.getDateMin(firstTime,1);
						 if(min120>firstTimes){
							 redisInit(redisService,appId,userName);//登陆第一次时间和最后一次超过了2小时，清空之前失败信息
						 }
					 }
				 }
			 }
    		
			String failNum=redisService.get(ConstantMessage.USERSERVICE_VALIDATELOGIN+appId+SEPARATOR+userName);
    		String firstTimes="";
    		String nums="";
    		int num=1;
    		if(nullAndEmpty(failNum)){//失败次数空 说明是第一次
    			firstTimes=DateTools.getNow();//第一次登陆时间
    		}else{
    			firstTimes=firstTime;//第一次登陆时间
    			num=Integer.parseInt(failNum)+1;
    		}
    		nums=num+"";
    		
    		String tryTimes=(Integer.parseInt(loginFaliureTime)-num)+"";
    		
    		if(nums.equals(loginFaliureTime)){
	    		vaildateFrozen=new LoginValidatefrozen();
	    		vaildateFrozen.setAppId(appId);
	    		vaildateFrozen.setUserName(userName);
	    		vaildateFrozen.setFrozenTime(DateTools.getTimeByMinute(Integer.parseInt(loginFrozenTime)));
	    		
	    		//将登陆失败次数以及失败信息存缓存
	    		jsonObjet=JSONObject.fromObject(vaildateFrozen);
	    		redisService.set(ConstantMessage.USERSERVICE_FROZENLOGIN+appId+SEPARATOR+userName,jsonObjet.toString());//失败信息 json格式
	    		redisService.set(ConstantMessage.USERSERVICE_VALIDATELOGIN+appId+SEPARATOR+userName,nums);//登陆失败 赋值+1
	    		
	    		timeMap.put("faliureTimes", num);
				timeMap.put("tryTimes", Integer.parseInt(tryTimes));
				timeMap.put("frozenTime", Integer.parseInt(loginFrozenTime));
	    		
	    		map.clear();
   			 	map.put("status", "0");//接口返回状态：1-正确 0-错误
   			 	map.put("errorCode","10");
   				map.put("message",userName+"用户已锁定");
   				map.put("payload",timeMap);
			}else{
				vaildate=new LoginVaildate();
				vaildate.setFaliureTimes(num);//失败次数
				vaildate.setAppId(appId);
				vaildate.setUserName(userName);
				vaildate.setFirstLoginTime(firstTimes);
				vaildate.setLastLoginTime(DateTools.getNow());
				vaildate.setIp(ip);
				vaildate.setFaliureTimes(num);
				vaildate.setTryTimes(Integer.parseInt(tryTimes));
				
				jsonObjet=JSONObject.fromObject(vaildate);
				redisService.set(ConstantMessage.USERSERVICE_FROZENLOGIN+appId+SEPARATOR+userName,jsonObjet.toString());//失败信息 json格式
				redisService.set(ConstantMessage.USERSERVICE_VALIDATELOGIN+appId+SEPARATOR+userName,nums);//登陆失败 赋值+1
				timeMap.put("faliureTimes", num);
				timeMap.put("tryTimes", Integer.parseInt(tryTimes));
				
				map.clear();
   			 	map.put("status", "0");//接口返回状态：1-正确 0-错误
   			 	map.put("errorCode",code);
   				map.put("message",message);
   				map.put("payload",timeMap);
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		return map;
    
    }
	
	/**
    * 清空redis登录锁定限制缓存
    * @param request
    * @param response
    */
	@Override
	public void redisInit(RedisServiceImpl redisService,String appId,String userName) {
		 redisService.delete(ConstantMessage.USERSERVICE_VALIDATELOGIN+appId+SEPARATOR+userName);
		 redisService.delete(ConstantMessage.USERSERVICE_FROZENLOGIN+appId+SEPARATOR+userName);
  }
	public String md5Init(OAUser user,String salt,String appSercret) {
		String md5=user.getUserName()+user.getIdNo()+salt+appSercret;
		return MD5.Md5(md5);
	}
	protected static boolean nullAndEmpty(String str){
        return null==str||str.isEmpty()||"".equals(str.trim());
    }
	
	 
}
