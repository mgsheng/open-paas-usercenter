package cn.com.open.openpaas.userservice.web.api.user;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.appuser.model.AppUser;
import cn.com.open.openpaas.userservice.app.appuser.service.AppUserService;
import cn.com.open.openpaas.userservice.app.tools.DES;
import cn.com.open.openpaas.userservice.app.tools.DateTools;
import cn.com.open.openpaas.userservice.app.user.model.User;
import cn.com.open.openpaas.userservice.app.user.service.UserService;

/**
 * 
 */
@Controller
@RequestMapping("/user/auto/")
public class AutoLoginController {
	private static final Logger log = LoggerFactory.getLogger(AutoLoginController.class);
	 @Autowired
	 private UserService userService;
	 @Autowired
	 private AppUserService appUserService;
	 @Autowired
	 private AppService appService; 
	 
    private boolean nullEmptyBlankJudge(String str){
        return null==str||str.isEmpty()||"".equals(str.trim());
    }
	 /** 
     * 自动登录
     * @param request
     * @param response
	 * @throws UnsupportedEncodingException 
     */
     @RequestMapping("login")
     public String  autoLogin(HttpServletRequest request,HttpServletResponse response,Model model) throws UnsupportedEncodingException {
         String secret=request.getParameter("secret");
         //String goodsName=new String(request.getParameter("goodsName").getBytes("iso-8859-1"),"utf-8");
         String app_id= request.getParameter("app_id");
         String desApp_id= request.getParameter("desApp_id");
         String desAddress= request.getParameter("desAddress");
         String source_id="";
         String time="";
         String salt="";
         String desAppUrl="";
         String desAppKey="";
         String desAppSecert="";
         String secertDesAddress="";
         String studentCode="";
         String realName="";
         if(nullEmptyBlankJudge(app_id)&&nullEmptyBlankJudge(desApp_id)){
//        	 model.addAttribute("message", "appId 为空");
//             model.addAttribute("error", "1");
//             return "redirect:oauth_error"; 
        	 return "redirect:"+desAddress; 
         }else{
        	  App app=appService.findById(Integer.parseInt(app_id));
        	  App desApp=appService.findById(Integer.parseInt(desApp_id));
        	  if(app!=null){
        		  desAppUrl=desApp.getWebServerRedirectUri();
        		  desAppKey=desApp.getAppkey();
        		  desAppSecert=desApp.getAppsecret();
        			try {					
    					secret=	DES.decrypt(secret, app.getAppsecret());
    					//secret=	AESUtil.decrypt(secret, app.getAppsecret());
    					log.info("解密后 secret："+secret);
    				    String sercret[]=secret.split("#");
    				    source_id=sercret[0];
    				    time=sercret[1];
    				    salt=sercret[3];
    				    secertDesAddress=sercret[4];
    				    if(sercret.length==7){
    				    	studentCode=sercret[5];
    				    	realName=sercret[6];
    				    }
    				    long timeSub = 0;
    				    String nowTime=DateTools.dateToString(new Date(),"yyyyMMddHHmmss");
    				    	if(!nullEmptyBlankJudge(time)){
    				    		timeSub=DateTools.timeSub2(time,nowTime)/60;
    				    	}
    				    	if(nullEmptyBlankJudge(source_id)){
    				    		   //model.addAttribute("error_message","用户名不存在");
//    				    		 model.addAttribute("message", "用户名不存在");
//    				             model.addAttribute("error", "2");
//    				             return "redirect:oauth_error"; 
    				    		  return "redirect:"+desAddress;
    				    	}
    				    	if(timeSub>30){
    				    		  // model.addAttribute("error_message","时间超时-超过30分钟有效期");
//    				    		 model.addAttribute("message", "时间超时-超过30分钟有效期");
//    				             model.addAttribute("error", "3");
//    				             return "redirect:oauth_error"; 
    				    		  return "redirect:"+desAddress;
    				    	}if(nullEmptyBlankJudge(secertDesAddress)||!secertDesAddress.equals(desAddress)){
    				    		 return "redirect:"+desAddress;	
    				    	}
    				    	AppUser  appUser=appUserService.findByCidSid(Integer.parseInt(app_id), source_id);
    				    	if(appUser==null){
    				    		 //model.addAttribute("error_message","source_id不存在");
//    				    		 model.addAttribute("message", "source_id不存在");
//    				             model.addAttribute("error", "6");
//    				             return "redirect:oauth_error"; 
    				    		  return "redirect:"+desAddress;
    				    	}else{
    				    		 User user = userService.findUserById(appUser.userId());
    				    		 if(user==null){
    				    			 //model.addAttribute("error_message","用户不存在");
//    				    			 model.addAttribute("message", "用户不存在");
//        				             model.addAttribute("error", "2");
//        				             return "redirect:oauth_error"; 
    				    		  return "redirect:"+desAddress;
    				    		 }else{
    				    			 String mcSercrt=app_id+"#"+user.username()+"#"+user.email()+"#"+user.phone()+"#"+user.guid()+"#"+time+"#"+desAppKey+"#"+salt+"#"+desAddress+"#"+studentCode+"#"+realName;
    				    			 String sendMcSercret=DES.encrypt(mcSercrt, desAppSecert);
    				    			 sendMcSercret=DES.getNewSecert(sendMcSercret);
    				    			 if(!nullEmptyBlankJudge(desAddress)){
										return "redirect:"+desAppUrl+"?secret="+sendMcSercret+"&desAddress="+desAddress;  
										
    				    			 }else{
        						    	 return "redirect:"+desAppUrl+"?secret="+sendMcSercret; 
    				    			 }
    				    		 }
    				    	}
    				}catch (Exception e) {
    				   //model.addAttribute("error_message","程序异常错误");
//    					 model.addAttribute("message", "程序异常错误");
//			             model.addAttribute("error", "4");
//			             return "redirect:oauth_error";
    				  return "redirect:"+desAddress;
    			}   
        	  }else{
//        		  model.addAttribute("message", "appId错误");
//		             model.addAttribute("error", "5");
//		             return "redirect:oauth_error";
        		  return "redirect:"+desAddress;
        	  }
			
         }
       
     }
	
    
}