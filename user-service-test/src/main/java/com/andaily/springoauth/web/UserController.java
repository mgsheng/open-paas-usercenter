package com.andaily.springoauth.web;

import com.andaily.springoauth.service.dto.AuthorizationCodeDto;
import com.andaily.springoauth.service.dto.UserBindDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * Handle 'authorization_code'  type actions
 *
 * 
 */
@Controller
public class UserController {


    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Value("#{properties['user-register-uri']}")
    private String userRegisterUri;

    @Value("#{properties['application-host']}")
    private String host;
    
    @Value("#{properties['user-bind-uri']}")
    private String userBindUri;
    
    /*
   * Entrance:   step-1
   * */
    @RequestMapping(value = "register", method = RequestMethod.GET)
    public String register(Model model) {
    	model.addAttribute("userRegisterUri", userRegisterUri);
        model.addAttribute("host", host);
        model.addAttribute("state", UUID.randomUUID().toString());
        return "register";
    }


    /*
   * Redirect to oauth-server register page:   step-2
   * */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String submitAuthorizationCode(AuthorizationCodeDto codeDto) throws Exception {
        final String fullUri = codeDto.getRegUri();
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);

        return "redirect:" + fullUri;
    }

    /*
     * Entrance:   step-1
     * */
      @RequestMapping(value = "bind", method = RequestMethod.GET)
      public String bind(Model model,HttpServletRequest request,String mess) {
      	  model.addAttribute("userBindUri", userBindUri);
          model.addAttribute("host", host);
          model.addAttribute("state", UUID.randomUUID().toString());
          if(mess!=null && !("").equals(mess)){
        	  model.addAttribute("mess", mess);
          }          
          return "bind";
      }


     /* 
     * Redirect to oauth-server bind page:   step-2
     * */
      @RequestMapping(value = "bind", method = RequestMethod.POST)
      public String submitBind(UserBindDto bindDto) throws Exception {
          final String fullUri = bindDto.getFullUri();
          LOG.debug("Send to Oauth-Server URL: {}", fullUri);

          return "redirect:" + fullUri;
      }
}