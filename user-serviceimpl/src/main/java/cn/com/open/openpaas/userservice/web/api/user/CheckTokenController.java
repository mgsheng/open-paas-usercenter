package cn.com.open.openpaas.userservice.web.api.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.open.openpaas.userservice.app.app.model.App;
import cn.com.open.openpaas.userservice.app.app.service.AppService;
import cn.com.open.openpaas.userservice.app.log.OauthControllerLog;
import cn.com.open.openpaas.userservice.app.web.WebUtils;
import cn.com.open.openpaas.userservice.web.api.oauth.OauthSignatureValidateHandler;

/**
 *  检验access_token
 */
@Controller
@RequestMapping("/user/")
public class CheckTokenController {
	private static final Logger log = LoggerFactory.getLogger(CheckTokenController.class);
	 @Autowired
	 private AppService appService; 
	 @Autowired
	 private DefaultTokenServices tokenServices;
    
    /**
      *  检验access_token
     */
    @RequestMapping("userCenterCheckToken")
    public void userInfo(HttpServletRequest request,HttpServletResponse response) throws Exception {
      String token=	request.getParameter("access_token");
      String clientId=request.getParameter("client_id");
      long startTime = System.currentTimeMillis();
      Map<String, Object> map=new HashMap<String,Object>();

      if(!WebUtils.paraMandatoryCheck(Arrays.asList(clientId,token))){
    	  WebUtils.paraMandaChkAndReturn(3,response,"必传参数有空值");
            return;
      }
      OAuth2AccessToken accessToken=tokenServices.readAccessToken(token);
      App app=appService.findIdByClientId(clientId);

	  Boolean f=OauthSignatureValidateHandler.validateSignature(request,app);
	  if(!f){
		WebUtils.paraMandaChkAndReturn(9, response,"认证失败");
		return;
	  }
      if(app==null){
    	  map.put("status", "0");
		  map.put("error_code", "1");//client_id错误
      }else{
	      if(accessToken!=null){	    	 
	    	  if(!tokenServices.getClientId(accessToken.getValue()).equals(clientId)){
	    		  map.put("status", "0");
	    		  map.put("error_code", "2");//access_token与client_id不匹配
	    	  }else{
	    			//map=checkClientIdOrToken(clientId,token);
	    		  map.put("status", "1");
	    	  }
	      }
      }
      if(map.get("status")=="0"){
  		  WebUtils.writeErrorJson(response,map);
  	  }else{
  		  WebUtils.writeSuccessJson(response,map);
  	  }
  	  OauthControllerLog.log(startTime,"","",app,map);
      return;
      
    }
}