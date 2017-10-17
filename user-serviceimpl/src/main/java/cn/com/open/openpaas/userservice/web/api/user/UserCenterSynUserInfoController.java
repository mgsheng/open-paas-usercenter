package cn.com.open.openpaas.userservice.web.api.user;


import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.domain.model.UserCenterRegDto;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.dev.UserserviceDev;

/**
 * 用户信息同步接口
 */
@Controller
@RequestMapping("/user/info")
public class UserCenterSynUserInfoController extends BaseControllerUtil {
	private static final Logger log = LoggerFactory.getLogger(UserCenterSynUserInfoController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private RedisClientTemplate redisClient;
	 @Autowired
	 private DefaultTokenServices tokenServices;
	 @Autowired
	 private UserserviceDev userserviceDev;
     /**
     * 用户信息同步接口
     * @return Json
     */
    @RequestMapping("synUserInfo")
    public void userSynUserInfo(HttpServletRequest request,HttpServletResponse response,UserCenterRegDto userCenterReg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    	if(null!=userCenterReg){
            if(!paraMandatoryCheck(Arrays.asList(userCenterReg.getGrant_type(),userCenterReg.getClient_id(),
                    userCenterReg.getAccess_token(),userCenterReg.getScope(),userCenterReg.getSource_id()))){
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
    		map=checkClientIdOrToken(userCenterReg.getClient_id(),userCenterReg.getAccess_token(),app,tokenServices);
            if(map.get("status").equals("1")) {//client_id和access_token正确
                    AppUser appUser = appUserService.findByCidSid(app.getId(), userCenterReg.getSource_id());
                    if(null!=appUser){
                        try {
                        /* user */
                            User user = userService.findUserById(appUser.userId());
                            if (null != user) {
                                if (!nullEmptyBlankJudge(userCenterReg.getNickname())) {
                                    user.nickName(userCenterReg.getNickname());
                                }
                                if(!nullEmptyBlankJudge(userCenterReg.getReal_name())){
                                	user.realName(userCenterReg.getReal_name());
                                }
                                if(!nullEmptyBlankJudge(userCenterReg.getIdentify_no())){
                                	user.identifyNo(userCenterReg.getIdentify_no());
                                }
                                if(!nullEmptyBlankJudge(userCenterReg.getIdentify_type())){
                                	user.identifyType(Integer.parseInt(userCenterReg.getIdentify_type()));
                                }if(!nullEmptyBlankJudge(userCenterReg.getHead_picture())){
                                	user.headPicture(userCenterReg.getHead_picture());
                                }
                                if (!nullEmptyBlankJudge(userCenterReg.getUsername())) {
                                user.setUsername(userCenterReg.getUsername());
                                }if(!nullEmptyBlankJudge(userCenterReg.getPhone())){
                                user.setPhone(userCenterReg.getPhone());
                                }if (!nullEmptyBlankJudge(userCenterReg.getEmail())) {
                                  user.setEmail(userCenterReg.getEmail());
                                 }
                                userService.updateUser(user);
                            }else{
                                user=new User(userCenterReg.getUsername(),userCenterReg.getPassword(),userCenterReg.getPhone(),
                                        userCenterReg.getEmail(),userCenterReg.getNickname(),
                                        userCenterReg.getReal_name(),userCenterReg.getHead_picture());
                                user.userState("1");
                                user.setEmailActivation(User.ACTIVATION_YES);
                                if(nullEmptyBlankJudge(userCenterReg.getIdentify_type()) && !("null").equals(userCenterReg.getIdentify_type())){
                                    user.identifyType(Integer.parseInt(userCenterReg.getIdentify_type()));
                                }
                                user.identifyNo(userCenterReg.getIdentify_no());
                                //注册(密码为空则用户为测试，不为空则非测试)
                                if(nullEmptyBlankJudge(userCenterReg.getPassword())|| ("null").equals(userCenterReg.getPassword())){
                                    user.userType(2);
                                }else{
                                    if(nullEmptyBlankJudge(userCenterReg.getUser_type()) && !("null").equals(userCenterReg.getUser_type())){
                                        user.userType(Integer.parseInt(userCenterReg.getUser_type()));
                                    }
                                }
                                user.userState(userCenterReg.getUser_state());
                                userService.save(user);
                            }
                        }catch(NumberFormatException ne){
                            map.clear();
                            map.put("status", "0");
                            map.put("error_code", "5");
                            map.put("errMsg",ne.getMessage());
                        }
                    }else {
                        map.clear();
                        map.put("status", "0");
                        map.put("errMsg","原业务系统用户不存在");
                        map.put("error_code", "4");//source_id不存在
                    }
            }
            if(map.get("status")=="0"){
                writeErrorJson(response,map);
            }else{
                writeSuccessJson(response,map);
            }
            //OauthControllerLog.log(startTime,userCenterReg.getUsername(),"",app,map,userserviceDev);
        }
        return;
    }
} 