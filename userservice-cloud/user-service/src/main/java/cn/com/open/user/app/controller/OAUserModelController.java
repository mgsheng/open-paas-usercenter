package cn.com.open.user.app.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.com.open.user.app.constant.ConstantMessage;
import cn.com.open.user.app.entiy.OAUser;
import cn.com.open.user.app.service.OAUserService;
import cn.com.open.user.app.vo.OAUserVo;
@RestController
@RequestMapping("/usercenter")
public class OAUserModelController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(OAUserModelController.class);
	   @Autowired
	   OAUserService oaUserService;
	   
	   @Value("${oa-url}")
	   String url;
	   
	   @Value("${oa-appId}")
	   String appId;
	   
	   @Value("${oa-appSercret}")
	   String appSercret;
	 
	   /**
	    * OA用户验证接口
	    * @param request
	    * @param response
	    */
		@RequestMapping(value = "/GetOAUserModel", method = {RequestMethod.POST} )
		public void login(HttpServletRequest request, HttpServletResponse response,OAUser user) {
 	     	log.info("OAUserModelController usercenter/GetOAUserModel IdNo"+user.getIdNo()+"UserName:"+user.getUserName()+"Salt:"+user.getSalt());
 	     	OAUserVo userVo=null;
		   try {
			   Map<String, Object> maps = new HashMap<String, Object>();
	 	    	if (!paraMandatoryCheck(Arrays.asList(user.getIdNo()))) {
				 /*   paraMandaChkAndReturn(7, response, "必传参数中有空值");
	 				return;*/
	 			}
	 	    	
	 	   	    userVo=oaUserService.GetOAUserModel(user,maps,url,appSercret,appId);
	 	    	if(maps.containsKey("message")){
	 	    		int status=Integer.parseInt(maps.get("status").toString());
	 	    		paraMandaChkAndReturn(status, response,maps.get("message").toString());
	 	    	}else{
	 	    		maps.clear();
 	    			maps.put("status", "1");//接口返回状态：1-正确 2-错误
 	    			maps.put("message","查询成功");
 	    			maps.put("errorCode","");
 	    			maps.put("payload", userVo);
				    writeSuccessJson(response,maps);
	 	    	}
		   } catch (Exception e) {
			   paraMandaChkAndReturn(ConstantMessage.USER_THREE, response,"系统异常!");
	 	     	log.info("失败-OAUserModelController usercenter/GetOAUserModel IdNo"+user.getIdNo()+"UserName:"+user.getUserName()+"Salt:"+user.getSalt());
			   e.printStackTrace();
		   }
			return ;
		}
}
