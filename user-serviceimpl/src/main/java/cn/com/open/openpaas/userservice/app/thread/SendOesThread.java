package cn.com.open.openpaas.userservice.app.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.tools.Help_Encrypt;
import cn.com.open.openpaas.userservice.app.tools.HttpTools;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;

import net.sf.json.JSONObject;

/**
 * 用户修改登录时间操作
 * @author dongminghao
 *
 */
public class SendOesThread implements Runnable {

	private UserService userService;
	private String password;
	private List<AppUser> appUserList;
    private String address;
    private String key;
	public SendOesThread(String password, UserService userService,List<AppUser> appUserList,String address,String key){
		this.password = password;
		this.userService = userService;
		this.appUserList=appUserList;
		this.address=address;
		this.key=key;
	}
	
	@Override
	public void run() {
		String returnValue="";
		int appid;
		String sourceid;
		User user1;
		String pwd="";
		if(appUserList!=null&&appUserList.size()>0){
			for(int i=0;i<appUserList.size();i++){
				appid=appUserList.get(i).appId();
				sourceid=appUserList.get(i).sourceId();
			
				if(appid==1||appid==8){
					List<Map<String,String>> infoList=new ArrayList<Map<String,String>>();
					String url=address+key;
					try {
						pwd=Help_Encrypt.encrypt(password);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					user1=userService.findUserById(appUserList.get(i).userId());
					Map<String,String> map1=new HashMap<String,String>();
					map1.put("sourceid", sourceid);
					map1.put("username", user1.getUsername());
					map1.put("password", pwd);
					map1.put("key", "91d921e029d0470b9eb41e39d895a0e0");
		    		infoList.add(map1);
		    		returnValue=HttpTools.post(url, infoList);
		    		break;
				}else{
					returnValue="";
				}
			}
		}
	/*	//解析OES返回的json数据
		if(nullEmptyBlankJudge(returnValue)){
			flag = false;
		}else{
		  JSONObject reqjson = JSONObject.fromObject(returnValue);
		if(analysisValue(reqjson)){
			flag = true;	
		}else{
			flag=false;
		}
   		
	}*/
		
}
	private boolean nullEmptyBlankJudge(String str){
	        return null==str||str.isEmpty()||"".equals(str.trim());
	   }
    public Boolean analysisValue(JSONObject obj ){
    	String state = obj.getString("state");
		if(!state.equals("ok")){
			return false;
		}else{
			return true;
		}
    }

}
