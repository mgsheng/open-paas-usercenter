package cn.com.open.openpaas.userservice.app.infrastructure.repository;

import java.util.List;

import cn.com.open.openpaas.userservice.app.oauth.model.OauthClientDetails;

/**
 * 
 */
public interface OauthRepository extends Repository {

    OauthClientDetails findOauthClientDetails(String clientId);

    List<OauthClientDetails> findAllOauthClientDetails();

}