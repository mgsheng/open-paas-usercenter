package com.andaily.springoauth.web;

import com.andaily.springoauth.service.OauthService;
import com.andaily.springoauth.service.dto.AccessTokenDto;
import com.andaily.springoauth.service.dto.AuthAccessTokenDto;
import com.andaily.springoauth.service.dto.AuthCallbackDto;
import com.andaily.springoauth.service.dto.AuthorizationCodeDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Handle 'authorization_code'  type actions
 *
 * 
 */
@Controller
public class AuthorizationCodeController {


    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationCodeController.class);


    @Value("#{properties['user-authorization-uri']}")
    private String userAuthorizationUri;


    @Value("#{properties['application-host']}")
    private String host;


    @Value("#{properties['userUserInfoUri']}")
    private String userUserInfoUri;


    @Autowired
    private OauthService oauthService;


    /*
   * Entrance:   step-1
   * */
    @RequestMapping(value = "authorization_code", method = RequestMethod.GET)
    public String authorizationCode(Model model) {
        model.addAttribute("userAuthorizationUri", userAuthorizationUri);
        model.addAttribute("host", host);
        model.addAttribute("userUserInfoUri", userUserInfoUri);
        model.addAttribute("state", UUID.randomUUID().toString());
        return "authorization_code";
    }


    /*
   * Redirect to oauth-server login page:   step-2
   * */
    @RequestMapping(value = "authorization_code", method = RequestMethod.POST)
    public String submitAuthorizationCode(AuthorizationCodeDto codeDto) throws Exception {
        final String fullUri = codeDto.getFullUri();
        LOG.debug("Send to Oauth-Server URL: {}", fullUri);

        return "redirect:" + fullUri;
    }


    /*
   * Oauth callback (redirectUri):   step-3
   *
   * Handle 'code', go to 'access_token' ,validation oauth-server response data
   *
   *  authorization_code_callback
   * */
    @RequestMapping(value = "authorization_code_callback")
    public String authorizationCodeCallback(AuthCallbackDto callbackDto, Model model) throws Exception {

        if (callbackDto.error()) {
            //Server response error
            model.addAttribute("message", callbackDto.getError_description());
            model.addAttribute("error", callbackDto.getError());
            return "redirect:oauth_error";
        } else if (correctState(callbackDto)) {
            //Go to retrieve access_token form
            AuthAccessTokenDto accessTokenDto = oauthService.createAuthAccessTokenDto(callbackDto);
            model.addAttribute("accessTokenDto", accessTokenDto);
            model.addAttribute("host", host);
            return "code_access_token";
        } else {
            //illegal state
            model.addAttribute("message", "Illegal \"state\": " + callbackDto.getState());
            model.addAttribute("error", "Invalid state");
            return "redirect:oauth_error";
        }

    }


    /**
     * Use HttpClient to get access_token :   step-4
     * <p/>
     * Then, 'authorization_code' flow is finished,  use 'access_token'  visit resources now
     *
     * @param tokenDto AuthAccessTokenDto
     * @param model    Model
     * @return View
     * @throws Exception
     */
    @RequestMapping(value = "code_access_token", method = RequestMethod.POST)
    public String codeAccessToken(AuthAccessTokenDto tokenDto, Model model) throws Exception {
        final AccessTokenDto accessTokenDto = oauthService.retrieveAccessTokenDto(tokenDto);
        if (accessTokenDto.error()) {
            model.addAttribute("message", accessTokenDto.getErrorDescription());
            model.addAttribute("error", accessTokenDto.getError());
            return "oauth_error";
        } else {
            model.addAttribute("accessTokenDto", accessTokenDto);
            model.addAttribute("userUserInfoUri", userUserInfoUri);
            return "access_token_result";
        }
    }


    private boolean correctState(AuthCallbackDto callbackDto) {
        final String state = callbackDto.getState();
        return StringUtils.isNotEmpty(state);
    }

}