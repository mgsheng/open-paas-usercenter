package cn.com.open.openpaas.userservice.app.app.service;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.common.model.Common;
import cn.com.open.openpaas.userservice.app.infrastructure.repository.AppRepository;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.DES;
import cn.com.open.openpaas.userservice.app.tools.DESUtil;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.tools.PropertiesTool;
import net.sf.json.JSONObject;


/**
 * 
 */
@Service("appService")
public class AppServiceImpl implements AppService {

    @Autowired
    private AppRepository appRepository;
    @Autowired
	private RedisClientTemplate redisClient;

	@Override
    public App findIdByClientId(String client_Id) {
		App app=appRepository.findIdByClientId(client_Id);
        if(app==null){
        	return null;
        }else{
        	return app;
        }
    }
	
	public App findIdByAppKey(String client_Id) {
        return appRepository.findIdByClientId(client_Id);
    }

	@Override
	public List<App> findAll() {
		return appRepository.findAll();
	}
	
	public List<App> findByAppIds(String appIds){
		Map map=new HashMap();
		Integer[] appIdArray ;
		if(appIds==null || appIds.length()==0){
			appIdArray = new Integer[]{0};
		}else{
			String[] a=appIds.split(",");
			appIdArray = new Integer[a.length];
			if(a!=null && a.length!=0){
				for(int i=0;i<a.length;i++){
					appIdArray[i]=Integer.parseInt(a[i]);
				}
			}
		}
		map.put("appIds", appIdArray);
		return appRepository.findByAppIds(map);
	}

	@Override
	public App findById(Integer appId) {
		return appRepository.findById(appId);
	}
	
	/*
	 * （旧）根据App和AppUser生成回调URL
	 * MD5加密方式
	 * @see cc.wdcy.service.AppService#findCallbackUrl(cc.wdcy.domain.app.App, cc.wdcy.domain.appuser.AppUser)
	 */
//	@Override
//	public String findCallbackUrl(App app, AppUser appUser){
//		if(app==null || appUser==null){
//			return "";
//		}
//		StringBuffer url = new StringBuffer(app.getWebServerRedirectUri());
//		//salt=4位随机数
//		String salt = StringTool.getRandomString(4);
//		//secret=MD5(sourceId+salt+appKey+appSecret);
//		String secret = MD5.Md5(appUser.sourceId()+salt+app.getAppkey()+app.getAppsecret());
//		url.append("?sourceId=").append(appUser.sourceId());
//		url.append("&salt=").append(salt);
//		url.append("&secret=").append(secret);
//		return url.toString();
//	}
	
	/*
	 * 根据App和AppUser生成回调URL
	 * DES加密方式
	 * @see cc.wdcy.service.AppService#findCallbackUrl(cc.wdcy.domain.app.App, cc.wdcy.domain.appuser.AppUser)
	 */
	@Override
	public String findCallbackUrl(App app, AppUser appUser,String serverHost,String platform){
		if(app==null || appUser==null){
			return "";
		}
		StringBuffer url = new StringBuffer(serverHost+"user/userCenterPublicLogin");
		//time：格式yyyyMMddHHmmss
		String time = DateTools.dateToString(new Date(), "yyyyMMddHHmmss");
		//appSecret为前8位
		App appUc=	(App) redisClient.getObject(RedisConstant.APP_INFO+Common.APPID);
		if(appUc==null){
			appUc=this.findById(Common.APPID);
			redisClient.setObject(RedisConstant.APP_INFO+Common.APPID, appUc);
		}
		if(appUc==null){
			//用户中心App不存在
			return "";
		}
		String appSecret = appUc.getAppsecret().substring(0, 8);
		//Des(（sourceId+“#”+time+“#”+appKey）, appSecret)
		String secret = "";
		try {
			
			//secret = DESUtil.encrypt(appUser.sourceId()+"#"+time+"#"+app.getAppkey(), appSecret);
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("sourceId", appUser.sourceId());
			map.put("time", time);
			map.put("appkey", app.getAppkey());
			map.put("appId", appUser.appId());
			map.put("platform",platform);
			secret = DES.encrypt(JSONObject.fromObject(map).toString(),appSecret);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		secret = secret.replaceAll("\\+", "%2B");
		url.append("?secret=").append(secret);
		return url.toString();
	}

}