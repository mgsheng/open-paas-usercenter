package cn.com.open.openpaas.userservice.app.oauth.service;


import java.util.List;

import cn.com.open.openpaas.userservice.app.oauth.model.OauthClientDetails;
import cn.com.open.openpaas.userservice.app.oauth.model.OauthClientDetailsDto;

/**
 * 
 */

public interface OauthService {

    OauthClientDetails loadOauthClientDetails(String clientId);

    List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos();

}