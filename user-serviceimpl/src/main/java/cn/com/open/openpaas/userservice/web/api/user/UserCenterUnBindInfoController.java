package cn.com.open.openpaas.userservice.web.api.user;


import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 同步解除用户应用解除绑定接口
 */
@Controller
@RequestMapping("/user/")
public class UserCenterUnBindInfoController  extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(UserCenterUnBindInfoController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private UserserviceDev userserviceDev;
	 
    /**
     * 用户应用解除绑定接口
     * @param request
     * @param response
     * @param userCenterReg
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * 1.单独应用的用户：根据source_id和appid查询用户 如果只查到一个appuser则删除这个appuser和user_account的用户。
     * 2.如果是绑定过后的用户：则根据userid查询出所有的appuser，删除跟输入的source_Id相同的appuser，并且更新用户数据的奥鹏卡号为空。
     * 3.如果是父类账户：则更新父类账户的用户名、手机号、邮箱、奥鹏卡号为空。
     * 4.如果是子类账户：则删除子类账户 和appuser中子类用户对应的数据。
     */
    @RequestMapping(value = "unBindUserInfo")
    public void unBindUser(HttpServletRequest request,HttpServletResponse response,UserCenterRegDto userCenterReg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
   	 long startTime = System.currentTimeMillis();
   	 String username=userCenterReg.getUsername();
            if(!paraMandatoryCheck(Arrays.asList(userCenterReg.getClient_id(),
                     userCenterReg.getUsername(),userCenterReg.getSource_id()))){
                paraMandaChkAndReturn(3, response,"必传参数中有空值");
                return;
            }
            log.info("client_id："+userCenterReg.getClient_id());
            Map<String, Object> map=new HashMap<String, Object>();
            App app = (App) redisClient.getObject(RedisConstant.APP_INFO+userCenterReg.getClient_id());
            if(app==null)
    		{
    			 app=appService.findIdByClientId(userCenterReg.getClient_id());
    			 redisClient.setObject(RedisConstant.APP_INFO+userCenterReg.getClient_id(), app);
    		}
            if(null!=userCenterReg){
           	 User user=null;
                if(!nullEmptyBlankJudge(username)){
    				user=userService.findByUsername(username);
                }
                if(null!=app){
                    AppUser appUser = appUserService.findByCidSid(app.getId(), userCenterReg.getSource_id());
                    if(null!=appUser){
                   	 if(user!=null){
                   		 deleteUserinfo(userCenterReg, user.getId(),user.defaultUser(),user.getPid());
                   	 }else{
                   			    map.clear();
                                map.put("status", "0");
                                map.put("errMsg","用户名不存在");
                                map.put("error_code", "6");//source_id不存在 
                   	 }
                    }else {
                        map.clear();
                        map.put("status", "0");
                        map.put("errMsg","source_id不存在");
                        map.put("error_code", "4");//source_id不存在
                    }
                }
            }
            if(map.get("status")=="0"){
                writeErrorJson(response,map);
            }else{
                writeSuccessJson(response,map);
               
            }
           // OauthControllerLog.log(startTime,username,"",app,map,userserviceDev);
           return;
    }
    /**
     * 删除用户信息
     * @param userCenterReg
     * @param id
     * @param defaultUser
     * @param pid
     */
	private void deleteUserinfo(UserCenterRegDto userCenterReg, int id,Boolean defaultUser,String pid) {
		List <AppUser> list=appUserService.findByUserId(id);
		 if(list!=null&&list.size()>1){
			for(int i=0;i<list.size();i++){
				if(list.get(i).sourceId().equals(userCenterReg.getSource_id())){
					if(list.get(i).appId()==1){
						userService.updateUserCardNoById(list.get(i).userId(),"");	
					}
					appUserService.deleteAppUser(list.get(i).appUid());
					break;
				}
			}
		 }else{
			 //存在Pid则证明该User为子账号,不存在Pid则证明该User为父账号
			if(defaultUser&&!nullEmptyBlankJudge(pid)&&!pid.equals("0")){
				 //更新父类账户defaultUser为false
			  userService.deleteUser(id); 
			 }else if(defaultUser&&nullEmptyBlankJudge(pid)){
			 //更新子账户pid为空defaultUser为false
			  //userService.updateDAPById("",false,String.valueOf(user.getId()));
			  userService.updateParentUser("","","","",id);
			 }else if(defaultUser&&pid.equals("0")){
				//更新子账户pid为空defaultUser为false
			  //userService.updateDAPById("",false,String.valueOf(user.getId()));
			  userService.updateParentUser("","","","",id);
			 }else{
				 userService.deleteUser(id); 
			 }
			 appUserService.deleteAppUser(list.get(0).appUid());
			
		 }
	}
    
}