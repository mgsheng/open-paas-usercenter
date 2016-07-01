package cn.com.open.openpaas.userservice.web.api.user;
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
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.redis.service.RedisClientTemplate;
import cn.com.open.openpaas.userservice.app.redis.service.RedisConstant;
import cn.com.open.openpaas.userservice.app.tools.BaseControllerUtil;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 * 用户账号验证接口
 */
@Controller
@RequestMapping("/user/")
public class VerifyController extends BaseControllerUtil{
	private static final Logger log = LoggerFactory.getLogger(VerifyController.class);
	@Autowired 
	private UserService userService;
	@Autowired
	private AppService appService; 
	@Autowired
	private DefaultTokenServices tokenServices;
	@Autowired
	private RedisClientTemplate redisClient;
    /**
     * 用户账号验证接口
     * @return Json
     */
    @RequestMapping("userCenterVerify")
    public void userCenterVerify(HttpServletRequest request,HttpServletResponse response) {
    	String client_id=request.getParameter("client_id");
    	String access_token=request.getParameter("access_token");
    	String account=request.getParameter("account");
    	String accountType=request.getParameter("accountType");
    	Map<String, Object> map=new HashMap<String,Object>();
    	long startTime = System.currentTimeMillis();
    	log.info("client_id:"+client_id);
        if(!WebUtils.paraMandatoryCheck(Arrays.asList(client_id,access_token,account))){
        	WebUtils.paraMandaChkAndReturn(4, response,"必传参数有空值");
            return;
        }
       /*UserVerifyDto dto=(UserVerifyDto) redisClient.getObject(RedisConstant.USER_NAME_CHECK+account+"_"+accountType);
       if(dto!=null)
       {
    	   if(dto.getAccount().equals(account)&&!dto.getClientId().equals(client_id))
    	   {
    		   WebUtils.paraMandaChkAndReturn(3,response,"account已存在");
    		   return;
    	   }
       }*/
       App app = (App) redisClient.getObject(RedisConstant.APP_INFO+client_id);
       if(app==null)
		{
			 app=appService.findIdByClientId(client_id);
			 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
		}
       Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
		if(!f){
			WebUtils.paraMandaChkAndReturn(9, response,"认证失败");
			return;
		}
		map=checkClientIdOrToken(client_id,access_token,app,tokenServices);
		
		if(map.get("status").equals("1")){//client_id,access_token正确
			/*if(app==null)
			{
				 app=appService.findIdByClientId(client_id);
				 redisClient.setObject(RedisConstant.APP_INFO+client_id, app);
			}*/
			
			User user=null;
	    	if(account!=null&&!"".equals(account)){	    		
	    		user=checkUsername(account,accountType,userService);
	    	}else{
	    		map.clear();
	    		map.put("status", "0");
	    		map.put("error_code", "4");//参数不全
	    		map.put("errMsg", "登录名为空");//参数不全
	    	}
	    	//判断account是否存在(对应判断属性：username,phone,email)
		    //	checkUsername(account);
	    	if(user!=null){
	    		map.clear();
	    		map.put("status", "0");
	    		map.put("error_code", "3");//用户已存在
	    		map.put("errMsg", "用户已存在");//参数不全
	    	}else{
	    		map.clear();
	    		map.put("status", "1");
	    	}
		}
    	if(map.get("status")=="0"){
    		WebUtils.writeErrorJson(response,map);
    	}else{
    		  /*UserVerifyDto verifyDto=new   UserVerifyDto();
    		  verifyDto.setAccount(account);
    		  verifyDto.setClientId(client_id);
    		  redisClient.setObjectByTime(RedisConstant.USER_NAME_CHECK+account+"_"+accountType, verifyDto, 60);*/
    		  WebUtils.writeSuccessJson(response,map);
    	}
    	OauthControllerLog.log(startTime,account,"",app,map);
        return;
    }
  
}